
package Visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Handles warning-canvas events (bottom black area).
 *
 * UI: Two event types:
 *  - INFO   : only a centered text field (click inside dismisses)
 *  - CHOICE : centered text + 4 options around it (top/right/bottom/left)
 *
 * Data: Events are loaded from CSV so you can edit them easily in Excel:
 *   DataCSV/EventData/Events.csv
 *
 * Runtime state (cooldowns + "once per month") is stored in:
 *   DataCSV/EventData/EventCooldown.csv
 *
 * The "time" the event system uses is a simple day counter stored in:
 *   DataCSV/EventData/GameTime.csv
 *
 * IMPORTANT CSV RULE:
 * This is a very simple comma-separated parser (no quoted commas).
 * So: do NOT put commas into your text fields.
 */
public class EventManager {

    // -------------------------
    // Event model
    // -------------------------

    public enum EventType {
        INFO,
        CHOICE
    }

    /** One row from Events.csv (Excel-friendly). */
    private static class EventDef {
        String id;
        EventType type;

        int weight;        // weighted random selection (higher = more likely)
        int cooldownDays;  // after triggering, event cannot appear for this many days
        int maxPerMonth;   // 0 = unlimited; e.g. 1 => max once per month
        boolean always;    // if true: ignore maxPerMonth (can still have cooldown)

        // Company type filter: ALL, Foodtruck, IT Manager, CraftBusiness (can be multiple separated by |)
        String companyTypes = "ALL";

        String text;

        // For CHOICE events: 0=TOP, 1=RIGHT, 2=BOTTOM, 3=LEFT
        String[] options = new String[] { "", "", "", "" };

        // Consequences:
        // - INFO uses effects[0]
        // - CHOICE uses effects[0..3] matching options
        String[] effects = new String[] { "", "", "", "" };
    }

    private static final EventManager INSTANCE = new EventManager();
    private final Random rng = new Random();

    // Tracks how much money events changed since the last cycle-report.
    private double pendingMoneyDeltaForReport = 0.0;

    // Two-step option confirmation: first click shows costs, second click confirms
    private int pendingChoiceIndex = -1;
    private String baseChoiceMessage = null;


    // -------------------------
    // Gameplay config
    // -------------------------

    /** How many "days" make up a month for "once per month" events. */
    private static final int DAYS_PER_MONTH = 30;

    /**
     * Chance that an event triggers when time advanced.
     * (You asked for 50% for testing.)
     */
    private static final double EVENT_ROLL_CHANCE = 0.50;

    // -------------------------
    // Current active event (UI)
    // -------------------------

    private static boolean active = false;
    private EventType type = EventType.INFO;

    private String message = "";
    // Order: 0=TOP, 1=RIGHT, 2=BOTTOM, 3=LEFT
    private final String[] options = new String[] { "", "", "", "" };

    /** Current event id (from Events.csv). */
    private String currentEventId = null;

    /** Current effects for INFO (index 0) or CHOICE (0..3). */
    private final String[] currentEffects = new String[] { "", "", "", "" };

    // -------------------------
    // Loaded CSV data + runtime state
    // -------------------------

    private final List<EventDef> definitions = new ArrayList<>();
    private boolean definitionsLoaded = false;

    /**
     * Runtime state loaded from EventCooldown.csv.
     * key = event id
     * value = String[] { id, nextAllowedDay, monthIndex, countThisMonth }
     */
    private final Map<String, String[]> state = new HashMap<>();

    // -------------------------
    // Layout cache (computed in computeLayout)
    // -------------------------

    private double lastMsgX, lastMsgY, lastMsgW, lastMsgH;
    // Option boxes: [idx][0]=x, [idx][1]=y, [idx][2]=w, [idx][3]=h
    private final double[][] lastOpt = new double[][] {
            {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}
    };

    // Cached fonts based on size (recomputed each draw/click)
    private Font msgFont = Font.font("Arial", FontWeight.BOLD, 18);
    private Font optFont = Font.font("Arial", FontWeight.NORMAL, 16);

    private EventManager() {}

    public static EventManager getInstance() {
        return INSTANCE;
    }

    // -------------------------
    // Public API used by the game
    // -------------------------

    /** VisualElementsHolder uses: Day / Week / Month / Year. */
    public int cycleToDays(String cycleLabel) {
        if (cycleLabel == null) return 1;
        switch (cycleLabel) {
            case "Day": return 1;
            case "Week": return 7;
            case "Month": return 30;
            case "Year": return 365;
            default: return 1;
        }
    }

    /**
     * Called when time has advanced (after the report, when the player goes back to work).
     *
     * Rules:
     *  - EVENT_ROLL_CHANCE decides whether ANY event appears.
     *  - Eligible events are filtered by cooldowns + monthly limits.
     *  - One event is selected via weighted random.
     *
     * INFO events apply their effect immediately and are persisted.
     * CHOICE events apply effect only after the player selects an option.
     */
    public void tryTriggerEventFromCSV(ReadCSVFiles reader, WriteCSVFiles writer, Company company) {
        ensureLoaded(reader);

        // If an event is currently on-screen, do NOT roll/clear/replace it.
        // This prevents events from disappearing or being skipped.
        if (active) {
            return;
        }

        int day = reader.readEventDay();

        // First 5 days: no events
        if (day < 5) {
            clear();
            return;
        }
        int monthIndex = day / DAYS_PER_MONTH;

        if (rng.nextDouble() > EVENT_ROLL_CHANCE) {
            clear();
            return;
        }

        ArrayList<EventDef> eligible = new ArrayList<>();
        int totalWeight = 0;

        for (EventDef def : definitions) {
            if (def == null || def.id == null) continue;

            if (!matchesCompanyType(def, company)) continue;


            String[] st = state.get(def.id);
            if (st == null) {
                st = new String[] { def.id, "0", Integer.toString(monthIndex), "0" };
                state.put(def.id, st);
            }

            // month reset (so 1/month works automatically)
            int savedMonth = parseIntSafe(st[2], monthIndex);
            if (savedMonth != monthIndex) {
                st[2] = Integer.toString(monthIndex);
                st[3] = "0";
            }

            int nextAllowed = parseIntSafe(st[1], 0);
            if (day < nextAllowed) continue;

            // max per month (0 = unlimited)
            if (!def.always && def.maxPerMonth > 0) {
                int count = parseIntSafe(st[3], 0);
                if (count >= def.maxPerMonth) continue;
            }

            int w = Math.max(0, def.weight);
            if (w == 0) continue;

            eligible.add(def);
            totalWeight += w;
        }

        if (eligible.isEmpty() || totalWeight <= 0) {
            clear();
            return;
        }

        // Weighted random pick
        int roll = rng.nextInt(totalWeight);
        int acc = 0;
        EventDef chosen = null;
        for (EventDef d : eligible) {
            acc += Math.max(0, d.weight);
            if (roll < acc) {
                chosen = d;
                break;
            }
        }

        if (chosen == null) {
            clear();
            return;
        }

        // Show event
        currentEventId = chosen.id;
        for (int i = 0; i < 4; i++) {
            currentEffects[i] = safe(chosen.effects[i]);
        }

        if (chosen.type == EventType.INFO) {
            setInfoEvent(chosen.text);

            // INFO consequence happens immediately
            applyEffectString(company, safe(chosen.effects[0]), reader, writer);
            finishEventAndPersist(reader, writer, company, chosen);
        } else {
            setChoiceEvent(chosen.text,
                    chosen.options[0], chosen.options[1], chosen.options[2], chosen.options[3]);
        }
    }

    /**
     * Handles mouse clicks and applies consequences if needed.
     *
     * INFO: click inside message box closes it.
     * CHOICE: click an option => apply effect => persist => show "Gewählt: ..." as INFO.
     *
     * @return true if click hit the event UI.
     */
    public boolean handleClickAndApply(Canvas canvas, double x, double y,
                                       ReadCSVFiles reader, WriteCSVFiles writer, Company company) {
        if (!active) return false;

        computeLayout(canvas);

        if (type == EventType.INFO) {
            if (contains(lastMsgX, lastMsgY, lastMsgW, lastMsgH, x, y)) {
                clear();
                return true;
            }
            return false;
        }

        // CHOICE (apply immediately on first click)
        for (int i = 0; i < 4; i++) {
            double ox = lastOpt[i][0];
            double oy = lastOpt[i][1];
            double ow = lastOpt[i][2];
            double oh = lastOpt[i][3];

            if (contains(ox, oy, ow, oh, x, y)) {
                applyEffectString(company, safe(currentEffects[i]), reader, writer);

                // find def for cooldown/maxPerMonth handling
                EventDef def = findDefinition(currentEventId);
                finishEventAndPersist(reader, writer, company, def);

                // reset any legacy preview state (kept for backward compatibility)
                pendingChoiceIndex = -1;
                baseChoiceMessage = null;

                // Keep the applied message clean (no extra summary like "Geldänderung ...").
                setInfoEvent("Applied: " + options[i]);
                return true;
            }
        }

return false;
    }

    // -------------------------
    // Backwards compatible method (old code used handleClick(...))
    // -------------------------

    /**
     * Older click handler without consequences.
     * Keeping it so older code does not crash.
     */
    public int handleClick(Canvas canvas, double x, double y) {
        if (!active) return -1;
        computeLayout(canvas);

        if (type == EventType.INFO) {
            if (contains(lastMsgX, lastMsgY, lastMsgW, lastMsgH, x, y)) {
                clear();
                return -1;
            }
            return -1;
        }

        for (int i = 0; i < 4; i++) {
            if (contains(lastOpt[i][0], lastOpt[i][1], lastOpt[i][2], lastOpt[i][3], x, y)) {
                setInfoEvent("Chosen: " + options[i]);
                return i;
            }
        }
        return -1;
    }

    public void setInfoEvent(String message) {
        this.type = EventType.INFO;
        this.message = message == null ? "" : message;
        options[0] = options[1] = options[2] = options[3] = "";
        this.active = true;
        ButtonManager.updateNextCycleButtonState();
    }

    public void setChoiceEvent(String message, String top, String right, String bottom, String left) {
        this.type = EventType.CHOICE;
        this.message = message == null ? "" : message;
        // Reset confirmation/preview state whenever a new choice-event is shown.
        this.pendingChoiceIndex = -1;
        this.baseChoiceMessage = this.message;
        options[0] = top == null ? "" : top;
        options[1] = right == null ? "" : right;
        options[2] = bottom == null ? "" : bottom;
        options[3] = left == null ? "" : left;
        this.active = true;
        ButtonManager.updateNextCycleButtonState();
    }

    public static boolean isActive() {
        return active;
    }

    public void clear() {
        active = false;
        pendingChoiceIndex = -1;
        baseChoiceMessage = null;
        ButtonManager.updateNextCycleButtonState();
        message = "";
        options[0] = options[1] = options[2] = options[3] = "";
        for (int i = 0; i < 4; i++) currentEffects[i] = "";
        currentEventId = null;
        type = EventType.INFO;
    }

/**
 * @return true if an event is currently active on the warning canvas.
 * Used to block skipping the day until the player chooses an option.
 */
public boolean isEventActive() {
    return active;
}

/**
 * Adds to the "event money delta" that should be counted in the next cycle report.
 */
private void addMoneyDeltaForReport(double delta) {
    pendingMoneyDeltaForReport += delta;
}

/**
 * Called by ButtonManager when showing the cycle report.
 * This returns the accumulated delta and resets it to 0.
 */
public double consumePendingMoneyDeltaForReport() {
    double v = pendingMoneyDeltaForReport;
    pendingMoneyDeltaForReport = 0.0;
    return v;
}


    // -------------------------
    // CSV loading + state update
    // -------------------------

private void ensureLoaded(ReadCSVFiles reader) {
    if (reader == null) return;

    if (!definitionsLoaded) {
        definitions.clear();

        List<String[]> raw = reader.readEventDefinitionsRaw();
        if (raw == null) raw = new ArrayList<>();

        // Detect header (supports both old schema and new schema with EventID/Option1Text etc.)
        Map<String, Integer> headerIndex = new HashMap<>();
        int startRow = 0;

        if (!raw.isEmpty() && raw.get(0) != null && raw.get(0).length > 0) {
            String firstCell = raw.get(0)[0] == null ? "" : raw.get(0)[0].trim().toLowerCase();
            if (firstCell.equals("id") || firstCell.startsWith("id") || firstCell.equals("eventid") || firstCell.startsWith("eventid")) {
                String[] header = raw.get(0);
                for (int i = 0; i < header.length; i++) {
                    String h = header[i] == null ? "" : header[i].trim();
                    if (!h.isEmpty()) headerIndex.put(h.toLowerCase(), i);
                }
                startRow = 1;
            }
        }

        for (int i = startRow; i < raw.size(); i++) {
            String[] row = raw.get(i);
            EventDef def = parseDefinitionRowFlexible(headerIndex, row);
            if (def != null) definitions.add(def);
        }

        definitionsLoaded = true;
    }

    if (state.isEmpty()) {
        state.putAll(reader.readEventCooldownStateRaw());
    }
}

private EventDef parseDefinitionRowFlexible(Map<String, Integer> headerIndex, String[] r) {
    if (r == null) return null;

    // If we have headers for the new schema:
    if (headerIndex != null && !headerIndex.isEmpty() && (headerIndex.containsKey("eventid") || headerIndex.containsKey("eventtext"))) {
        String id = cell(headerIndex, r, "EventID");
        if (id == null || id.isBlank()) id = cell(headerIndex, r, "id");
        if (id == null || id.isBlank()) return null;

        EventDef d = new EventDef();
        d.id = id.trim();

        d.text = safe(cell(headerIndex, r, "EventText"));

        // options (Option1..4)
        String o1 = safe(cell(headerIndex, r, "Option1Text"));
        String o2 = safe(cell(headerIndex, r, "Option2Text"));
        String o3 = safe(cell(headerIndex, r, "Option3Text"));
        String o4 = safe(cell(headerIndex, r, "Option4Text"));

        d.options[0] = o1;
        d.options[1] = o2;
        d.options[2] = o3;
        d.options[3] = o4;

        // Auto-detect event type: if there is at least one option => CHOICE, else INFO
        boolean hasChoice = !(o1.isBlank() && o2.isBlank() && o3.isBlank() && o4.isBlank());
        d.type = hasChoice ? EventType.CHOICE : EventType.INFO;

        // Effects: Type + Value -> "TYPE:VALUE" (existing effect parser reads KEY:VALUE)
        d.effects[0] = buildEffect(cell(headerIndex, r, "Option1Type"), cell(headerIndex, r, "Option1Value"));
        d.effects[1] = buildEffect(cell(headerIndex, r, "Option2Type"), cell(headerIndex, r, "Option2Value"));
        d.effects[2] = buildEffect(cell(headerIndex, r, "Option3Type"), cell(headerIndex, r, "Option3Value"));
        d.effects[3] = buildEffect(cell(headerIndex, r, "Option4Type"), cell(headerIndex, r, "Option4Value"));

        d.cooldownDays = parseIntSafe(cell(headerIndex, r, "CooldownDays"), 0);
        d.maxPerMonth = parseIntSafe(cell(headerIndex, r, "MaxPerMonth"), 0);
        d.weight = parseIntSafe(cell(headerIndex, r, "Weight"), 1); // optional
        d.always = "true".equalsIgnoreCase(safe(cell(headerIndex, r, "Always")));

        d.companyTypes = safe(cell(headerIndex, r, "CompanyType"));
        if (d.companyTypes.isBlank()) d.companyTypes = "ALL";

        return d;
    }

    // Fallback: old schema (15 cols)
    String[] a = new String[15];
    for (int i = 0; i < a.length; i++) {
        a[i] = (i < r.length && r[i] != null) ? r[i].trim() : "";
    }
    if (a[0].isEmpty()) return null;

    EventDef d = new EventDef();
    d.id = a[0];
    d.type = "CHOICE".equalsIgnoreCase(a[1]) ? EventType.CHOICE : EventType.INFO;
    d.weight = parseIntSafe(a[2], 1);
    d.cooldownDays = parseIntSafe(a[3], 0);
    d.maxPerMonth = parseIntSafe(a[4], 0);
    d.always = "true".equalsIgnoreCase(a[5]);
    d.text = a[6];

    d.options[0] = a[7];
    d.options[1] = a[8];
    d.options[2] = a[9];
    d.options[3] = a[10];

    d.effects[0] = a[11];
    d.effects[1] = a[12];
    d.effects[2] = a[13];
    d.effects[3] = a[14];

    // optional company type as 16th column in old schema
    if (r.length >= 16 && r[15] != null && !r[15].trim().isEmpty()) {
        d.companyTypes = r[15].trim();
    } else {
        d.companyTypes = "ALL";
    }

    return d;
}

private static String cell(Map<String, Integer> headerIndex, String[] row, String head) {
    if (headerIndex == null || row == null || head == null) return "";
    Integer idx = headerIndex.get(head.toLowerCase());
    if (idx == null) return "";
    if (idx < 0 || idx >= row.length) return "";
    String v = row[idx];
    return v == null ? "" : v.trim();
}

private static String buildEffect(String type, String value) {
    String t = type == null ? "" : type.trim();
    String v = value == null ? "" : value.trim();
    if (t.isEmpty() || v.isEmpty()) return "";
    // allow both integer and decimal values; keep as-is
    return t + ":" + v;
}


private static String buildEffectSummary(String effect) {
    if (effect == null) return "";
    String e = effect.trim();
    if (e.isEmpty()) return "";
    // effect format: Type:Value
    String[] parts = e.split(":", 2);
    String type = parts.length > 0 ? parts[0].trim() : "";
    String value = parts.length > 1 ? parts[1].trim() : "";
    if (type.isEmpty() || value.isEmpty()) return e;

    // Normalize common variants
    String t = type.replaceAll("\\s+", "").toLowerCase();    if (t.equals("money") || t.equals("gesamtgeld")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "";
    }
    if (t.equals("rep") || t.equals("reputation")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "Reputation " + v;
    }
    if (t.equals("resourcen") || t.equals("resources")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "Ressourcen-Kosten " + v;
    }
    if (t.equals("produkte") || t.equals("products")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "Produkt-Kosten " + v;
    }
    if (t.equals("maschinengeld") || t.equals("machinecost") || t.equals("maschinenkosten")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "Maschinen-Kosten " + v;
    }
    if (t.equals("maschinenhaltbarkeit") || t.equals("machinedurability") || t.equals("durability")) {
        String v = value.trim();
        if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
        return "Maschinen-Haltbarkeit " + v;
    }

    // Fallback: still show something readable, but never append raw "-900 Gesamtgeld" etc.
    String v = value.trim();
    if (!(v.startsWith("+") || v.startsWith("-"))) v = "+" + v;
    return type + ": " + v;
}


    /**
     * Checks whether an event definition is allowed for the current company type.
     * Allowed values in CSV (CompanyTypes column):
     *  - ALL
     *  - Foodtruck
     *  - IT Manager
     *  - CraftBusiness
     * You can allow multiple by separating with | or ; or , (e.g. "Foodtruck|ALL").
     */
    private boolean matchesCompanyType(EventDef def, Company company) {
        if (def == null) return true;
        String filter = def.companyTypes;
        if (filter == null || filter.isBlank()) return true;

        // Normalize
        String f = filter.trim();
        if (f.equalsIgnoreCase("ALL")) return true;

        String companyType = (company != null) ? company.getCompanyType() : null;
        if (companyType == null || companyType.isBlank()) {
            // If we don't know the company type, don't block events.
            return true;
        }

        String ct = companyType.trim();
        String[] tokens = f.split("[|;,]");
        for (String t : tokens) {
            if (t == null) continue;
            String token = t.trim();
            // tolerate trailing punctuation like "ALL." from Excel
            token = token.replaceAll("[\\p{Punct}]+$", "").trim();
            if (token.isEmpty()) continue;
            if (token.equalsIgnoreCase("ALL")) return true;
            if (token.equalsIgnoreCase(ct)) return true;
        }
        return false;
    }

    /** Parses a double safely (supports both '.' and ',' as decimal separators). */
    private double parseDoubleSafe(String s, double fallback) {
        try {
            if (s == null) return fallback;
            String t = s.trim();
            if (t.isEmpty()) return fallback;
            t = t.replace(",", ".");
            return Double.parseDouble(t);
        } catch (Exception e) {
            return fallback;
        }
    }

private EventDef findDefinition(String id) {
        if (id == null) return null;
        for (EventDef d : definitions) {
            if (d != null && id.equals(d.id)) return d;
        }
        return null;
    }

    private int parseIntSafe(String s, int fallback) {
        try {
            if (s == null || s.trim().isEmpty()) return fallback;
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Applies a consequence string to the company.
     *
     * Supported tokens (separated by "|"):
     *  - MONEY:+500 / MONEY:-200
     *  - REP:+2 / REP:-1
     *
     * Example: MONEY:+200|REP:+1
     */
        private void applyEffectString(Company company, String effect, ReadCSVFiles reader, WriteCSVFiles writer) {
        if (company == null) return;
        if (effect == null) return;
        String e = effect.trim();
        if (e.isEmpty()) return;

        // Effects can be chained with |
        String[] parts = e.split(Pattern.quote("|"));
        for (String part : parts) {
            if (part == null) continue;
            String p = part.trim();
            if (p.isEmpty()) continue;

            String[] kv = p.split(":", 2);
            if (kv.length != 2) continue;

            String rawKey = kv[0] == null ? "" : kv[0].trim();
            String key = rawKey.toUpperCase().replaceAll("\\s+", ""); // tolerate spaces in CSV
            String val = kv[1] == null ? "" : kv[1].trim();

            // Values in CSV can be integers or decimals (e.g. 1100, 1100.0, 1100,0).
            double dVal = parseDoubleSafe(val, Double.NaN);
            if (Double.isNaN(dVal)) {
                continue;
            }
            int iDelta = (int) Math.round(dVal);

            switch (key) {
                // --------------------
                // Company stats
                // --------------------
                case "MONEY":
                case "GESAMTGELD":
                case "TOTALMONEY": {
                    // Apply immediately to the in-memory company object
                    company.setMoneyOfCompany(company.getMoneyOfCompany() + dVal);

                    // IMPORTANT: Persist immediately using a guaranteed writer instance.
                    // Some call sites may pass null; also ensures the UI that reloads from CSV sees the change right away.
                    WriteCSVFiles w2 = (writer != null) ? writer : new WriteCSVFiles();
                    if (w2 != null) {
                        w2.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
                        // Track for next-cycle report (does NOT change money there; only for display)
                        w2.addPendingMoneyDelta(dVal);
                        w2.addPendingEventNote("Event money: " + (dVal >= 0 ? "+" : "") +
                                ((Math.round(dVal) == dVal) ? String.valueOf((long) Math.round(dVal)) : String.valueOf(dVal)));
                    }

                    // Refresh the current money label immediately (if the label currently exists)
                    ButtonManager.refreshMoneyLabel(company);

                    // Also track locally (used by the event system if needed)
                    addMoneyDeltaForReport(dVal);
                    break;
                }

                case "REP":
                case "REPUTATION":
                    company.setReputation(company.getReputation() + iDelta);
                    break;

                // --------------------
                // Market modifiers from your Events.csv types
                // These change COSTS/VALUES in the CSV market files.
                // --------------------
                case "RESOURCEN":
                case "RESOURCES":
                    if (writer != null) writer.changeAllResourceMarketCosts(company, iDelta, reader);
                    if (writer != null) writer.addPendingEventNote("Resource costs " + (iDelta >= 0 ? "+" : "") + iDelta);
                    // Update UI immediately if the player is currently in the resource screen
                    ButtonManager.refreshMarketScreen(reader, company);
                    break;

                case "PRODUKTE":
                case "PRODUCTS":
                    if (writer != null) writer.changeAllProductMarketCosts(company, iDelta, reader);
                    if (writer != null) writer.addPendingEventNote("Product costs " + (iDelta >= 0 ? "+" : "") + iDelta);
                    break;

                case "MASCHINENGELD":
                case "MACHINECOST":
                case "MACHINES":
                    if (writer != null) writer.changeAllMachineMarketCosts(company, iDelta, reader);
                    if (writer != null) writer.addPendingEventNote("Machine costs " + (iDelta >= 0 ? "+" : "") + iDelta);
                    // (Equipment screen refresh can be added later if needed)
                    break;

                case "MASCHINENHALTBARKEIT":
                case "MACHINEDURABILITY":
                case "MACHINERELIABILITY":
                    if (writer != null) writer.changeAllMachineReliability(company, iDelta, reader);
                    if (writer != null) writer.addPendingEventNote("Machine durability " + (iDelta >= 0 ? "+" : "") + iDelta);
                    break;

                default:
                    // Unknown token -> ignore (keeps system forward compatible)
                    break;
            }
        }
    }

    /**
     * Update event state (cooldown + monthly counter) and persist:
     *  - EventCooldown.csv
     *  - CompanyData.csv
     */
    private void finishEventAndPersist(ReadCSVFiles reader, WriteCSVFiles writer, Company company, EventDef def) {
        if (currentEventId == null || writer == null || reader == null || company == null) return;

        int day = reader.readEventDay();

        // First 5 days: no events
        if (day < 5) {
            clear();
            return;
        }
        int monthIndex = day / DAYS_PER_MONTH;

        String[] st = state.get(currentEventId);
        if (st == null) {
            st = new String[] { currentEventId, "0", Integer.toString(monthIndex), "0" };
            state.put(currentEventId, st);
        }

        // month reset
        int savedMonth = parseIntSafe(st[2], monthIndex);
        if (savedMonth != monthIndex) {
            st[2] = Integer.toString(monthIndex);
            st[3] = "0";
        }

        if (def != null) {
            // monthly counter increment (only if there is a limit and not 'always')
            if (!def.always && def.maxPerMonth > 0) {
                int count = parseIntSafe(st[3], 0);
                st[3] = Integer.toString(count + 1);
            }

            // cooldown
            int nextAllowed = day + Math.max(0, def.cooldownDays);
            st[1] = Integer.toString(nextAllowed);
        }

        writer.writeEventCooldownState(state);

        // Save company after applying consequences
        writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
    }

    // -------------------------
    // Rendering + hitboxes
    // -------------------------

    private boolean contains(double rx, double ry, double rw, double rh, double x, double y) {
        return x >= rx && x <= (rx + rw) && y >= ry && y <= (ry + rh);
    }

    private boolean overlaps(double x1, double y1, double w1, double h1,
                             double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    /**
     * Draws the current event into the warning canvas.
     * @return true if an event was drawn, false if no active event exists.
     */
    public boolean draw(Canvas canvas, GraphicsContext pencil) {
        if (!active) return false;

        computeLayout(canvas);

        // Message panel
        pencil.save();
        pencil.setFill(Color.rgb(0, 0, 0, 0.55));
        pencil.fillRoundRect(lastMsgX, lastMsgY, lastMsgW, lastMsgH, 14, 14);
        pencil.setStroke(Color.WHITE);
        pencil.strokeRoundRect(lastMsgX, lastMsgY, lastMsgW, lastMsgH, 14, 14);

        pencil.setFont(msgFont);
        pencil.setFill(Color.WHITE);
        drawWrappedCenteredText(pencil, message, msgFont, lastMsgX, lastMsgY, lastMsgW, lastMsgH);
        pencil.restore();

        if (type == EventType.INFO) return true;

        // Options
        drawOptionBox(pencil, lastOpt[0][0], lastOpt[0][1], lastOpt[0][2], lastOpt[0][3], options[0]);
        drawOptionBox(pencil, lastOpt[1][0], lastOpt[1][1], lastOpt[1][2], lastOpt[1][3], options[1]);
        drawOptionBox(pencil, lastOpt[2][0], lastOpt[2][1], lastOpt[2][2], lastOpt[2][3], options[2]);
        drawOptionBox(pencil, lastOpt[3][0], lastOpt[3][1], lastOpt[3][2], lastOpt[3][3], options[3]);

        return true;
    }

    private void drawOptionBox(GraphicsContext pencil, double x, double y, double w, double h, String text) {
        pencil.save();
        pencil.setFill(Color.rgb(0, 0, 0, 0.55));
        pencil.fillRoundRect(x, y, w, h, 14, 14);
        pencil.setStroke(Color.WHITE);
        pencil.strokeRoundRect(x, y, w, h, 14, 14);

        pencil.setFont(optFont);
        pencil.setFill(Color.WHITE);
        drawWrappedCenteredText(pencil, text, optFont, x, y, w, h);

        pencil.restore();
    }

    /** Computes and caches layout based on current canvas size. */
    private void computeLayout(Canvas canvas) {
        // Renderer has a 20px top offset in the warning area.
        final double topOffset = 20.0;

        final double cw = canvas.getWidth();
        final double ch = canvas.getHeight();
        final double usableH = Math.max(1.0, ch - topOffset);

        // Responsive typography (base sizes)
        double baseMsgSize = clamp(14, 24, usableH * 0.22);
        double baseOptSize = clamp(12, 20, usableH * 0.18);

        // Responsive paddings + gap (base values)
        double basePadX = clamp(10, 22, usableH * 0.18);
        double basePadY = clamp(8, 18, usableH * 0.14);
        double baseOptPadX = clamp(10, 20, usableH * 0.16);
        double baseOptPadY = clamp(7, 16, usableH * 0.12);
        double gap = clamp(10, 24, usableH * 0.18);
        double margin = clamp(8, 18, usableH * 0.12);

        // Message max sizes so it stays nice in fullscreen
        // (For CHOICE, keep it a bit smaller to leave room for options)
        double msgMaxW = Math.max(220, cw * 0.72);
        double msgMaxH = Math.max(60, usableH * (type == EventType.CHOICE ? 0.58 : 0.70));

        // ---- Message box ----
        msgFont = Font.font("Arial", FontWeight.BOLD, baseMsgSize);

        // Wrap + measure message
        List<String> msgLines = wrapToWidth(message, msgFont, msgMaxW - 2 * basePadX);
        double msgLineH = textHeight("Ag", msgFont);
        double msgTextW = maxLineWidth(msgLines, msgFont);
        double msgTextH = msgLines.size() * msgLineH;

        double msgW = clamp(200, msgMaxW, msgTextW + 2 * basePadX);
        double msgH = clamp(52, msgMaxH, msgTextH + 2 * basePadY);

        double centerX = cw / 2.0;
        double centerY = topOffset + usableH / 2.0;

        lastMsgW = msgW;
        lastMsgH = msgH;
        lastMsgX = centerX - msgW / 2.0;
        lastMsgY = centerY - msgH / 2.0;

        // Clamp the message (very small windows)
        double[] msgClamped = clampRectToCanvas(topOffset, margin, cw, ch, lastMsgX, lastMsgY, lastMsgW, lastMsgH);
        lastMsgX = msgClamped[0];
        lastMsgY = msgClamped[1];
        lastMsgW = msgClamped[2];
        lastMsgH = msgClamped[3];

        if (type != EventType.CHOICE) {
            return;
        }

        // ---- Options ----
        // Option sizes depend on their own text (tight border),
        // but we also adaptively shrink them until they do NOT overlap the message or each other.
        double optMaxW = Math.max(120, cw * 0.26);
        double optMaxH = Math.max(34, usableH * 0.32);

        double optScale = 1.0;

        for (int iter = 0; iter < 10; iter++) {
            double optSize = clamp(10, baseOptSize, baseOptSize * optScale);
            optFont = Font.font("Arial", FontWeight.NORMAL, optSize);

            double optPadX = clamp(6, baseOptPadX, baseOptPadX * optScale);
            double optPadY = clamp(4, baseOptPadY, baseOptPadY * optScale);

            double[] optW = new double[4];
            double[] optH = new double[4];

            for (int i = 0; i < 4; i++) {
                List<String> lines = wrapToWidth(options[i], optFont, optMaxW - 2 * optPadX);
                double oTextW = maxLineWidth(lines, optFont);
                double oTextH = lines.size() * textHeight("Ag", optFont);

                optW[i] = clamp(80, optMaxW, oTextW + 2 * optPadX);
                optH[i] = clamp(28, optMaxH, oTextH + 2 * optPadY);
            }

            // Desired placement around the (already clamped) message box
            double desiredTopX = (lastMsgX + lastMsgW / 2.0) - optW[0] / 2.0;
            double desiredTopY = lastMsgY - gap - optH[0];

            double desiredRightX = lastMsgX + lastMsgW + gap;
            double desiredRightY = (lastMsgY + lastMsgH / 2.0) - optH[1] / 2.0;

            double desiredBottomX = (lastMsgX + lastMsgW / 2.0) - optW[2] / 2.0;
            double desiredBottomY = lastMsgY + lastMsgH + gap;

            double desiredLeftX = lastMsgX - gap - optW[3];
            double desiredLeftY = (lastMsgY + lastMsgH / 2.0) - optH[3] / 2.0;

            setOptBoundsClamped(0, desiredTopX, desiredTopY, optW[0], optH[0], topOffset, margin, cw, ch);
            setOptBoundsClamped(1, desiredRightX, desiredRightY, optW[1], optH[1], topOffset, margin, cw, ch);
            setOptBoundsClamped(2, desiredBottomX, desiredBottomY, optW[2], optH[2], topOffset, margin, cw, ch);
            setOptBoundsClamped(3, desiredLeftX, desiredLeftY, optW[3], optH[3], topOffset, margin, cw, ch);

            boolean bad = false;

            // options must not overlap the message
            for (int i = 0; i < 4; i++) {
                if (overlaps(lastOpt[i][0], lastOpt[i][1], lastOpt[i][2], lastOpt[i][3],
                        lastMsgX, lastMsgY, lastMsgW, lastMsgH)) {
                    bad = true;
                    break;
                }
            }

            // options must not overlap each other
            if (!bad) {
                for (int i = 0; i < 4 && !bad; i++) {
                    for (int j = i + 1; j < 4; j++) {
                        if (overlaps(lastOpt[i][0], lastOpt[i][1], lastOpt[i][2], lastOpt[i][3],
                                lastOpt[j][0], lastOpt[j][1], lastOpt[j][2], lastOpt[j][3])) {
                            bad = true;
                            break;
                        }
                    }
                }
            }

            if (!bad) {
                // Found a layout that fits without overlaps
                return;
            }

            optScale *= 0.88;
            if (optScale < 0.55) {
                // Give up shrinking further; keep the last computed layout (best effort).
                return;
            }
        }
    }

    private void setOptBoundsClamped(int idx, double x, double y, double w, double h,
                                     double topOffset, double margin, double cw, double ch) {
        double[] r = clampRectToCanvas(topOffset, margin, cw, ch, x, y, w, h);
        lastOpt[idx][0] = r[0];
        lastOpt[idx][1] = r[1];
        lastOpt[idx][2] = r[2];
        lastOpt[idx][3] = r[3];
    }

    /**
     * Clamps a rect inside the canvas (returns possibly adjusted x/y; shrinks if bigger than canvas).
     */
    private double[] clampRectToCanvas(double topOffset, double margin, double cw, double ch,
                                       double x, double y, double w, double h) {
        // shrink if bigger than available
        double maxW = Math.max(1.0, cw - 2 * margin);
        double maxH = Math.max(1.0, (ch - topOffset) - 2 * margin);
        if (w > maxW) w = maxW;
        if (h > maxH) h = maxH;

        double minX = margin;
        double maxX = cw - margin - w;
        double minY = topOffset + margin;
        double maxY = ch - margin - h;

        x = clamp(minX, maxX, x);
        y = clamp(minY, maxY, y);

        return new double[] { x, y, w, h };
    }

    private double clamp(double min, double max, double v) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    // ---- Text helpers ----

    private double textWidth(String s, Font f) {
        Text t = new Text(s == null ? "" : s);
        t.setFont(f);
        return t.getLayoutBounds().getWidth();
    }

    private double textHeight(String s, Font f) {
        Text t = new Text(s == null ? "" : s);
        t.setFont(f);
        return t.getLayoutBounds().getHeight();
    }

    private double maxLineWidth(List<String> lines, Font f) {
        double m = 0;
        for (String line : lines) {
            m = Math.max(m, textWidth(line, f));
        }
        return m;
    }

    /**
     * Simple word-wrap that tries to keep each line <= maxWidth.
     * If a single word is longer than maxWidth, it will still be placed on its own line.
     */
    private List<String> wrapToWidth(String text, Font font, double maxWidth) {
        List<String> out = new ArrayList<>();
        String s = text == null ? "" : text.trim();
        if (s.isEmpty()) {
            out.add("");
            return out;
        }

        String[] words = s.split("\\s+");
        StringBuilder line = new StringBuilder();

        for (String w : words) {
            if (line.length() == 0) {
                line.append(w);
                continue;
            }

            String candidate = line + " " + w;
            if (textWidth(candidate, font) <= maxWidth) {
                line.append(" ").append(w);
            } else {
                out.add(line.toString());
                line.setLength(0);
                line.append(w);
            }
        }
        if (line.length() > 0) out.add(line.toString());
        return out;
    }

    private void drawWrappedCenteredText(GraphicsContext pencil, String text, Font font,
                                        double x, double y, double w, double h) {
        // Keep wrapping stable (use a small internal pad for wrapping width only)
        double wrapPad = Math.max(8, Math.min(18, h * 0.22));
        List<String> lines = wrapToWidth(text, font, Math.max(1, w - 2 * wrapPad));

        double lineH = textHeight("Ag", font);
        double totalH = lines.size() * lineH;

        // baseline Y of first line
        double startY = y + (h - totalH) / 2.0 + lineH * 0.82;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            double lw = textWidth(line, font);
            double tx = x + (w - lw) / 2.0;
            double ty = startY + i * lineH;
            pencil.fillText(line, tx, ty);
        }
    }
}
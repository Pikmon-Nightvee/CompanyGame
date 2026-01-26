package ExternalResources;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class GraphicsManager {
	private String folder = "/Graphic/";
	
	//Menu:
	private Image icon;
	private Image backgroundMenu;
	
	private BackgroundImage buttonImgUnpressed;
	private Background buttonUnpressed;
	
	private BackgroundImage buttonImgPressed;
	private Background buttonPressed;
	
	private BackgroundImage buttonImgHover;
	private Background buttonHover;

	//UIGameState:
	private Image backgroundUI;
	private Image warningCanvasOverlay;
	
	private BackgroundImage ComboImgUnpressed;
	private Background ComboUnpressed;
	
	private BackgroundImage ComboImgPressed;
	private Background ComboPressed;
	
	private BackgroundImage ComboImgHover;
	private Background ComboHover;
	
	//ChatGPT to make the Background button size:
	private BackgroundSize bgSize = new BackgroundSize(
	        100, 100,      // width, height
	        true, true,    // width & height are percentages
	        true, false   // contain = true, cover = false
	);

	
	public void loadMenu() {
		try {
			String filePath = "Icon.png";
			filePath = folder + filePath;
			icon = new Image(getClass().getResource(filePath).toExternalForm());

			filePath = "MenuImage.png";
			filePath = folder + filePath;
			backgroundMenu = new Image(getClass().getResource(filePath).toExternalForm());

			filePath = "ButtonUnpressed.png";
			filePath = folder + filePath;
			buttonImgUnpressed = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			buttonUnpressed = new Background(buttonImgUnpressed);

			filePath = "Buttonpressed.png";
			filePath = folder + filePath;
			buttonImgPressed = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			buttonPressed = new Background(buttonImgPressed);

			filePath = "ButtonHover.png";
			filePath = folder + filePath;
			buttonImgHover = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			buttonHover = new Background(buttonImgHover);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadUI() {
		try {
			String filePath = "UIBackgroundImage.png";
			filePath = folder + filePath;
			backgroundUI = new Image(getClass().getResource(filePath).toExternalForm());

			filePath = "WarningCanvas.png";
			filePath = folder + filePath;
			warningCanvasOverlay = new Image(getClass().getResource(filePath).toExternalForm());

			filePath = "ComboBoxNormal.png";
			filePath = folder + filePath;
			ComboImgUnpressed = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			ComboUnpressed = new Background(ComboImgUnpressed);

			filePath = "ComboBoxClick.png";
			filePath = folder + filePath;
			ComboImgPressed = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			ComboPressed = new Background(ComboImgPressed);

			filePath = "ComboBoxHover.png";
			filePath = folder + filePath;
			ComboImgHover = new BackgroundImage(new Image(getClass().getResource(filePath).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
			ComboHover = new Background(ComboImgHover);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Image getIcon() {
		return icon;
	}

	public Image getBackgroundMenu() {
		return backgroundMenu;
	}

	public Image getBackgroundUI() {
		return backgroundUI;
	}

	public Image getWarningCanvasOverlay() {
		return warningCanvasOverlay;
	}

	public Background getButtonUnpressed() {
		return buttonUnpressed;
	}

	public Background getButtonPressed() {
		return buttonPressed;
	}

	public Background getButtonHover() {
		return buttonHover;
	}

	public Background getComboUnpressed() {
		return ComboUnpressed;
	}

	public Background getComboPressed() {
		return ComboPressed;
	}

	public Background getComboHover() {
		return ComboHover;
	}
}

package tictactoeGUI;

import javafx.scene.control.Button;
import javafx.stage.Screen;

public class BlockButton extends Button{
	public static final String BACKGROUND = "-fx-text-fill: gainsboro; -fx-background-color: gainsboro; -fx-border-color: rgba(0, 0, 0, 0.25); -fx-border-width: 1px; -fx-border-radius:100px; -fx-background-radius: 100px;";
	public static final String FILLED = BACKGROUND+"-fx-background-color: yellow;";
	public BlockButton() {
		this.setPrefSize(Screen.getPrimary().getBounds().getWidth()/12, Screen.getPrimary().getBounds().getWidth()/12);
		this.restore();
	}
	public void restore() {
		this.setText("");
		this.setStyle(BACKGROUND);
	}
	public void fill(Occupant occupant) {
		this.setStyle(BACKGROUND + "-fx-background-color: " + occupant.getColor() + ";" + "-fx-text-fill: " + occupant.getColor());
		this.setText(occupant.getText());
	}
}

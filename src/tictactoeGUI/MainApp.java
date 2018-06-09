package tictactoeGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {

	/*
	 * To do:
	 * 4. Github
	 * 
	 * 
	 */
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Wuziqi");
		int width = 19;
		int height = 19;
		Playable game = new Game(width, height);
		BorderPane root = new BorderPane();
		root.setCenter(new BoardGUI(width, height, game));
		HBox gameControlPanel = new HBox();
		Button end = new Button("END GAME");
		end.setOnAction(e -> game.onEnd());
		Button undo = new Button("UNDO");
		undo.setOnAction(e -> game.undo());
		gameControlPanel.getChildren().addAll(end, undo);
		root.setBottom(gameControlPanel);
		double size = Screen.getPrimary().getBounds().getWidth() > Screen.getPrimary().getBounds().getHeight() ? Screen.getPrimary().getBounds().getHeight()/2 : Screen.getPrimary().getBounds().getWidth()/2;
		primaryStage.setScene(new Scene(root, size, size ));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}

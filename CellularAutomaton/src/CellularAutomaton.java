// Cellular Automaton https://en.wikipedia.org/wiki/Cellular_automaton

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class CellularAutomaton extends Application {
	
	int SIZE_OF_RECTANGLE = 10;
	int NUMBER_OF_ROWS = 91;
	int NUMBER_OF_COLUMNS = NUMBER_OF_ROWS * 2;
	
	private int rule = 0;
	Scene initialScene, scene;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage window) throws Exception {
		GridPane grid = new GridPane();
		scene = new Scene(grid, NUMBER_OF_COLUMNS * SIZE_OF_RECTANGLE, NUMBER_OF_ROWS * SIZE_OF_RECTANGLE);
		
		TextField input = new TextField();
		input.setAlignment(Pos.CENTER);
		input.setMaxWidth(100);
		Label label = new Label("Enter a rule number (default is 0):");
		Button submit = new Button();
		submit.setText("Generate");

		submit.setOnAction(e -> { 
			setRule(input.getText());
			buildGrid(grid);	
			window.setScene(scene);
		});
		
		input.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				submit.fire();
			}
		});

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label, input, submit);
		layout.setAlignment(Pos.CENTER);
		initialScene = new Scene(layout, NUMBER_OF_COLUMNS * SIZE_OF_RECTANGLE, NUMBER_OF_ROWS *SIZE_OF_RECTANGLE);
		
		window.setTitle("Cellular Automata");
		window.setScene(initialScene);
		window.show(); 
	}

	public void setRule(String input) {
		try {
			if (Integer.parseInt(input) < 256) {
				this.rule = Integer.parseInt(input);
			}
		} 
		catch (NumberFormatException e) {
		}
	}
	
	public GridPane buildGrid(GridPane grid) {
		boolean[] previous = new boolean[NUMBER_OF_COLUMNS];
		boolean[] current = new boolean[NUMBER_OF_COLUMNS];
		previous[NUMBER_OF_COLUMNS/2 + 1] = true;
		draw(grid, true, NUMBER_OF_COLUMNS/2 + 1, 0);
		
		for (int i = 1; i < NUMBER_OF_ROWS - 1; i++) {
			for (int j = 1; j < NUMBER_OF_COLUMNS - 1; j++) {
				current[j] = getRule(getRuleIndex(previous[j+1], previous[j], previous[j-1]));
				draw(grid, current[j], j, i);
			}
			previous = current.clone();
			current = new boolean[NUMBER_OF_COLUMNS];
		}
		return grid;
	}
	
	public boolean getRule(int index) {
		String bin = Integer.toBinaryString(this.rule);
		String[] binArray = bin.split("");
		
		if (index >= bin.length()) {
			return false;
		} else {
			return (binArray[(bin.length() - 1) - index].equals("1") ? true : false);
		}
	}
	
	public void draw(GridPane grid, boolean black, int column, int row) {
		Color color = black ? Color.BLACK : Color.WHITE;
		grid.add(new Rectangle(SIZE_OF_RECTANGLE, SIZE_OF_RECTANGLE, color), column, row);
	}
	
	public int getRuleIndex(boolean num1, boolean num2, boolean num3) {
		return (num1 ? 1 : 0) + (num2 ? 1 : 0) * 2 + (num3 ? 1 : 0) * 4;
	}
}

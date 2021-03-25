package lipid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConvinienceMethodsExample extends Application
{
	private Circle currentCircle;

	@Override
	public void start(Stage stage)
	{
		currentCircle = newCircle(300.0, 135.0);
		// Creating a Group object
		Group root = new Group(currentCircle);

		// Creating a scene object
		Scene scene = new Scene(root, 600, 300);
		scene.setOnMouseClicked(e -> {
			Circle newS = newCircle(e.getSceneX(), e.getSceneY());
			root.getChildren().add(newS);
		});
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			switch(key.getCode())
			{
			case RIGHT:
				currentCircle.setCenterX(currentCircle.getCenterX() + 5);
				break;
			case LEFT:
				currentCircle.setCenterX(currentCircle.getCenterX() - 5);
				break;
			case UP:
				currentCircle.setCenterY(currentCircle.getCenterY() - 5);
				break;
			case DOWN:
				currentCircle.setCenterY(currentCircle.getCenterY() + 5);
				break;
			default:
				break;
			}
		});

		Timeline tick = TimelineBuilder.create()// creates a new Timeline
				.keyFrames(new KeyFrame(new Duration(10),// This is how often it updates in milliseconds
						new EventHandler<ActionEvent>(){
							public void handle(ActionEvent t)
							{
								// You put what you want to update here
							}
						}))
				.cycleCount(Timeline.INDEFINITE).build();
		tick.play();// Starts the timeline
		scene.setFill(Color.LAVENDER);

		// Setting title to the Stage
		stage.setTitle("Convenience Methods Example");

		// Adding scene to the stage
		stage.setScene(scene);

		// Displaying the contents of the stage
		stage.show();
	}

	private Circle newCircle(double x, double y)
	{
		// Drawing a Circle
		Circle circle = new Circle();

		// Setting the position of the circle
		circle.setCenterX(x);
		circle.setCenterY(y);

		// Setting the radius of the circle
		circle.setRadius(25.0f);

		// Setting the color of the circle
		circle.setFill(Color.BROWN);

		// Setting the stroke width of the circle
		circle.setStrokeWidth(20);

		circle.setOnMouseClicked(e -> {
			e.consume();
			currentCircle = circle;
			circle.setFill(Color.DARKSLATEBLUE);
		});
		return circle;
	}

	public static void main(String args[])
	{
		launch(args);
	}
}

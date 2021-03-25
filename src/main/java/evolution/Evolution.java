/* Copyright 2019 jonatanjonsson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package evolution;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import physics.ObjectUpdatedListener;
import physics.PhysicalObject;
import physics.Vec2;
import physics.World;

@SuppressWarnings("restriction")
public class Evolution extends Application
{
	Engine engine = new Engine();
	World world = new World();

	// private final Text velocityDisplay;
	private final Map<PhysicalObject, Node> shapes = new HashMap<>();

	private final ObjectUpdatedListener uiUpdater = new ObjectUpdatedListener(){

		@Override
		public void wasRotated(PhysicalObject obj, int newTotalDegrees)
		{
			shapes.get(obj).rotateProperty().set(newTotalDegrees + 90);
		}

		@Override
		public void newPosition(PhysicalObject obj, Vec2 pos)
		{
			// velocityDisplay.textProperty().set(obj.toString());
			shapes.get(obj).relocate(pos.magnitude(), pos.direction());
		}
	};

	public Evolution()
	{
		// velocityDisplay = new Text(200, 20, "A");
	}

	@Override
	public void start(Stage stage)
	{
		engine.start();

		Vec2 positionOfCar = new Vec2(30, 200);
		Rectangle carUi = new Rectangle(positionOfCar.magnitude(), positionOfCar.direction(), 10, 20);
		carUi.rotateProperty().set(90);
		PhysicalObject car = new PhysicalObject(positionOfCar, uiUpdater);
		shapes.put(car, carUi);
		world.addObject(car);
		// TODO: how to group different shapes? velocityDisplay
		// Group uiElements = new Group(directionArrow, waves);

		Group root = new Group(shapes.values());
		// root.getChildren().add(uiElements);

		// Creating a scene object
		Scene scene = new Scene(root, 1000, 1000);

		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			// final World w = world;

			switch(key.getCode())
			{
			case Q:
				// amplitude++;
				break;
			}
		});
		// scene.setCamera(cam);
		scene.setFill(Color.LAVENDER);

		// Setting title to the Stage
		stage.setTitle("Physics Playground");

		// Adding scene to the stage
		stage.setScene(scene);

		engine.startSignal.countDown();
		// Displaying the contents of the stage
		stage.show();
	}

	private class Engine extends Thread
	{
		float desiredFrameRate = 60.0f;
		CountDownLatch startSignal = new CountDownLatch(1);

		@Override
		public void run()
		{
			try
			{
				startSignal.await();
			}
			catch(InterruptedException e)
			{
				return;
			}
			RateLimiter fpsLimiter = RateLimiter.create(desiredFrameRate);
			while(true)
			{
				boolean aquired = fpsLimiter.tryAcquire(1, TimeUnit.SECONDS);
				if(Thread.currentThread().isInterrupted())
					return;
				if(!aquired)
				{
					System.err.println("Slow FPS... Waiting to step until UI catches up");
					continue;
				}
				// System.out.println("Waiting time for frame: " + waitingTime);
				// step();
			}
		}
	}

	public static void main(String args[])
	{
		launch(args);
	}

	@Override
	public void stop() throws Exception
	{
		engine.interrupt();
	}
}

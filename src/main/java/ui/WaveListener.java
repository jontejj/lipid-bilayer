/* Copyright 2018 jonatanjonsson
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
package ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import physics.ObjectUpdatedListener;
import physics.PhysicalObject;
import physics.Vec2;

@SuppressWarnings("restriction")
public class WaveListener implements ObjectUpdatedListener
{
	private final Group waveGroup;
	private double previousEndX;
	private double previousEndY;
	private int memory;
	private int index;

	public WaveListener(int memory, Vec2 start, Group waveGroup)
	{
		this.memory = memory;
		this.previousEndX = start.magnitude();
		this.previousEndY = start.direction();
		this.waveGroup = waveGroup;

		ObservableList<Node> children = waveGroup.getChildren();
		for(int i = 0; i < memory; i++)
		{
			children.add(new Line(previousEndX, previousEndY, previousEndX, previousEndY));
		}
		// Arc arc = new Arc();
		// arc.setCenterX(50.0f);
		// arc.setCenterY(50.0f);
		// arc.setRadiusX(50.0f);
		// arc.setRadiusY(50.0f);
		// arc.setStartAngle(180.0f);
		// arc.setLength(180.0f);
		// arc.setStrokeWidth(1);
		// arc.setStroke(Color.CORAL);
		// arc.setStrokeType(StrokeType.INSIDE);
		// arc.setFill(null);
		// arc.setType(ArcType.OPEN);
	}

	@Override
	public void wasRotated(PhysicalObject obj, int newTotalDegrees)
	{

	}

	@Override
	public void newPosition(PhysicalObject obj, Vec2 pos)
	{
		Line line = (Line) waveGroup.getChildren().get(index % memory);

		final double cPreviousEndX = previousEndX;
		final double cpreviousEndY = previousEndY;
		Platform.runLater(() -> {
			line.setStartX(cPreviousEndX);
			line.setStartY(cpreviousEndY);
			line.setEndX(pos.magnitude());
			line.setEndY(pos.direction());
		});

		previousEndX = pos.magnitude();
		previousEndY = pos.direction();
		index++;
	}
}

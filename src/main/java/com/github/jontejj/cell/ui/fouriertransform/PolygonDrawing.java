/* Copyright 2021 jonatanjonsson
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
package com.github.jontejj.cell.ui.fouriertransform;

import java.util.List;

import com.github.jontejj.cell.physics.Vec2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class PolygonDrawing extends Application
{
	private final List<Vec2> points;

	public PolygonDrawing(List<Vec2> points)
	{
		this.points = points;
	}

	@Override
	public void start(Stage stage)
	{
		Group polygon = new Group();
		Path directionArrow = new Path();
		Vec2 previousPoint = points.get(0);
		for(Vec2 point : points.subList(1, points.size()))
		{
			directionArrow.getElements().add(new MoveTo(previousPoint.x(), previousPoint.y()));
			directionArrow.getElements().add(new LineTo(point.x(), point.y()));
			previousPoint = point;
		}

		Scene scene = new Scene(polygon, 1000, 1000);

		scene.setFill(Color.LAVENDER);

		// Setting title to the Stage
		stage.setTitle("Cycle Playground");

		stage.setScene(scene);

		stage.show();
	}

	@Override
	public void stop() throws Exception
	{
	}
}

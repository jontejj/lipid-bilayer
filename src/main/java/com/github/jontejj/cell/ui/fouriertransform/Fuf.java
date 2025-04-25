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
import com.google.common.collect.Lists;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class Fuf extends Application
{
	/**
	 * 10 "seconds"
	 */
	private static final int SECONDS = 10;
	private static final int VALUES_PER_SECOND = 100;
	private static final int TOTAL_VALUES = SECONDS * VALUES_PER_SECOND;
	private static final double[] generatedData;
	// private static XYSeriesDemo CENTER_OF_MASS_ANALYSIS;
	private Group root;
	private static Group polygon;

	static
	{
		generatedData = new double[TOTAL_VALUES];
		for(int i = 0; i < TOTAL_VALUES; i++)
		{
			double sum = a(i) + b(i) + c(i);
			System.out.print(", " + sum);
			generatedData[i] = sum;
		}
	}

	private void centerOfMassAnalysis(double cyclesPerSecond)
	{
		List<Vec2> plottedData = Lists.newArrayList();
		// Draw data series as circles using a rotating vector
		int cycles = (int) (cyclesPerSecond * SECONDS);
		int valuesPerCycle = TOTAL_VALUES / cycles;
		for(int cycle = 0; cycle < cycles; cycle++)
		{
			double degreesPerValue = 360 / valuesPerCycle;
			Vec2 rotatingVector = new Vec2(0, 0);
			for(int i = 0; i < valuesPerCycle; i++)
			{
				int index = i + cycle * i;
				double valueToPlot = generatedData[index];
				rotatingVector = rotatingVector.add(new Vec2(valueToPlot, 0));
				// Convert rotatingVector to XY coordinates
				Vec2 coordinate = Vec2.accelerateInDirection(rotatingVector.magnitude(), rotatingVector.direction());
				plottedData.add(coordinate);

				rotatingVector = new Vec2(0, rotatingVector.direction() + degreesPerValue);
			}
		}
		System.out.println(plottedData);

		if(polygon != null)
		{
			root.getChildren().remove(polygon);
		}
		polygon = new Group();
		Path paths = new Path();
		Vec2 previousPoint = plottedData.get(0);
		for(Vec2 point : plottedData.subList(1, plottedData.size()))
		{
			paths.getElements().add(new MoveTo(previousPoint.x(), previousPoint.y()));
			paths.getElements().add(new LineTo(point.x(), point.y()));
			previousPoint = point;
		}
		root.getChildren().add(paths);

		// if(CENTER_OF_MASS_ANALYSIS != null)
		// {
		// CENTER_OF_MASS_ANALYSIS.dispose();
		// }
		// CENTER_OF_MASS_ANALYSIS = new XYSeriesDemo(plottedData);
		// CENTER_OF_MASS_ANALYSIS.pack();
		// CENTER_OF_MASS_ANALYSIS.setVisible(true);
		// RefineryUtilities.centerFrameOnScreen(CENTER_OF_MASS_ANALYSIS);
	}

	private static double a(int n)
	{
		// 1 second per cycle
		return Math.sin(periodify(n, 100) + 3);
	}

	private static double b(int n)
	{
		// 0.5 second per cycle
		return Math.sin(periodify(n, 50) + 6);
	}

	private static double c(int n)
	{
		return Math.sin(periodify(n, 200) + 0);
	}

	private static double periodify(int n, int period)
	{
		int v = n % period;
		double stepValue = v * (2 * Math.PI) / period;
		return stepValue;
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		// For DTFT -> 4 is the frequency variable for this input (i.e one cycle contains 4 values)
		// final XYSeriesDemo demo = new XYSeriesDemo(generatedData);
		// demo.pack();
		// RefineryUtilities.centerFrameOnScreen(demo);
		// demo.setVisible(true);
		// TODO: jfree does not use JPMS modules so I removed XYSeriesDemo

		Slider.create(this::centerOfMassAnalysis);
		root = new Group();

		Scene scene = new Scene(root, 1000, 1000);

		scene.setFill(Color.LAVENDER);

		// Setting title to the Stage
		stage.setTitle("Cycle Playground");

		stage.setScene(scene);

		stage.show();
		centerOfMassAnalysis(0.5);
	}
}

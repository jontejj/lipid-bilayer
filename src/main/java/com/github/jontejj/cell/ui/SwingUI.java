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
package com.github.jontejj.cell.ui;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.github.jontejj.cell.physics.ObjectUpdatedListener;
import com.github.jontejj.cell.physics.PhysicalObject;
import com.github.jontejj.cell.physics.Rectangle;
import com.github.jontejj.cell.physics.Vec2;
import com.github.jontejj.cell.physics.World;

public class SwingUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final MyCanvas myCanvas;

	public SwingUI()
	{
		World world = new World();
		myCanvas = new MyCanvas(world);

		PhysicalObject flyingPlate = new Rectangle(50, 50, new Vec2(20, 100), ObjectUpdatedListener.VOID);
		flyingPlate.applyForceInCurrentDirection(1);

		PhysicalObject collidingPlate = new Rectangle(50, 50, new Vec2(104, 100), ObjectUpdatedListener.VOID);
		collidingPlate.rotate(180);
		collidingPlate.applyForceInCurrentDirection(3);
		// Rectangle rectangle = new Rectangle(60, 40, new Vec2(100, 150), ObjectUpdatedListener.VOID);
		world.addObject(flyingPlate);
		world.addObject(collidingPlate);
		// world.control(rectangle);
		// world.applyForceToControllableObjectInCurrentDirection(1.0f);
		UIEngine engine = new UIEngine(myCanvas);
		engine.start();

		engine.startSignal.countDown();
		initUI();
	}

	private void initUI()
	{

		add(myCanvas);

		setTitle("Simulation");
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		// Math.fm
		// System.out.println(System.getenv());
		// System.out.println(System.getProperties());
		// System.out.println(FloatVector.SPECIES_PREFERRED.length());
		EventQueue.invokeAndWait(new Runnable(){
			@Override
			public void run()
			{
				SwingUI ex = new SwingUI();
				ex.setVisible(true);
			}
		});
	}
}

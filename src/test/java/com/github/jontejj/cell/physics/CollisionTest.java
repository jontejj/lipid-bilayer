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
package com.github.jontejj.cell.physics;

import org.junit.jupiter.api.Test;

import com.github.jontejj.cell.physics.ObjectUpdatedListener;
import com.github.jontejj.cell.physics.PhysicalObject;
import com.github.jontejj.cell.physics.Rectangle;
import com.github.jontejj.cell.physics.Vec2;
import com.github.jontejj.cell.physics.World;

public class CollisionTest
{
	@Test
	void testThatCollisionsAreDetectedAndResultsInElasticCollision() throws Exception
	{
		ObjectUpdatedListener listener = new ObjectUpdatedListener(){
			@Override
			public void wasRotated(PhysicalObject obj, int newTotalDegrees)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void newPosition(PhysicalObject obj, Vec2 pos)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void collidedWith(PhysicalObject other)
			{
				System.out.println("Collision with " + other);
			}
		};
		// double expectedXVelocity = 50 - 30 - (45 * Math.cos(Math.toRadians(30)));
		// double expectedYVelocity = 15 - 28 + (45 * Math.sin(Math.toRadians(30)));
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingPlate = new Rectangle(50, 50, new Vec2(50, 100), listener);
		flyingPlate.applyForceInCurrentDirection(1);

		PhysicalObject collidingPlate = new Rectangle(50, 50, new Vec2(104, 100), ObjectUpdatedListener.VOID);
		collidingPlate.rotate(180);
		collidingPlate.applyForceInCurrentDirection(1);

		World world = new World();
		world.addObject(flyingPlate);
		world.addObject(collidingPlate);

		world.step();
		world.step();

		System.out.println(flyingPlate);
		System.out.println(collidingPlate);
		// assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity), Delta.delta(0.1));
	}
}

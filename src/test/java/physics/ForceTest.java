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
package physics;

import static org.fest.assertions.CustomAssertions.assertThat;

import org.fest.assertions.Delta;
import org.junit.jupiter.api.Test;

public class ForceTest
{
	final ObjectUpdatedListener voidListener = new ObjectUpdatedListener(){

		@Override
		public void wasRotated(PhysicalObject obj, int newTotalDegrees)
		{
		}

		@Override
		public void newPosition(PhysicalObject obj, Vec2 pos)
		{
		}
	};

	@Test
	void testThatForceCanBeAppliedInDifferentDirections() throws Exception
	{
		double expectedXVelocity = 50 - 30 - (45 * Math.cos(Math.toRadians(30)));
		double expectedYVelocity = 15 - 28 + (45 * Math.sin(Math.toRadians(30)));
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(50, 100), voidListener);
		Vec2 newVelocity = flyingSaucer.applyForceInCurrentDirection(50);
		flyingSaucer.rotate(180);
		flyingSaucer.applyForceInCurrentDirection(30);
		flyingSaucer.rotate(30);
		newVelocity = flyingSaucer.applyForceInCurrentDirection(45);
		flyingSaucer.rotate(60);
		newVelocity = flyingSaucer.applyForceInCurrentDirection(15);
		flyingSaucer.rotate(180);
		newVelocity = flyingSaucer.applyForceInCurrentDirection(28);

		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity), Delta.delta(0.1));
	}

	@Test
	void testThatForceForDefaultRotationMeansToTheRight() throws Exception
	{
		double expectedXVelocity = 20;
		double expectedYVelocity = 0;
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
		Vec2 newVelocity = flyingSaucer.applyForceInCurrentDirection(20);
		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity));
	}

	@Test
	void testThatDownwardsForceCanBeApplied() throws Exception
	{
		double expectedXVelocity = 0;
		double expectedYVelocity = -20;
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
		flyingSaucer.rotate(90);
		Vec2 newVelocity = flyingSaucer.applyForceInCurrentDirection(20);
		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity));
	}

	@Test
	void testThatBackwardsForceCanBeApplied() throws Exception
	{
		double expectedXVelocity = -20;
		double expectedYVelocity = 0;
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
		flyingSaucer.rotate(180);
		Vec2 newVelocity = flyingSaucer.applyForceInCurrentDirection(20);
		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity));
	}

	@Test
	void testThatUpwardsForceCanBeApplied() throws Exception
	{
		double expectedXVelocity = 0;
		double expectedYVelocity = 20;
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
		flyingSaucer.rotate(-90);
		Vec2 newVelocity = flyingSaucer.applyForceInCurrentDirection(20);
		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity));
	}

	@Test
	void testThatOneForceInEachDirectionSumsToZeroNetForce() throws Exception
	{
		double expectedXVelocity = 0.000000;
		double expectedYVelocity = -0.000000;
		// Default mass = 1, Rotation = 0
		PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
		Vec2 newVelocity = null;
		for(int a = 0; a < 360; a++)
		{
			flyingSaucer.rotate(1);
			newVelocity = flyingSaucer.applyForceInCurrentDirection(1);
		}
		assertThat(newVelocity).isEqualTo(new Vec2(expectedXVelocity, expectedYVelocity), Delta.delta(1e-10));
	}

	@Test
	void testThatTheOppositeForceCancellesOutFirstForce() throws Exception
	{
		Vec2 zero = new Vec2(0, 0);
		// Default mass = 1, Rotation = 0
		Vec2 newVelocity = null;
		for(int force = -5; force < 5; force++)
		{
			final int currentForce = force;
			for(int a = 0; a < 360; a++)
			{
				final int currentAngle = a;
				PhysicalObject flyingSaucer = new PhysicalObject(new Vec2(0, 0), voidListener);
				flyingSaucer.rotate(a);
				flyingSaucer.applyForceInCurrentDirection(force);
				flyingSaucer.rotate(180);
				newVelocity = flyingSaucer.applyForceInCurrentDirection(force);
				assertThat(newVelocity).as(() -> "opposite force " + currentForce + " did not work for rotation " + currentAngle).isEqualTo(zero);
			}
		}
	}
}

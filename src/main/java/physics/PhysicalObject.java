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

public class PhysicalObject
{
	private final ObjectUpdatedListener listener;

	private Vec2 position;

	/**
	 * Ineartia of the object, in kg
	 */
	private float mass = 1;

	/**
	 * 1N = 1pixel for simplicity's sake. Represents current velocity in x and y. +x means right, +y means up.
	 */
	private Vec2 velocity = new Vec2(0, 0);

	/**
	 * Clockwise. Default rotation is 0, which means that if there is any velocity, the object is going to the right. Goes from 0 to 359.
	 */
	private int rotation = 0;

	public PhysicalObject(Vec2 position, ObjectUpdatedListener listener)
	{
		this.position = position;
		this.listener = listener;
	}

	public void rotate(int value)
	{
		rotation += value;
		rotation = rotation % 360;
		if(rotation < 0)
		{
			rotation += 360;
		}
		listener.wasRotated(this, rotation);
	}

	/**
	 * @return the new velocity vector
	 */
	public Vec2 applyForceInCurrentDirection(float force)
	{
		// F=m*a, a = F / m
		int directionQuadrant = (rotation / 90) % 4;
		// int = BigDecimal.valueOf(rotation / 90f).round(new MathContext(1, RoundingMode.UP)).intValueExact();
		int localAngleInDegrees = rotation % 90;
		double localAngleInRadians = Math.toRadians(localAngleInDegrees);
		Vec2 acceleration = null;
		double cosAcceleration = force / mass;
		double sinAcceleration = force / mass;
		if(localAngleInRadians != 0)
		{
			sinAcceleration *= Math.sin(localAngleInRadians);
			cosAcceleration *= Math.cos(localAngleInRadians);
			switch(directionQuadrant)
			{
			case 0: // Down-Right 1-89
				acceleration = new Vec2(cosAcceleration, -sinAcceleration);
				break;
			case 1: // Down-Left 91-179
				acceleration = new Vec2(-sinAcceleration, -cosAcceleration);
				break;
			case 2: // Up-Left 181-269
				acceleration = new Vec2(-cosAcceleration, sinAcceleration);
				break;
			case 3: // Up-Right 271-359
				acceleration = new Vec2(sinAcceleration, cosAcceleration);
				break;
			}
		}
		else
		{
			switch(directionQuadrant)
			{
			case 0: // 0
				acceleration = new Vec2(sinAcceleration, 0);
				break;
			case 1: // 90
				acceleration = new Vec2(0, -sinAcceleration);
				break;
			case 2: // 180
				acceleration = new Vec2(-sinAcceleration, 0);
				break;
			case 3: // 270
				acceleration = new Vec2(0, sinAcceleration);
				break;
			}
		}
		velocity = velocity.add(acceleration);
		// System.out.println("Applied force " + force + " in " + directionQuadrant + " with " + localAngleInDegrees + " degrees. Mass:" +
		// mass + ",A="
		// + acceleration);
		// System.out.println("New velocity: " + velocity);
		return velocity;
	}

	public void step()
	{
		move(velocity);
	}

	private void move(Vec2 change)
	{
		position = position.add(change.mul(new Vec2(1, -1)));
		listener.newPosition(this, position);
	}

	protected void moveTo(Vec2 absolutePos)
	{
		position = absolutePos;
		listener.newPosition(this, absolutePos);
	}

	@Override
	public String toString()
	{
		return position + ", v=" + velocity + ",r=" + rotation;
	}

	public Vec2 pos()
	{
		return position;
	}
}

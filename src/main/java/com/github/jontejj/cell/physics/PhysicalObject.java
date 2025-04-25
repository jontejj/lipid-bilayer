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
package com.github.jontejj.cell.physics;

public abstract class PhysicalObject
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
	 * TODO: fractional degrees?
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
		Vec2 acceleration = Vec2.accelerateInDirection(force / mass, rotation);
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

	public final void checkCollision(PhysicalObject other)
	{
		if(isCollidingWith(other))
		{
			System.out.println(this + " collided with " + other);
			listener.collidedWith(other);
			Vec2 otherVelocity = other.velocity;
			// TODO: take into account mass
			other.velocity = new Vec2(velocity.magnitude(), -velocity.direction());
			velocity = new Vec2(otherVelocity.magnitude(), -otherVelocity.direction());
		}
	}

	/**
	 * TODO: for fast moving objects, check the swept volume
	 * between two time steps to avoid objects passing through each other
	 * https://www.kth.se/social/files/574684e8f2765458722b5565/DH2323%20Collision%20Detection%20I.pdf
	 * https://en.wikipedia.org/wiki/Solid_sweep
	 */
	/**
	 * https://resources.mpi-inf.mpg.de/departments/d1/teaching/ss10/Seminar_CGGC/Slides/07_Bock_MS.pdf
	 * Minkowski sum:
	 * Algebraic: Summing the vertices (+ convex hull)
	 * A+B=(5,0), B+B=(10,0), C+B=(5,5), A+D=(8,0),
	 * B+D=(13,0), C+D=(8,5), A+E=(8,3), B+E=(13,3),
	 * C+E=(8,8), A+F=(5,3), B+F=(10,3), C+F=(5,8)
	 * adapted by Korcz
	 */
	protected abstract boolean isCollidingWith(PhysicalObject other);

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

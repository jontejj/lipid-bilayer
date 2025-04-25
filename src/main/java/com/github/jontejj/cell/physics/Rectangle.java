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

/**
 * TODO: For 3d, this would be a box
 */
public class Rectangle extends PhysicalObject
{
	private float width, height;

	public Rectangle(float width, float height, Vec2 position, ObjectUpdatedListener listener)
	{
		super(position, listener);
		this.width = width;
		this.height = height;
	}

	@Override
	protected boolean isCollidingWith(PhysicalObject other)
	{
		if(other instanceof Rectangle rect)
			return isCollidingWith(rect);
		// TODO: how to handle others here?
		return false;
	}

	/**
	 * From https://silentmatt.com/rectangle-intersection/
	 */
	public boolean isCollidingWith(Rectangle other)
	{
		return (pos().x() < other.pos().x() + other.width()) && 	//
				(pos().x() + width() > other.pos().x()) && 			//
				(pos().y() < other.pos().y() + other.height()) &&  	//
				(pos().y() + height() > other.pos().y());
	}

	public float width()
	{
		return width;
	}

	public float height()
	{
		return height;
	}

	@Override
	public String toString()
	{
		return "w:" + width + ",h=" + height + super.toString();
	}
}

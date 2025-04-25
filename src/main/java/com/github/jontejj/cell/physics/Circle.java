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
public class Circle extends PhysicalObject
{
	private float radius;

	public Circle(float radius, Vec2 position, ObjectUpdatedListener listener)
	{
		super(position, listener);
		this.radius = radius;
	}

	public boolean isCollidingWith(PhysicalObject other)
	{
		// TODO: fix
		return false;
	}

	public float radius()
	{
		return radius;
	}
}

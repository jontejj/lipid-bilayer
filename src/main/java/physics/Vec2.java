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

import java.util.Objects;

public class Vec2
{
	final double magnitude, direction;

	public Vec2(double magnitude, double direction)
	{
		this.magnitude = magnitude;
		this.direction = direction;
	}

	public Vec2 add(Vec2 vec)
	{
		return new Vec2(magnitude + vec.magnitude, direction + vec.direction);
	}

	public Vec2 mul(Vec2 vec)
	{
		return new Vec2(magnitude * vec.magnitude, direction * vec.direction);
	}

	public Vec2 divide(Vec2 vec)
	{
		return new Vec2(magnitude / vec.magnitude, direction / vec.direction);
	}

	public double magnitude()
	{
		return magnitude;
	}

	public double direction()
	{
		return direction;
	}

	@Override
	public boolean equals(Object obj)
	{
		Vec2 vec2 = (Vec2) obj;
		return magnitude == vec2.magnitude && direction == vec2.direction;
		// return ComparisonChain.start().compare(magnitude, vec2.magnitude).compare(direction, vec2.direction).result() == 0;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(magnitude, direction);
	}

	@Override
	public String toString()
	{
		return String.format("<%f,%f>", magnitude, direction);
		// return "<" + magnitude + "," + direction + ">";
	}
}

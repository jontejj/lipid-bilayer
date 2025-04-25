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

import java.util.Objects;

import com.google.common.collect.ComparisonChain;

public class Vec2 implements Comparable<Vec2>
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

	/**
	 * @alias {@link #magnitude()}
	 */
	public double x()
	{
		return magnitude;
	}

	/**
	 * @alias {@link #direction()}
	 */
	public double y()
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

	public static Vec2 accelerateInDirection(double acceleration, double rotation)
	{
		int directionQuadrant = (int) ((rotation / 90) % 4);
		// int = BigDecimal.valueOf(rotation / 90f).round(new MathContext(1, RoundingMode.UP)).intValueExact();
		double localAngleInDegrees = rotation % 90;
		double localAngleInRadians = Math.toRadians(localAngleInDegrees);
		Vec2 res = null;
		double cosAcceleration = acceleration;
		double sinAcceleration = acceleration;
		if(localAngleInRadians != 0)
		{
			sinAcceleration *= Math.sin(localAngleInRadians);
			cosAcceleration *= Math.cos(localAngleInRadians);
			switch(directionQuadrant)
			{
			case 0: // Down-Right 1-89
				res = new Vec2(cosAcceleration, -sinAcceleration);
				break;
			case 1: // Down-Left 91-179
				res = new Vec2(-sinAcceleration, -cosAcceleration);
				break;
			case 2: // Up-Left 181-269
				res = new Vec2(-cosAcceleration, sinAcceleration);
				break;
			case 3: // Up-Right 271-359
				res = new Vec2(sinAcceleration, cosAcceleration);
				break;
			}
		}
		else
		{
			switch(directionQuadrant)
			{
			case 0: // 0
				res = new Vec2(sinAcceleration, 0);
				break;
			case 1: // 90
				res = new Vec2(0, -sinAcceleration);
				break;
			case 2: // 180
				res = new Vec2(-sinAcceleration, 0);
				break;
			case 3: // 270
				res = new Vec2(0, sinAcceleration);
				break;
			}
		}
		return res;
	}

	@Override
	public int compareTo(Vec2 o)
	{
		return ComparisonChain.start().compare(magnitude, o.magnitude).compare(direction, o.direction).result();
	}
}

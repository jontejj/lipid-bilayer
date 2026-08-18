/* Copyright 2025 jonatanjonsson
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
package com.github.jontejj.cell.evolution.organs;

import java.awt.Color;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Ray;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.DetectFilter;
import org.dyn4j.world.result.RaycastResult;

import com.github.jontejj.cell.evolution.Eatable;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.game.CellWorld;

public class Eye extends SimulationBody
{
	private final Organism owner;

	public Eye(Organism owner, double radius)
	{
		this.owner = owner;
		this.addFixture(Geometry.createCircle(radius));
		this.setMass(MassType.NORMAL);
		this.setColor(Color.WHITE);
	}

	public void look(CellWorld world)
	{
		Vector2 start = this.getWorldCenter();
		double angle = this.getTransform().getRotationAngle();
		Vector2 direction = new Vector2(Math.cos(angle), Math.sin(angle));

		Ray ray = new Ray(start, direction);
		RaycastResult<SimulationBody, BodyFixture> closest = world.world()
				.raycastClosest(ray, 50, new DetectFilter<SimulationBody, BodyFixture>(true, true, null));

		if(closest != null)
		{
			SimulationBody hit = closest.getBody();
			if(hit instanceof Organism)
			{
				System.out.println("Eye detected: " + hit);
			}
			else if(hit instanceof Eatable food)
			{
				Vector2 hitPoint = closest.getRaycast().getPoint();
				Vector2 toTarget = hitPoint.difference(start);

				double distance = toTarget.getMagnitude();
				double targetAngle = Math.atan2(toTarget.y, toTarget.x);
				double bodyAngle = owner.getTransform().getRotationAngle();
				double relativeAngle = targetAngle - bodyAngle;
				owner.onFoodSeen(food, distance, relativeAngle);
			}
		}
	}
}

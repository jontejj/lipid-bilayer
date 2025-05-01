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
package com.github.jontejj.cell.evolution;

import java.util.Map;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;

public class UnicellularOrganism extends Organism implements HasMouth
{
	private double circleRadius;
	private BodyFixture fixture;
	private final Nucleus nucleus;
	private final Cytoplasm cytoplasm;
	private long fissionIndex = 1;

	private static double GOLDEN_ANGLE = 2 * Math.PI * (1 - 1.0 / ((1 + Math.sqrt(5)) / 2));

	public UnicellularOrganism(String name, Nucleus nucleus, World<SimulationBody> world)
	{
		this(name, nucleus, world, 1.0);

	}

	public UnicellularOrganism(String name, Nucleus nucleus, World<SimulationBody> world, double circleRadius)
	{
		super(name);
		this.circleRadius = circleRadius;
		fixture = addFixture(Geometry.createCircle(circleRadius)); // a circular cell body
		setMass(MassType.NORMAL);
		this.nucleus = nucleus;
		this.cytoplasm = new Cytoplasm(nucleus, world);
	}

	public Cytoplasm cytoplasm()
	{
		return cytoplasm;
	}

	public double circleRadius()
	{
		return circleRadius;
	}

	@Override
	public boolean timestep()
	{
		cytoplasm().timestep();

		if(cytoplasm().shouldTriggerApoptosis())
			return true;
		return false;
	}

	@Override
	public String toString()
	{
		return name() + ": " + cytoplasm();
	}

	// private static final long FISSION_COOLDOWN_MS = 500; // 0.5 seconds delay
	//
	// public boolean canFissionNow()
	// {
	// return System.currentTimeMillis() - lastFissionTime > FISSION_COOLDOWN_MS;
	// }

	@Override
	public Optional<Organism> binaryFission()
	{
		if(cytoplasm().supportsBinaryFission(nucleus.genome().totalNucleotideCounts()))
		{
			Nucleus newNucleus = nucleus.binaryFission(cytoplasm());
			adjustBodySize(-0.5);
			UnicellularOrganism newOrganism = new UnicellularOrganism(name(), newNucleus, cytoplasm().world(), circleRadius());
			newOrganism.cytoplasm().setLastWormSegment(newOrganism);
			double separationDistance = circleRadius * 2.0 + 0.1;
			double radiusForFission = separationDistance * Math.sqrt(fissionIndex); // spread outward
			double angle = GOLDEN_ANGLE * fissionIndex;
			fissionIndex++;

			double dx = radiusForFission * Math.cos(angle);
			double dy = radiusForFission * Math.sin(angle);
			Vector2 offset = new Vector2(dx, dy);
			newOrganism.translate(this.getWorldCenter().sum(offset));
			// Set velocities for separation
			double pushSpeed = 2.5; // adjust as needed (higher = faster separation)
			Vector2 pushVelocity = offset.getNormalized().product(pushSpeed);
			// Apply opposite velocities
			this.setLinearVelocity(pushVelocity.product(-1.0)); // Parent goes backward
			newOrganism.setLinearVelocity(pushVelocity);
			// TODO: share resources
			return Optional.of(newOrganism);
		}
		return Optional.absent();
	}

	void adjustBodySize(double sizeToAdd)
	{
		// circleRadius = circleRadius + circleRadius * sizeToAdd;
		removeFixture(fixture);
		fixture = addFixture(Geometry.createCircle(circleRadius));
	}

	@Override
	public void eat(Eatable food)
	{
		long averageAmount = (long) (food.molecularMass() / Nucleobases.averageMolecularMass());
		cytoplasm().increaseResourceAmountForAllNucleotides(averageAmount);
	}

	@Override
	public Map<Nucleobases, Long> nucleotideResources()
	{
		return cytoplasm().nucleotideResources();
	}

	@Override
	public double totalMolecularMass()
	{
		return cytoplasm.totalMolecularMass();
	}

	@Override
	public void removeFromWorld(World<SimulationBody> world)
	{
		world.removeBody(this);
	}
}

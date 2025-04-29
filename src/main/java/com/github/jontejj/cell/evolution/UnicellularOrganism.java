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

import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;

public class UnicellularOrganism extends Organism implements HasMouth
{
	private final Cytoplasm cytoplasm;
	private final String name;
	private final Nucleus nucleus;

	public UnicellularOrganism(String name, Nucleus nucleus, World<SimulationBody> world)
	{
		super();
		this.name = name;
		this.nucleus = nucleus;
		this.cytoplasm = new Cytoplasm(nucleus, world);
	}

	public UnicellularOrganism(String name, Nucleus nucleus, World<SimulationBody> world, double circleRadius)
	{
		super(circleRadius);
		this.name = name;
		this.nucleus = nucleus;
		this.cytoplasm = new Cytoplasm(nucleus, world);
	}

	@Override
	public void timestep()
	{
		cytoplasm.timestep();
	}

	@Override
	public String toString()
	{
		return name + ": " + cytoplasm;
	}

	public String name()
	{
		return name;
	}

	public Cytoplasm cytoplasm()
	{
		return cytoplasm;
	}

	@Override
	public Optional<Organism> binaryFission()
	{
		if(cytoplasm.supportsBinaryFission(nucleus.genome().totalNucleotideCounts()))
		{
			Nucleus newNucleus = nucleus.binaryFission(cytoplasm);
			adjustBodySize(-0.5);
			UnicellularOrganism newOrganism = new UnicellularOrganism(name, newNucleus, cytoplasm.world(), circleRadius());
			newOrganism.cytoplasm().setLastWormSegment(newOrganism);
			Vector2 offset = new Vector2(0.5, 0.0); // New organism appears 0.5 units to the right
			newOrganism.translate(this.getWorldCenter().sum(offset));
			// Set velocities for separation
			double pushSpeed = 1.0; // adjust as needed (higher = faster separation)
			Vector2 pushVelocity = offset.getNormalized().product(pushSpeed);

			// Apply opposite velocities
			this.setLinearVelocity(pushVelocity.product(-1.0)); // Parent goes backward
			newOrganism.setLinearVelocity(pushVelocity);
			// TODO: share resources
			return Optional.of(newOrganism);
		}
		return Optional.absent();
	}

	@Override
	public void eat(Eatable food)
	{
		cytoplasm.increaseResourceAmountForAllNucleotides(food.getCalories());
	}
}

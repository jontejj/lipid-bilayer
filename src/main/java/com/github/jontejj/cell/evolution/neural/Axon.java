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
package com.github.jontejj.cell.evolution.neural;

import java.util.HashSet;
import java.util.Set;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import com.github.jontejj.cell.evolution.Organism;

public class Axon extends SimulationBody
{
	private final Set<Organism> connectedTargets = new HashSet<>();

	private final SimulationBody cellBody;

	private final Neurotransmitter neurotransmitter;

	public Axon(SimulationBody organism, Neurotransmitter neurotransmitter)
	{
		cellBody = organism;
		this.neurotransmitter = neurotransmitter;
		Vector2 origin = cellBody.getWorldCenter();
		Vector2 direction = new Vector2(1, 0).rotate(Math.random() * 2 * Math.PI);
		Vector2 offset = direction.multiply(2.0); // protrusion length

		addFixture(Geometry.createCapsule(2, 1));
		translate(origin.sum(offset));
		setMass(MassType.NORMAL);
	}

	public boolean hasFormedSynapseWith(Organism target)
	{
		return connectedTargets.contains(target);
	}

	public void registerSynapse(Organism target)
	{
		connectedTargets.add(target);
	}

	public void transmitSignalToTargets()
	{
		for(Organism target : connectedTargets)
		{
			signalTo(target);
		}
	}

	private void signalTo(Organism target)
	{
		// Example behavior; expand as needed
		System.out.println("Signal sent from axon to " + target.name());

		switch(neurotransmitter)
		{
		// case GLUTAMATE -> target.excite(); // Increases activation
		// case GABA -> target.inhibit(); // Decreases activity
		// case DOPAMINE -> target.adjustMotivation(); // e.g., resource-seeking
		// case SEROTONIN -> target.adjustRiskAversion(); // or movement inhibition
		// case ACETYLCHOLINE -> target.triggerMuscleContraction(); // trigger movement
		}
	}

	@Override
	public String toString()
	{
		return cellBody.toString();
	}
}

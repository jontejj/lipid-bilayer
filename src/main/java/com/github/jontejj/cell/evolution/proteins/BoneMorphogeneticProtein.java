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
package com.github.jontejj.cell.evolution.proteins;

import java.util.Random;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.samples.framework.SimulationBody;

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.neural.Axon;
import com.github.jontejj.cell.evolution.neural.Neurotransmitter;

public class BoneMorphogeneticProtein extends FunctionalProtein
{
	private boolean isInhibited = false;
	private boolean hasTriggeredNeuralFate = false;

	public BoneMorphogeneticProtein(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		if(isInhibited && !hasTriggeredNeuralFate)
		{
			triggerNeuralFate(env, organism);
			hasTriggeredNeuralFate = true;
		}
	}

	private void triggerNeuralFate(Cytoplasm env, Organism organism)
	{
		createCytoplasmicProtrusion(env, organism);
	}

	public void inhibit()
	{
		isInhibited = true;
	}

	private void createCytoplasmicProtrusion(Cytoplasm env, Organism organism)
	{
		Neurotransmitter neurotransmitter = Neurotransmitter.values()[new Random().nextInt(Neurotransmitter.values().length)];
		Axon protrusion = new Axon(organism, neurotransmitter);
		env.cellWorld().world().addBody(protrusion);
		// Attach with a joint
		DistanceJoint<SimulationBody> joint = new DistanceJoint<>(organism, protrusion, organism.getWorldCenter(), protrusion.getWorldCenter());
		joint.setSpringFrequency(3.0);
		joint.setSpringDampingRatio(0.6);
		env.cellWorld().world().addJoint(joint);
	}
}

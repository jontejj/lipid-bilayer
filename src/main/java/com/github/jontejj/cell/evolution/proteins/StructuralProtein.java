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
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Nucleobases;

public class StructuralProtein extends FunctionalProtein
{

	private boolean used = false;

	public StructuralProtein(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
	}

	@Override
	public void performFunction(Cytoplasm env)
	{
		if(used)
			return;
		used = true;

		if(env.consumeEnergy(1))
		{
			Nucleobases baseToUseForStructure = Nucleobases.values()[(int) (Math.abs(sequenceSignature()) % 5)];
			if(env.decreaseResourceAmount(baseToUseForStructure, aminoAcids().size())) // Bigger enzymes consumes less resources!
			{
				// Generate pseudo-random but deterministic variations based on sequence signature
				Random random = new Random(sequenceSignature());

				// Find the last worm segment
				SimulationBody lastSegment = env.getLastWormSegment();

				// Create new body part (segment)
				SimulationBody segment = new SimulationBody();
				double size = 0.3 + (molecularMass() % 10000) / 50000.0; // ~0.3 - 0.5
				segment.addFixture(Geometry.createCircle(size));
				segment.setMass(MassType.NORMAL);

				Vector2 position;
				if(lastSegment == null)
				{
					// First segment: place at origin
					position = new Vector2(0.0, 0.0);
				}
				else
				{
					// Add next to last segment
					Vector2 lastPos = lastSegment.getTransform().getTranslation();
					double angle = (random.nextDouble() - 0.5) * Math.toRadians(20);
					double dx = Math.cos(angle) * (size * 2.0);
					double dy = Math.sin(angle) * (size * 2.0);
					position = lastPos.sum(new Vector2(dx, dy));
				}
				segment.translate(position);
				env.world().addBody(segment);

				// If there is a previous segment, create a joint!
				if(lastSegment != null)
				{
					DistanceJoint<SimulationBody> joint = new DistanceJoint<>(lastSegment,
							segment,
							lastSegment.getWorldCenter(),
							segment.getWorldCenter());
					joint.setSpringFrequency(2.0);  // How springy it is (higher = stiffer)
					joint.setSpringDampingRatio(0.2);  // How much it resists oscillation
					env.world().addJoint(joint);
				}

				// Update the reference to the last segment
				env.setLastWormSegment(segment);
			}
		}
	}
}

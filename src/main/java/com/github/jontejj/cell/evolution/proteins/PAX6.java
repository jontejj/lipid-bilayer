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

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.organs.Eye;

public class PAX6 extends FunctionalProtein
{
	public PAX6(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
	}

	private boolean used = false;

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		// TODO: degrade protein instead
		if(used)
			return;
		used = true;

		double size = 0.3 + (molarMass() % 10000) / 50000.0; // ~0.3 - 0.5
		Eye eye = new Eye(organism, size);
		Vector2 position = organism.getWorldCenter(); // Place at center of organism
		eye.translate(position);
		env.world().addBody(eye);

		DistanceJoint<SimulationBody> eyeJoint = new DistanceJoint<>(organism, eye, organism.getWorldCenter(), eye.getWorldCenter());
		eyeJoint.setSpringFrequency(5.0);
		eyeJoint.setSpringDampingRatio(0.7);
		env.world().addJoint(eyeJoint);
	}
}

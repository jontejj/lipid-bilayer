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

import org.dyn4j.geometry.Vector2;

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Organism;

public class MotorProtein extends FunctionalProtein
{
	private static final double MOVEMENT_FORCE = 5.0;
	private static final int ATP_COST = 1;
	private static final Random RND = new Random(42);

	public MotorProtein(AminoAcidSequence sequence)
	{
		super(sequence);
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		if(env.isMovementInhibited())
			return;

		if(!env.consumeEnergy(ATP_COST))
			return;

		Vector2 direction = chooseMovementDirection(organism, env);
		if(direction != null)
		{
			organism.applyForce(direction.multiply(MOVEMENT_FORCE));
		}
	}

	private Vector2 chooseMovementDirection(Organism organism, Cytoplasm cytoplasm)
	{
		// Default behavior: random movement
		double angle = RND.nextDouble() * 2 * Math.PI;
		return new Vector2(Math.cos(angle), Math.sin(angle));
	}
}

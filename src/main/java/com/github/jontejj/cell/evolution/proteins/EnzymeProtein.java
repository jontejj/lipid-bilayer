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

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Nucleobases;

public class EnzymeProtein extends FunctionalProtein
{
	public static long MINIMUM_SIZE = 30000;
	private final Nucleobases baseToConsume;

	public EnzymeProtein(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
		baseToConsume = Nucleobases.values()[(int) (Math.abs(sequenceSignature()) % 5)];
	}

	@Override
	public void performFunction(Cytoplasm env)
	{
		if(env.decreaseResourceAmount(baseToConsume, (long) (MINIMUM_SIZE / molarMass()))) // Bigger enzymes consume less resources!
		{
			env.addEnergy((long) (4.0 * molarMass() / 1000.0)); // And adds more energy
		}
	}

	@Override
	public String toString()
	{
		return "Enzyme that consumes " + (long) (MINIMUM_SIZE / molarMass()) + " of " + baseToConsume + " and gives "
				+ (long) (4.0 * molarMass() / 1000.0) + " atp";
	}
}

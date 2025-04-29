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

import com.github.jontejj.cell.evolution.AminoAcid;
import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;

public class AminoAcidSynthaseProtein extends FunctionalProtein
{
	private final AminoAcid aminoAcidToSynthase;

	public AminoAcidSynthaseProtein(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
		aminoAcidToSynthase = aminoAcids().get(2);
	}

	@Override
	public void performFunction(Cytoplasm env)
	{

		// TODO: consume the actual nucleotides needed?
		if(env.consumeEnergy(1))
		{
			env.synthesizeAminoAcid(aminoAcidToSynthase);
		}
	}

	@Override
	public String toString()
	{
		return aminoAcidToSynthase + " SynthaseProtein";
	}

}

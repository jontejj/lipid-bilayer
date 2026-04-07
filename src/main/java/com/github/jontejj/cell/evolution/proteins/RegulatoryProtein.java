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

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.DNA;
import com.github.jontejj.cell.evolution.Nucleobases;
import com.github.jontejj.cell.evolution.Organism;

public class RegulatoryProtein extends FunctionalProtein
{
	private final DNA sourceDNA;
	private final Nucleobases baseToMonitor;

	public RegulatoryProtein(AminoAcidSequence aminoAcidSequence, DNA sourceDNA)
	{
		super(aminoAcidSequence);
		this.sourceDNA = sourceDNA;
		Random seededRNG = new Random(sequenceSignature());
		baseToMonitor = Nucleobases.values()[seededRNG.nextInt(Nucleobases.values().length)];
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		if(env.nucleotideResources().get(baseToMonitor) < 100)
		{
			env.activateBackupGenes(sourceDNA.position());
		}
	}

	@Override
	public String toString()
	{
		return "Regulatory protein that monitors for low levels of " + baseToMonitor;
	}
}

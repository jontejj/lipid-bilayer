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
import com.github.jontejj.cell.evolution.Organism;

public class ReceptorProtein extends FunctionalProtein
{
	/**
	 * TODO: communication with other cells, i.e Stimulus A/B -> Response A/B
	 * 1. Kontaktberoende (receptor with ligand on it interacts with receptor on another cell)
	 * 2. endokrin (circulatory system)
	 * 3. Parakrin (from inside one cell and to a receptor on the other)
	 * 4. Autokrin (signaling molecule is the same as on the receiving side)
	 * Plasmamembranereceptor, intracellular receptor
	 * High and low affinity receptors
	 * Ligand = det som receptorn binder, inducerar en strukturförändring av receptorn
	 * Antagonist and agonist
	 * Nucleus receptors
	 * Effector proteins: metabolism, gene expression and movement
	 * Indirect signaltransduction - cAMP - "2nd messenger"
	 * Networks of signaltransduction: Negative feedback, positive feedback, feed forward, stimulative crosstalk, inhibatory crosstalk
	 */
	public ReceptorProtein(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		// TODO Auto-generated method stub
	}
}

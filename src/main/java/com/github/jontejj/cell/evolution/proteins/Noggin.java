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
import com.github.jontejj.cell.evolution.signaling.InhibitBMP;

public class Noggin extends FunctionalProtein
{
	private boolean used = false;

	public Noggin(AminoAcidSequence aminoAcidSequence)
	{
		super(aminoAcidSequence);
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		if(!used)
		{
			organism.signal(new InhibitBMP());
			used = true;
		}
	}
}

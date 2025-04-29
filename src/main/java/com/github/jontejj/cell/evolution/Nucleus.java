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
package com.github.jontejj.cell.evolution;

import java.util.List;

import com.github.jontejj.cell.evolution.proteins.Protein;

public class Nucleus
{
	// TODO: division for some cell types and not for others
	// TODO: communication with other cells
	// TODO: create proteins based on transcription factors

	private final Genome genome;

	public Nucleus(Genome genome)
	{
		this.genome = genome;
	}

	public Nucleus binaryFission(Cytoplasm cytoplasm)
	{
		// TODO: also mimic recombination
		return new Nucleus(genome.replicate(cytoplasm));
	}

	public Genome genome()
	{
		return genome;
	}

	public List<Protein> timestep(Cytoplasm cytoplasm)
	{
		List<Protein> newProteins = genome.timestep(cytoplasm);
		return newProteins;
	}

	@Override
	public String toString()
	{
		return genome.toString();
	}
}

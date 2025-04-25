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
import java.util.Random;

import org.assertj.core.util.Lists;

import com.google.common.collect.ImmutableList;

public class DNA
{
	private static Random RND = new Random(42);

	private List<Nucleobases> nucleotides;

	public DNA(List<Nucleobases> nucleotides)
	{
		this.nucleotides = nucleotides;
	}

	/**
	 * DNA -> (transcription) pre-mRNA -> mRNA -> tRNA (adapter) -> (translation) Ribosome translates mRNA into polypeptide
	 * DNA nucleobasepairs:
	 * A-T
	 * T-A
	 * G-C
	 * C-G
	 */

	// TODO: differentiate between euchromatin and heterochromatin (which affects what DNA that is transcriptionally active)
	public mRNA transcribe()
	{
		List<Codon> codons = Lists.newArrayList();
		for(int i = 0; i < nucleotides.size(); i += 3)
		{
			codons.add(new Codon(nucleotides.get(i).toRNA(), nucleotides.get(i + 1).toRNA(), nucleotides.get(i + 2).toRNA()));
		}
		return new mRNA(codons);
	}

	public DNA replicate()
	{
		// TODO: make this more realistic
		List<Nucleobases> newNucleotides = Lists.newArrayList();
		for(Nucleobases base : nucleotides)
		{
			if(RND.nextInt(100000) == 1)
			{
				// Mutation
				ImmutableList<Nucleobases> otherBases = base.othersDna();
				Nucleobases mutation = otherBases.get(RND.nextInt(otherBases.size()));
				newNucleotides.add(mutation);
				continue;
			}
			if(RND.nextInt(1000000) == 2)
			{
				newNucleotides.add(base);
				Nucleobases addition = Nucleobases.values()[(RND.nextInt(Nucleobases.values().length))];
				newNucleotides.add(addition);
				continue;
			}
			if(RND.nextInt(1000000) == 3)
			{
				// Deletion
				continue;
			}
			newNucleotides.add(base);
		}
		return this;
	}
}

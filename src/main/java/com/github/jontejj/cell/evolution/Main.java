/* Copyright 2019 jonatanjonsson
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

public class Main
{
	// private final CodonRegistry codons = new CodonRegistry();

	public static void main(String[] args)
	{
		// many codons -> DNA
		Codon dna = new Codon(Nucleobases.ADENINE, Nucleobases.CYTOSINE, Nucleobases.THYMINE);
		System.out.println("DNA" + dna);
		Codon rna = new Codon(Nucleobases.ADENINE, Nucleobases.CYTOSINE, Nucleobases.URACIL);
		System.out.println("RNA" + rna);
		System.out.println(generateProtein(10));
		System.out.println(new DNA(Nucleobases.fromString("AUGCCAGAUCACUAA")).transcribe().translate());

	}

	private static Protein generateProtein(int lengthInCodons)
	{
		Random rnd = new Random(42);
		Nucleobases[] nucleobases = Nucleobases.values();
		List<Nucleobases> nucleotides = Lists.newArrayList();
		for(int i = 0; i < lengthInCodons; i++)
		{
			nucleotides.add(nucleobases[rnd.nextInt(nucleobases.length)]);
			nucleotides.add(nucleobases[rnd.nextInt(nucleobases.length)]);
			nucleotides.add(nucleobases[rnd.nextInt(nucleobases.length)]);
		}
		return new DNA(nucleotides).transcribe().translate();
	}

}

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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;

import com.github.jontejj.cell.evolution.DNA.ChromatinMode;
import com.github.jontejj.cell.evolution.proteins.Protein;
import com.google.common.base.Optional;

public class Genome
{
	private final static Random RND = new Random();
	private final List<DNA> dnas;
	private final double molecularMass;
	private final Map<Nucleobases, Integer> totalNucleotideCounts;

	public Genome(List<DNA> dnas)
	{
		this.dnas = dnas;
		this.molecularMass = dnas.stream().mapToDouble(DNA::molecularMass).sum();
		this.totalNucleotideCounts = calculateTotalNucleotideCounts();
	}

	private Map<Nucleobases, Integer> calculateTotalNucleotideCounts()
	{
		Map<Nucleobases, Integer> total = new EnumMap<>(Nucleobases.class);
		for(DNA dna : dnas)
		{
			for(Map.Entry<Nucleobases, Integer> entry : dna.nucleotideCounts().entrySet())
			{
				total.merge(entry.getKey(), entry.getValue(), Integer::sum);
			}
		}
		return total;
	}

	public Map<Nucleobases, Integer> totalNucleotideCounts()
	{
		return totalNucleotideCounts;
	}

	public double molecularMass()
	{
		return molecularMass;
	}

	public Genome replicate(Cytoplasm cytoplasm)
	{
		// TODO: use dnas.parallelStream() and make decreaseResourceAmount in Cytoplasm thread-safe?
		List<DNA> replicatedDnas = dnas.stream().map(dna -> dna.replicate(cytoplasm)).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toList());
		return new Genome(replicatedDnas);
	}

	public static Genome generate(int nrOfDNAs)
	{
		Nucleobases[] basesWithoutUracilInRNA = new Nucleobases[]{Nucleobases.CYTOSINE, Nucleobases.GUANINE, Nucleobases.ADENINE};
		List<DNA> dnas = Lists.newArrayList();
		Nucleobases[] nucleobases = Nucleobases.DNA.toArray(new Nucleobases[4]);
		for(int dnaPosition = 0; dnaPosition < nrOfDNAs; dnaPosition++)
		{
			List<Nucleobases> nucleotides = Lists.newArrayList();
			// Generate a random number of codons (each codon is 3 nucleotides)

			// Use a skewed random distribution to prefer smaller proteins
			double skewedRandom = Math.pow(RND.nextDouble(), 2); // Square to skew towards 0
			int numberOfCodons = (int) (skewedRandom * 38000) + 10; // Ensuring at least 10 codons, The largest known protein in terms of
																	 // amino acid sequence is titin (also known as connectin) (38,000 amino
																	 // acids)
			for(int i = 0; i < numberOfCodons; i++)
			{
				// Select a random codon (3 nucleotides)

				// To not generate too small proteins, around 20 aa in average,
				// this makes T/U appear less often in the first position of the codon,
				// this essentially rules out any stop codons being generated except for every 10th codon
				Nucleobases base1 = null;
				if(RND.nextInt(10) == 1)
				{
					base1 = nucleobases[RND.nextInt(nucleobases.length)];
				}
				else
				{

					base1 = basesWithoutUracilInRNA[RND.nextInt(basesWithoutUracilInRNA.length)];
				}
				Nucleobases base2 = nucleobases[RND.nextInt(nucleobases.length)];
				Nucleobases base3 = nucleobases[RND.nextInt(nucleobases.length)];

				// Minimize further the chance of a stop codon appearing
				if(i % 2 == 0)
				{
					if(Codon.get(base1.toRNA(), base2.toRNA(), base3.toRNA()).translate() == null)
					{
						continue;
					}
				}
				// Add the codon (3 nucleotides) to the list
				nucleotides.add(base1);
				nucleotides.add(base2);
				nucleotides.add(base3);

			}
			dnas.add(new DNA(nucleotides, dnaPosition));
			// System.out.println("Generated DNA " + dna);
		}
		return new Genome(dnas);
	}

	/**
	 * @return new proteins created during the timestep
	 */
	public List<Protein> timestep(Cytoplasm cytoplasm)
	{
		List<Protein> newProteins = Lists.newArrayList();
		for(DNA dna : dnas)
		{
			dna.setEuchromatinModeRandomly();
			newProteins.addAll(dna.transcribe(cytoplasm).transform(mRNA -> mRNA.translate(cytoplasm)).asSet());
		}
		return newProteins;
	}

	public void enableDNAAfterPosition(int position)
	{
		for(int i = position; i < dnas.size(); i++)
		{
			DNA dna = dnas.get(i);
			if(dna.chromatinMode() == ChromatinMode.HETEROCHROMATIN)
			{
				dna.forceEuchromatinMode();
				break;
			}
		}
	}

	@Override
	public String toString()
	{
		return "Number of dnas = " + dnas.size() + ", mass = " + molecularMass;
	}
}

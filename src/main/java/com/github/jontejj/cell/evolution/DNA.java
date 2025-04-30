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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.assertj.core.util.Lists;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class DNA
{
	public enum ChromatinMode
	{
		/**
		 * Only DNA in euchromatin mode can be transcribed
		 */
		EUCHROMATIN,
		HETEROCHROMATIN
	}

	private static Random RND = new Random(42);

	private List<Nucleobases> nucleotides;
	private final double molecularMass;

	private ChromatinMode chromatinMode = ChromatinMode.HETEROCHROMATIN;

	private final int position;

	private Map<Nucleobases, Integer> nucleotideCounts;

	/**
	 * @param nucleotides the nucleotides that this DNA represents
	 * @param position where in the Genome the DNA is located
	 * @param nucleotideCounts counts for how many of each nucleotide occurs in the nucleotides parameter
	 */
	public DNA(List<Nucleobases> nucleotides, int position, Map<Nucleobases, Integer> nucleotideCounts)
	{
		this.nucleotides = nucleotides;
		this.position = position;
		this.molecularMass = nucleotides.stream().mapToDouble(Nucleobases::molecularMass).sum();
		setEuchromatinModeForHouseKeepingGenes();
		this.nucleotideCounts = nucleotideCounts;
	}

	public DNA(List<Nucleobases> nucleotides, int position)
	{
		this(nucleotides, position, calculateNucleotideCounts(nucleotides));

	}

	private static Map<Nucleobases, Integer> calculateNucleotideCounts(List<Nucleobases> nucleotides)
	{
		Map<Nucleobases, Integer> nucleotideCounts = new EnumMap<>(Nucleobases.class);
		for(int i = 0; i < nucleotides.size(); i++)
		{
			nucleotideCounts.merge(nucleotides.get(i), 1, Integer::sum);
		}
		return nucleotideCounts;
	}

	private void setEuchromatinModeForHouseKeepingGenes()
	{
		if(nucleotides.get(0) == Nucleobases.ADENINE)
		{
			// nucleotides.get(0) == Nucleobases.ADENINE simulates that some housekeeping genes are always in euchromatin mode
			this.chromatinMode = ChromatinMode.EUCHROMATIN;
		}
	}

	public void setEuchromatinModeRandomly()
	{
		if(RND.nextInt(10) == 1)
		{
			// Only about 10% of the genome is euchromatin (open for transcription).)
			this.chromatinMode = ChromatinMode.EUCHROMATIN;
		}
		else
		{
			this.chromatinMode = ChromatinMode.HETEROCHROMATIN;
		}
		setEuchromatinModeForHouseKeepingGenes();
	}

	public ChromatinMode chromatinMode()
	{
		return chromatinMode;
	}

	public void forceEuchromatinMode()
	{
		this.chromatinMode = ChromatinMode.EUCHROMATIN;
	}

	public double molecularMass()
	{
		return molecularMass;
	}

	public Map<Nucleobases, Integer> nucleotideCounts()
	{
		return nucleotideCounts;
	}

	/**
	 * DNA -> (transcription) pre-mRNA -> mRNA -> tRNA (adapter) -> (translation) Ribosome translates mRNA into polypeptide
	 * DNA nucleobasepairs:
	 * A-T
	 * T-A
	 * G-C
	 * C-G
	 */
	public Optional<mRNA> transcribe(Cytoplasm cytoplasm)
	{
		if(chromatinMode == ChromatinMode.HETEROCHROMATIN)
			return Optional.absent();

		// Check if all are available
		for(Map.Entry<Nucleobases, Integer> entry : nucleotideCounts.entrySet())
		{
			if(cytoplasm.nucleotideResources().getOrDefault(entry.getKey(), 0L) < entry.getValue())
				return Optional.absent();
		}

		// Consume the nucleotides
		for(Map.Entry<Nucleobases, Integer> entry : nucleotideCounts.entrySet())
		{
			if(!cytoplasm.decreaseResourceAmount(entry.getKey(), entry.getValue()))
				return Optional.absent(); // Failsafe
		}

		List<Codon> codons = Lists.newArrayList();
		for(int i = 0; i < nucleotides.size() - 2; i += 3)
		{
			codons.add(Codon.getOrInitialize(nucleotides.get(i).toRNA(), nucleotides.get(i + 1).toRNA(), nucleotides.get(i + 2).toRNA()));
		}
		return Optional.of(new mRNA(codons, this));
	}

	public DNA replicate(Cytoplasm cytoplasm)
	{
		for(Map.Entry<Nucleobases, Integer> entry : nucleotideCounts.entrySet())
		{
			if(!cytoplasm.decreaseResourceAmount(entry.getKey(), entry.getValue()))
				throw new IllegalStateException("Not enough resources to replicate: " + nucleotideCounts + "in " + cytoplasm);
		}
		// TODO: make this more realistic
		final int dnaSize = nucleotides.size();

		// Very rough expected probabilities
		final double mutationChancePerBase = 1.0 / 100_000;
		final double mutationThreshold = dnaSize * mutationChancePerBase;

		// Only attempt to mutate if we roll under the expected threshold
		boolean attemptMutation = RND.nextDouble() < mutationThreshold;

		if(!attemptMutation)
		{
			// Fast path, no mutation
			DNA dna = new DNA(nucleotides, position, nucleotideCounts);
			dna.chromatinMode = this.chromatinMode;
			return dna;
		}

		List<Nucleobases> newNucleotides = null;
		for(int i = 0; i < nucleotides.size(); i++)
		{
			Nucleobases base = nucleotides.get(i);
			// TODO: only make a Lists.newArrayList(); if there are mutations, this will minimize the required memory by a lot
			if(RND.nextInt(100_000) == 1)
			{
				// Mutation
				if(newNucleotides == null)
				{
					newNucleotides = new ArrayList<>(nucleotides.subList(0, i));
				}
				ImmutableList<Nucleobases> otherBases = base.othersDna();
				Nucleobases mutation = otherBases.get(RND.nextInt(otherBases.size()));
				newNucleotides.add(mutation);
				Stats.pointMutations++;
				continue;
			}
			if(RND.nextInt(1_000_000) == 2)
			{
				// Addition
				if(newNucleotides == null)
				{
					newNucleotides = new ArrayList<>(nucleotides.subList(0, i));
				}
				newNucleotides.add(base);
				Nucleobases addition = Nucleobases.values()[(RND.nextInt(Nucleobases.values().length))];
				newNucleotides.add(addition);
				Stats.additionMutations++;
				continue;
			}
			if(RND.nextInt(1_000_000) == 3)
			{
				// Deletion
				if(newNucleotides == null)
				{
					newNucleotides = new ArrayList<>(nucleotides.subList(0, i));
				}
				Stats.deletionMutations++;
				continue;
			}
		}
		DNA dna;
		if(newNucleotides != null)
		{
			dna = new DNA(newNucleotides, position);
		}
		else
		{
			dna = new DNA(nucleotides, dnaSize, nucleotideCounts);
		}
		dna.chromatinMode = this.chromatinMode;
		return dna;
	}

	public int position()
	{
		return position;
	}

	@Override
	public String toString()
	{
		return nucleotides.toString();
	}
}

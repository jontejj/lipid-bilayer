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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.Lists;

import com.github.jontejj.cell.evolution.proteins.AminoAcidSynthaseProtein;
import com.github.jontejj.cell.evolution.proteins.BoneMorphogeneticProtein;
import com.github.jontejj.cell.evolution.proteins.DnaA;
import com.github.jontejj.cell.evolution.proteins.EnzymeProtein;
import com.github.jontejj.cell.evolution.proteins.MotorProtein;
import com.github.jontejj.cell.evolution.proteins.Noggin;
import com.github.jontejj.cell.evolution.proteins.PAX6;
import com.github.jontejj.cell.evolution.proteins.Protein;
import com.github.jontejj.cell.evolution.proteins.RegulatoryProtein;
import com.github.jontejj.cell.evolution.proteins.StructuralProtein;
import com.github.jontejj.cell.evolution.proteins.SweetTasteReceptor;
import com.github.jontejj.cell.evolution.proteins.TransporterProtein;
import com.github.jontejj.cell.evolution.signaling.AMPK;
import com.google.common.base.Optional;

public class mRNA
{
	private static final List<AminoAcid> MOTOR_PROTEIN_AMINO_ACIDS = Arrays.asList(AminoAcid.Glycine, AminoAcid.Proline, AminoAcid.Alanine);
	private static final List<AminoAcid> SWEET_TASTE_AMINO_ACIDS = Arrays.asList(AminoAcid.Glycine, AminoAcid.Asparagine, AminoAcid.Serine);
	private final List<Codon> codons;
	private final DNA sourceDNA;

	public mRNA(List<Codon> codons, DNA sourceDNA)
	{
		this.codons = codons;
		this.sourceDNA = sourceDNA;
	}

	public Optional<Protein> translate(Cytoplasm cytoplasm)
	{
		// TODO: splicing?
		// TODO: repression
		// TODO: operons
		// TODO: what if multiple proteins exist in the codons (Polycistronic)? Right now this only returns the first protein
		List<AminoAcid> aminoacids = Lists.newArrayList();
		boolean startCodonEncountered = false;
		for(Codon c : codons)
		{
			AminoAcid aa = c.translate();
			if(aa == null) // null is stop marker
			{
				break;
			}
			if(aa == AminoAcid.Methionine)
			{
				startCodonEncountered = true;
			}
			if(startCodonEncountered)
			{
				if(!cytoplasm.decreaseResourceAmount(aa, 1))
				{
					for(AminoAcid aaToRefund : aminoacids)
					{
						cytoplasm.increaseResourceAmount(aaToRefund, 1);
					}
					Stats.abortedProteins++;
					return Optional.absent();
				}
				aminoacids.add(aa);
			}
		}
		if(aminoacids.isEmpty())
			return Optional.absent();
		Stats.totalNumberOfAminoAcids = Stats.totalNumberOfAminoAcids.add(BigDecimal.valueOf(aminoacids.size()));
		// TODO: how to fold the protein based on the amino acids?
		double molarMass = aminoacids.stream().mapToDouble(AminoAcid::molarMass).sum();
		// TODO: how to make the sequenceSignature not change that much after a small mutation?
		long sequenceSignature = aminoacids.stream().mapToLong(a -> a.shortName().hashCode()).reduce(1L, (acc, h) -> 31L * acc + h);
		AminoAcidSequence aminoAcidSequence = new AminoAcidSequence(aminoacids, molarMass, sequenceSignature);
		// Choose protein type based on features
		if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Leucine && aminoacids.get(2) == AminoAcid.Leucine)
		{
			Stats.structuralProteinsCreated++;
			// Leucine in the start was chosen completely by random
			return Optional.of(new StructuralProtein(aminoAcidSequence));
		}
		else if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Serine && aminoacids.get(2) == AminoAcid.Leucine)
		{
			Stats.dnaReplicationProteinsCreated++;
			// The first few amino acids that make up DnaA
			return Optional.of(new DnaA(aminoAcidSequence));
		}
		else if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Glutamate && aminoacids.get(2) == AminoAcid.Threonine)
		{
			Stats.AMPKProteinsCreated++;
			// The first few amino acids that make up AMPK
			return Optional.of(new AMPK(aminoAcidSequence));
		}
		else if(aminoacids.size() > 3 && aminoacids.get(1) == AminoAcid.Alanine)
		{
			Stats.aminoAcidSynthaseProteinCreated++;
			// Alanine in the start was chosen completely by random
			return Optional.of(new AminoAcidSynthaseProtein(aminoAcidSequence));
		}
		else if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Isoleucine && aminoacids.get(2) == AminoAcid.Proline)
		{
			Stats.boneMorphogeneticProteinCreated++;
			// The first amino acids of Bone Morphogenetic Protein 4 (BMP4)
			BoneMorphogeneticProtein boneMorphogeneticProtein = new BoneMorphogeneticProtein(aminoAcidSequence);
			cytoplasm.registerBMP(boneMorphogeneticProtein);
			return Optional.of(boneMorphogeneticProtein);
		}
		else if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Glutamate && aminoacids.get(2) == AminoAcid.Arginine)
		{
			Stats.nogginProteinCreated++;
			return Optional.of(new Noggin(aminoAcidSequence));
		}
		else if(aminoacids.size() > 5 && aminoacids.get(1) == AminoAcid.Glutamine && aminoacids.get(2) == AminoAcid.Asparagine)
		{
			// The first amino acids of PAX6
			Stats.PAX6ProteinsCreated++;
			return Optional.of(new PAX6(aminoAcidSequence));
		}
		else if(aminoacids.size() > 10 && aminoacids.subList(1, Math.min(5, aminoacids.size())).containsAll(MOTOR_PROTEIN_AMINO_ACIDS))
		{
			Stats.motorProteinsCreated++;
			return Optional.of(new MotorProtein(aminoAcidSequence));
		}
		else if(aminoacids.size() > 4 && aminoacids.subList(1, Math.min(5, aminoacids.size())).containsAll(SWEET_TASTE_AMINO_ACIDS))
		{
			Stats.sweetReceptorsCreated++;
			return Optional.of(new SweetTasteReceptor(aminoAcidSequence));
		}
		else if(molarMass > EnzymeProtein.MINIMUM_SIZE)
		{
			Stats.enzymeProteinsCreated++;
			return Optional.of(new EnzymeProtein(aminoAcidSequence));
		}
		else if(aminoacids.size() > 50)
		{
			Stats.transporterProteinsCreated++;
			return Optional.of(new TransporterProtein(aminoAcidSequence));
		}
		else if(aminoacids.contains(AminoAcid.Tryptophan) || aminoacids.contains(AminoAcid.Serine))
		{
			Stats.regulatoryProteinCreated++;
			return Optional.of(new RegulatoryProtein(aminoAcidSequence, this.sourceDNA));
		}
		else
		{
			Stats.genericProteinsCreated++;
			return Optional.of(new Protein(aminoAcidSequence)); // fallback, could be your original Protein
		}
	}
}

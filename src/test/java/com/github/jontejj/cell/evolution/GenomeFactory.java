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
import java.util.Arrays;
import java.util.List;

public class GenomeFactory
{
	public static List<DNA> buildAllProteinDNAs()
	{
		List<DNA> genome = new ArrayList<>();

		// StructuralProtein → needs Leucine, Leucine after Methionine
		genome.add(DNA.fromAminoAcidSequence(Arrays.asList(	AminoAcid.Leucine, AminoAcid.Leucine, // signature
															AminoAcid.Alanine, AminoAcid.Valine, AminoAcid.Isoleucine),
												0));

		// DnaA → Serine, Leucine after Methionine
		genome.add(DNA
				.fromAminoAcidSequence(	Arrays.asList(AminoAcid.Serine, AminoAcid.Leucine, AminoAcid.Glycine, AminoAcid.Glycine, AminoAcid.Valine),
										1));

		// AMPK → Glutamate, Threonine after Methionine
		genome.add(DNA.fromAminoAcidSequence(Arrays.asList(	AminoAcid.Glutamate, AminoAcid.Threonine, AminoAcid.Aspartate, AminoAcid.Lysine,
															AminoAcid.Isoleucine),
												2));

		// AminoAcidSynthaseProtein → Alanine after Methionine and long enough
		List<AminoAcid> synthase = new ArrayList<>();
		synthase.add(AminoAcid.Alanine);
		for(int i = 0; i < 20; i++)
		{
			synthase.add(AminoAcid.Glycine);
		}
		genome.add(DNA.fromAminoAcidSequence(synthase, 3));

		// EnzymeProtein → molarMass > threshold (long, but no Alanine as 2nd AA)
		List<AminoAcid> enzyme = new ArrayList<>();
		enzyme.add(AminoAcid.Valine);
		for(int i = 0; i < 30; i++)
		{
			enzyme.add(AminoAcid.Tyrosine);
		}
		genome.add(DNA.fromAminoAcidSequence(enzyme, 4));

		// RegulatoryProtein → contains Tryptophan or Serine
		genome.add(DNA.fromAminoAcidSequence(Arrays.asList(AminoAcid.Glycine, AminoAcid.Tryptophan, AminoAcid.Valine), 5));

		// TransporterProtein → size > 50
		List<AminoAcid> transporter = new ArrayList<>();
		for(int i = 0; i < 55; i++)
		{
			transporter.add(AminoAcid.Asparagine);
		}
		genome.add(DNA.fromAminoAcidSequence(transporter, 6));

		// MotorProtein → Glycine, Proline, Alanine after Methionine, length > 20
		List<AminoAcid> motor = new ArrayList<>();
		motor.add(AminoAcid.Glycine);
		motor.add(AminoAcid.Proline);
		motor.add(AminoAcid.Alanine);
		for(int i = 0; i < 18; i++)
		{
			motor.add(AminoAcid.Serine);
		}
		genome.add(DNA.fromAminoAcidSequence(motor, 7));

		return genome;
	}
}

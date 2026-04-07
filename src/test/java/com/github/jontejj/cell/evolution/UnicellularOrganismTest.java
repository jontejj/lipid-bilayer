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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.nd4j.shade.protobuf.common.collect.Lists;

import com.github.jontejj.cell.evolution.game.TestCellWorld;
import com.github.jontejj.cell.evolution.proteins.DnaA;
import com.github.jontejj.cell.evolution.proteins.EnzymeProtein;

public class UnicellularOrganismTest
{
	@Test
	void testThatFissionCanOnlyHappenOnceAfterADeadCellWithTheSameGenomeHasBeenEaten() throws Exception
	{
		TestCellWorld world = new TestCellWorld();
		Genome withAllSimulatedProteins = new Genome(GenomeFactory.buildAllProteinDNAs());
		UnicellularOrganism smallOrganism = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(withAllSimulatedProteins), world);

		assertThat(smallOrganism.binaryFission()).isEqualTo(Optional.empty());

		// Make sure that some needed proteins exist in the cytoplasm
		List<AminoAcid> enzymeAminoAcids = Lists.newArrayList();
		for(int i = 0; i < 40000; i++)
		{
			enzymeAminoAcids.add(AminoAcid.Methionine);
		}
		double molarMass = enzymeAminoAcids.stream().mapToDouble(AminoAcid::molarMass).sum();
		smallOrganism.cytoplasm().addProtein(new EnzymeProtein(new AminoAcidSequence(enzymeAminoAcids, molarMass, 0)));

		smallOrganism.cytoplasm()
				.addProtein(new DnaA(new AminoAcidSequence(List.of(AminoAcid.Methionine, AminoAcid.Serine, AminoAcid.Leucine), 0, 0)));
		// Three timesteps are needed to produce enough atp for fission
		smallOrganism.timestep(world);
		smallOrganism.timestep(world);
		smallOrganism.timestep(world);

		// Simulate that the organism has enough resources even after the timesteps
		Map<Nucleobases, Integer> requiredNucleotideCountsForFission = withAllSimulatedProteins.totalNucleotideCounts();
		for(Map.Entry<Nucleobases, Integer> entry : requiredNucleotideCountsForFission.entrySet())
		{
			smallOrganism.cytoplasm().increaseResourceAmount(entry.getKey(), entry.getValue());
		}

		Optional<Organism> binaryFissionResult = smallOrganism.binaryFission();
		Organism newOrganism = binaryFissionResult.orElseThrow();
		assertThat(newOrganism).isInstanceOf(UnicellularOrganism.class);

		smallOrganism.cytoplasm()
				.addProtein(new DnaA(new AminoAcidSequence(List.of(AminoAcid.Methionine, AminoAcid.Serine, AminoAcid.Leucine), 0, 0)));

		smallOrganism.timestep(world);
		smallOrganism.timestep(world);
		smallOrganism.timestep(world);

		binaryFissionResult = smallOrganism.binaryFission();
		assertThat(binaryFissionResult).isEqualTo(Optional.empty());

		// Organism miniOrganism = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(Genome.generate(10)), world);
	}
}

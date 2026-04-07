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

import org.junit.jupiter.api.Test;

import com.github.jontejj.cell.evolution.game.CellWorld;

public class MulticellularOrganismTest
{
	@Test
	void testThatMultipleOrganismsNucleotideResourcesCanBeSummed() throws Exception
	{
		CellWorld world = new CellWorld();
		Organism smallOrganism = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(Genome.generate(50)), world);
		Organism miniOrganism = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(Genome.generate(10)), world);

		MulticellularOrganism multicellularOrganism = new MulticellularOrganism("TestMulti", List.of(smallOrganism, miniOrganism));
		Map<Nucleobases, Long> actualNucleotideResources = multicellularOrganism.nucleotideResources();
		Map<Nucleobases, Long> miniNucleotideResources = miniOrganism.nucleotideResources();
		for(Map.Entry<Nucleobases, Long> entry : smallOrganism.nucleotideResources().entrySet())
		{
			assertThat(actualNucleotideResources.get(entry.getKey())).isEqualTo(entry.getValue() + miniNucleotideResources.get(entry.getKey()));
		}
	}
}

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

import static com.github.jontejj.cell.evolution.AminoAcid.Aspartate;
import static com.github.jontejj.cell.evolution.AminoAcid.Histidine;
import static com.github.jontejj.cell.evolution.AminoAcid.Isoleucine;
import static com.github.jontejj.cell.evolution.AminoAcid.Methionine;
import static com.github.jontejj.cell.evolution.AminoAcid.Proline;
import static com.github.jontejj.cell.evolution.AminoAcid.Serine;
import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;
import org.junit.jupiter.api.Test;

import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.DNA;
import com.github.jontejj.cell.evolution.Genome;
import com.github.jontejj.cell.evolution.Nucleobases;
import com.github.jontejj.cell.evolution.Nucleus;
import com.google.common.collect.ImmutableList;

public class ProteinTest
{
	@Test
	public void testThatTranslationTranslatesCorrectAminoAcids() throws Exception
	{
		// TODO: refactor so that world is not needed here
		World<SimulationBody> world = new World<SimulationBody>();

		Cytoplasm minimalCytoplasm = new Cytoplasm(new Nucleus(Genome.generate(4)), world);

		Protein protein = new DNA(Nucleobases.fromString("AUGCCAGAUCACUAA"), 0).transcribe(minimalCytoplasm).get().translate(minimalCytoplasm).get();
		assertThat(protein.aminoAcids()).hasSameElementsAs(ImmutableList.of(Methionine, Proline, Aspartate, Histidine));

		protein = new DNA(Nucleobases.fromString("AUGAUCUCCUAA"), 0).transcribe(minimalCytoplasm).get().translate(minimalCytoplasm).get();
		assertThat(protein.aminoAcids()).hasSameElementsAs(of(Methionine, Isoleucine, Serine));
	}
}

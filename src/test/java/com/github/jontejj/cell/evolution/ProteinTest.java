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

import static com.github.jontejj.cell.evolution.AminoAcid.Aspartate;
import static com.github.jontejj.cell.evolution.AminoAcid.Histidine;
import static com.github.jontejj.cell.evolution.AminoAcid.Isoleucine;
import static com.github.jontejj.cell.evolution.AminoAcid.Methionine;
import static com.github.jontejj.cell.evolution.AminoAcid.Proline;
import static com.github.jontejj.cell.evolution.AminoAcid.Serine;
import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

public class ProteinTest
{
	@Test
	public void testThatTranslationTranslatesCorrectAminoAcids() throws Exception
	{
		Protein protein = new DNA(Nucleobases.fromString("AUGCCAGAUCACUAA")).transcribe().translate();
		assertThat(protein.aminoAcids()).hasSameElementsAs(ImmutableList.of(Methionine, Proline, Aspartate, Histidine));

		protein = new DNA(Nucleobases.fromString("AUGAUCUCCUAA")).transcribe().translate();
		assertThat(protein.aminoAcids()).hasSameElementsAs(of(Methionine, Isoleucine, Serine));
	}
}

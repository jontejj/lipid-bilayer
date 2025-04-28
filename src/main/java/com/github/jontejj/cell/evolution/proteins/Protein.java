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

import java.util.List;

import com.github.jontejj.cell.evolution.AminoAcid;
import com.github.jontejj.cell.evolution.AminoAcidSequence;

public class Protein
{
	private final AminoAcidSequence sequence;
	private final boolean selfInteracts;

	public Protein(AminoAcidSequence aminoAcidSequence)
	{
		this.sequence = aminoAcidSequence;
		this.selfInteracts = this.sequence.signature() % 10 <= 2;
	}

	public double polarity()
	{
		// TODO: implement so that each protein functions differently
		return 0.0;
	}

	// TODO: add physical size etc about the aminoacids? Folding?

	@Override
	public String toString()
	{
		return aminoAcids().toString();
	}

	public double molecularMass()
	{
		return sequence.molecularMass();
	}

	public List<AminoAcid> aminoAcids()
	{
		return sequence.aminoacids();
	}

	/**
	 * Simulates rare, non-specific protein interactions (~0.1%), with optional self-interaction.
	 * TODO: what type of interactions?
	 * 1. Form ProteinComplex with each other
	 * 2. destroy each other (degradation)
	 * 3. Short-lived, reversible interactions. (Example: Enzyme-substrate binding.)
	 * 4. Proteins can only function when bound together. (Example: Subunits of viral capsids.)
	 * 5. Highly selective, based on complementary surfaces or sequences. (Important for signal transduction or immune recognition.)
	 */
	public boolean interactsWith(Protein other)
	{
		if(!this.selfInteracts && this.sequence.signature() == other.sequence.signature())
			return false;
		// about 0.1% of all possible protein pairs interact in humans.
		if(this.sequence.signature() % 1000 == other.sequence.signature() % 1000)
			return true;
		return false;
	}
}

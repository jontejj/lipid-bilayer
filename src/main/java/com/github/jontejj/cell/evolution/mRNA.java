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

import java.util.List;

import org.assertj.core.util.Lists;

public class mRNA
{
	private List<Codon> codons;

	public mRNA(List<Codon> codons)
	{
		this.codons = codons;
	}

	public Protein translate()
	{
		// TODO: splicing?
		// TODO: what if multiple proteins exist in the codons? Right now this only returns the first protein
		List<AminoAcid> aminoacids = Lists.newArrayList();
		for(Codon c : codons)
		{
			AminoAcid aa = AminoAcid.translate(c);
			if(aa == null) // null is stop marker
			{
				break;
			}
			aminoacids.add(aa);
		}
		// TODO: how to fold the protein based on the amino acids?
		return new Protein(aminoacids);
	}
}

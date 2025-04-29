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

public enum AminoAcid
{
	/**
	 * mRNA:
	 * AUG -> Met
	 * AUC -> lle
	 * UCC -> Ser
	 * UAA -> Stop
	 * etc.
	 */
	Alanine("Ala", 89.094),
	Arginine("Arg", 174.203),
	Asparagine("Asn", 132.119),
	Aspartate("Asp", 133.104),
	Cysteine("Cys", 121.154),
	Glutamine("Gln", 146.146),
	Glutamate("Glu", 147.131),
	Glycine("Gly", 75.067),
	Histidine("His", 155.156),
	Isoleucine("Ile", 131.175),
	Leucine("Leu", 131.175),
	Lysine("Lys", 146.189),
	Methionine("Met", 149.208),
	Phenylalanine("Phe", 165.192),
	Proline("Pro", 115.132),
	Serine("Ser", 105.093),
	Threonine("Thr", 119.119),
	Tryptophan("Trp", 204.228),
	Tyrosine("Tyr", 181.191),
	Valine("Val", 117.148);

	private final String shortName;
	private final double molecularMass;

	private AminoAcid(String shortName, double molecularMass)
	{
		this.shortName = shortName;
		this.molecularMass = molecularMass;
	}

	@Override
	public String toString()
	{
		return shortName;
	}

	public String shortName()
	{
		return shortName;
	}

	public double molecularMass()
	{
		return molecularMass;
	}

}

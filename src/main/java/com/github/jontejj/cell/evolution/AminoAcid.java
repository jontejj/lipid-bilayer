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

import static com.github.jontejj.cell.evolution.Nucleobases.ADENINE;
import static com.github.jontejj.cell.evolution.Nucleobases.CYTOSINE;
import static com.github.jontejj.cell.evolution.Nucleobases.GUANINE;
import static com.github.jontejj.cell.evolution.Nucleobases.URACIL;

import java.util.Map;

import com.google.common.collect.Maps;

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

	private static final Map<Codon, AminoAcid> aminoAcids;

	static
	{
		aminoAcids = Maps.newHashMap();

		// First letter Uracil
		aminoAcids.put(Codon.getOrInitialize(URACIL, URACIL, URACIL), Phenylalanine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, URACIL, CYTOSINE), Phenylalanine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, URACIL, ADENINE), Leucine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, URACIL, GUANINE), Leucine);

		aminoAcids.put(Codon.getOrInitialize(URACIL, CYTOSINE, URACIL), Serine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, CYTOSINE, CYTOSINE), Serine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, CYTOSINE, ADENINE), Serine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, CYTOSINE, GUANINE), Serine);

		aminoAcids.put(Codon.getOrInitialize(URACIL, ADENINE, URACIL), Tyrosine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, ADENINE, CYTOSINE), Tyrosine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, ADENINE, ADENINE), null);
		aminoAcids.put(Codon.getOrInitialize(URACIL, ADENINE, GUANINE), null);

		aminoAcids.put(Codon.getOrInitialize(URACIL, GUANINE, URACIL), Cysteine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, GUANINE, CYTOSINE), Cysteine);
		aminoAcids.put(Codon.getOrInitialize(URACIL, GUANINE, ADENINE), null);
		aminoAcids.put(Codon.getOrInitialize(URACIL, GUANINE, GUANINE), Tryptophan);

		// First letter Cytosine
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, URACIL, URACIL), Leucine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, URACIL, CYTOSINE), Leucine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, URACIL, ADENINE), Leucine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, URACIL, GUANINE), Leucine);

		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, CYTOSINE, URACIL), Proline);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, CYTOSINE, CYTOSINE), Proline);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, CYTOSINE, ADENINE), Proline);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, CYTOSINE, GUANINE), Proline);

		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, ADENINE, URACIL), Histidine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, ADENINE, CYTOSINE), Histidine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, ADENINE, ADENINE), Glutamine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, ADENINE, GUANINE), Glutamine);

		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, GUANINE, URACIL), Arginine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, GUANINE, CYTOSINE), Arginine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, GUANINE, ADENINE), Arginine);
		aminoAcids.put(Codon.getOrInitialize(CYTOSINE, GUANINE, GUANINE), Arginine);

		// First letter Adenine
		aminoAcids.put(Codon.getOrInitialize(ADENINE, URACIL, URACIL), Isoleucine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, URACIL, CYTOSINE), Isoleucine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, URACIL, ADENINE), Isoleucine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, URACIL, GUANINE), Methionine);

		aminoAcids.put(Codon.getOrInitialize(ADENINE, CYTOSINE, URACIL), Threonine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, CYTOSINE, CYTOSINE), Threonine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, CYTOSINE, ADENINE), Threonine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, CYTOSINE, GUANINE), Threonine);

		aminoAcids.put(Codon.getOrInitialize(ADENINE, ADENINE, URACIL), Asparagine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, ADENINE, CYTOSINE), Asparagine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, ADENINE, ADENINE), Lysine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, ADENINE, GUANINE), Lysine);

		aminoAcids.put(Codon.getOrInitialize(ADENINE, GUANINE, URACIL), Serine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, GUANINE, CYTOSINE), Serine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, GUANINE, ADENINE), Arginine);
		aminoAcids.put(Codon.getOrInitialize(ADENINE, GUANINE, GUANINE), Arginine);

		// First letter Guanine
		aminoAcids.put(Codon.getOrInitialize(GUANINE, URACIL, URACIL), Valine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, URACIL, CYTOSINE), Valine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, URACIL, ADENINE), Valine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, URACIL, GUANINE), Valine);

		aminoAcids.put(Codon.getOrInitialize(GUANINE, CYTOSINE, URACIL), Alanine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, CYTOSINE, CYTOSINE), Alanine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, CYTOSINE, ADENINE), Alanine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, CYTOSINE, GUANINE), Alanine);

		aminoAcids.put(Codon.getOrInitialize(GUANINE, ADENINE, URACIL), Aspartate);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, ADENINE, CYTOSINE), Aspartate);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, ADENINE, ADENINE), Glutamate);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, ADENINE, GUANINE), Glutamate);

		aminoAcids.put(Codon.getOrInitialize(GUANINE, GUANINE, URACIL), Glycine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, GUANINE, CYTOSINE), Glycine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, GUANINE, ADENINE), Glycine);
		aminoAcids.put(Codon.getOrInitialize(GUANINE, GUANINE, GUANINE), Glycine);
	}

	/**
	 * returns null for the stop marker
	 * Codon table: https://cdn.kastatic.org/ka-perseus-images/f5de6355003ee322782b26404ef0733a1d1a61b0.png
	 */
	public static AminoAcid translate(Codon codon)
	{
		if(codon.isDNAForSure())
			throw new UnsupportedOperationException("Codon must be RNA");
		AminoAcid aa = aminoAcids.get(codon);
		return aa;
	}
}

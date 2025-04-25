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

	public double molecularMass()
	{
		return molecularMass;
	}

	private static final Map<Codon, AminoAcid> aminoAcids;

	static
	{
		aminoAcids = Maps.newHashMap();

		// First letter Uracil
		aminoAcids.put(new Codon(URACIL, URACIL, URACIL), Phenylalanine);
		aminoAcids.put(new Codon(URACIL, URACIL, CYTOSINE), Phenylalanine);
		aminoAcids.put(new Codon(URACIL, URACIL, ADENINE), Leucine);
		aminoAcids.put(new Codon(URACIL, URACIL, GUANINE), Leucine);

		aminoAcids.put(new Codon(URACIL, CYTOSINE, URACIL), Serine);
		aminoAcids.put(new Codon(URACIL, CYTOSINE, CYTOSINE), Serine);
		aminoAcids.put(new Codon(URACIL, CYTOSINE, ADENINE), Serine);
		aminoAcids.put(new Codon(URACIL, CYTOSINE, GUANINE), Serine);

		aminoAcids.put(new Codon(URACIL, ADENINE, URACIL), Tyrosine);
		aminoAcids.put(new Codon(URACIL, ADENINE, CYTOSINE), Tyrosine);
		aminoAcids.put(new Codon(URACIL, ADENINE, ADENINE), null);
		aminoAcids.put(new Codon(URACIL, ADENINE, GUANINE), null);

		aminoAcids.put(new Codon(URACIL, GUANINE, URACIL), Cysteine);
		aminoAcids.put(new Codon(URACIL, GUANINE, CYTOSINE), Cysteine);
		aminoAcids.put(new Codon(URACIL, GUANINE, ADENINE), null);
		aminoAcids.put(new Codon(URACIL, GUANINE, GUANINE), Tryptophan);

		// First letter Cytosine
		aminoAcids.put(new Codon(CYTOSINE, URACIL, URACIL), Leucine);
		aminoAcids.put(new Codon(CYTOSINE, URACIL, CYTOSINE), Leucine);
		aminoAcids.put(new Codon(CYTOSINE, URACIL, ADENINE), Leucine);
		aminoAcids.put(new Codon(CYTOSINE, URACIL, GUANINE), Leucine);

		aminoAcids.put(new Codon(CYTOSINE, CYTOSINE, URACIL), Proline);
		aminoAcids.put(new Codon(CYTOSINE, CYTOSINE, CYTOSINE), Proline);
		aminoAcids.put(new Codon(CYTOSINE, CYTOSINE, ADENINE), Proline);
		aminoAcids.put(new Codon(CYTOSINE, CYTOSINE, GUANINE), Proline);

		aminoAcids.put(new Codon(CYTOSINE, ADENINE, URACIL), Histidine);
		aminoAcids.put(new Codon(CYTOSINE, ADENINE, CYTOSINE), Histidine);
		aminoAcids.put(new Codon(CYTOSINE, ADENINE, ADENINE), Glutamine);
		aminoAcids.put(new Codon(CYTOSINE, ADENINE, GUANINE), Glutamine);

		aminoAcids.put(new Codon(CYTOSINE, GUANINE, URACIL), Arginine);
		aminoAcids.put(new Codon(CYTOSINE, GUANINE, CYTOSINE), Arginine);
		aminoAcids.put(new Codon(CYTOSINE, GUANINE, ADENINE), Arginine);
		aminoAcids.put(new Codon(CYTOSINE, GUANINE, GUANINE), Arginine);

		// First letter Adenine
		aminoAcids.put(new Codon(ADENINE, URACIL, URACIL), Isoleucine);
		aminoAcids.put(new Codon(ADENINE, URACIL, CYTOSINE), Isoleucine);
		aminoAcids.put(new Codon(ADENINE, URACIL, ADENINE), Isoleucine);
		aminoAcids.put(new Codon(ADENINE, URACIL, GUANINE), Methionine);

		aminoAcids.put(new Codon(ADENINE, CYTOSINE, URACIL), Threonine);
		aminoAcids.put(new Codon(ADENINE, CYTOSINE, CYTOSINE), Threonine);
		aminoAcids.put(new Codon(ADENINE, CYTOSINE, ADENINE), Threonine);
		aminoAcids.put(new Codon(ADENINE, CYTOSINE, GUANINE), Threonine);

		aminoAcids.put(new Codon(ADENINE, ADENINE, URACIL), Asparagine);
		aminoAcids.put(new Codon(ADENINE, ADENINE, CYTOSINE), Asparagine);
		aminoAcids.put(new Codon(ADENINE, ADENINE, ADENINE), Lysine);
		aminoAcids.put(new Codon(ADENINE, ADENINE, GUANINE), Lysine);

		aminoAcids.put(new Codon(ADENINE, GUANINE, URACIL), Serine);
		aminoAcids.put(new Codon(ADENINE, GUANINE, CYTOSINE), Serine);
		aminoAcids.put(new Codon(ADENINE, GUANINE, ADENINE), Arginine);
		aminoAcids.put(new Codon(ADENINE, GUANINE, GUANINE), Arginine);

		// First letter Guanine
		aminoAcids.put(new Codon(GUANINE, URACIL, URACIL), Valine);
		aminoAcids.put(new Codon(GUANINE, URACIL, CYTOSINE), Valine);
		aminoAcids.put(new Codon(GUANINE, URACIL, ADENINE), Valine);
		aminoAcids.put(new Codon(GUANINE, URACIL, GUANINE), Valine);

		aminoAcids.put(new Codon(GUANINE, CYTOSINE, URACIL), Alanine);
		aminoAcids.put(new Codon(GUANINE, CYTOSINE, CYTOSINE), Alanine);
		aminoAcids.put(new Codon(GUANINE, CYTOSINE, ADENINE), Alanine);
		aminoAcids.put(new Codon(GUANINE, CYTOSINE, GUANINE), Alanine);

		aminoAcids.put(new Codon(GUANINE, ADENINE, URACIL), Aspartate);
		aminoAcids.put(new Codon(GUANINE, ADENINE, CYTOSINE), Aspartate);
		aminoAcids.put(new Codon(GUANINE, ADENINE, ADENINE), Glutamate);
		aminoAcids.put(new Codon(GUANINE, ADENINE, GUANINE), Glutamate);

		aminoAcids.put(new Codon(GUANINE, GUANINE, URACIL), Glycine);
		aminoAcids.put(new Codon(GUANINE, GUANINE, CYTOSINE), Glycine);
		aminoAcids.put(new Codon(GUANINE, GUANINE, ADENINE), Glycine);
		aminoAcids.put(new Codon(GUANINE, GUANINE, GUANINE), Glycine);
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

/* Copyright 2019 jonatanjonsson
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

import static com.github.jontejj.cell.evolution.AminoAcid.Alanine;
import static com.github.jontejj.cell.evolution.AminoAcid.Arginine;
import static com.github.jontejj.cell.evolution.AminoAcid.Asparagine;
import static com.github.jontejj.cell.evolution.AminoAcid.Aspartate;
import static com.github.jontejj.cell.evolution.AminoAcid.Cysteine;
import static com.github.jontejj.cell.evolution.AminoAcid.Glutamate;
import static com.github.jontejj.cell.evolution.AminoAcid.Glutamine;
import static com.github.jontejj.cell.evolution.AminoAcid.Glycine;
import static com.github.jontejj.cell.evolution.AminoAcid.Histidine;
import static com.github.jontejj.cell.evolution.AminoAcid.Isoleucine;
import static com.github.jontejj.cell.evolution.AminoAcid.Leucine;
import static com.github.jontejj.cell.evolution.AminoAcid.Lysine;
import static com.github.jontejj.cell.evolution.AminoAcid.Methionine;
import static com.github.jontejj.cell.evolution.AminoAcid.Phenylalanine;
import static com.github.jontejj.cell.evolution.AminoAcid.Proline;
import static com.github.jontejj.cell.evolution.AminoAcid.Serine;
import static com.github.jontejj.cell.evolution.AminoAcid.Threonine;
import static com.github.jontejj.cell.evolution.AminoAcid.Tryptophan;
import static com.github.jontejj.cell.evolution.AminoAcid.Tyrosine;
import static com.github.jontejj.cell.evolution.AminoAcid.Valine;
import static com.github.jontejj.cell.evolution.Nucleobases.ADENINE;
import static com.github.jontejj.cell.evolution.Nucleobases.CYTOSINE;
import static com.github.jontejj.cell.evolution.Nucleobases.GUANINE;
import static com.github.jontejj.cell.evolution.Nucleobases.URACIL;

import java.util.EnumMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;

/**
 * mRNA is built up from multiple codons, which itself contains several
 * nucleobases; cytosine [C], guanine [G], adenine [A] or thymine [T].
 */
@Immutable
public record Codon(Nucleobases first, Nucleobases middle, Nucleobases last)
{
	public boolean isDNAForSure()
	{
		if(first == Nucleobases.THYMINE || middle == Nucleobases.THYMINE || last == Nucleobases.THYMINE)
			return true;

		return false;
	}

	private static final Map<Nucleobases, Map<Nucleobases, Map<Nucleobases, Codon>>> codonPool = Maps.newHashMap();

	public static Codon get(Nucleobases first, Nucleobases middle, Nucleobases last)
	{
		return codonPool.get(first).get(middle).get(last);
	}

	public static Codon getOrInitialize(Nucleobases first, Nucleobases middle, Nucleobases last)
	{
		Map<Nucleobases, Map<Nucleobases, Codon>> firstLevel = codonPool.get(first);
		if(firstLevel == null)
		{
			firstLevel = Maps.newHashMap();
			codonPool.put(first, firstLevel);
		}
		Map<Nucleobases, Codon> middleLevel = firstLevel.get(middle);
		if(middleLevel == null)
		{
			middleLevel = Maps.newHashMap();
			firstLevel.put(middle, middleLevel);
		}
		Codon lastLevel = middleLevel.get(last);
		if(lastLevel == null)
		{
			lastLevel = new Codon(first, middle, last);
			middleLevel.put(last, lastLevel);
		}
		return lastLevel;
	}

	private static final Map<Codon, AminoAcid> aminoAcids;
	private static final Map<AminoAcid, Nucleobases[]> aminoAcidNucleotides;

	static
	{
		aminoAcids = Maps.newHashMap();
		aminoAcidNucleotides = new EnumMap<>(AminoAcid.class);

		// First letter Uracil
		mapNucleotidesToAminoAcid(URACIL, URACIL, URACIL, Phenylalanine);
		mapNucleotidesToAminoAcid(URACIL, URACIL, CYTOSINE, Phenylalanine);
		mapNucleotidesToAminoAcid(URACIL, URACIL, ADENINE, Leucine);
		mapNucleotidesToAminoAcid(URACIL, URACIL, GUANINE, Leucine);

		mapNucleotidesToAminoAcid(URACIL, CYTOSINE, URACIL, Serine);
		mapNucleotidesToAminoAcid(URACIL, CYTOSINE, CYTOSINE, Serine);
		mapNucleotidesToAminoAcid(URACIL, CYTOSINE, ADENINE, Serine);
		mapNucleotidesToAminoAcid(URACIL, CYTOSINE, GUANINE, Serine);

		mapNucleotidesToAminoAcid(URACIL, ADENINE, URACIL, Tyrosine);
		mapNucleotidesToAminoAcid(URACIL, ADENINE, CYTOSINE, Tyrosine);
		mapNucleotidesToAminoAcid(URACIL, ADENINE, ADENINE, null); // null == Stop marker
		mapNucleotidesToAminoAcid(URACIL, ADENINE, GUANINE, null);

		mapNucleotidesToAminoAcid(URACIL, GUANINE, URACIL, Cysteine);
		mapNucleotidesToAminoAcid(URACIL, GUANINE, CYTOSINE, Cysteine);
		mapNucleotidesToAminoAcid(URACIL, GUANINE, ADENINE, null);
		mapNucleotidesToAminoAcid(URACIL, GUANINE, GUANINE, Tryptophan);

		// First letter Cytosine
		mapNucleotidesToAminoAcid(CYTOSINE, URACIL, URACIL, Leucine);
		mapNucleotidesToAminoAcid(CYTOSINE, URACIL, CYTOSINE, Leucine);
		mapNucleotidesToAminoAcid(CYTOSINE, URACIL, ADENINE, Leucine);
		mapNucleotidesToAminoAcid(CYTOSINE, URACIL, GUANINE, Leucine);

		mapNucleotidesToAminoAcid(CYTOSINE, CYTOSINE, URACIL, Proline);
		mapNucleotidesToAminoAcid(CYTOSINE, CYTOSINE, CYTOSINE, Proline);
		mapNucleotidesToAminoAcid(CYTOSINE, CYTOSINE, ADENINE, Proline);
		mapNucleotidesToAminoAcid(CYTOSINE, CYTOSINE, GUANINE, Proline);

		mapNucleotidesToAminoAcid(CYTOSINE, ADENINE, URACIL, Histidine);
		mapNucleotidesToAminoAcid(CYTOSINE, ADENINE, CYTOSINE, Histidine);
		mapNucleotidesToAminoAcid(CYTOSINE, ADENINE, ADENINE, Glutamine);
		mapNucleotidesToAminoAcid(CYTOSINE, ADENINE, GUANINE, Glutamine);

		mapNucleotidesToAminoAcid(CYTOSINE, GUANINE, URACIL, Arginine);
		mapNucleotidesToAminoAcid(CYTOSINE, GUANINE, CYTOSINE, Arginine);
		mapNucleotidesToAminoAcid(CYTOSINE, GUANINE, ADENINE, Arginine);
		mapNucleotidesToAminoAcid(CYTOSINE, GUANINE, GUANINE, Arginine);

		// First letter Adenine
		mapNucleotidesToAminoAcid(ADENINE, URACIL, URACIL, Isoleucine);
		mapNucleotidesToAminoAcid(ADENINE, URACIL, CYTOSINE, Isoleucine);
		mapNucleotidesToAminoAcid(ADENINE, URACIL, ADENINE, Isoleucine);
		mapNucleotidesToAminoAcid(ADENINE, URACIL, GUANINE, Methionine);

		mapNucleotidesToAminoAcid(ADENINE, CYTOSINE, URACIL, Threonine);
		mapNucleotidesToAminoAcid(ADENINE, CYTOSINE, CYTOSINE, Threonine);
		mapNucleotidesToAminoAcid(ADENINE, CYTOSINE, ADENINE, Threonine);
		mapNucleotidesToAminoAcid(ADENINE, CYTOSINE, GUANINE, Threonine);

		mapNucleotidesToAminoAcid(ADENINE, ADENINE, URACIL, Asparagine);
		mapNucleotidesToAminoAcid(ADENINE, ADENINE, CYTOSINE, Asparagine);
		mapNucleotidesToAminoAcid(ADENINE, ADENINE, ADENINE, Lysine);
		mapNucleotidesToAminoAcid(ADENINE, ADENINE, GUANINE, Lysine);

		mapNucleotidesToAminoAcid(ADENINE, GUANINE, URACIL, Serine);
		mapNucleotidesToAminoAcid(ADENINE, GUANINE, CYTOSINE, Serine);
		mapNucleotidesToAminoAcid(ADENINE, GUANINE, ADENINE, Arginine);
		mapNucleotidesToAminoAcid(ADENINE, GUANINE, GUANINE, Arginine);

		// First letter Guanine
		mapNucleotidesToAminoAcid(GUANINE, URACIL, URACIL, Valine);
		mapNucleotidesToAminoAcid(GUANINE, URACIL, CYTOSINE, Valine);
		mapNucleotidesToAminoAcid(GUANINE, URACIL, ADENINE, Valine);
		mapNucleotidesToAminoAcid(GUANINE, URACIL, GUANINE, Valine);

		mapNucleotidesToAminoAcid(GUANINE, CYTOSINE, URACIL, Alanine);
		mapNucleotidesToAminoAcid(GUANINE, CYTOSINE, CYTOSINE, Alanine);
		mapNucleotidesToAminoAcid(GUANINE, CYTOSINE, ADENINE, Alanine);
		mapNucleotidesToAminoAcid(GUANINE, CYTOSINE, GUANINE, Alanine);

		mapNucleotidesToAminoAcid(GUANINE, ADENINE, URACIL, Aspartate);
		mapNucleotidesToAminoAcid(GUANINE, ADENINE, CYTOSINE, Aspartate);
		mapNucleotidesToAminoAcid(GUANINE, ADENINE, ADENINE, Glutamate);
		mapNucleotidesToAminoAcid(GUANINE, ADENINE, GUANINE, Glutamate);

		mapNucleotidesToAminoAcid(GUANINE, GUANINE, URACIL, Glycine);
		mapNucleotidesToAminoAcid(GUANINE, GUANINE, CYTOSINE, Glycine);
		mapNucleotidesToAminoAcid(GUANINE, GUANINE, ADENINE, Glycine);
		mapNucleotidesToAminoAcid(GUANINE, GUANINE, GUANINE, Glycine);
	}

	/**
	 * returns null for the stop marker
	 * Codon table: https://cdn.kastatic.org/ka-perseus-images/f5de6355003ee322782b26404ef0733a1d1a61b0.png
	 */
	public AminoAcid translate()
	{
		if(isDNAForSure())
			throw new UnsupportedOperationException("Codon must be RNA");
		AminoAcid aa = aminoAcids.get(this);
		return aa;
	}

	private static void mapNucleotidesToAminoAcid(Nucleobases first, Nucleobases middle, Nucleobases last, AminoAcid aminoAcid)
	{
		aminoAcids.put(Codon.getOrInitialize(first, middle, last), aminoAcid);
		if(aminoAcid != null)
		{
			aminoAcidNucleotides.put(aminoAcid, new Nucleobases[]{first, middle, last});
		}
	}

	public static Codon fromAminoAcid(AminoAcid aminoAcid)
	{
		Nucleobases[] bases = aminoAcidNucleotides.get(aminoAcid);
		if(bases == null)
			throw new IllegalArgumentException("No codon found for amino acid: " + aminoAcid);
		return new Codon(bases[0], bases[1], bases[2]);
	}

}

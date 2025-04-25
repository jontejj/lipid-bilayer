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

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A simplification of https://en.wikipedia.org/wiki/Nucleobase
 */
public enum Nucleobases
{
	CYTOSINE('C', 111.1),
	GUANINE('G', 151.13),
	ADENINE('A', 135.13),
	THYMINE('T', 126.1133)
	{
		public Nucleobases toRNA()
		{
			return URACIL;
		}
	},

	URACIL('U', 112.0868);

	private final char abv;

	private final double molecularMass;

	private ImmutableList<Nucleobases> othersDna;

	/**
	 * @param abv
	 * @param molecularMass expressed in g/mol (also called molar mass)
	 */
	private Nucleobases(char abv, double molecularMass)
	{
		this.abv = abv;
		this.molecularMass = molecularMass;
	}

	@Override
	public String toString()
	{
		return "" + abv;
	}

	public double molecularMass()
	{
		return molecularMass;
	}

	public Nucleobases toRNA()
	{
		return this;
	}

	public ImmutableList<Nucleobases> othersDna()
	{
		return othersDna;
	}

	public static ImmutableSet<Nucleobases> DNA = Sets.immutableEnumSet(CYTOSINE, GUANINE, ADENINE, THYMINE);

	public static ImmutableSet<Nucleobases> RNA = Sets.immutableEnumSet(CYTOSINE, GUANINE, ADENINE, URACIL);

	static
	{
		for(Nucleobases base : Nucleobases.values())
		{
			EnumSet<Nucleobases> others = EnumSet.copyOf(DNA);
			others.remove(base);
			base.othersDna = ImmutableList.copyOf(others);
		}
	}

	public static List<Nucleobases> fromString(String str)
	{
		return str.chars().mapToObj(c -> {
			switch((char) c)
			{
			case 'A':
				return Nucleobases.ADENINE;
			case 'C':
				return Nucleobases.CYTOSINE;
			case 'G':
				return Nucleobases.GUANINE;
			case 'T':
				return Nucleobases.THYMINE;
			case 'U':
				return Nucleobases.URACIL;
			}
			throw new UnsupportedOperationException(c + " is not identified as a nucleobase");
		}).collect(Collectors.toList());
	}
}

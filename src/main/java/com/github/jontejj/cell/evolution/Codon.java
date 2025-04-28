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
}

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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Stats
{
	public static long dnaReplicationProteinsCreated = 0;
	public static long structuralProteinsCreated = 0;
	public static long enzymeProteinsCreated = 0;
	public static long transporterProteinsCreated = 0;
	public static long regulatoryProteinCreated = 0;
	public static long genericProteinsCreated = 0;
	public static BigDecimal totalMassOfProteins = BigDecimal.ZERO;
	public static BigDecimal totalNumberOfAminoAcids = BigDecimal.ZERO;
	public static long pointMutations = 0;
	public static long additionMutations = 0;
	public static long deletionMutations = 0;

	public static String asString()
	{
		return "dnaReplicationProteinsCreated=" + dnaReplicationProteinsCreated + ", pointMutations=" + pointMutations + ", additionMutations="
				+ additionMutations + ", deletionMutations=" + deletionMutations + ", structuralProteinsCreated=" + structuralProteinsCreated
				+ ", enzymeProteinsCreated=" + enzymeProteinsCreated + ", transporterProteinsCreated=" + transporterProteinsCreated
				+ ", regulatoryProteinCreated=" + regulatoryProteinCreated + ", genericProteinsCreated=" + genericProteinsCreated
				+ ", totalProteinsCreated=" + totalProteinsCreated() + ", totalMassOfProteins=" + totalMassOfProteins + ", averageProteinMass="
				+ totalMassOfProteins.divide(BigDecimal.valueOf(totalProteinsCreated()), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP)
				+ ", averageNumberOfAminoAcids=" + totalNumberOfAminoAcids.divide(BigDecimal.valueOf(totalProteinsCreated()), MathContext.DECIMAL128)
						.setScale(0, RoundingMode.HALF_UP);
	}

	public static long totalProteinsCreated()
	{
		return structuralProteinsCreated + enzymeProteinsCreated + transporterProteinsCreated + regulatoryProteinCreated + genericProteinsCreated;
	}
}

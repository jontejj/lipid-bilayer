/* Copyright 2018 jonatanjonsson
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
package atoms;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OrbitalsTest
{
	@ParameterizedTest
	@CsvSource({"H, 1", "He, 0", "Li, 1", "Be, 2", "B, 3", "C, 4", "N, -3", "O, -2", "F, -1", "Ne, 0", "Ti, 3"})
	public void testThatTypicalValancesAreComputedCorrectly(ChemicalElements element, int typicalValance) throws Exception
	{
		assertThat(Orbitals.computeTypicalValance(element)).isEqualTo(typicalValance);
	}
}

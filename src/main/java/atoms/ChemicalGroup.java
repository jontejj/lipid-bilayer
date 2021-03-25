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

public enum ChemicalGroup
{
	HYDROGEN(1),
	ALKALI(1),
	ALKALINE_EARTH_METALS(2),
	COINAGE_METALS(null),
	TRIELS(3),
	/**
	 * +4 - -4
	 */
	TETRELS(4),
	PNICTOGENS(-3),
	CHALCOGENS(-2),
	HALOGENS(-1),
	NOBLE_GASES(0);

	private final Integer typicalValance;

	ChemicalGroup(Integer typicalValance)
	{
		this.typicalValance = typicalValance;
	}
}

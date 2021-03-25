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

/**
 * An instance of {@link ChemicalElements}
 */
public class Atom
{
	private final ChemicalElements element;

	private Atom(ChemicalElements element)
	{
		this.element = element;
	}

	public static Atom create(ChemicalElements element)
	{
		return new Atom(element);
	}

	@Override
	public String toString()
	{
		return element.shortName();
	}
}

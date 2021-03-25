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
package evolution;

import java.util.EnumSet;
import java.util.Set;

/**
 * A simplification of https://en.wikipedia.org/wiki/Nucleobase
 */
public enum Nucleobases
{
	CYTOSINE('C'),
	GUANINE('G'),
	ADENINE('A'),
	THYMINE('T'),

	URACIL('U');

	private final char abv;

	private Nucleobases(char abv)
	{
		this.abv = abv;
	}

	public static Set<Nucleobases> DNA = EnumSet.of(CYTOSINE, GUANINE, ADENINE, THYMINE);

	public static Set<Nucleobases> RNA = EnumSet.of(CYTOSINE, GUANINE, ADENINE, URACIL);
}

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
package org.fest.assertions;

import static java.lang.Math.abs;
import static org.fest.assertions.ErrorMessages.unexpectedNotEqual;
import static org.fest.assertions.Formatting.format;

import physics.Vec2;

public class Vec2Assertion extends GenericAssert<Vec2Assertion, Vec2>
{
	Vec2Assertion(Vec2 actual)
	{
		super(Vec2Assertion.class, actual);
	}

	public Vec2Assertion isEqualTo(Vec2 expected, Delta acceptableDelta)
	{
		if(isEqual(expected, acceptableDelta.doubleValue()))
			return this;
		super.failIfCustomMessageIsSet();
		throw failure(unexpectedNotEqual(actual, expected) + format(" using delta:<%s>", acceptableDelta.doubleValue()));
	}

	private boolean isEqual(Vec2 expected, double acceptableDelta)
	{
		return isEqualTo(actual.magnitude(), expected.magnitude(), acceptableDelta)
				&& isEqualTo(actual.direction(), expected.direction(), acceptableDelta);
	}

	private boolean isEqualTo(double actualDouble, double expected, double deltaValue)
	{
		if(Double.valueOf(actualDouble).compareTo(expected) == 0)
			return true;
		if(abs(expected - actualDouble) <= deltaValue)
			return true;
		return false;
	}
}

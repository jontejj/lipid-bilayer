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
package com.github.jontejj.cell.evolution.food;

import org.dyn4j.samples.framework.SimulationBody;

import com.github.jontejj.cell.evolution.Constants;
import com.github.jontejj.cell.evolution.Eatable;
import com.github.jontejj.cell.evolution.game.CellWorld;

public class GlucoseMolecules extends SimulationBody implements Eatable
{

	private static final double MOLECULAR_MASS = 180.16e-3; // g/mol for glucose
	private static final long MOLECULE_COUNT = 1_000_000; // 1 million glucose molecules

	@Override
	public double mass()
	{
		return (MOLECULAR_MASS / Constants.AVOGADROS_NUMBER) * MOLECULE_COUNT;
	}

	@Override
	public void onEaten(CellWorld world)
	{
		world.deferRemoval(this);
	}

	@Override
	public String toString()
	{
		return "GlucoseMolecule";
	}
}

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

import com.github.jontejj.cell.evolution.Eatable;
import com.github.jontejj.cell.evolution.game.CellWorld;

public class DeadCell extends SimulationBody implements Eatable
{
	private final double molecularMass;

	/**
	 * @param molecularMass from a dead cell this would be the sum of all nucleotides of its genome in the nucleus and perhaps the total
	 *            molecular mass of its proteins
	 */
	public DeadCell(double molecularMass)
	{
		this.molecularMass = molecularMass;
	}

	@Override
	public double molecularMass()
	{
		return molecularMass;
	}

	@Override
	public void onEaten(CellWorld world)
	{
		world.deferRemoval(this);
	}

	@Override
	public String toString()
	{
		return "Dead cell with " + molecularMass + " molecular mass";
	}
}

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

import java.util.Map;
import java.util.Optional;

import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.github.jontejj.cell.evolution.game.CellWorld;
import com.github.jontejj.cell.evolution.signaling.Signal;

public abstract class Organism extends SimulationBody
{
	private final String name;
	private Organism parentOrganism = null;
	private static long ORGANISM_ID = 1;

	private long organismId;

	public Organism(String name)
	{
		this.name = name;
		this.organismId = ORGANISM_ID++;
	}

	public String name()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name + "#" + organismId;
	}

	public long organismId()
	{
		return organismId;
	}

	public abstract boolean timestep(CellWorld cellworld);

	public abstract Optional<Organism> binaryFission();

	public abstract double totalMass();

	public abstract Map<Nucleobases, Long> nucleotideResources();

	public void setParentOrganism(Organism parent)
	{
		this.parentOrganism = parent;
	}

	public Organism getRootOrganism()
	{
		return (parentOrganism != null) ? parentOrganism.getRootOrganism() : this;
	}

	public abstract void removeFromWorld(World<SimulationBody> world);

	public abstract void signal(Signal signal);

	protected void cellDied(CellWorld cellworld)
	{
		this.removeFromWorld(cellworld.world());
		cellworld.addDeadCellForOrganism(this);
		cellworld.removeOrganism(this);
	}
}

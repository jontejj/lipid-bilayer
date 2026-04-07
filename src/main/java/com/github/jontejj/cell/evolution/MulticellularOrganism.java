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

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.github.jontejj.cell.evolution.game.CellWorld;
import com.github.jontejj.cell.evolution.signaling.Signal;

public class MulticellularOrganism extends Organism
{
	private final List<Organism> cells;
	private final List<Organism> originalCells;

	public MulticellularOrganism(String name, List<Organism> cells)
	{
		super(name);
		this.cells = Lists.newArrayList(cells);
		this.originalCells = Lists.newArrayList(cells);
	}

	@Override
	public boolean timestep(CellWorld cellworld)
	{
		Iterator<Organism> it = cells.iterator();
		while(it.hasNext())
		{
			Organism cell = it.next();
			if(cell.timestep(cellworld))
			{
				it.remove();
			}
		}
		if(cells.isEmpty()) // Organism dies if all cells are gone
		{
			cellDied(cellworld);
			return true;
		}
		return false;
	}

	@Override
	public Optional<Organism> binaryFission()
	{
		// TODO: implement this?
		return Optional.empty();
	}

	/**
	 * Returns the total nucleotide resources for all cells
	 */
	@Override
	public Map<Nucleobases, Long> nucleotideResources()
	{
		Map<Nucleobases, Long> totalResources = new EnumMap<>(Nucleobases.class);
		for(Nucleobases base : Nucleobases.values())
		{
			totalResources.put(base, 0L);
		}

		for(Organism cell : cells)
		{
			Map<Nucleobases, Long> cellResources = cell.nucleotideResources();
			for(Map.Entry<Nucleobases, Long> entry : cellResources.entrySet())
			{
				totalResources.merge(entry.getKey(), entry.getValue(), Long::sum);
			}
		}

		return totalResources;
	}

	@Override
	public double totalMass()
	{
		// TODO: return zero here? Otherwise there will more mass in the dead cell than in the alive ones (cellDied is called twice)
		return cells.stream().mapToDouble(cell -> cell.totalMass()).sum();
	}

	@Override
	public void removeFromWorld(World<SimulationBody> world)
	{
		world.removeBody(this);
		for(Organism organism : originalCells)
		{
			organism.removeFromWorld(world);
		}
	}

	@Override
	public void signal(Signal signal)
	{
		for(Organism cell : cells)
		{
			cell.signal(signal);
		}
	}
}

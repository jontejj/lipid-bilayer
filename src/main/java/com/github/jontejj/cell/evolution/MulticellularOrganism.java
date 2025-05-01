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

import org.assertj.core.util.Lists;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;

public class MulticellularOrganism extends Organism
{
	private final List<Organism> cells;

	public MulticellularOrganism(String name, List<Organism> cells)
	{
		super(name);
		this.cells = Lists.newArrayList(cells);
	}

	@Override
	public boolean timestep()
	{
		Iterator<Organism> it = cells.iterator();
		while(it.hasNext())
		{
			Organism cell = it.next();
			if(cell.timestep())
			{
				it.remove();
			}
		}
		return cells.isEmpty(); // Organism dies if all cells are gone
	}

	@Override
	public Optional<Organism> binaryFission()
	{
		// TODO: implement this?
		return Optional.absent();
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
	public double totalMolecularMass()
	{
		return cells.stream().mapToDouble(cell -> cell.totalMolecularMass()).sum();
	}

	@Override
	public void removeFromWorld(World<SimulationBody> world)
	{
		world.removeBody(this);
		for(Organism organism : cells)
		{
			organism.removeFromWorld(world);
		}
	}
}

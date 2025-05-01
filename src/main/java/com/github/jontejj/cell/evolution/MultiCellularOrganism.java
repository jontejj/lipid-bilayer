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

import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;

public class MultiCellularOrganism extends Organism
{

	public MultiCellularOrganism(String name, Nucleus nucleus, World<SimulationBody> world)
	{
		super(name, nucleus, world);
	}

	@Override
	public boolean timestep()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Organism> binaryFission()
	{
		// TODO Auto-generated method stub
		return null;
	}

}

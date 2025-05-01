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

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;

public abstract class Organism extends SimulationBody
{
	private double circleRadius;
	private BodyFixture fixture;
	private final Cytoplasm cytoplasm;
	private final String name;

	public Organism(String name, Nucleus nucleus, World<SimulationBody> world)
	{
		this(name, nucleus, world, 1.0);
	}

	public Organism(String name, Nucleus nucleus, World<SimulationBody> world, double circleRadius)
	{
		this.name = name;
		this.circleRadius = circleRadius;
		fixture = addFixture(Geometry.createCircle(circleRadius)); // a circular cell body
		setMass(MassType.NORMAL);
		this.cytoplasm = new Cytoplasm(nucleus, world);
	}

	public String name()
	{
		return name;
	}

	/**
	 * @return true if the organism has reached a critical resource level and is about to die
	 */
	public abstract boolean timestep();

	public abstract Optional<Organism> binaryFission();

	public double circleRadius()
	{
		return circleRadius;
	}

	public Cytoplasm cytoplasm()
	{
		return cytoplasm;
	}

	void adjustBodySize(double sizeToAdd)
	{
		// circleRadius = circleRadius + circleRadius * sizeToAdd;
		removeFixture(fixture);
		fixture = addFixture(Geometry.createCircle(circleRadius));
	}

	public double totalMolecularMass()
	{
		return cytoplasm().totalMolecularMass();
	}
}

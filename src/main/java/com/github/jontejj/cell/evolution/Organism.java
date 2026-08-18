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

	private Eatable seenFood;
	private double seenFoodDistance = Double.POSITIVE_INFINITY;
	private double seenFoodAngle = 0.0;

	public void onFoodSeen(Eatable food, double distance, double relativeAngle)
	{
		// Keep the closest food seen this update
		if(seenFood == null || distance < seenFoodDistance)
		{
			seenFood = food;
			seenFoodDistance = distance;
			seenFoodAngle = relativeAngle;
		}
	}

	public Eatable getSeenFood()
	{
		return seenFood;
	}

	public double getSeenFoodAngle()
	{
		return seenFoodAngle;
	}

	private double activation = 0.5;
	private double motivation = 0.5;
	private double riskAversion = 0.5;
	private double muscleContraction = 0.0;

	public void excite()
	{
		activation = clamp01(activation + 0.2);
	}

	public void inhibit()
	{
		activation = clamp01(activation - 0.2);
	}

	public void adjustMotivation(double amount)
	{
		motivation = clamp01(motivation + amount);
	}

	public void adjustRiskAversion(double amount)
	{
		riskAversion = clamp01(riskAversion + amount);
	}

	public void triggerMuscleContraction()
	{
		muscleContraction = clamp01(muscleContraction + 0.5);
	}

	public double activation()
	{
		return activation;
	}

	public double motivation()
	{
		return motivation;
	}

	public double riskAversion()
	{
		return riskAversion;
	}

	public double muscleContraction()
	{
		return muscleContraction;
	}

	public void decayNeuralState()
	{
		activation = approach(activation, 0.5, 0.01);
		motivation = approach(motivation, 0.5, 0.01);
		riskAversion = approach(riskAversion, 0.5, 0.01);
		muscleContraction = Math.max(0.0, muscleContraction - 0.1);
	}

	private static double clamp01(double value)
	{
		return Math.max(0.0, Math.min(1.0, value));
	}

	private static double approach(double value, double target, double step)
	{
		if(value < target)
			return Math.min(target, value + step);
		return Math.max(target, value - step);
	}
}

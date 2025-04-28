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

import java.util.List;

import org.assertj.core.util.Lists;

import com.github.jontejj.cell.evolution.proteins.FunctionalProtein;
import com.github.jontejj.cell.evolution.proteins.Protein;

public class Cytoplasm
{
	// TODO: change these to amino acids instead and consume from these resources when transcribing DNA
	private double resourceLevel = 100;
	private double energy = 0;
	private double storedResource = 0;

	private final Nucleus nucleus;
	private final List<Protein> proteinsInCytoplasm;

	public Cytoplasm(Nucleus nucleus)
	{
		this.nucleus = nucleus;
		this.proteinsInCytoplasm = Lists.newArrayList();
	}

	public void timestep()
	{
		for(Protein p : proteinsInCytoplasm)
		{
			if(p instanceof FunctionalProtein)
			{
				((FunctionalProtein) p).performFunction(this);
			}
		}
		proteinsInCytoplasm.addAll(nucleus.timestep());
	}

	public void increaseResourceAmount(double amount)
	{
		resourceLevel += amount;
	}

	public void convertResourceToEnergy(double amount)
	{
		if(resourceLevel >= amount)
		{
			resourceLevel -= amount;
			energy += amount * 0.8; // 80% efficient conversion
		}
	}

	public void storeResource(double amount)
	{
		if(resourceLevel >= amount)
		{
			resourceLevel -= amount;
			storedResource += amount;
		}
	}

	public void activateBackupGenes(int position)
	{
		// System.out.println("Backup genes activated!");
		nucleus.genome().enableDNAAfterPosition(position);
	}

	public double getResourceLevel()
	{
		return resourceLevel;
	}

	public double getEnergy()
	{
		return energy;
	}

	public double getStoredResource()
	{
		return storedResource;
	}

	public boolean supportsBinaryFission()
	{
		// TODO: base this on available amino acids etc
		return resourceLevel > 500 && energy > 50000;
	}

	@Override
	public String toString()
	{
		return "Cytoplasm [resourceLevel=" + resourceLevel + ", energy=" + energy + ", storedResource=" + storedResource + ", proteins in cell: "
				+ proteinsInCytoplasm.size() + ", nucleus=" + nucleus + "]";
	}
}

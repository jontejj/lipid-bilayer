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
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.github.jontejj.cell.evolution.proteins.FunctionalProtein;
import com.github.jontejj.cell.evolution.proteins.Protein;
import com.google.common.collect.ImmutableMap;

public class Cytoplasm
{
	private static double ATP_MOLAR_MASS = 507.18;

	private long atp = 1000;
	private Map<Nucleobases, Long> nucleotides = new EnumMap<>(Nucleobases.class);
	private Map<AminoAcid, Long> aminoAcids = new EnumMap<>(AminoAcid.class);

	private final Nucleus nucleus;
	private final List<Protein> proteinsInCytoplasm;
	private SimulationBody lastWormSegment;
	private World<SimulationBody> world;

	private boolean triggerFissionWhenPossible = false;

	private int apoptosisWarningCounter = 0;
	private static final int APOPTOSIS_THRESHOLD_TICKS = 1000;
	private static final long NUCLEOTIDE_MIN_THRESHOLD = 100L;

	public Cytoplasm(Nucleus nucleus, World<SimulationBody> world)
	{
		this.nucleus = nucleus;
		this.world = world;
		this.proteinsInCytoplasm = Lists.newArrayList();
		for(Nucleobases base : Nucleobases.values())
		{
			nucleotides.put(base, 10000L);
		}
		for(AminoAcid aa : AminoAcid.values())
		{
			aminoAcids.put(aa, 20000L);
		}
	}

	public void triggerFissionWhenPossible()
	{
		triggerFissionWhenPossible = true;
	}

	public boolean shouldTriggerApoptosis()
	{
		boolean criticalNucleotideShortage = false;
		for(Nucleobases base : Nucleobases.values())
		{
			if(nucleotides.getOrDefault(base, 0L) < NUCLEOTIDE_MIN_THRESHOLD)
			{
				criticalNucleotideShortage = true;
				break;
			}
		}
		if(criticalNucleotideShortage)
		{
			apoptosisWarningCounter++;
		}
		else
		{
			apoptosisWarningCounter = 0; // reset if recovered
		}

		boolean shouldTriggerApoptosis = apoptosisWarningCounter >= APOPTOSIS_THRESHOLD_TICKS;
		if(shouldTriggerApoptosis)
			return true;
		return false;
	}

	public double totalMass()
	{
		double totalMass = 0.0;
		totalMass += nucleus.genome().mass();
		for(Map.Entry<Nucleobases, Long> entry : nucleotides.entrySet())
		{
			double molarMass = entry.getKey().molarMass(); // g/mol
			long count = entry.getValue(); // number of molecules
			totalMass += count * (molarMass / Constants.AVOGADROS_NUMBER); // g
		}
		for(Map.Entry<AminoAcid, Long> entry : aminoAcids.entrySet())
		{
			double molarMass = entry.getKey().molarMass(); // g/mol
			long count = entry.getValue(); // number of molecules
			totalMass += count * (molarMass / Constants.AVOGADROS_NUMBER); // g
		}
		for(Protein p : proteinsInCytoplasm)
		{
			totalMass += 1 * (p.molarMass() / Constants.AVOGADROS_NUMBER);
		}

		totalMass += atp * (ATP_MOLAR_MASS / Constants.AVOGADROS_NUMBER);

		return totalMass;
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
		proteinsInCytoplasm.addAll(nucleus.timestep(this));
	}

	/**
	 * @param amount the energy to consume
	 * @return false if not enough available energy (ATP)
	 */
	public boolean consumeEnergy(long amount)
	{
		if(atp < amount)
			return false;
		atp -= amount;
		return true;
	}

	public void addEnergy(long amount)
	{
		atp += amount;
	}

	public void increaseResourceAmount(Nucleobases base, long amount)
	{
		nucleotides.merge(base, amount, Long::sum);
	}

	public void increaseResourceAmountForAllNucleotides(long amount)
	{
		for(Nucleobases base : Nucleobases.values())
		{
			nucleotides.merge(base, amount, Long::sum);
		}
	}

	public boolean decreaseResourceAmount(Nucleobases base, long amount)
	{
		Long current = nucleotides.get(base);
		if(current < amount)
			return false;
		nucleotides.put(base, current - amount);
		return true;
	}

	public void increaseResourceAmount(AminoAcid aa, long amount)
	{
		aminoAcids.merge(aa, amount, Long::sum);
	}

	public boolean decreaseResourceAmount(AminoAcid aa, long amount)
	{
		Long current = aminoAcids.get(aa);
		if(current < amount)
			return false;
		aminoAcids.put(aa, current - amount);
		return true;
	}

	public void activateBackupGenes(int position)
	{
		nucleus.genome().enableDNAAfterPosition(position);
	}

	public boolean supportsBinaryFission(Map<Nucleobases, Integer> requiredNucleotides)
	{
		if(!triggerFissionWhenPossible)
			return false;
		for(Map.Entry<Nucleobases, Integer> entry : requiredNucleotides.entrySet())
		{
			if(nucleotideResources().getOrDefault(entry.getKey(), 0L) < entry.getValue())
				return false;
		}
		// TODO: base atp requirement on size of organism?
		boolean hasEnoughATP = atp > 50000;
		if(hasEnoughATP)
		{
			// Only trigger one fission per DnaA protein
			triggerFissionWhenPossible = false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Cytoplasm [nucleotideEesources=" + nucleotides + ", aminoAcids=" + aminoAcids + ", atp=" + atp + ", proteins in cell: "
				+ proteinsInCytoplasm.size() + ", nucleus=" + nucleus + ", apoptosisWarningCounter=" + apoptosisWarningCounter + "]";
	}

	public World<SimulationBody> world()
	{
		return world;
	}

	public SimulationBody getLastWormSegment()
	{
		return lastWormSegment;
	}

	public void setLastWormSegment(SimulationBody segment)
	{
		lastWormSegment = segment;
	}

	public void synthesizeAminoAcid(AminoAcid aminoAcidToSynthase)
	{
		aminoAcids.merge(aminoAcidToSynthase, 1L, Long::sum);

	}

	public Map<Nucleobases, Long> nucleotideResources()
	{
		return ImmutableMap.copyOf(nucleotides);
	}

	public Map<AminoAcid, Long> aminoAcidResources()
	{
		return ImmutableMap.copyOf(aminoAcids);
	}
}

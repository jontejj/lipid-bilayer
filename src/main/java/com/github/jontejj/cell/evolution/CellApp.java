/* Copyright 2019 jonatanjonsson
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

import java.util.Set;

import org.assertj.core.util.Sets;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.World;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;

public class CellApp
{
	public static void main(String[] args)
	{
		World<SimulationBody> world = new World<SimulationBody>();

		Cytoplasm minimalCytoplasm = new Cytoplasm(new Nucleus(Genome.generate(4)), world);

		System.out.println(new DNA(Nucleobases.fromString("AUGCCAGAUCACUAA"), 0).transcribe(minimalCytoplasm).get().translate(minimalCytoplasm));

		Stopwatch stopwatch = Stopwatch.createStarted();
		// String humanGenomeFromFile = Resources.toString(Resources.getResource("human_genome_small.txt"), StandardCharsets.UTF_8);
		// System.out.println("Length of genome from file: " + humanGenomeFromFile.length());
		// System.out.println("Time to read genome from file: " + stopwatch);

		Set<Organism> organisms = Sets.newHashSet();
		// It is estimated that there are around 20,000 to 25,000 protein-coding genes in the human genome.

		// E. coli (bacteria) have around 4400 protein coding genes
		stopwatch = Stopwatch.createStarted();
		Organism eColi = new UnicellularOrganism("E. coli", new Nucleus(Genome.generate(4400)), world);
		System.out.println("Time to generate genome: " + stopwatch);
		organisms.add(eColi);

		stopwatch = Stopwatch.createStarted();
		Organism mycoplasmaGenitalium = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(Genome.generate(480)), world);
		System.out.println("Time to generate genome: " + stopwatch);
		organisms.add(mycoplasmaGenitalium);

		Stopwatch stopwatchForAllTimesteps = Stopwatch.createStarted();
		for(int timestep = 0; timestep < 5; timestep++)
		{
			Set<Organism> newOrganisms = Sets.newHashSet();
			for(Organism organism : organisms)
			{
				stopwatch = Stopwatch.createStarted();
				organism.timestep();
				System.out.println("Time to execute timestep " + timestep + ": " + stopwatch);
				System.out.println("Stats: " + Stats.asString());
				System.out.println("Cell " + organism);
				stopwatch = Stopwatch.createStarted();
				Optional<Organism> binaryFissionResult = organism.binaryFission();
				if(binaryFissionResult.isPresent())
				{
					System.out.println("Time to execute fission: " + stopwatch);
				}
				newOrganisms.addAll(binaryFissionResult.asSet());
			}
			organisms.addAll(newOrganisms);
		}
		System.out.println("Time to execute all timesteps: " + stopwatchForAllTimesteps);
	}
}

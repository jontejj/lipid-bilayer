/*
 * Copyright (c) 2010-2022 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.jontejj.cell.evolution.game;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.Sets;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.Camera;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.samples.framework.SimulationFrame;
import org.dyn4j.samples.framework.input.ToggleStateKeyboardInputHandler;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;

import com.github.jontejj.cell.evolution.Genome;
import com.github.jontejj.cell.evolution.Nucleobases;
import com.github.jontejj.cell.evolution.Nucleus;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.Stats;
import com.github.jontejj.cell.evolution.UnicellularOrganism;
import com.github.jontejj.cell.evolution.food.DeadCell;
import com.github.jontejj.cell.evolution.food.GlucoseMolecules;
import com.google.common.base.Stopwatch;

public class CellWorld extends SimulationFrame
{
	private static final long serialVersionUID = 5663760293144882635L;
	public static final int RESOURCE_TILE_SIZE = 100;
	private Set<Organism> organisms;
	private final List<SimulationBody> bodiesToRemove = new ArrayList<>();
	private final List<Joint<SimulationBody>> jointsToAdd = new ArrayList<>();
	private Duration durationOfLastTimestep;
	private Duration durationOfFission;
	private ToggleStateKeyboardInputHandler printStats;
	private Organism selectedOrganism;
	private ResourceTile[][] resourceTiles;
	private long timeStep = 0;

	public CellWorld()
	{
		super("Cell World");

		this.printStats = new ToggleStateKeyboardInputHandler(this.canvas, KeyEvent.VK_NUMPAD3, KeyEvent.VK_3);
		printStats.install();
		this.printControls();
		printControl("Print Stats", "3", "Use the 3 key to print the stats");
	}

	protected void initializeWorld()
	{
		// TODO: refactor into its own method? (THis does not have anything to do with world)
		organisms = createOrganisms();
		resourceTiles = new ResourceTile[RESOURCE_TILE_SIZE][RESOURCE_TILE_SIZE];
		for(int i = 0; i < RESOURCE_TILE_SIZE; i++)
		{
			for(int j = 0; j < RESOURCE_TILE_SIZE; j++)
			{
				resourceTiles[i][j] = new ResourceTile();
			}
		}

		world.setGravity(PhysicsWorld.ZERO_GRAVITY);
		world.addContactListener(new MyContactListener(this));

		// One body per organism
		for(Organism organism : organisms)
		{
			this.world.addBody(organism);
		}

		// Create some food
		Circle shape = Geometry.createCircle(0.1);
		GlucoseMolecules glucose = new GlucoseMolecules();
		glucose.addFixture(shape);
		glucose.setMass(MassType.NORMAL);
		glucose.translate(-1.0, 2.0);
		this.world.addBody(glucose);
	}

	public Optional<ResourceTile> getResourceTile(int x, int y)
	{
		if(x >= 0 && x < RESOURCE_TILE_SIZE && y >= 0 && y < RESOURCE_TILE_SIZE)
			return Optional.of(resourceTiles[x][y]);
		return Optional.empty();
	}

	public void addOrganism(Organism organism)
	{
		organisms.add(organism);
	}

	public void removeOrganism(Organism organism)
	{
		organisms.remove(organism);
	}

	public void addDeadCellForOrganism(Organism organism)
	{

		Rectangle shape = Geometry.createRectangle(0.5, 0.5);
		DeadCell deadCell = new DeadCell(organism.name(), organism.totalMass());
		deadCell.addFixture(shape);
		deadCell.setMass(MassType.NORMAL);
		// Set dead cell position to organism's position
		deadCell.translate(organism.getTransform().getTranslationX(), organism.getTransform().getTranslationY());
		deadCell.setLinearVelocity(organism.getLinearVelocity());
		deadCell.setAngularVelocity(organism.getAngularVelocity());
		deadCell.setLinearDamping(5.0); // Higher = slows faster
		deadCell.setAngularDamping(5.0);
		this.world.addBody(deadCell);
	}

	private Set<Organism> createOrganisms()
	{
		Stopwatch stopwatch = Stopwatch.createStarted();
		Set<Organism> initialOrganisms = Sets.newHashSet();
		// UnicellularOrganism human = new UnicellularOrganism("Homo sapiens", new Nucleus(Genome.generate(20000)), this.world);
		// System.out.println("Time to generate " + human.name() + " genome: " + stopwatch);
		// initialOrganisms.add(human);
		// stopwatch = Stopwatch.createStarted();
		// E. coli (bacteria) have around 4400 protein coding genes
		// stopwatch = Stopwatch.createStarted();
		// UnicellularOrganism eColi = new UnicellularOrganism("E. coli", new Nucleus(Genome.generate(4400)), this.world);
		// eColi.cytoplasm().setLastWormSegment(eColi);
		// System.out.println("Time to generate genome: " + stopwatch);
		// TODO: initialOrganisms.add(eColi); E.coli is currently too heavy
		// stopwatch = Stopwatch.createStarted();
		for(int i = 0; i < 1; i++)
		{
			Genome mycoplasmaGenitaliumGenome = Genome.generate(20000);
			UnicellularOrganism mycoplasmaGenitalium = new UnicellularOrganism("Homo sapiens", // Mycoplasma genitalium
					new Nucleus(mycoplasmaGenitaliumGenome),
					this);
			mycoplasmaGenitalium.cytoplasm().setLastWormSegment(mycoplasmaGenitalium);
			System.out.println("Time to generate " + mycoplasmaGenitalium.name() + " genome: " + stopwatch);

			double x = Math.random() * 50.0;
			double y = Math.random() * 50.0;
			mycoplasmaGenitalium.translate(new Vector2(x, y));

			Map<Nucleobases, Integer> requiredNucleotideCountsForFission = mycoplasmaGenitaliumGenome.totalNucleotideCounts();
			for(Map.Entry<Nucleobases, Integer> entry : requiredNucleotideCountsForFission.entrySet())
			{
				// This is done so the simulation can trigger the first fission faster
				mycoplasmaGenitalium.cytoplasm().increaseResourceAmount(entry.getKey(), entry.getValue());
			}
			initialOrganisms.add(mycoplasmaGenitalium);
		}

		return initialOrganisms;
	}

	private void oneTimestep()
	{
		Set<Organism> newOrganisms = Sets.newHashSet();
		Stopwatch stopwatch = Stopwatch.createStarted();
		for(Organism organism : Sets.newHashSet(organisms))
		{
			organism.timestep(this);
		}
		durationOfLastTimestep = stopwatch.elapsed();

		for(Organism organism : organisms)
		{
			stopwatch = Stopwatch.createStarted();
			Optional<Organism> binaryFissionResult = organism.binaryFission();
			if(binaryFissionResult.isPresent())
			{
				newOrganisms.add(binaryFissionResult.get());
				// System.out.println("Time to execute fission for " + organism.name() + ": " + stopwatch);
				durationOfFission = stopwatch.elapsed();
			}
		}

		newOrganisms.forEach(newOrganism -> addOrganism(newOrganism));
		if(organisms.isEmpty())
		{
			organisms = createOrganisms();
			for(Organism organism : newOrganisms)
			{
				organism.translate(-1.0, 10.0);
			}
			newOrganisms = organisms;
		}
		newOrganisms.forEach(org -> this.world.addBody(org));
		removeDeletedBodies();
		addJoints();
		timeStep++;
	}

	@Override
	protected void initializeCamera(Camera camera)
	{
		super.initializeCamera(camera);
		camera.scale = 45.0;
	}

	@Override
	protected void render(Graphics2D g, double elapsedTime)
	{
		// Render resource tiles *in world space*
		for(int i = 0; i < RESOURCE_TILE_SIZE; i++)
		{
			for(int j = 0; j < RESOURCE_TILE_SIZE; j++)
			{
				ResourceTile tile = resourceTiles[i][j];
				int amt = tile.amount();

				// Cap amount to prevent overflow and scale brightness (0–255)
				int brightness = (int) (255.0 * amt / ResourceTile.MAX_RESOURCE_AMOUNT);

				Color color;
				switch(tile.baseResource())
				{
				case ADENINE:
					color = new Color(brightness, 0, 0);
					break;       // Red
				case THYMINE:
					color = new Color(0, brightness, 0);
					break;       // Green
				case GUANINE:
					color = new Color(0, 0, brightness);
					break;       // Blue
				case CYTOSINE:
					color = new Color(brightness, brightness, 0);
					break; // Yellow
				case URACIL:
					color = new Color(0, brightness, brightness);
					break; // Cyan
				default:
					color = Color.GRAY;
				}

				g.setColor(color);
				int xOffset = RESOURCE_TILE_SIZE / 2;
				int yOffset = RESOURCE_TILE_SIZE / 2;
				// g.fillRect((i - xOffset) * 1, (j - yOffset) * 1, 1, 1);
				double x = (i - xOffset);
				double y = (j - yOffset);
				Rectangle2D.Double tileRect = new Rectangle2D.Double(x * getCameraScale(), y * getCameraScale(), getCameraScale(), getCameraScale());
				// g.fill(tileRect);
				g.draw(tileRect);
			}
		}
		super.render(g, elapsedTime);

		AffineTransform tx = g.getTransform();
		g.scale(1, -1);
		g.translate(-this.getWidth() * 0.5 - this.getCameraOffsetX(), -this.getHeight() * 0.5 + this.getCameraOffsetY());

		g.setColor(Color.BLACK);
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		// String.format("Power: %1$.2f", 50.5)
		if(durationOfLastTimestep != null)
		{
			TimeUnit unit = chooseUnit(durationOfLastTimestep.toNanos());
			long convertedTime = unit.convert(durationOfLastTimestep);
			g.drawString("Time/step: " + convertedTime + " " + unit, 20, 30);
		}
		if(durationOfFission != null)
		{
			TimeUnit unit = chooseUnit(durationOfFission.toNanos());
			long convertedTime = unit.convert(durationOfFission);
			g.drawString("Fission time: " + convertedTime + " " + unit, 20, 50);
		}
		g.drawString("Time step: " + timeStep, 20, 70);

		g.drawString("Organisms: " + organisms.size(), 20, 90);
		int y = 110;
		if(selectedOrganism != null)
		{
			g.drawString("Name: " + selectedOrganism.name() + "#" + selectedOrganism.organismId(), 20, y);
			y += 15;
			Map<Nucleobases, Long> nucleotideResources = selectedOrganism.nucleotideResources();
			for(Entry<Nucleobases, Long> entry : nucleotideResources.entrySet())
			{
				g.drawString("" + entry.getKey() + ":" + entry.getValue(), 20, y);
				y += 15;
			}
		}
		g.setTransform(tx);
	}

	private static TimeUnit chooseUnit(long nanos)
	{
		if(DAYS.convert(nanos, NANOSECONDS) > 0)
			return DAYS;
		if(HOURS.convert(nanos, NANOSECONDS) > 0)
			return HOURS;
		if(MINUTES.convert(nanos, NANOSECONDS) > 0)
			return MINUTES;
		if(SECONDS.convert(nanos, NANOSECONDS) > 0)
			return SECONDS;
		if(MILLISECONDS.convert(nanos, NANOSECONDS) > 0)
			return MILLISECONDS;
		if(MICROSECONDS.convert(nanos, NANOSECONDS) > 0)
			return MICROSECONDS;
		return NANOSECONDS;
	}

	@Override
	protected void handleEvents()
	{
		oneTimestep();
		super.handleEvents();
		if(this.printStats.isActive())
		{
			this.printStats.setActive(false);
			System.out.println("Stats: " + Stats.asString());
		}
	}

	public void deferRemoval(SimulationBody body)
	{
		bodiesToRemove.add(body);
	}

	/**
	 * Runs after the collision detection is done to avoid ConcurrentModificationException
	 */
	void removeDeletedBodies()
	{
		for(SimulationBody body : bodiesToRemove)
		{
			world.removeBody(body);
		}
		bodiesToRemove.clear();
	}

	public void deferAddition(Joint<SimulationBody> joint)
	{
		jointsToAdd.add(joint);
	}

	void addJoints()
	{
		for(Joint<SimulationBody> joint : jointsToAdd)
		{
			boolean allBodiesContained = true;
			for(SimulationBody body : joint.getBodies())
			{
				if(!world.containsBody(body))
				{
					allBodiesContained = false;
				}
			}
			if(allBodiesContained)
			{
				world.addJoint(joint);
			}
		}
		jointsToAdd.clear();
	}

	@Override
	protected void onBodyMousePickingStart(SimulationBody body)
	{
		super.onBodyMousePickingStart(body);
		if(body instanceof Organism)
		{
			selectedOrganism = (Organism) body;
			System.out.println("Selected: " + selectedOrganism);
		}
	}

	public World<SimulationBody> world()
	{
		return this.world;
	}

	public static void main(String[] args)
	{
		CellWorld simulation = new CellWorld();
		simulation.run();
	}
}

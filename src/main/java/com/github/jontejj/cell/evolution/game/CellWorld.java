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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.Sets;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.samples.framework.Camera;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.samples.framework.SimulationFrame;
import org.dyn4j.samples.framework.input.ToggleStateKeyboardInputHandler;
import org.dyn4j.world.PhysicsWorld;

import com.github.jontejj.cell.evolution.Genome;
import com.github.jontejj.cell.evolution.Nucleobases;
import com.github.jontejj.cell.evolution.Nucleus;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.Stats;
import com.github.jontejj.cell.evolution.UnicellularOrganism;
import com.github.jontejj.cell.evolution.food.Apple;
import com.github.jontejj.cell.evolution.food.DeadCell;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;

public class CellWorld extends SimulationFrame
{
	private static final long serialVersionUID = 5663760293144882635L;
	private Set<Organism> organisms;
	private final List<SimulationBody> bodiesToRemove = new ArrayList<>();
	private Duration durationOfLastTimestep;
	private ToggleStateKeyboardInputHandler printStats;

	public CellWorld()
	{
		super("Cell World");

		this.printStats = new ToggleStateKeyboardInputHandler(this.canvas, KeyEvent.VK_NUMPAD3, KeyEvent.VK_3);
		printStats.install();
		this.printControls();
		printControl("Print Stats", "3", "Use the 3 key to print the stats");
		organisms = createOrganisms();
	}

	protected void initializeWorld()
	{
		world.setGravity(PhysicsWorld.ZERO_GRAVITY);
		world.addContactListener(new MyContactListener(this));
		// double wallWidth = 0.5;
		// double worldSize = 30;
		// double offset = worldSize / 2.0 - wallWidth / 2.0;
		// outer walls
		// SimulationBody rigthWall = new SimulationBody();
		// rigthWall.addFixture(Geometry.createRectangle(wallWidth, worldSize));
		// rigthWall.setMass(MassType.INFINITE);
		// rigthWall.translate(offset, 0);
		// this.world.addBody(rigthWall);
		//
		// SimulationBody topWall = new SimulationBody();
		// topWall.addFixture(Geometry.createRectangle(worldSize, wallWidth));
		// topWall.setMass(MassType.INFINITE);
		// topWall.translate(0, offset);
		// this.world.addBody(topWall);
		//
		// SimulationBody bottomWall = new SimulationBody();
		// bottomWall.addFixture(Geometry.createRectangle(worldSize, wallWidth));
		// bottomWall.setMass(MassType.INFINITE);
		// bottomWall.translate(0, -offset);
		// this.world.addBody(bottomWall);
		//
		// SimulationBody leftWall = new SimulationBody();
		// leftWall.addFixture(Geometry.createRectangle(wallWidth, worldSize));
		// leftWall.setMass(MassType.INFINITE);
		// leftWall.translate(-offset, 0);
		// this.world.addBody(leftWall);

		// One body per organism
		for(Organism organism : organisms)
		{
			this.world.addBody(organism);
		}

		// create an apple object (food)
		Circle shape = Geometry.createCircle(0.1);
		Apple apple = new Apple();
		apple.addFixture(shape);
		apple.setMass(MassType.NORMAL);
		apple.translate(-1.0, 2.0);
		// test having a velocity
		// apple.getLinearVelocity().set(5.0, 0.0);
		this.world.addBody(apple);
	}

	private void addDeadCellForOrganism(Organism organism)
	{

		Rectangle shape = Geometry.createRectangle(0.5, 0.5);
		DeadCell deadCell = new DeadCell(organism.cytoplasm().totalMolecularMass());
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
		// E. coli (bacteria) have around 4400 protein coding genes
		// stopwatch = Stopwatch.createStarted();
		// UnicellularOrganism eColi = new UnicellularOrganism("E. coli", new Nucleus(Genome.generate(4400)), this.world);
		// eColi.cytoplasm().setLastWormSegment(eColi);
		// System.out.println("Time to generate genome: " + stopwatch);
		// TODO: initialOrganisms.add(eColi); E.coli is currently too heavy
		// stopwatch = Stopwatch.createStarted();
		UnicellularOrganism mycoplasmaGenitalium = new UnicellularOrganism("Mycoplasma genitalium", new Nucleus(Genome.generate(480)), this.world);
		mycoplasmaGenitalium.cytoplasm().setLastWormSegment(mycoplasmaGenitalium);
		System.out.println("Time to generate genome: " + stopwatch);
		initialOrganisms.add(mycoplasmaGenitalium);

		return initialOrganisms;
	}

	private void oneTimestep()
	{
		Set<Organism> newOrganisms = Sets.newHashSet();
		Set<Organism> deadOrganisms = Sets.newHashSet();
		for(Organism organism : organisms)
		{
			Stopwatch stopwatch = Stopwatch.createStarted();
			boolean organismShouldDie = organism.timestep();
			durationOfLastTimestep = stopwatch.elapsed();
			if(organismShouldDie)
			{
				world.removeBody(organism);
				addDeadCellForOrganism(organism);
				deadOrganisms.add(organism);
			}
			stopwatch = Stopwatch.createStarted();
			Optional<Organism> binaryFissionResult = organism.binaryFission();
			if(binaryFissionResult.isPresent())
			{
				System.out.println("Time to execute fission: " + stopwatch);
			}
			newOrganisms.addAll(binaryFissionResult.asSet());
		}
		organisms.addAll(newOrganisms);
		organisms.removeAll(deadOrganisms);
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
			g.drawString("Time/step: " + convertedTime + " " + unit, 20, 50);
		}
		int y = 70;
		for(Organism org : organisms)
		{
			g.drawString("Name: " + ((UnicellularOrganism) org).name(), 20, y);
			y += 15;
			Map<Nucleobases, Long> nucleotideResources = org.cytoplasm().nucleotideResources();
			for(Entry<Nucleobases, Long> entry : nucleotideResources.entrySet())
			{
				g.drawString("" + entry.getKey() + ":" + entry.getValue(), 20, y);
				y += 15;
			}
		}
		// g.drawString("Hello", 20, 70);

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

	public static void main(String[] args)
	{
		CellWorld simulation = new CellWorld();
		simulation.run();
	}
}

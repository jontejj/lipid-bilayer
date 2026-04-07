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
package com.github.jontejj.cell.evolution.game;

import java.awt.Color;
import java.util.List;

import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.listener.ContactListenerAdapter;

import com.github.jontejj.cell.evolution.Eatable;
import com.github.jontejj.cell.evolution.HasMouth;
import com.github.jontejj.cell.evolution.MulticellularOrganism;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.UnicellularOrganism;
import com.github.jontejj.cell.evolution.neural.Axon;

public class MyContactListener extends ContactListenerAdapter<SimulationBody>
{
	private final CellWorld world;

	public MyContactListener(CellWorld world)
	{
		this.world = world;
	}

	@Override
	public void begin(ContactCollisionData<SimulationBody> collision, Contact contact)
	{
		super.begin(collision, contact);
		SimulationBody body1 = collision.getBody1();
		SimulationBody body2 = collision.getBody2();

		if(body1 instanceof HasMouth && body2 instanceof Eatable)
		{
			// System.out.println("Collision detected between: " + body1 + " and " + body2);
			((HasMouth) body1).eat((Eatable) body2);
			((Eatable) body2).onEaten(world);
		}
		else if(body2 instanceof HasMouth && body1 instanceof Eatable)
		{
			// System.out.println("Collision detected between: " + body2 + " and " + body1);
			((HasMouth) body2).eat((Eatable) body1);
			((Eatable) body1).onEaten(world);
		}
		else if(body1 instanceof UnicellularOrganism && body2 instanceof UnicellularOrganism)
		{
			// Handle cell fusion
			Organism org1 = (Organism) body1;
			Organism org2 = (Organism) body2;

			if(org1.getRootOrganism() != org2.getRootOrganism())
			{
				// Create a new MulticellularOrganism with the two organisms as cells
				MulticellularOrganism multicellularOrganism = new MulticellularOrganism("Multicellular", List.of(org1, org2));

				// Set the parent organism for each cell in the new multicellular organism
				org1.setParentOrganism(multicellularOrganism);
				org2.setParentOrganism(multicellularOrganism);

				// Create a physical joint between organisms
				DistanceJoint<SimulationBody> joint = new DistanceJoint<>(org1, org2, org1.getWorldCenter(), org2.getWorldCenter());
				joint.setCollisionAllowed(false);  // Prevent overlapping
				joint.setSpringFrequency(3.0);           // Springy connection
				joint.setSpringDampingRatio(0.6);        // Some damping
				world.deferAddition(joint);
				world.addOrganism(multicellularOrganism);
				world.removeOrganism(org1);
				world.removeOrganism(org2);
			}
		}
		else if(body1 instanceof Axon && body2 instanceof Organism)
		{
			handleAxonContact((Axon) body1, (Organism) body2);
		}
		else if(body2 instanceof Axon && body1 instanceof Organism)
		{
			handleAxonContact((Axon) body2, (Organism) body1);
		}
	}

	private void handleAxonContact(Axon axon, Organism target)
	{
		if(!axon.hasFormedSynapseWith(target))
		{
			System.out.println("Axon for " + axon + " contacted " + target.name());

			// Add synapse marker / connection
			axon.registerSynapse(target);
			axon.setColor(Color.BLUE);

			// TODO: simulate signaling somewhere else
			axon.transmitSignalToTargets();

			// Optionally attach a soft joint (like synapse anchoring)
			DistanceJoint<SimulationBody> synapseJoint = new DistanceJoint<>(axon, target, axon.getWorldCenter(), target.getWorldCenter());
			synapseJoint.setSpringFrequency(5.0);
			synapseJoint.setSpringDampingRatio(0.7);
			synapseJoint.setCollisionAllowed(false);
			world.deferAddition(synapseJoint);
		}
	}
}

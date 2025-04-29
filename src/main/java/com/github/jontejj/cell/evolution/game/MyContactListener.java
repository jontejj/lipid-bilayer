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

import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.listener.ContactListenerAdapter;

import com.github.jontejj.cell.evolution.Eatable;
import com.github.jontejj.cell.evolution.HasMouth;

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

		System.out.println("Collision detected between: " + body1 + " and " + body2);

		if(body1 instanceof HasMouth && body2 instanceof Eatable)
		{
			((HasMouth) body1).eat((Eatable) body2);
			((Eatable) body2).onEaten(world);
		}
		else if(body2 instanceof HasMouth && body1 instanceof Eatable)
		{
			((HasMouth) body2).eat((Eatable) body1);
			((Eatable) body1).onEaten(world);
		}
	}
}

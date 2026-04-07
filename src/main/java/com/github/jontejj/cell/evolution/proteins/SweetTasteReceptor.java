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
package com.github.jontejj.cell.evolution.proteins;

import java.util.Optional;
import java.util.Random;

import org.dyn4j.geometry.Vector2;

import com.github.jontejj.cell.evolution.AminoAcidSequence;
import com.github.jontejj.cell.evolution.Cytoplasm;
import com.github.jontejj.cell.evolution.Nucleobases;
import com.github.jontejj.cell.evolution.Organism;
import com.github.jontejj.cell.evolution.game.CellWorld;
import com.github.jontejj.cell.evolution.game.ResourceTile;

/**
 * Found in taste buds, gut epithelium, and even in the pancreas.
 * Upon binding glucose (or other sugars), It initiates signaling via G-proteins (e.g., gustducin), which modulate cell behavior.
 * In this simulation it detects the level of nucleotides in the environment
 */
public class SweetTasteReceptor extends FunctionalProtein
{
	private final Nucleobases baseToDetect;
	private final double detectionThreshold = 100.0; // tweak as needed

	public SweetTasteReceptor(AminoAcidSequence sequence)
	{
		super(sequence);

		Random seededRNG = new Random(sequenceSignature());
		baseToDetect = Nucleobases.values()[seededRNG.nextInt(Nucleobases.values().length)];
	}

	@Override
	public void performFunction(Cytoplasm env, Organism organism)
	{
		Vector2 pos = organism.getWorldCenter();
		int i = (int) Math.floor(pos.x + CellWorld.RESOURCE_TILE_SIZE / 2.0);
		int j = (int) Math.floor(pos.y + CellWorld.RESOURCE_TILE_SIZE / 2.0);

		Optional<ResourceTile> tileOpt = env.cellWorld().getResourceTile(i, j);
		tileOpt.ifPresent(tile -> {
			if(tile.baseResource() == baseToDetect && tile.amount() >= detectionThreshold)
			{
				env.setMovementInhibited(true);
			}
			else
			{
				env.setMovementInhibited(false);
			}
		});
	}

	@Override
	public String toString()
	{
		return baseToDetect + " sweet receptor";
	}

}

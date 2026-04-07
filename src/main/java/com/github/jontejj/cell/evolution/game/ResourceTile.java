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

import java.util.Random;

import com.github.jontejj.cell.evolution.Nucleobases;

public class ResourceTile
{
	private static Random RND = new Random(42);
	public static int MAX_RESOURCE_AMOUNT = 1000000;

	private Nucleobases baseResource = Nucleobases.values()[RND.nextInt(Nucleobases.values().length)];
	private int amount = Math.abs((int) (Math.random() * MAX_RESOURCE_AMOUNT));

	public int amount()
	{
		return amount;
	}

	public Nucleobases baseResource()
	{
		return baseResource;
	}

	public int consume(int amountToConsume)
	{
		int consumedAmount = 0;
		if(amountToConsume > this.amount)
		{
			consumedAmount = this.amount;
			this.amount = 0;
		}
		else if(this.amount > 0)
		{
			this.amount -= amountToConsume;
			consumedAmount = amountToConsume;
		}
		return consumedAmount;
	}
}

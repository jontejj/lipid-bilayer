/* Copyright 2021 jonatanjonsson
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
package com.github.jontejj.cell.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.github.jontejj.cell.physics.PhysicalObject;
import com.github.jontejj.cell.physics.Rectangle;
import com.github.jontejj.cell.physics.World;

public class MyCanvas extends Canvas
{
	private static final long serialVersionUID = 1L;
	final World worldToPaint;

	MyCanvas(World worldToPaint)
	{
		this.worldToPaint = worldToPaint;

	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		for(PhysicalObject obj : worldToPaint.objects)
		{
			if(obj instanceof Rectangle rect)
			{
				g2d.drawRect((int) rect.pos().x(), (int) rect.pos().y(), (int) rect.width(), (int) rect.height());
			}

		}
	}
}

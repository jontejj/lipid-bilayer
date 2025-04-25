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

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.google.common.util.concurrent.RateLimiter;

public final class UIEngine extends Thread
{
	private final MyCanvas canvas;

	public UIEngine(MyCanvas canvas)
	{
		this.canvas = canvas;
	}

	float desiredFrameRate = 60.0f;
	CountDownLatch startSignal = new CountDownLatch(1);

	@Override
	public void run()
	{
		try
		{
			startSignal.await();
		}
		catch(InterruptedException e)
		{
			return;
		}
		RateLimiter fpsLimiter = RateLimiter.create(desiredFrameRate);
		while(true)
		{
			boolean aquired = fpsLimiter.tryAcquire(1, TimeUnit.SECONDS);
			if(Thread.currentThread().isInterrupted())
				return;
			if(!aquired)
			{
				System.err.println("Slow FPS... Waiting to step until UI catches up");
				continue;
			}
			// System.out.println("Waiting time for frame: " + waitingTime);
			canvas.worldToPaint.step();
			try
			{
				SwingUtilities.invokeAndWait(canvas::repaint);
			}
			catch(InvocationTargetException | InterruptedException e)
			{
				return;
			}
		}
	}
}

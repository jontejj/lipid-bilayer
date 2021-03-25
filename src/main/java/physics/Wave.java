/* Copyright 2018 jonatanjonsson
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
package physics;

public class Wave extends PhysicalObject
{
	int time;
	double x;
	double y;
	int waveLength;
	int amplitude;

	public Wave(int amplitude, int waveLength, Vec2 position, ObjectUpdatedListener listener)
	{
		super(position, listener);
		x = position.magnitude;
		y = position.direction;
		this.amplitude = amplitude;
		this.waveLength = waveLength;
	}

	@Override
	public void step()
	{
		time++;
		x++;
		// y(x, t) = A * cos(2PI/x+2PI/T*t)
		// y(t) = A * cos(2PI/T*t)
		double yOffset = amplitude * Math.cos((2 * Math.PI / waveLength) * time);
		System.out.println("x:" + x + ",y:" + y + yOffset);
		moveTo(new Vec2(x, y + yOffset));
	}

}

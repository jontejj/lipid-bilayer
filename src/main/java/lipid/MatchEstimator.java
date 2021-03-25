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
package lipid;

import java.math.RoundingMode;

import com.google.common.math.IntMath;

public class MatchEstimator
{
	public static void main(String[] args)
	{
		int teams = (int) Math.pow(2, 7);
		int teamsPerGroup = 4;
		int teamsThatGoForwardFromGroups = 1;
		int gamesPerGroup = teamsPerGroup * (teamsPerGroup - 1) / 2;
		int groups = teams / teamsPerGroup;
		int gamesInGroups = groups * gamesPerGroup;

		if(teams % teamsPerGroup != 0)
		{
			System.err.println("Nr of teams is not divisable by teams per group");
			System.exit(1);
		}
		System.out.println("teams: " + teams);
		System.out.println("groups: " + groups);
		System.out.println("Games per group: " + gamesPerGroup);

		try
		{
			int roundsInFinal = IntMath.log2(groups * teamsThatGoForwardFromGroups, RoundingMode.UNNECESSARY);
			double gamesInFinals = Math.pow(2, roundsInFinal);
			System.out.println("Final rounds: " + roundsInFinal);
			System.out.println(gamesInGroups + gamesInFinals);
		}
		catch(ArithmeticException e)
		{
			System.err.println("Not an even number of finals for " + groups + " groups");
		}
	}
}

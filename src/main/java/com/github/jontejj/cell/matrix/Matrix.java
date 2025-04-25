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
package com.github.jontejj.cell.matrix;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Matrix
{

	public static void main(String[] args)
	{
		int[][] A = {{6, 7}, {8, 9}, {10, 11}, {12, 13}};
		int[][] B = {{0, 1, 3}, {3, 4, 5}};
		rollMyOwn(A, B);
		nd4j(A, B);
	}

	private static void nd4j(int[][] a, int[][] b)
	{
		INDArray A = Nd4j.create(a);
		INDArray B = Nd4j.create(b);

		INDArray AB = A.mmul(B);
		System.out.println(AB);

		INDArray x = Nd4j.zeros(3, 4);

		// The number of axes (dimensions) of the array.
		int dimensions = x.rank();

		// The dimensions of the array. The size in each dimension.
		long[] shape = x.shape();

		// The total number of elements.
		long length = x.length();

		// The type of the array elements.
		DataType dt = x.dataType();
	}

	private static void rollMyOwn(int[][] a, int[][] b)
	{
		int[][] AB = matrixMultiply(a, b);
		for(int row = 0; row < AB.length; row++)
		{
			System.out.print("[");
			for(int column = 0; column < AB[row].length; column++)
			{
				System.out.print(AB[row][column] + ", ");
			}
			System.out.println("]");
		}
	}

	private static int[][] matrixMultiply(int[][] a, int[][] b)
	{
		int m = a.length;
		int n = b.length;
		int p = b[0].length;
		if(a[0].length != n)
			throw new IllegalArgumentException("matrixes are not compatible");

		int[][] res = new int[m][p];
		for(int row = 0; row < m; row++)
		{
			for(int column = 0; column < p; column++)
			{
				res[row][column] = dotProduct(getRow(a, row), getColumn(b, column));
			}
		}
		return res;
	}

	private static int[] getColumn(int[][] a, int c)
	{
		int[] res = new int[a.length];
		for(int i = 0; i < a.length; i++)
		{
			res[i] = a[i][c];
		}
		return res;
	}

	private static int[] getRow(int[][] a, int r)
	{
		return a[r];
	}

	private static int dotProduct(int[] a, int[] b)
	{
		int sum = 0;
		for(int i = 0; i < a.length; i++)
		{
			sum += a[i] * b[i];
		}
		return sum;
	}
}

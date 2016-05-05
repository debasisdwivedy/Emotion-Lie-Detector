package Model.MatrixOperations;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class Matrix_Substraction {
	
	public double[][] substract(double[][] input1, double[][] input2)
	{
		double[][] output=new double[input1.length][input1[0].length];
		if(Math.abs(input1.length-input2.length)>2 || Math.abs(input1[0].length-input2[0].length)>2)
		{
			System.out.println("incompatable matrix");
			System.out.println("Input 1 size:::::"+input1.length+","+input1[0].length);
			System.out.println("Input 2 size:::::"+input2.length+","+input2[0].length);
		}
		else
		{
			int rows=(input1.length>=input2.length)?input2.length:input1.length;
			int cols=(input1[0].length>=input2[0].length)?input2[0].length:input1[0].length;
			for(int i=0 ; i<rows;i++)
			{
				for(int j=0;j<cols;j++)
				{
					output[i][j]=input1[i][j]-input2[i][j];
				}
			}
		}
		return output;
	}
	
	public static Mat add(Mat a,Scalar b,Mat c)
	{
		Core.add(a, b, c);
		return c;
	}
	
	public static Mat sub(Mat a,Scalar b,Mat c)
	{
		Core.subtract(a, b, c);
		return c;
	}

}

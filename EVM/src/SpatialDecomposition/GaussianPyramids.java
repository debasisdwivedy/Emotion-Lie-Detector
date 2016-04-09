package SpatialDecomposition;

import MatrixOperations.Convolution;
import MatrixOperations.MatrixMultiplication;
import MatrixOperations.Transpose;

public class GaussianPyramids {
	
	public double[][] reduce(double[][] image_matrix,int reduction_factor,boolean input_alpha)
	{
		int reduce_by=(int) Math.pow(2, reduction_factor);
		double[][] w=get_W();
		double[][] prev_w=new double[w.length][w[0].length];
		double[][] downsampled_image=new double[image_matrix.length/reduce_by][image_matrix[0].length/reduce_by];
		prev_w=w;
		Convolution c=new Convolution();
		for(int i=0;i<reduce_by;i++)
		{
			prev_w=c.perform_convolution_with_zero_padding(prev_w, w);
		}
		double[][] smoothedImage=c.perform_convolution_with_firstLast_padding(image_matrix, prev_w, input_alpha);
		for(int i=0;i<downsampled_image.length;i++)
		{
			for(int j=0;j<downsampled_image[0].length;j++)
			{
				downsampled_image[i][j]=smoothedImage[reduce_by*i][reduce_by*j];
			}
		}
		return downsampled_image;
	}
	
	public static double[][] get_W()
	{
		double a=0.4;// a can be from 0.3 to 0.6(At 0.4 it is gaussian)
		double[][] w1={
				{0.25f-a/2},
				{0.25f},
				{a},
				{0.25f},
				{0.25f-a/2}
		};
		double[][] w2=new Transpose().transposeMatrix(w1);
		return new MatrixMultiplication().matrixMultiply(w1, w2);
	}

}

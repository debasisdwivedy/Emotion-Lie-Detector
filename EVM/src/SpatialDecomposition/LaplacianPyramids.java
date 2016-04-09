package SpatialDecomposition;

import MatrixOperations.Convolution;
import MatrixOperations.Matrix_Substraction;
public class LaplacianPyramids {
	
	public double[][] expand(double[][] image_matrix,int expansion_factor,boolean input_alpha)
	{
		int expand_by=(int) Math.pow(2, expansion_factor+1);
		double[][] w=GaussianPyramids.get_W();
		double[][] g1=new GaussianPyramids().reduce(image_matrix, expansion_factor, input_alpha);
		double[][] g2=new GaussianPyramids().reduce(image_matrix, expansion_factor+1, input_alpha);
		double[][] upsampled_image=new double[expand_by*g2.length][expand_by*g2[0].length];
		Convolution c=new Convolution();
		double[][] temp=c.perform_convolution_with_firstLast_padding(g2, w, input_alpha);
		for(int i=0;i<upsampled_image.length;i++)
		{
			for(int j=0;j<upsampled_image[0].length;j++)
			{
				upsampled_image[i][j]=temp[i/expand_by][j/expand_by];
			}
		}
		
		return new Matrix_Substraction().substract(g1, upsampled_image);	
	}

}

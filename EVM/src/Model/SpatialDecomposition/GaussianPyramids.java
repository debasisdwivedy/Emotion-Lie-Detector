package Model.SpatialDecomposition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Model.MatrixOperations.Convolution;
import Model.MatrixOperations.CreateMatrix;
import Model.MatrixOperations.MatrixMultiplication;
import Model.MatrixOperations.Transpose;



public class GaussianPyramids {
	
	public static double[][] reduce(double[][] image_matrix,int reduction_factor,boolean input_alpha)
	{
		int reduce_by=(int) Math.pow(2, reduction_factor);
		double[][] w=get_W();
		double[][] w1=new double[w.length][w[0].length];
		for(int i=0;i<w.length;i++)
		{
			for(int j=0;j<w[0].length;j++)
			{
				w1[i][j]=w[i][j]/256;
			}
		}
		//double[][] prev_w=new double[w1.length][w1[0].length];
		double[][] downsampled_image=new double[image_matrix.length/reduce_by][image_matrix[0].length/reduce_by];
		double[][] test=new double[image_matrix.length][image_matrix[0].length];
		double[][] test1=new double[image_matrix.length][image_matrix[0].length];
		//prev_w=w1;
		Convolution c=new Convolution();
		for(int i=0;i<image_matrix.length;i++)
		{
			for(int j=0;j<image_matrix[0].length;j++)
			{
				test[i][j]=(int)image_matrix[i][j]&0xFF;
			}
		}
		double[][] smoothedImage=test;
		for(int i=0;i<reduction_factor;i++)
		{
			//prev_w=c.convolution2DPadded(prev_w,prev_w.length,prev_w[0].length, w1,w1.length,w1[0].length);
			//prev_w=c.perform_convolution_with_zero_padding(prev_w,w1);
			smoothedImage=c.perform_convolution_with_firstLast_padding(smoothedImage,w1,input_alpha);
			smoothedImage=reject_even_rows_cols(smoothedImage);
		}
		//double[][] smoothedImage=c.convolution2DPadded(test,test.length,test[0].length, prev_w,prev_w.length,prev_w[0].length);
		//double[][] smoothedImage=c.perform_convolution_with_zero_padding(test,prev_w);
		for(int i=0;i<downsampled_image.length;i++)
		{
			for(int j=0;j<downsampled_image[0].length;j++)
			{
				downsampled_image[i][j]=(new Color((int)smoothedImage[i][j],(int)smoothedImage[i][j],(int)smoothedImage[i][j])).getRGB();
				//test1[i][j]=(int)smoothedImage[i][j]&0xFF;
			}
		}
		System.out.println("size :"+smoothedImage.length);
		System.out.println("size 1 :"+smoothedImage[0].length);
		/*for(int i=0;i<downsampled_image.length;i++)
		{
			for(int j=0;j<downsampled_image[0].length;j++)
			{
				downsampled_image[i][j]=smoothedImage[reduce_by*i][reduce_by*j];
			}
		}*/
		return downsampled_image;
	}
	
	public static double[][] get_W()
	{
		double a=0.4;// a can be from 0.3 to 0.6(At 0.4 it is gaussian)
		double[][] w1={
				{1.0},
				{4.0},
				{6.0},
				{4.0},
				{1.0}
		};
		double[][] w2=new Transpose().transposeMatrix(w1);
		return new MatrixMultiplication().matrixMultiply(w1, w2);
	}
	
	public static void main(String[] args)
	{
		BufferedImage img=null;
		try
		{
			img=ImageIO.read(new File("1.png"));
			double[][] input_image=new CreateMatrix().create_matrix(img);
			double[][] output_image=reduce(input_image,3,img.isAlphaPremultiplied());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static double[][] reject_even_rows_cols(double[][] mat)
	{
		double[][] output=new double[mat.length/2][mat[0].length/2];
		int rows=0;
		for(int i=0;i<mat.length;i++)
		{
			int cols=0;
			if(i%2!=0)
			{
			for(int j=0;j<mat[0].length;j++)
			{
			if(j%2!=0)
			{
				output[rows][cols]=mat[i][j];
				cols++;
			}
			}
			rows++;
			}
		}
		return output;
	}

}

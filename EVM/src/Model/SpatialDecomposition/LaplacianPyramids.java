package Model.SpatialDecomposition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import Model.MatrixOperations.Convolution;
import Model.MatrixOperations.Matrix_Substraction;


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
	
	public Mat[] buildLpyr(Mat a, Mat[] pyr, int level,Mat buildLpyrAux) {
        a.copyTo(pyr[0]);
        for (int i = 0; i < level; i++) {
            Imgproc.pyrDown(pyr[i], pyr[i+1]);
            Imgproc.pyrUp(pyr[i+1], buildLpyrAux);
            Imgproc.resize(buildLpyrAux, buildLpyrAux, pyr[i].size());
            Core.subtract(pyr[i], buildLpyrAux, pyr[i]);
        }
        return pyr;
    }

    public Mat reconLpyr(Mat[] pyr, Mat o) {
        pyr[pyr.length - 1].copyTo(o);
        for (int i = pyr.length - 1; i > 0; i--) {
            Imgproc.pyrUp(o, o);
            Imgproc.resize(o, o, pyr[i-1].size());
            Core.add(pyr[i-1], o, o);
        }
        return o;
    }

}

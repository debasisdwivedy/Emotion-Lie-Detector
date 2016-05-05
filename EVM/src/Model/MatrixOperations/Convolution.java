package Model.MatrixOperations;

public class Convolution {
	
	public double[][] perform_convolution_with_firstLast_padding(double[][] image, double[][] filter, boolean alpha)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		double[][] output=new double[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;
			int tempi=0;
			int tempj=0;

			for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      double sum=0;
			      //double r = 0, g = 0, b = 0, a = 0;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i-k>=0 && i-k<image_matrix_rows && j-l>=0 && j-l<image_matrix_cols)
			          {
			        	  	//a += filter[k+filter_center_row][l+filter_center_col] * (((int)image[i-k][j-l] >> 24) & 0xff);
							//r += filter[k+filter_center_row][l+filter_center_col] * (((int)image[i-k][j-l] >> 16) & 0xff);
							//g += filter[k+filter_center_row][l+filter_center_col] * (((int)image[i-k][j-l] >> 8) & 0xff);
							//b += filter[k+filter_center_row][l+filter_center_col] * ((int)image[i-k][j-l] & 0xff);
			            sum=sum+image[i-k][j-l]*filter[k+filter_center_row][l+filter_center_col];
			          }
			          else
			          {
			        	 if(i-k<0)
			        	 {
			        		 tempi=0;
			        	 }
			        	 else
			        	 {
			        		 if(i-k>=image_matrix_rows)
				        	 {
				        		 tempi=image_matrix_rows-1;
				        	 }
			        		 else
			        		 {
			        			 tempi=i-k;
			        		 }
			        	 }
			        	 if(j-l<0)
			        	 {
			        		 tempj=0;
			        	 }
			        	 else
			        	 {
			        		 if(j-l>=image_matrix_cols)
				        	 {
				        		 tempj=image_matrix_cols-1;
				        	 }
				        	 else
				        	 {
				        		 tempj=j-l;
				        	 } 
			        	 }
			        	 	//a += filter[k+filter_center_row][l+filter_center_col] * (((int)image[tempi][tempj] >> 24) & 0xff);
							//r += filter[k+filter_center_row][l+filter_center_col] * (((int)image[tempi][tempj] >> 16) & 0xff);
							//g += filter[k+filter_center_row][l+filter_center_col] * (((int)image[tempi][tempj] >> 8) & 0xff);
							//b += filter[k+filter_center_row][l+filter_center_col] * ((int)image[tempi][tempj] & 0xff);
			        	  sum=sum+image[tempi][tempj]*filter[k+filter_center_row][l+filter_center_col]; 
			          }
			        }
			      }
			      //output[i][j]=(Math.round(sum * 100.0) / 100.0);
			      	//int ia = alpha ? roundup((int)(a)) : 0xff;
					//int ir = roundup((int)(r));
					//int ig = roundup((int)(g));
					//int ib = roundup((int)(b));
					//output[i][j] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
			      output[i][j]=sum;
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}
	
	public double[][] perform_convolution_with_zero_padding(double[][] image, double[][] filter)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		double[][] output=new double[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;

			for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      double sum=0;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i-k>=0 && i-k<image_matrix_rows && j-l>=0 && j-l<image_matrix_cols)
			          {
			            sum=sum+image[i-k][j-l]*filter[k+filter_center_row][l+filter_center_col];
			          }
			        }
			      }
			      //output[i][j]=(float) (Math.round(sum * 100.0) / 100.0);
			      output[i][j]=sum;
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}
	public static double[][] convolution2D(double[][] input,
            int width, int height,
            double[][] kernel,
            int kernelWidth,
            int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        double[][] output = new double[smallWidth][smallHeight];
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight);
            }
        }
        return output;
    }
	public static double [][] convolution2DPadded(double [][] input,
			int width, int height,
			double [][] kernel,
			int kernelWidth,
			int kernelHeight){
int smallWidth = width - kernelWidth + 1;
int smallHeight = height - kernelHeight + 1;
int top = kernelHeight/2;
int left = kernelWidth/2;
double small [][] = new double [smallWidth][smallHeight];
small = convolution2D(input,width,height,
  kernel,kernelWidth,kernelHeight);
double large [][] = new double [width][height];
for(int j=0;j<height;++j){
for(int i=0;i<width;++i){
large[i][j] = 0;
}
}
for(int j=0;j<smallHeight;++j){
for(int i=0;i<smallWidth;++i){
//if (i+left==32 && j+top==100) System.out.println("Convolve2DP: "+small[i][j]);
large[i+left][j+top]=small[i][j];
}
}
return large;
}
	
	public static double singlePixelConvolution(double [][] input,
		      int x, int y,
		      double [][] k,
		      int kernelWidth,
		      int kernelHeight){
double output = 0;
for(int i=0;i<kernelWidth;++i){
for(int j=0;j<kernelHeight;++j){
output = output + (input[x+i][y+j] * k[i][j]);
}
}
return output;
}
	
	public float[][] separable_convolution(float[][] image, float[][] filter1, float[][] filter2)
	{
		int image_rows=image.length;
		int image_cols=image[0].length;
		float[][] output =new float[image_rows][image_cols];
		return output;
	}
	
	private static int roundup(int pixel) {
		if (pixel < 0)
			return 0;
		if (pixel > 255)
			return 255;
		return pixel;
	}

}

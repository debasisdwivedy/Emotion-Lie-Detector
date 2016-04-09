package MatrixOperations;

public class Correlation {
	
	public float[][] perform_correlation_with_firstLast_padding(float[][] image, float[][] filter)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		float[][] output=new float[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;
			int tempi=0;
			int tempj=0;
			  for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      float sum=0;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i+k>=0 && i+k<image_matrix_rows && j+l>=0 && j+l<image_matrix_cols)
			          {
			            sum=sum+image[i+k][j+l]*filter[k+filter_center_row][l+filter_center_col];
			          }
			          else
			          {
			        	 if(i+k<0)
			        	 {
			        		 tempi=0;
			        	 }
			        	 else
			        	 {
			        		 if(i+k>=image_matrix_rows)
				        	 {
				        		 tempi=image_matrix_rows-1;
				        	 }
			        		 else
			        		 {
			        			 tempi=i+k;
			        		 }
			        	 }
			        	 if(j+l<0)
			        	 {
			        		 tempj=0;
			        	 }
			        	 else
			        	 {
			        		 if(j+l>=image_matrix_cols)
				        	 {
				        		 tempj=image_matrix_cols-1;
				        	 }
				        	 else
				        	 {
				        		 tempj=j+l;
				        	 } 
			        	 }
			        	  sum=sum+image[tempi][tempj]*filter[k+filter_center_row][l+filter_center_col]; 
			          }
			        }
			      }
			      output[i][j]=(float) (Math.round(sum * 100.0) / 100.0);
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}
	
	public float[][] perform_correlation_with_zero_padding(float[][] image, float[][] filter)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		float[][] output=new float[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;
			  for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      float sum=0;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i+k>=0 && i+k<image_matrix_rows && j+l>=0 && j+l<image_matrix_cols)
			          {
			            sum=sum+image[i+k][j+l]*filter[k+filter_center_row][l+filter_center_col];
			          }
			        }
			      }
			      output[i][j]=(float) (Math.round(sum * 100.0) / 100.0);
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}
	
	public float[][] normalized_correlation_with_firstLast_padding(float[][] image, float[][] filter)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		float[][] output=new float[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;
			int tempi=0;
			int tempj=0;
			  for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      float sum=0;
			      float I=0.0f;
					float F=0.0f;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i+k>=0 && i+k<image_matrix_rows && j+l>=0 && j+l<image_matrix_cols)
			          {
			            sum=sum+(float)image[i+k][j+l]*filter[k+filter_center_row][l+filter_center_col];
			            I=I+(float)(image[i+k][j+l]*image[i+k][j+l]);
			            F=F+(float)(filter[k+filter_center_row][l+filter_center_col]*filter[k+filter_center_row][l+filter_center_col]);
			          }
			          else
			          {
			        	 if(i+k<0)
			        	 {
			        		 tempi=0;
			        	 }
			        	 else
			        	 {
			        		 if(i+k>=image_matrix_rows)
				        	 {
				        		 tempi=image_matrix_rows-1;
				        	 }
			        		 else
			        		 {
			        			 tempi=i+k;
			        		 }
			        	 }
			        	 if(j+l<0)
			        	 {
			        		 tempj=0;
			        	 }
			        	 else
			        	 {
			        		 if(j+l>=image_matrix_cols)
				        	 {
				        		 tempj=image_matrix_cols-1;
				        	 }
				        	 else
				        	 {
				        		 tempj=j+l;
				        	 } 
			        	 }
			        	  sum=sum+(float)image[tempi][tempj]*filter[k+filter_center_row][l+filter_center_col];
			        	  I=I+(float)(image[tempi][tempj]*image[tempi][tempj]);
			        	  F=F+(float)(filter[k+filter_center_row][l+filter_center_col]*filter[k+filter_center_row][l+filter_center_col]);
			          }
			        }
			      }
			      output[i][j]=(float) ((Math.round(sum * 100.0) / 100.0)/(float)Math.sqrt(I*F));
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}
	
	public float[][] normalized_correlation_with_zero_padding(float[][] image, float[][] filter)
	{
		int image_matrix_rows=image.length;
		int image_matrix_cols=image[0].length;
		float[][] output=new float[image_matrix_rows][image_matrix_cols];
		try
		{
			int filter_center_row=filter.length/2;
			int filter_center_col=filter[0].length/2;
			  for(int i=0;i<image_matrix_rows;i++){
			    for(int j=0;j<image_matrix_cols;j++){
			      float sum=0;
			      float I=0.0f;
				 float F=0.0f;
			      for(int k=-filter_center_row;k<=filter_center_row;k++){
			        for(int l=-filter_center_col;l<=filter_center_col;l++){
			          if(i+k>=0 && i+k<image_matrix_rows && j+l>=0 && j+l<image_matrix_cols)
			          {
			            sum=sum+image[i+k][j+l]*filter[k+filter_center_row][l+filter_center_col];
			            I=I+(float)(image[i+k][j+l]*image[i+k][j+l]);
			            F=F+(float)(filter[k+filter_center_row][l+filter_center_col]*filter[k+filter_center_row][l+filter_center_col]);
			          }
			        }
			      }
			      output[i][j]=(float) ((Math.round(sum * 100.0) / 100.0)/(float)Math.sqrt(I*F));
			    }
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return output;
	}

}
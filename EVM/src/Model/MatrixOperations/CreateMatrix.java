package Model.MatrixOperations;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class CreateMatrix {
	
	public double[][] create_matrix(BufferedImage img)
	   {
		int height=img.getHeight();
		int width=img.getWidth();
		//System.out.println(img.getType());
		//BufferedImage formatted_image=new BufferedImage(width,height,image_format);
		   double matrix[][] = new double[height][width];
		   try
		   {
			   for(int i=0;i<height;i++)
				{
					for(int j=0;j<width;j++)
					{
						//formatted_image.setRGB(i, j, img.getRGB(i, j));
						matrix[i][j]=img.getRGB(j, i);
					}
				}
			   
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		   return matrix;
	   }
	
	public BufferedImage reconstructImage(double[][] image_matrix,int image_type)
	{
		int image_rows=image_matrix.length;//height of image
		int image_cols=image_matrix[0].length;//width of image
		BufferedImage img=new BufferedImage(image_cols,image_rows,image_type);
		for(int i=0;i<image_cols;i++)
		{
			for(int j=0;j<image_rows;j++)
			{
				img.setRGB(i, j, (int)image_matrix[j][i]);
			}
		}
		
		
		return img;
	}
	
	public BufferedImage reconstructFTInverseImage(double[][][] image_matrix,int image_type)
	{
		int image_rows=image_matrix.length;//height of image
		int image_cols=image_matrix[0].length;//width of image
		BufferedImage img=new BufferedImage(image_cols,image_rows,image_type);
		for(int i=0;i<image_cols;i++)
		{
			for(int j=0;j<image_rows;j++)
			{
				int red=(int)image_matrix[j][i][0];
				int green=(int)image_matrix[j][i][1];
				int blue=(int)image_matrix[j][i][2];
				img.setRGB(i, j, (new Color(red,green,blue)).getRGB());
			}
		}
		
		
		return img;
	}

}

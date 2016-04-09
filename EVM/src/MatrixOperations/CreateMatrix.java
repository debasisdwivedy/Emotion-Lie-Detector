package MatrixOperations;

import java.awt.image.BufferedImage;

public class CreateMatrix {
	
	public double[][] create_matrix(BufferedImage img)
	   {
		int height=img.getHeight();
		int width=img.getWidth();
		//System.out.println(img.getType());
		//BufferedImage formatted_image=new BufferedImage(width,height,image_format);
		   double matrix[][] = new double[width][height];
		   try
		   {
			   for(int i=0;i<width;i++)
				{
					for(int j=0;j<height;j++)
					{
						//formatted_image.setRGB(i, j, img.getRGB(i, j));
						matrix[i][j]=img.getRGB(i, j);
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
		int image_rows=image_matrix.length;
		int image_cols=image_matrix[0].length;
		BufferedImage img=new BufferedImage(image_rows,image_cols,image_type);
		for(int i=0;i<image_rows;i++)
		{
			for(int j=0;j<image_cols;j++)
			{
				img.setRGB(i, j, (int)image_matrix[i][j]);
			}
		}
		
		return img;
	}

}

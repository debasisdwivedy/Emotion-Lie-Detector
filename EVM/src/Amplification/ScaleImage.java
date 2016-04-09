package Amplification;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ScaleImage {
	
	public BufferedImage scaleImage(BufferedImage larger_original_image,BufferedImage smaller_laplacian_image)
	{
		double scale_w =(double) larger_original_image.getWidth()/smaller_laplacian_image.getWidth();
		double scale_h = (double)larger_original_image.getHeight()/smaller_laplacian_image.getHeight();
		BufferedImage after = new BufferedImage(larger_original_image.getWidth(), larger_original_image.getHeight(), larger_original_image.getType());
		AffineTransform at = new AffineTransform();
		at.scale(scale_w, scale_h);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(smaller_laplacian_image, after);
		return after;
	}
	
	public BufferedImage makeImageSquare(BufferedImage image)
	{
		int width=image.getWidth();
		int height=image.getHeight();
		int width1=1;
		int prev_width1=1;
		int height1=1;
		int prev_height1=1;
		double scale_w=1;
		double scale_h=1;
		while (width1 < width)
        {
			prev_width1=width1;
			width1 = width1 << 1;
        }
		while (height1 < height)
        {
			prev_height1=height1;
			height1 = height1 << 1;
        }
		if(width-prev_width1>width1-width)
		{
			scale_w=(double)width1/width;
		}
		else
		{
			scale_w=(double)prev_width1/width;
			width1=prev_width1;
		}
		if(height-prev_height1>height1-height)
		{
			scale_h=(double)height1/height;
		}
		else
		{
			scale_h=(double)prev_height1/height;
			height1=prev_height1;
		}
		//System.out.println(scale_w+"::::::"+scale_h);
		BufferedImage after = new BufferedImage(width1, height1, image.getType());
		AffineTransform at = new AffineTransform();
		at.scale(scale_w, scale_h);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(image, after);
		return after;
	}

}

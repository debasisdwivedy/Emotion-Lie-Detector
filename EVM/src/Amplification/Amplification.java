package Amplification;

import java.awt.image.BufferedImage;

public class Amplification {
	
	public BufferedImage frequencyAmplification(BufferedImage image, int amplication_factor)
	{
		for(int i=0;i<image.getWidth();i++)
		{
			for(int j=0;j<image.getHeight();j++)
			{
				int alpha=(image.getRGB(i, j)>>24)&0xFF;
				int red=(image.getRGB(i, j)>>16)&0xFF;
				int green=(image.getRGB(i, j)>>8)&0xFF;
				int blue=(image.getRGB(i, j))&0xFF;
				int amplified_pixel=(alpha << 24) | 
									(amplication_factor*red << 16) | 
									(amplication_factor*green << 8) | 
									(amplication_factor*blue);
				image.setRGB(i, j, amplified_pixel);
			}
		}
		return image;
	}

}

package Model.Amplification;

import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

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
	
	public static Mat amplify(int i,int length,double lambda,double cutoffDelta,int alpha_prop,int exaggeration_factor,Mat pyramid)
	{
		double alpha_calc = lambda / cutoffDelta / 8 - 1;
		alpha_calc *= exaggeration_factor;
		Scalar ZERO = Scalar.all(0);
		Scalar ALPHA=Scalar.all(alpha_prop);
		if (0 == i || length - 1 == i) {
			Core.multiply(pyramid, ZERO, pyramid);
        } else if (alpha_calc > alpha_prop) {
        	Core.multiply(pyramid, ALPHA, pyramid);
        } else {
        	Core.multiply(pyramid, ZERO, pyramid);
        }
		return pyramid;
	}

}

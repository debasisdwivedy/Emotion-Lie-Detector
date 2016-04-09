package VideoProcessing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.FrameGrab.MediaInfo;
import org.jcodec.api.JCodecException;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import Amplification.ScaleImage;

public class VideoFrames {
	private Image image;  
    private BufferedImage bufferedImage;  
      
    int height = 0;  
    int width = 0; 
	
	public void setFrames(String fileName, String folderDest)
	{
		int frameNumber = 0;
	    Picture frame = null;
	    try {
	    	//int i=0;
	    for(int i=0;i<302;i++)
	    {   
	        

	    //video from which frames can be retrieved, declare frame number,
	    //returns numbered frame from video 

	    	//FrameGrab f=new FrameGrab((SeekableByteChannel) new File(fileName));
	    	//MediaInfo m=f.getMediaInfo();
	    frame = FrameGrab.getNativeFrame(new File(fileName), frameNumber);
	    // while(frame!=null)
	    // {
	     frameNumber = i;
	     BufferedImage dst=toBufferedImage(frame);
	    
	    //write frame as image declare image format and file path where image
	    //is to be write

	     	dst=new ScaleImage().makeImageSquare(dst);
			ImageIO.write(dst, "png", new File(folderDest+"/"+frameNumber+".png"));
			//i++;
	    // }
		
	}
	    }
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println("Finished set frames");
	}
	
	public ArrayList<String> getFrames(String folderDest)
	{
		//Read and return all the files from this folder
		File folder=new File(folderDest);
		ArrayList<String> listOfFrames=new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	getFrames(fileEntry.getAbsolutePath());
	        } else {
	        	listOfFrames.add(fileEntry.getAbsolutePath());
	        }
	    }
		System.out.println("Finished get frames");
		return listOfFrames;
	}
	
	public static BufferedImage toBufferedImage(Picture src) { 
        if (src.getColor() != ColorSpace.RGB) { 
            Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB); 
            Picture rgb = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB, src.getCrop()); 
            transform.transform(src, rgb); 
            src = rgb; 
        }
        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(), 
        		java.awt.color.ColorSpace.TYPE_RGB);
        
 
        if (src.getCrop() == null) 
            toBufferedImage(src, dst); 
        else 
            toBufferedImageCropped(src, dst); 
 
        
        return dst; 
    }
	
	public static void toBufferedImage(Picture src, BufferedImage dst) {
		
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData(); 
        //Arrays.
        int[] srcData = src.getPlaneData(0); 
        for (int i = 0; i < data.length; i++) { 
            data[i] = (byte) srcData[i]; 
        } 
    } 
	
	private static void toBufferedImageCropped(Picture src, BufferedImage dst) { 
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData(); 
        int[] srcData = src.getPlaneData(0); 
        int dstStride = dst.getWidth() * 3; 
        int srcStride = src.getWidth() * 3; 
        for (int line = 0, srcOff = 0, dstOff = 0; line < dst.getHeight(); line++) { 
            for (int id = dstOff, is = srcOff; id < dstOff + dstStride; id += 3, is += 3) { 
                data[id] = (byte) srcData[is]; 
                data[id + 1] = (byte) srcData[is + 1]; 
                data[id + 2] = (byte) srcData[is + 2]; 
            } 
            srcOff += srcStride; 
            dstOff += dstStride; 
        } 
    }
	public static ArrayList getYuvFromImage(BufferedImage image)
    {
        ArrayList yuv = new ArrayList();
        int[][] y = null;
        int[][] u = null;
        int[][] v = null;
        int r = 0;
        int g = 0;
        int b = 0;
        int width = 0;
        int height = 0;

        width = image.getWidth();
        height = image.getHeight();

        y = new int[height][width];
        u = new int[height][width];
        v = new int[height][width];

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                r = (image.getRGB(j, i) >> 16) & 0xFF;
                g = (image.getRGB(j, i) >> 8) & 0xFF;
                b = (image.getRGB(j, i) >> 0) & 0xFF;

                // Convert RGB to YUV colorspace
                //y[i][j] = (int) ((0.257 * r) + (0.504 * g) + (0.098 * b) + 16);
                //u[i][j] = (int) (-(0.148 * r) - (0.291 * g) + (0.439 * b) + 128);
                //v[i][j] = (int) ((0.439 * r) - (0.368 * g) - (0.071 * b) + 128);
                //y[i][j] = (int) ((0.2990 * r) + (0.5870 * g) + (0.1140 * b));
                //u[i][j] = (int) ((-0.1687 * r) - (0.3313 * g) + (0.5000 * b) + 128);
                //v[i][j] = (int) ((0.5000 * r) - (0.4187 * g) - (0.0813 * b) + 128);
                y[i][j] = (int) ((0.299 * r) + (0.587 * g) + (0.114 * b));
                u[i][j] = (int) ((-0.147 * r) - (0.289 * g) + (0.436 * b));
                v[i][j] = (int) ((0.615 * r) - (0.515 * g) - (0.100 * b));
            }
        }

        yuv.add(y);
        yuv.add(u);
        yuv.add(v);

        return yuv;
    }

    /**
     * Get image (with RGB data) from given YUV data
     * @param yuv List with three elements of two-dimensional int's - Y, U and V
     * @return Image
     */
    public static BufferedImage getImageFromYuv(ArrayList yuv)
    {
        BufferedImage image = null;
        int width = 0;
        int height = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int[][] y = null;
        int[][] u = null;
        int[][] v = null;

        y = (int[][]) yuv.get(0);
        u = (int[][]) yuv.get(1);
        v = (int[][]) yuv.get(2);

        height = y.length;
        width = y[0].length;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                // Convert YUV back to RGB
                //r = pixelRange(1.164 * (y[i][j] - 16) + 1.596 * (v[i][j] - 128));
                //g = pixelRange(1.164 * (y[i][j] - 16) - 0.391 * (u[i][j] - 128) - 0.813 * (v[i][j] - 128));
                //b = pixelRange(1.164 * (y[i][j] - 16) + 2.018 * (u[i][j] - 128));
                //r = pixelRange(y[i][j] + 1.40200 * (v[i][j] - 128));
                //g = pixelRange(y[i][j] - 0.34414 * (u[i][j] - 128) - 0.71414 * (v[i][j] - 128));
                //b = pixelRange(y[i][j] + 1.77200 * (u[i][j] - 128));
                r = pixelRange(y[i][j] + 1.140 * v[i][j]);
                g = pixelRange(y[i][j] - 0.395 * u[i][j] - 0.581 * v[i][j]);
                b = pixelRange(y[i][j] + 2.032 * u[i][j]);

                image.setRGB(j, i, (r << 16) + (g << 8) + b);
            }
        }

        return image;
    }
    
    /*
     * Get Gray Scale Image from a int[][] of pixel values
     * 
     */
    
    public static BufferedImage array2GrayScaleImage(int[][] pixels)
	{
    	int height=pixels.length;
		int width=pixels[0].length;
		BufferedImage newImg=null;
		
		try
		{
		newImg=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				int new_pixel= 
						(pixels[i][j] << 16) | 
						(pixels[i][j] << 8) | 
						(pixels[i][j]);
				newImg.setRGB(j, i, new_pixel);
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return newImg;
    	
	}
    
    /**
     * 
     * Get array from a grayscale image
     * 
     */
    
    public static int[][] grayScaleImage2Array(BufferedImage image)
	{
    	int height=image.getHeight();
		int width=image.getWidth();
		int[][] pixels=new int[height][width];
		
		try
		{
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				pixels[j][i]=image.getRGB(i, j)&0xFF;
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return pixels;
    	
	}
    
    /**
     * Utility method to limit the value within [0,255] range
     * @param p Input value
     * @return Limited value
     */
    public static int pixelRange(int p)
    {
        return ((p > 255) ? 255 : (p < 0) ? 0 : p);
    }

    /**
     * Utility method to limit the value within [0,255] range
     * @param p Input value
     * @return Limited value
     */
    public static int pixelRange(double p)
    {
        return ((p > 255) ? 255 : (p < 0) ? 0 : (int) p);
    }
	 
    
    public static void main(String[] args) {
    	new VideoFrames().setFrames("face.mp4", 
    					"/Users/debasis/Documents/Computer_Vision/EVM_FaceDetection/dummy/");
    }

}

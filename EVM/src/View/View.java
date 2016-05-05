package View;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import Model.ColorAmplification;
import Model.EulerianVideoMagnification;
import Model.MotionAmplification;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IVideoPicture;

public class View extends MediaToolAdapter{
	private ColorAmplification cAmp;
	private MotionAmplification mAmp;
	boolean flagColor=false;
	boolean flagMotion=false;
	private boolean flag = true;
    private byte[] imagebyteMatrix;
    private Mat imageMatrix;
    private Mat imageFrame;
    private byte[] pixelValues;
    private int frameRows;
    private int frameCols;
    private int frameChannels;
    private int[] imageData;
	public View(ColorAmplification cAmp)
	{
		flagColor=true;
		this.cAmp=cAmp;
	}
	public View(MotionAmplification mAmp)
	{
		flagMotion=true;
		this.mAmp=mAmp;
	}
	
    public void onVideoPicture(IVideoPictureEvent event) {
		Mat resultMatrix=null;
        IVideoPicture p = event.getPicture();
        if (flag) {
        	imagebyteMatrix = new byte[p.getSize()];
        	imageMatrix = new Mat(p.getHeight() + p.getHeight() / 2, p.getWidth(), CvType.CV_8UC1);
        	imageFrame = new Mat();
        }

        p.get(0, imagebyteMatrix, 0, imagebyteMatrix.length);
        imageMatrix.put(0, 0, imagebyteMatrix);
        Imgproc.cvtColor(imageMatrix, imageFrame, Imgproc.COLOR_YUV420p2RGBA, 4);

        if (flag) {
            frameRows = imageFrame.rows();
            frameCols = imageFrame.cols();
            frameChannels = imageFrame.channels();
            pixelValues = new byte[frameCols * frameRows * frameChannels];
            imageData = new int[frameCols * frameRows];
            if (flagColor)
            {
            	cAmp.setFrame(frameCols, frameRows);
            }
            else if(flagMotion)
            {
            	mAmp.setFrame(frameCols, frameRows);
            }
            else
            {
            	System.out.println("Unknown amplification in XugglerVideoController");
            	System.exit(0);
            }
            flag = false;
        }

        if (flagColor)
        {
        	resultMatrix = cAmp.amplifyColor(imageFrame);
        }
        else if(flagMotion)
        {
        	resultMatrix = mAmp.amplifyMotion(imageFrame);
        }
        resultMatrix.get(0, 0, pixelValues);
        imageData=getImageData(frameRows,frameCols,frameChannels,pixelValues,imageData);
        event.getImage().setRGB(0, 0, frameCols, frameRows, imageData, 0, frameCols);
        
        super.onVideoPicture(event);
    }
	
	public static int[] getImageData(int row,int cols,int channel,byte[] pixelValues,int[] image)
	{
		for (int y = 0; y < row; y++) {
            for (int x = 0; x < cols; x++) {
                int index = (x + y * cols) * channel;
                int alphaValue=(0xff & pixelValues[index + 3]);
                int redValue=(0xff & pixelValues[index + 2]);
                int greenValue=(0xff & pixelValues[index + 1]);
                int blueValue=(0xff & pixelValues[index + 0]);
                image[x + y * cols] =(alphaValue<<24)|(redValue<<16)|(greenValue<<8)|blueValue;
                                             ;
            }
        }
		return image;
	}
	

}

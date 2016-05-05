package Model.TemporalFiltering;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import Model.Amplification.Amplification;
import Model.SpatialDecomposition.LaplacianPyramids;
import Utils.Constants;

public class FrequencyFilter {

	private int min=Integer.MIN_VALUE;
	private int max=Integer.MAX_VALUE;

    /**
     * Initializes a new instance of the FrequencyFilter class.
     */
    public FrequencyFilter() {}
    
    /**
     * Initializes a new instance of the FrequencyFilter class.
     * @param min Minimum value for to keep.
     * @param max Maximum value for to keep.
     */
    public FrequencyFilter(int min, int max){
        this.min = min;
        this.max=max;
    }


    public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
    
    /**
     * Apply filter to an fourierTransform.
     * @param fourierTransform Fourier transformed.
     */
    public void ApplyInPlace(FourierTransform fourierTransform){
        if (!fourierTransform.isFourierTransformed()) {
            try {
                throw new Exception("the image should be fourier transformed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fourierTransform.getWidth();
        int height = fourierTransform.getHeight();
        
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        
        int min = this.getMin();
        int max = this.getMax();
        
        ComplexNumber[][] c = fourierTransform.getData();
        
        //int counter=0;
        for ( int i = 0; i < height; i++ ){
            int y = i - halfHeight;

            for ( int j = 0; j < width; j++ ){
                int x = j - halfWidth;
                int d = (int) Math.sqrt( x * x + y * y );
                //if(d>360)
                //System.out.println(d);
                //System.out.println(c[i][j].get);

                // filter values outside the range
                if ( ( d > max ) || ( d < min ) ){
                    c[i][j].setRe(0);
                    c[i][j].setIm(0);
                }
                //counter++;
            }
        }
        
    }
    
    /*
     * Return frequency range of the input signal
     * n->number_of_frames
     * d->1/frames per second
     * f = [0, 1, ...,   (n/2)-1,     -n/2, ..., -1] / (d*n)   if n is even
	   f = [0, 1, ..., (n-1)/2, -(n-1)/2, ..., -1] / (d*n)   if n is odd
     * 
     */
    
    public static void getFrequencyRange(int number_of_frames,int fps)
    {
    	float[] frequency_range=new float[number_of_frames];
    	int count=0;
    	if(number_of_frames%2==0)
    	{
    		for(int i=0;i<=(number_of_frames/2)-1;i++)
    		{
    			float d=(float)(i*fps)/(number_of_frames);
    			frequency_range[i]=d;
    			count=i;
    		}
    		for(int i=-(number_of_frames/2);i<0;i++)
    		{
    			count++;
    			float d=(float)(i*fps)/(number_of_frames);
    			frequency_range[count]=d;
    		}
    	}
    	else
    	{
    		for(int i=0;i<=(number_of_frames-1)/2;i++)
    		{
    			float d=(float)(i*fps)/(number_of_frames);
    			frequency_range[i]=d;
    			count=i;
    		}
    		for(int i=-(number_of_frames-1)/2;i<0;i++)
    		{
    			count++;
    			float d=(float)(i*fps)/(number_of_frames);
    			frequency_range[count]=d;
    		}
    	}
    }
    
    public static Mat frequencyFilter(int frameCount,Mat[] lowpassHigh,Mat[] lowpassHighAux,
    		Mat[] lowpassLow, Mat[] lowpassLowAux,Mat[] pyr,double cutoffDelta,
    		int exaggerationFactor,double initialLambdaCutoff,Mat frame,Mat output,Mat frameMatrix)
    	{
    	Mat final_output=new Mat();
    	if (0 == frameCount) {
            for (int i = 0; i < pyr.length; i++) {
                pyr[i].copyTo(lowpassHigh[i]);
                pyr[i].copyTo(lowpassLow[i]);
            }
            frame.copyTo(output);
        } else {
            double lambda = initialLambdaCutoff;

            for (int i = pyr.length - 1; i > 0; i--) {
            	Core.multiply(lowpassHigh[i], Scalar.all(1-Constants.F_HIGH/10), lowpassHigh[i]);
                Core.multiply(pyr[i], Scalar.all(Constants.F_HIGH/10), lowpassHighAux[i]);
                Core.add(lowpassHigh[i], lowpassHighAux[i], lowpassHigh[i]);

                Core.multiply(lowpassLow[i], Scalar.all(1-Constants.F_LOW/10), lowpassLow[i]);
                Core.multiply(pyr[i], Scalar.all(Constants.F_LOW/10), lowpassLowAux[i]);
                Core.add(lowpassLow[i], lowpassLowAux[i], lowpassLow[i]);

                Core.subtract(lowpassHigh[i], lowpassLow[i], pyr[i]);
                pyr[i]=Amplification.amplify(i,pyr.length,lambda,cutoffDelta,Constants.ALPHA,exaggerationFactor,pyr[i]);
                lambda /= 2.0;
            }
    }
    	// rebuild frame from Laplacian pyramid
    	final_output= new LaplacianPyramids().reconLpyr(pyr, final_output);

        // add back to original frame
        Core.add(frameMatrix, final_output, final_output);

        // add back alpha channel and convert to 8 bit
        Imgproc.cvtColor(final_output, final_output, Imgproc.COLOR_RGB2RGBA);
        
        final_output.convertTo(output, CvType.CV_8U);
        return output;
}
}

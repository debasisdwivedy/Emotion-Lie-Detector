package Model.TemporalFiltering;

import java.awt.image.BufferedImage;

public class FourierTransform {
	
	private ComplexNumber[][] data;
    private int width, height;
    private boolean fourierTransformed = false;
    private double min_data=Double.MAX_VALUE;
    private double max_data=-Double.MAX_VALUE;

    /**
     * Initialize a new instance of the FourierTransform class.
     * @param buffImg BufferedImage.
     */
    public FourierTransform(BufferedImage buffImg) {
        if (buffImg.getType()==10) {
            this.width = buffImg.getWidth();
            this.height = buffImg.getHeight();
            double pixel_value=0.0;
            data = new ComplexNumber[height][width];
            //data = new ComplexNumber[height*width];

            int counter=0;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    data[x][y] = new ComplexNumber(0, 0);
                	//data[counter] = new ComplexNumber(0, 0);
                    pixel_value=(buffImg.getRGB(y, x)&0xFF);
                    //System.out.print(pixel_value+"\t");
                    data[x][y].setRe(pixel_value);
                    //data[counter].setRe(pixel_value);
                    //counter++;
                }
                //System.out.println();
            }
        }
        else{
            try {
                throw new Exception("FourierTransform works only with Grayscale images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Complex image width.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Complex image height.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Complex image's data.
     * @return Data.
     */
    public ComplexNumber[][] getData() {
        return data;
    }

    /**
     * Complex image's data.
     * @param data Data.
     */
    public void setData(ComplexNumber[][] data) {
        this.data = data;
    }

    /**
     * Status of the image - Fourier transformed or not.
     * @return <b>True</b>: is transformed, otherwise returns <b>False</b>.
     */
    public boolean isFourierTransformed() {
        return fourierTransformed;
    }
    
    /**
     * Convert Complex image's data to BufferedImage.
     * @return BufferedImage.
     */
    public BufferedImage toBufferedImage(){
        
    	BufferedImage fb = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        if(fourierTransformed){
            
            //Calculate the magnitude
            double[][] mag = new double[height][width];
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            //int counter=0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    //Compute log for perceptual scaling and +1 since log(0) is undefined.
                    //mag[i][j] = Math.log(data[counter].getMagnitude() + 1);
                    mag[i][j] = Math.log(data[i][j].getMagnitude() + 1); 
                    if(mag[i][j] < min) min = mag[i][j];
                    if(mag[i][j] > max) max = mag[i][j];
                    //counter++;
                }
            }
            
            //Scale the image
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                	int temp=(int)Scale(min, max, 0, 1, mag[i][j]);
                	int new_pixel= 
    						(temp << 16) | 
    						(temp << 8) | 
    						(temp);
                    fb.setRGB(i, j, new_pixel);
                }
            }
        }
        else{
        	
        	int counter=0;
            //Show only the real part
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    //int real = (int)data[counter].getRe();
                	int real = (int)data[i][j].getRe();
                    int temp=clampValues(real, 0, 1);
                    int new_pixel= 
    						(temp << 16) | 
    						(temp << 8) | 
    						(temp);
                    fb.setRGB(i, j, new_pixel);
                    counter++;
                }
            }
            
        }
        
        return fb;
    }
    
    /**
     * Applies forward fast Fourier transformation to the complex image.
     */
    public void Forward(){
        if (!fourierTransformed){
        	//int counter=0;
            for ( int x = 0; x < height; x++ ){
                for ( int y = 0; y < width; y++ ){
                    if ( ( ( x + y ) & 0x1 ) != 0 ){
                        data[x][y].setRe(data[x][y].getRe()* (-1));
                        data[x][y].setIm(data[x][y].getIm()* (-1));
                    }
                    //counter++;
                }
            }
            //System.out.println(data.length);
            FFT.FFT2(data, FFT.Direction.Forward);
            fourierTransformed = true;
        }
    }
    
    /**
     * Applies backward fast Fourier transformation to the complex image.
     */
    public void Backward( ){
        if ( fourierTransformed ){
            FFT.FFT2(data, FFT.Direction.Backward);
            fourierTransformed = false;

            int counter=0;
            for ( int x = 0; x < height; x++ ){
                for ( int y = 0; y < width; y++ ){
                    if ( ( ( x + y ) & 0x1 ) != 0 ){
                    	data[x][y].setRe(data[x][y].getRe()* (-1));
                    	data[x][y].setIm(data[x][y].getIm()* (-1));
                    }
                    counter++;
                }
            }
        }
    }
    
    public static double Scale(double fromMin, double fromMax, double toMin, double toMax, double x){
        if (fromMax - fromMin == 0) return 0;
        return (toMax - toMin) * (x - fromMin) / (fromMax - fromMin) + toMin;
}
    
    public int clampValues(int value, int min, int max){
        if(value < min)
            return min;
        else if(value > max)
            return max;
        return value;
    }
    
    public void setMin_Max_Data()
    {
    	int counter=0;
    	for ( int x = 0; x < height; x++ ){
            for ( int y = 0; y < width; y++ ){
            	if(data[x][y].getRe()>this.max_data)
            		this.max_data=data[x][y].getRe();
            	if(data[x][y].getRe()<this.min_data)
            		this.min_data=data[x][y].getRe();
            	counter++;
            }
    	}
    }
    
    public double getMinData()
    {
    	return this.min_data;
    }
    public double getMaxData()
    {
    	return this.max_data;
    }

}

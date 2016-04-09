package TemporalFiltering;

public class FrequencyFilter {

	private int min=0;
	private int max=1024;

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
        
        for ( int i = 0; i < height; i++ ){
            int y = i - halfHeight;

            for ( int j = 0; j < width; j++ ){
                int x = j - halfWidth;
                int d = (int) Math.sqrt( x * x + y * y );

                // filter values outside the range
                if ( ( d > max ) || ( d < min ) ){
                    c[i][j].setRe(0);
                    c[i][j].setIm(0);
                }
            }
        }
        
    }
}

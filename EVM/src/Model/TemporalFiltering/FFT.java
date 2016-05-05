package Model.TemporalFiltering;

/*
 * 
 * Modified Code from googles code project repository
 */

public class FFT {
	
	/**
     * Transformation direction.
     */
    public enum Direction {

        /**
         * Forward direction of Fourier transformation.
         */
        Forward,
        /**
         * Backward direction of Fourier transformation.
         */
        Backward
    };
    
    /**
     * 1-D Discrete Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void DFT(ComplexNumber[] data, Direction direction){
        int n = data.length;
        ComplexNumber[] c = new ComplexNumber[n];
        
        // for each destination element
        for ( int i = 0; i < n; i++ ){
                c[i] = new ComplexNumber(0, 0);
                double sumRe = 0;
                double sumIm = 0;
                double phim = 2 * Math.PI * i / n;

                // sum source elements
                for ( int j = 0; j < n; j++ ){
                        double gRe = data[j].getRe();
                        double gIm = data[j].getIm();
                        double cosw = Math.cos(phim * j);
                        double sinw = Math.sin(phim * j);
                        if(direction == Direction.Backward)
                            sinw = -sinw;

                        sumRe += ( gRe * cosw + data[j].getIm() * sinw );
                        sumIm += ( gIm * cosw - data[j].getRe() * sinw );
                }
                
                c[i] = new ComplexNumber(sumRe, sumIm);
        }
        
        if(direction == Direction.Backward){
            for (int i = 0; i < c.length; i++) {
                data[i].setRe(c[i].getRe() / n) ;
                data[i].setIm(c[i].getIm() / n);
            }
        }
        else{
            for (int i = 0; i < c.length; i++) {
            	data[i].setRe(c[i].getRe()) ;
            	data[i].setIm(c[i].getIm());
            }
        }
    }
    
    /**
     * 2-D Discrete Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void DFT2(ComplexNumber[][] data, Direction direction){
        
        int n = data.length;
        int m = data[0].length;
        ComplexNumber[] row = new ComplexNumber[Math.max(m, n)];
        
        for ( int i = 0; i < n; i++ ){
                // copy row
                for ( int j = 0; j < n; j++ )
                        row[j] = data[i][j];
                // transform it
                FFT.DFT( row, direction );
                // copy back
                for ( int j = 0; j < n; j++ )
                        data[i][j] = row[j];
        }

        // process columns
        ComplexNumber[]	col = new ComplexNumber[n];

        for ( int j = 0; j < n; j++ ){
                // copy column
                for ( int i = 0; i < n; i++ )
                        col[i] = data[i][j];
                // transform it
                FFT.DFT( col, direction );
                // copy back
                for ( int i = 0; i < n; i++ )
                        data[i][j] = col[i];
        }
    }
    
    /**
     * 1-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void FFT(ComplexNumber[] data, Direction direction){
        double[] real = ComplexNumber.getReal(data);
        double[] img = ComplexNumber.getImaginary(data);
        if(direction == Direction.Forward)
            FFT(real,img);
        else
            FFT(img, real);
        if(direction == Direction.Forward){
            for (int i = 0; i < real.length; i++) {
                data[i] = new ComplexNumber(real[i], img[i]);
            }
        }
        else{
            int n = real.length;
            for (int i = 0; i < n; i++) {
                data[i] = new ComplexNumber(real[i] / n, img[i] / n);
            }
        }
    }
    
    /**
     * 2-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void FFT2(ComplexNumber[][] data, Direction direction){
        int n = data.length;
        int m = data[0].length;
        //ComplexNumber[] row = new ComplexNumber[m];//Math.max(m, n)];
        
        for ( int i = 0; i < n; i++ ){
                // copy row
                //for ( int j = 0; j < m; j++ )
                        //row[j] = data[i][j];
                ComplexNumber[] row = data[i];
                // transform it
                FFT.FFT( row, direction );
                // copy back
                for ( int j = 0; j < m; j++ )
                        data[i][j] = row[j];
        }

        // process columns
        ComplexNumber[]	col = new ComplexNumber[n];

        for ( int j = 0; j < m; j++ ){
                // copy column
                for ( int i = 0; i < n; i++ )
                        col[i] = data[i][j];
                // transform it
                FFT.FFT( col, direction );
                // copy back
                for ( int i = 0; i < n; i++ )
                        data[i][j] = col[i];
        }
    }
    
    /* 
     * Computes the discrete Fourier transform (DFT) of the given complex vector, storing the result back into the vector.
     * The vector can have any length. This is a wrapper function.
     */
    private static void FFT(double[] real, double[] imag) {
        int n = real.length;
        if (n == 0) {
            return;
        } else if ((n & (n - 1)) == 0)  // Is power of 2
            transformRadix2(real, imag);
        else  // More complicated algorithm for arbitrary sizes
            transformBluestein(real, imag);
    }
	
	
    /* 
     * Computes the inverse discrete Fourier transform (IDFT) of the given complex vector, storing the result back into the vector.
     * The vector can have any length. This is a wrapper function. This transform does not perform scaling, so the inverse is not a true inverse.
     */
    private static void inverseTransform(double[] real, double[] imag) {
        FFT(imag, real);
    }
	
    /* 
     * Computes the discrete Fourier transform (DFT) of the given complex vector, storing the result back into the vector.
     * The vector's length must be a power of 2. Uses the Cooley-Tukey decimation-in-time radix-2 algorithm.
     */
    private static void transformRadix2(double[] real, double[] imag) {
        int n = real.length;
        int levels = 31 - Integer.numberOfLeadingZeros(n);  // Equal to floor(log2(n))
//        if (1 << levels != n)
//            throw new IllegalArgumentException("Length is not a power of 2");
        double[] cosTable = new double[n / 2];
        double[] sinTable = new double[n / 2];
        for (int i = 0; i < n / 2; i++) {
            cosTable[i] = Math.cos(2 * Math.PI * i / n);
            sinTable[i] = Math.sin(2 * Math.PI * i / n);
        }

        // Bit-reversed addressing permutation
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> (32 - levels);
            if (j > i) {
                    double temp = real[i];
                    real[i] = real[j];
                    real[j] = temp;
                    temp = imag[i];
                    imag[i] = imag[j];
                    imag[j] = temp;
            }
        }

        // Cooley-Tukey decimation-in-time radix-2 FFT
        for (int size = 2; size <= n; size *= 2) {
            int halfsize = size / 2;
            int tablestep = n / size;
            for (int i = 0; i < n; i += size) {
                for (int j = i, k = 0; j < i + halfsize; j++, k += tablestep) {
                    double tpre =  real[j+halfsize] * cosTable[k] + imag[j+halfsize] * sinTable[k];
                    double tpim = -real[j+halfsize] * sinTable[k] + imag[j+halfsize] * cosTable[k];
                    real[j + halfsize] = real[j] - tpre;
                    imag[j + halfsize] = imag[j] - tpim;
                    real[j] += tpre;
                    imag[j] += tpim;
                }
            }
            
            // Prevent overflow in 'size *= 2'
            if (size == n)
                break;
        }
    }
	
    /* 
     * Computes the discrete Fourier transform (DFT) of the given complex vector, storing the result back into the vector.
     * The vector can have any length. This requires the convolution function, which in turn requires the radix-2 FFT function.
     * Uses Bluestein's chirp z-transform algorithm.
     */
    private static void transformBluestein(double[] real, double[] imag) {
        int n = real.length;
        int m = Integer.highestOneBit(n * 2 + 1) << 1;

        // Trignometric tables
        double[] cosTable = new double[n];
        double[] sinTable = new double[n];
        for (int i = 0; i < n; i++) {
            int j = (int)((long)i * i % (n * 2));  // This is more accurate than j = i * i
            cosTable[i] = Math.cos(Math.PI * j / n);
            sinTable[i] = Math.sin(Math.PI * j / n);
        }

        // Temporary vectors and preprocessing
        double[] areal = new double[m];
        double[] aimag = new double[m];
        for (int i = 0; i < n; i++) {
            areal[i] =  real[i] * cosTable[i] + imag[i] * sinTable[i];
            aimag[i] = -real[i] * sinTable[i] + imag[i] * cosTable[i];
        }
        double[] breal = new double[m];
        double[] bimag = new double[m];
        breal[0] = cosTable[0];
        bimag[0] = sinTable[0];
        for (int i = 1; i < n; i++) {
            breal[i] = breal[m - i] = cosTable[i];
            bimag[i] = bimag[m - i] = sinTable[i];
        }

        // Convolution
        double[] creal = new double[m];
        double[] cimag = new double[m];
        convolve(areal, aimag, breal, bimag, creal, cimag);

        // Postprocessing
        for (int i = 0; i < n; i++) {
            real[i] =  creal[i] * cosTable[i] + cimag[i] * sinTable[i];
            imag[i] = -creal[i] * sinTable[i] + cimag[i] * cosTable[i];
        }
    }
	
    /* 
     * Computes the circular convolution of the given real vectors. Each vector's length must be the same.
     */
    private static void convolve(double[] x, double[] y, double[] out) {
//        if (x.length != y.length || x.length != out.length)
//                throw new IllegalArgumentException("Mismatched lengths");
        int n = x.length;
        convolve(x, new double[n], y, new double[n], out, new double[n]);
    }
    
    /* 
     * Computes the circular convolution of the given complex vectors. Each vector's length must be the same.
     */
    private static void convolve(double[] xreal, double[] ximag, double[] yreal, double[] yimag, double[] outreal, double[] outimag) {
//        if (xreal.length != ximag.length || xreal.length != yreal.length || yreal.length != yimag.length || xreal.length != outreal.length || outreal.length != outimag.length)
//            throw new IllegalArgumentException("Mismatched lengths");

        int n = xreal.length;

        FFT(xreal, ximag);
        FFT(yreal, yimag);
        for (int i = 0; i < n; i++) {
            double temp = xreal[i] * yreal[i] - ximag[i] * yimag[i];
            ximag[i] = ximag[i] * yreal[i] + xreal[i] * yimag[i];
            xreal[i] = temp;
        }
        inverseTransform(xreal, ximag);
        
        // Scaling (because this FFT implementation omits it)
        for (int i = 0; i < n; i++) {
            outreal[i] = xreal[i] / n;
            outimag[i] = ximag[i] / n;
        }
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param x Data.
     * @param direction Transformation direction.
     */
    public static void FFTShift1D(double[] x, Direction direction) {
        
        if (x.length == 1)
            return;
        
        double[] temp = x.clone();
        int move = x.length / 2;
        
        if(direction == Direction.Forward){
            int c = 0;
            for (int i = x.length - move; i < x.length; i++)
                x[c++] = temp[i];

            for (int i = 0; i < x.length - move; i++)
                x[c++] = temp[i];
            
        }
        else{
            int c = 0;
            for (int i = move; i < x.length; i++)
                x[c++] = temp[i];
            
            for (int i = 0; i < move; i++)
                x[c++] = temp[i];
            
        }
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param <E> Object.
     * @param x Data.
     * @param direction Transformation direction.
     */
    public static <E> void FFTShift1D(E[] x, Direction direction) {
        
        if (x.length == 1)
            return;
        
        E[] temp = x.clone();
        int move = x.length / 2;
        
        if(direction == Direction.Forward){
            int c = 0;
            for (int i = x.length - move; i < x.length; i++)
                x[c++] = temp[i];

            for (int i = 0; i < x.length - move; i++)
                x[c++] = temp[i];
        }
        else{
            int c = 0;
            for (int i = move; i < x.length; i++)
                x[c++] = temp[i];
            
            for (int i = 0; i < move; i++)
                x[c++] = temp[i];
        }
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param x Data.
     * @param direction Direction.
     */
    public static void FFTShift2D(double[][] x, Direction direction){
        FFTShift2D(x, direction, 1);
        FFTShift2D(x, direction, 2);
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param x Data.
     * @param direction Direction.
     * @param dimension Dimension.
     */
    public static void FFTShift2D(double[][] x, Direction direction, int dimension){
        
        //Create a copy
        double[][] temp = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                temp[i][j] = x[i][j];
            }
        }
        
        if(direction == Direction.Forward){
            //Perform fftshift in the first dimension
            if(dimension == 1){
                int move =  temp.length / 2;
                for (int i = 0; i < move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[temp.length - move + i][j];
                    }
                }

                for (int i = move; i < x.length; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[i - move][j];
                    }
                }

            }

            //Perform fftshift in the second dimension
            if (dimension == 2){
                for (int i = 0; i < x.length; i++) {
                    FFTShift1D(x[i], FFT.Direction.Forward);
                }
            }
        }
        else{
            if(dimension == 1){
                int move =  temp.length / 2;
                for (int i = 0; i < x.length - move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[move+i][j];
                    }
                }
                for (int i = 0; i < move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[x.length-move+i][j] = temp[i][j];
                    }
                }
                
            }
            if(dimension == 2){
                for (int i = 0; i < x.length; i++) {
                    FFTShift1D(x[i], FFT.Direction.Backward);
                }
            }
        }
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param x Data.
     * @param direction Direction.
     */
    public static <E> void FFTShift2D(E[][] x, Direction direction){
        FFTShift2D(x, direction, 1);
        FFTShift2D(x, direction, 2);
    }
    
    /**
     * Shift zero-frequency component to center of spectrum.
     * @param x Data.
     * @param direction Direction.
     * @param dimension Dimension.
     */
    public static <E> void FFTShift2D(E[][] x, Direction direction, int dimension){
        
        //Create a copy
        E[][] temp = (E[][])new Object[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                temp[i][j] = x[i][j];
            }
        }
        
        if(direction == Direction.Forward){
            //Perform fftshift in the first dimension
            if(dimension == 1){
                int move =  temp.length / 2;
                for (int i = 0; i < move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[temp.length - move + i][j];
                    }
                }

                for (int i = move; i < x.length; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[i - move][j];
                    }
                }

            }

            //Perform fftshift in the second dimension
            if (dimension == 2){
                for (int i = 0; i < x.length; i++) {
                    FFTShift1D(x[i], FFT.Direction.Forward);
                }
            }
        }
        else{
            if(dimension == 1){
                int move =  temp.length / 2;
                for (int i = 0; i < x.length - move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[i][j] = temp[move+i][j];
                    }
                }
                for (int i = 0; i < move; i++) {
                    for (int j = 0; j < x[0].length; j++) {
                        x[x.length-move+i][j] = temp[i][j];
                    }
                }
                
            }
            if(dimension == 2){
                for (int i = 0; i < x.length; i++) {
                    FFTShift1D(x[i], FFT.Direction.Backward);
                }
            }
        }
    }

}

package Model.TemporalFiltering;

public class ComplexNumber {
	
	private double re, im;

    public ComplexNumber() {
    }

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public ComplexNumber(ComplexNumber source) {
        if (source != null) {
            re = source.re;
            im = source.im;
        }
    }

    public double getRe() {
        return re;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }

    public ComplexNumber getConjugate() {
        return new ComplexNumber(re, im * -1);
    }

    public ComplexNumber add(ComplexNumber op) {
        return new ComplexNumber(re + op.getRe(), im + op.getIm());
    }

    public ComplexNumber sub(ComplexNumber op) {
        return new ComplexNumber(re - op.getRe(), im - op.getIm());
    }

    public ComplexNumber mul(ComplexNumber op) {
        return new ComplexNumber(re * op.getRe() - im * op.getIm(), re * op.getIm() + im * op.getRe());
    }

    public ComplexNumber div(ComplexNumber op) {
        ComplexNumber result = new ComplexNumber(this);
        result = result.mul(op.getConjugate());
        double opNormSq = op.getRe() * op.getRe() + op.getIm() * op.getIm();
        result.setRe(result.getRe() / opNormSq);
        result.setIm(result.getIm() / opNormSq);
        return result;
    }

    public static ComplexNumber fromPolar(double magnitude, double angle) {
        ComplexNumber result = new ComplexNumber();
        result.setRe(magnitude * Math.cos(angle));
        result.setIm(magnitude * Math.sin(angle));
        return result;
    }
    
    /**
     * Get real part from the complex numbers.
     * @param cn Complex numbers.
     * @return Real part.
     */
    public static double[] getReal(ComplexNumber[] cn){
        double[] n = new double[cn.length];
        for (int i = 0; i < n.length; i++) {
            n[i] = cn[i].getRe();
        }
        return n;
    }
    
    /**
     * Get imaginary part from the complex numbers.
     * @param cn Complex numbers.
     * @return Imaginary part.
     */
    public static double[] getImaginary(ComplexNumber[] cn){
        double[] n = new double[cn.length];
        for (int i = 0; i < n.length; i++) {
            n[i] = cn[i].getIm();
        }
        return n;
    }
    
    /**
     * Get real part from the complex numbers.
     * @param cn Complex numbers.
     * @return Real part.
     */
    public static double[][] getReal(ComplexNumber[][] cn){
        double[][] n = new double[cn.length][cn[0].length];
        for (int i = 0; i < n.length; i++) {
            for (int j = 0; j < n[0].length; j++) {
                n[i][j] = cn[i][j].getRe();
            }
        }
        return n;
    }
    
    /**
     * Get imaginary part from the complex numbers.
     * @param cn Complex numbers.
     * @return Imaginary part.
     */
    public static double[][] getImaginary(ComplexNumber[][] cn){
        double[][] n = new double[cn.length][cn[0].length];
        for (int i = 0; i < n.length; i++) {
            for (int j = 0; j < n[0].length; j++) {
                n[i][j] = cn[i][j].getIm();
            }
        }
        return n;
    }

    public double getMagnitude() {
        return Math.sqrt(re * re + im * im);
    }

    public double getArgument() {
        return Math.atan2(im, re);
    }
    
    public ComplexNumber times(ComplexNumber b) 
    {
    	ComplexNumber a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new ComplexNumber(real, imag);
    }
    
    public ComplexNumber plus(ComplexNumber b) 
    {
    	ComplexNumber a = this; // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new ComplexNumber(real, imag);
    }
    
    public ComplexNumber minus(ComplexNumber b) 
    {
    	ComplexNumber a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new ComplexNumber(real, imag);
    }

    @Override
    public String toString() {
        return re + "+i." + im + " (" + getMagnitude() + ".exp(i " + (getArgument() / Math.PI) + " PI)";
    }

}

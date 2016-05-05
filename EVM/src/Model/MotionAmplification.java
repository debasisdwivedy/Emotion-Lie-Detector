package Model;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import Model.SpatialDecomposition.LaplacianPyramids;
import Model.TemporalFiltering.FrequencyFilter;
import Utils.Constants;

public class MotionAmplification{
	public static final Scalar BLUE = new Scalar(255, 0, 0, 255);
    public static final Scalar RED = new Scalar(0, 0, 255, 255);
    public static final Scalar ZERO = Scalar.all(0);
    private Properties prop;
	private CascadeClassifier faceDetector;
	private int frameCount;
    public Properties getProp() {
		return prop;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	public CascadeClassifier getFaceDetector() {
		return faceDetector;
	}
	public void setFaceDetector(CascadeClassifier faceDetector) {
		this.faceDetector = faceDetector;
	}
	public int getFrameCount() {
		return frameCount;
	}
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
	public Size getFaceLowerBound() {
		return faceLowerBound;
	}
	public void setFaceLowerBound(Size faceLowerBound) {
		this.faceLowerBound = faceLowerBound;
	}
	public Size getFaceUpperBound() {
		return faceUpperBound;
	}
	public void setFaceUpperBound(Size faceUpperBound) {
		this.faceUpperBound = faceUpperBound;
	}
	public MatOfRect getFaceMatrix() {
		return faceMatrix;
	}
	public void setFaceMatrix(MatOfRect faceMatrix) {
		this.faceMatrix = faceMatrix;
	}
	public Mat getGrayScaleMatrix() {
		return grayScaleMatrix;
	}
	public void setGrayScaleMatrix(Mat grayScaleMatrix) {
		this.grayScaleMatrix = grayScaleMatrix;
	}
	public Mat getBlurredKernelMatrix() {
		return blurredKernelMatrix;
	}
	public void setBlurredKernelMatrix(Mat blurredKernelMatrix) {
		this.blurredKernelMatrix = blurredKernelMatrix;
	}
	public Mat getResultMatrix() {
		return resultMatrix;
	}
	public void setResultMatrix(Mat resultMatrix) {
		this.resultMatrix = resultMatrix;
	}
	public Mat getResultFloat() {
		return resultFloat;
	}
	public void setResultFloat(Mat resultFloat) {
		this.resultFloat = resultFloat;
	}
	public Mat getFloatingMatrixFrame() {
		return floatingMatrixFrame;
	}
	public void setFloatingMatrixFrame(Mat floatingMatrixFrame) {
		this.floatingMatrixFrame = floatingMatrixFrame;
	}
	public Mat getLaplacianAuxillaryMatrix() {
		return laplacianAuxillaryMatrix;
	}
	public void setLaplacianAuxillaryMatrix(Mat laplacianAuxillaryMatrix) {
		this.laplacianAuxillaryMatrix = laplacianAuxillaryMatrix;
	}
	public Mat[] getMatrixlowHigh() {
		return matrixlowHigh;
	}
	public void setMatrixlowHigh(Mat[] matrixlowHigh) {
		this.matrixlowHigh = matrixlowHigh;
	}
	public Mat[] getMatrixlowLow() {
		return matrixlowLow;
	}
	public void setMatrixlowLow(Mat[] matrixlowLow) {
		this.matrixlowLow = matrixlowLow;
	}
	public Mat[] getMatrixlowHighAuxillary() {
		return matrixlowHighAuxillary;
	}
	public void setMatrixlowHighAuxillary(Mat[] matrixlowHighAuxillary) {
		this.matrixlowHighAuxillary = matrixlowHighAuxillary;
	}
	public Mat[] getMatrixlowLowAuxillary() {
		return matrixlowLowAuxillary;
	}
	public void setMatrixlowLowAuxillary(Mat[] matrixlowLowAuxillary) {
		this.matrixlowLowAuxillary = matrixlowLowAuxillary;
	}
	public Mat[] getPyramid() {
		return pyramid;
	}
	public void setPyramid(Mat[] pyramid) {
		this.pyramid = pyramid;
	}
	public double getInitialLambdaCutoff() {
		return initialLambdaCutoff;
	}
	public void setInitialLambdaCutoff(double initialLambdaCutoff) {
		this.initialLambdaCutoff = initialLambdaCutoff;
	}
	public double getDeltaCutoff() {
		return deltaCutoff;
	}
	public void setDeltaCutoff(double deltaCutoff) {
		this.deltaCutoff = deltaCutoff;
	}
	public int getAmplificationFactor() {
		return amplificationFactor;
	}
	public void setAmplificationFactor(int amplificationFactor) {
		this.amplificationFactor = amplificationFactor;
	}
	public static Scalar getBlue() {
		return BLUE;
	}
	public static Scalar getRed() {
		return RED;
	}
	public static Scalar getZero() {
		return ZERO;
	}

	private Size faceLowerBound;
    private Size faceUpperBound;
    private MatOfRect faceMatrix;
    private Mat grayScaleMatrix;
    private Mat blurredKernelMatrix;
    private Mat resultMatrix;
    private Mat resultFloat;
    private Mat floatingMatrixFrame;
    private Mat laplacianAuxillaryMatrix;
    private Mat[] matrixlowHigh;
    private Mat[] matrixlowLow;
    private Mat[] matrixlowHighAuxillary;
    private Mat[] matrixlowLowAuxillary;
    private Mat[] pyramid;
    private double initialLambdaCutoff;
    private double deltaCutoff;
    private int amplificationFactor;
    
    public MotionAmplification(String fileName)
	{
    	this.prop=new Properties();
		InputStream is=getClass().getClassLoader().getResourceAsStream(fileName);
		if(is!=null)
		{
			try{
			prop.load(is);
			}
			catch(Exception e)
			{
				throw new RuntimeException("Failed to load property file");
			}
		}
		this.faceDetector=Constants.faceDetector;
		if (faceDetector.empty()) {
            throw new RuntimeException("Failed to load face detection XML file");
        }
	}
    public void setFrame(int width, int height) {
        setInitialLambdaCutoff(Math.sqrt(width * width + height * height) / 3.0);
        setDeltaCutoff(Constants.CUTOFF / 8.0 / (1 + Scalar.all(Constants.ALPHA).val[0]));
        setAmplificationFactor(Constants.AMPLIFICATION_FACTOR);
        setFrameCount( 0);
        setFaceLowerBound(new Size(Constants.FACE_LOWER_BOUND * width, Constants.FACE_LOWER_BOUND * height));
        setFaceUpperBound(new Size());
        setFaceMatrix (new MatOfRect());
        setGrayScaleMatrix (new Mat());
        setBlurredKernelMatrix(new Mat());
        setResultMatrix(new Mat());
        setResultFloat (new Mat());
        setFloatingMatrixFrame (new Mat());
        setLaplacianAuxillaryMatrix (new Mat());
        setMatrixlowHigh (new Mat[Constants.PYRAMID_LEVEL+ 1]);
        setMatrixlowLow ( new Mat[Constants.PYRAMID_LEVEL + 1]);
        setMatrixlowHighAuxillary ( new Mat[Constants.PYRAMID_LEVEL + 1]);
        setMatrixlowLowAuxillary ( new Mat[Constants.PYRAMID_LEVEL + 1]);
        setPyramid (new Mat[Constants.PYRAMID_LEVEL + 1]);
        for (int i = 0; i < pyramid.length; i++) {
        	matrixlowHigh[i] = new Mat();
        	matrixlowLow[i] = new Mat();
        	matrixlowHighAuxillary[i] = new Mat();
        	matrixlowLowAuxillary[i] = new Mat();
        	pyramid[i] = new Mat();
        }
    }

    public Mat amplifyMotion(Mat frame) {
        Imgproc.cvtColor(frame, grayScaleMatrix, Imgproc.COLOR_RGB2GRAY);
        faceDetector.detectMultiScale(grayScaleMatrix, faceMatrix, 1.1, 2, 0, faceLowerBound, faceUpperBound);
        frame.convertTo(floatingMatrixFrame, CvType.CV_32F);
        Imgproc.cvtColor(floatingMatrixFrame, floatingMatrixFrame, Imgproc.COLOR_RGBA2RGB);
        pyramid=new LaplacianPyramids().buildLpyr(floatingMatrixFrame, pyramid, Constants.PYRAMID_LEVEL,laplacianAuxillaryMatrix);
        FrequencyFilter.frequencyFilter(frameCount, matrixlowHigh, matrixlowHighAuxillary, matrixlowLow, matrixlowLowAuxillary, pyramid, deltaCutoff, amplificationFactor, initialLambdaCutoff, frame, resultMatrix,floatingMatrixFrame);
        frameCount++;
        return resultMatrix;
    }

}

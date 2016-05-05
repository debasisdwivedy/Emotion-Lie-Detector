package Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import Utils.Constants;

public class ColorAmplification{
	
	public static ArrayList<Integer> BPM;
	public static final Scalar BLUE = new Scalar(255, 0, 0, 255);
    public static final Scalar RED = new Scalar(0, 0, 255, 255);
    public static final Scalar ZERO = Scalar.all(0);
	protected CascadeClassifier faceDetector;
	private Properties prop;
    private int frameCount;
    public static ArrayList<Integer> getBPM() {
		return BPM;
	}

	public static void setBPM(ArrayList<Integer> bPM) {
		BPM = bPM;
	}

	public CascadeClassifier getFaceDetector() {
		return faceDetector;
	}

	public void setFaceDetector(CascadeClassifier faceDetector) {
		this.faceDetector = faceDetector;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public Point getRegionOfInterest() {
		return regionOfInterest;
	}

	public void setRegionOfInterest(Point regionOfInterest) {
		this.regionOfInterest = regionOfInterest;
	}

	public Size getFaceLowerBound() {
		return faceLowerBound;
	}

	public void setFaceLowerBound(Size faceLowerBound) {
		this.faceLowerBound = faceLowerBound;
	}

	public static int getHighCutOff() {
		return HighCutOff;
	}

	public static void setHighCutOff(int highCutOff) {
		HighCutOff = highCutOff;
	}

	public static int getLowCutOff() {
		return lowCutOff;
	}

	public static void setLowCutOff(int lowCutOff) {
		ColorAmplification.lowCutOff = lowCutOff;
	}

	public static int getFPS() {
		return FPS;
	}

	public static void setFPS(int fPS) {
		FPS = fPS;
	}

	public static Scalar getALPHA() {
		return ALPHA;
	}

	public static void setALPHA(Scalar aLPHA) {
		ALPHA = aLPHA;
	}

	public static int getBLUR_LEVEL() {
		return BLUR_LEVEL;
	}

	public static void setBLUR_LEVEL(int bLUR_LEVEL) {
		BLUR_LEVEL = bLUR_LEVEL;
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

	public Mat getCutoffRectangle() {
		return cutoffRectangle;
	}

	public void setCutoffRectangle(Mat cutoffRectangle) {
		this.cutoffRectangle = cutoffRectangle;
	}

	public Mat getFilteredMatrix() {
		return filteredMatrix;
	}

	public void setFilteredMatrix(Mat filteredMatrix) {
		this.filteredMatrix = filteredMatrix;
	}

	public List<Mat> getColorChannel() {
		return colorChannel;
	}

	public void setColorChannel(List<Mat> colorChannel) {
		this.colorChannel = colorChannel;
	}

	public Mat getFourierTransformedChannel() {
		return fourierTransformedChannel;
	}

	public void setFourierTransformedChannel(Mat fourierTransformedChannel) {
		this.fourierTransformedChannel = fourierTransformedChannel;
	}

	public Mat getInverseFourierTransformedChannel() {
		return inverseFourierTransformedChannel;
	}

	public void setInverseFourierTransformedChannel(
			Mat inverseFourierTransformedChannel) {
		this.inverseFourierTransformedChannel = inverseFourierTransformedChannel;
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

	private Point regionOfInterest;
    private Size faceLowerBound;
    private static int HighCutOff;
    private static int lowCutOff ;
    private static int FPS;
    private static Scalar ALPHA;
    private static int BLUR_LEVEL;
    private Size faceUpperBound;
    private MatOfRect faceMatrix;
    private Mat grayScaleMatrix;
    private Mat blurredKernelMatrix;
    private Mat resultMatrix;
    private Mat resultFloat;
    private Mat floatingMatrixFrame;
    private Mat cutoffRectangle;
    private Mat filteredMatrix;
    private List<Mat> colorChannel;
    private Mat fourierTransformedChannel;
    private Mat inverseFourierTransformedChannel;
	public ColorAmplification(String fileName)
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
			BPM=new ArrayList<Integer>();
		}
		this.faceDetector=Constants.faceDetector;
		if (faceDetector.empty()) {
            throw new RuntimeException("Failed to load face detection XML file");
        }
	}

    public void setFrame(int width, int height) {
    	setFrameCount(0);
    	setRegionOfInterest(new Point());
    	setFPS(Integer.parseInt(prop.getProperty("FPS")));
        setFaceLowerBound(new Size(Constants.FACE_LOWER_BOUND * width, Constants.FACE_LOWER_BOUND * height));
        setLowCutOff( (int) (Constants.F_LOW/Constants.FPS*Constants.FPS + 1));
        setHighCutOff((int) (Constants.F_HIGH/Constants.FPS*Constants.FPS + 1)) ;
        setALPHA(Scalar.all(Constants.ALPHA));
        setBLUR_LEVEL(Constants.BLUR_LEVEL);
        setFaceUpperBound( new Size());
        setFaceMatrix(new MatOfRect());
        setGrayScaleMatrix(new Mat());
        setBlurredKernelMatrix( new Mat());
        setResultMatrix( new Mat());
        setResultFloat(new Mat());
        setFloatingMatrixFrame( new Mat());
        setCutoffRectangle( new Mat());
        setFilteredMatrix(new Mat());
        setColorChannel(new ArrayList<Mat>());
        setFourierTransformedChannel(new Mat());
        setInverseFourierTransformedChannel(new Mat());
    }

    public Mat amplifyColor(Mat frame) {
        Imgproc.cvtColor(frame, getGrayScaleMatrix(), Imgproc.COLOR_RGB2GRAY);
        faceDetector.detectMultiScale(getGrayScaleMatrix(), getFaceMatrix(), 1.1, 2, 0, getFaceLowerBound(), getFaceUpperBound());
        frame.convertTo(getFloatingMatrixFrame(), CvType.CV_32F);
        Imgproc.cvtColor(getFloatingMatrixFrame(), getFloatingMatrixFrame(), Imgproc.COLOR_RGBA2RGB);
        getFloatingMatrixFrame().copyTo(getBlurredKernelMatrix());
        for (int i = 0; i < getBLUR_LEVEL(); i++) {
            Imgproc.pyrDown(getBlurredKernelMatrix(), getBlurredKernelMatrix());
        }

        if (getFrameCount() < getFPS()) {
            frame.copyTo(resultMatrix);
            if (getCutoffRectangle().empty()) {
            	getCutoffRectangle().create(getBlurredKernelMatrix().width() * getBlurredKernelMatrix().height(), getFPS(), getBlurredKernelMatrix().type());
            }
            for (int y = 0; y < getBlurredKernelMatrix().rows(); y++) {
                for (int x = 0; x < getBlurredKernelMatrix().cols(); x++) {
                	getCutoffRectangle().put(y * getBlurredKernelMatrix().cols() + x, getFrameCount(), getBlurredKernelMatrix().get(y, x));
                }
            }
        } else {
        	getCutoffRectangle().colRange(1, getFPS()).copyTo(getCutoffRectangle().colRange(0, getFPS() - 1));
            for (int y = 0; y < getBlurredKernelMatrix().rows(); y++) {
                for (int x = 0; x < getBlurredKernelMatrix().cols(); x++) {
                	getCutoffRectangle().put(y * getBlurredKernelMatrix().cols() + x, getFrameCount(), getBlurredKernelMatrix().get(y, x));
                }
            }

            Core.split(getCutoffRectangle(), getColorChannel());
            for (Mat channel : getColorChannel()) {
                Core.dft(channel, getFourierTransformedChannel(), Core.DFT_ROWS, 0);
                getFourierTransformedChannel().colRange(0, getLowCutOff()).setTo(ZERO);
                getFourierTransformedChannel().colRange(getHighCutOff(), getFPS()).setTo(ZERO);
                Core.idft(getFourierTransformedChannel(), getInverseFourierTransformedChannel(), Core.DFT_ROWS + Core.DFT_SCALE, 0);
                getInverseFourierTransformedChannel().copyTo(channel);
            }
            Core.merge(getColorChannel(), getFilteredMatrix());
            for (int y = 0; y < getBlurredKernelMatrix().rows(); y++) {
                for (int x = 0; x < getBlurredKernelMatrix().cols(); x++) {
                	getBlurredKernelMatrix().put(y, x, getFilteredMatrix().get(y * getBlurredKernelMatrix().cols() + x, getFPS() - 1));
                }
            }
            Core.multiply(getBlurredKernelMatrix(), getALPHA(), getBlurredKernelMatrix());
            for (int i = 0; i < getBLUR_LEVEL(); i++) {
                Imgproc.pyrUp(getBlurredKernelMatrix(), getBlurredKernelMatrix());
            }
            Imgproc.resize(getBlurredKernelMatrix(), getResultFloat(), frame.size());
            Core.add(getFloatingMatrixFrame(), getResultFloat(), getResultFloat());
            Imgproc.cvtColor(getResultFloat(), getResultFloat(), Imgproc.COLOR_RGB2RGBA);
            getResultFloat().convertTo(getResultMatrix(), CvType.CV_8U);
        }
        for (Rect face : getFaceMatrix().toArray()) {
            Core.rectangle(getResultMatrix(), face.tl(), face.br(), BLUE, 4);
            getRegionOfInterest().x = face.tl().x + face.size().width * 0.5;
            getRegionOfInterest().y = face.tl().y + face.size().width * 0.2;
            int value =(int)getResultMatrix().get((int) getRegionOfInterest().y, (int) getRegionOfInterest().x)[2];
            Core.circle(getResultMatrix(), getRegionOfInterest(), 4, RED, 8);
            getRegionOfInterest().x += 10;
            Core.putText(getResultMatrix(), String.valueOf(value/2), getRegionOfInterest(), Core.FONT_ITALIC, 2, RED);
            regionOfInterest.x -= 10;
            BPM.add(value/2);
        }
        frameCount++;

        return getResultMatrix();
    }


}

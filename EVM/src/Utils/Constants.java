package Utils;
import java.io.InputStream;
import java.util.Properties;

import org.opencv.objdetect.CascadeClassifier;

public class Constants {
	public static CascadeClassifier faceDetector;
	public static Properties prop;	
	public static final String fileName="Resources/Config.properties";
	public static double FACE_LOWER_BOUND;
	public static int FPS;
	public static int BLUR_LEVEL;
	public static double F_LOW;
	public static double F_HIGH;
	public static int ALPHA;
	public static int CUTOFF;
	public static int PYRAMID_LEVEL;
	public static int AMPLIFICATION_FACTOR;
	static {
	    	prop=new Properties();
			InputStream is=Constants.class.getClassLoader().getResourceAsStream(fileName);
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
			String faceDetectorConfigFile=prop.getProperty("FACE_DETECTOR_XML");
			faceDetector=new CascadeClassifier(faceDetectorConfigFile);
			if (faceDetector.empty()) {
	            throw new RuntimeException("Failed to load cascade classifier");
	        }
			FACE_LOWER_BOUND=Double.parseDouble(prop.getProperty("FACE_LOWER_BOUND"));
			FPS=Integer.parseInt(prop.getProperty("FPS"));
			BLUR_LEVEL=Integer.parseInt(prop.getProperty("BLUR_LEVEL"));
			F_LOW=Double.parseDouble(prop.getProperty("F_LOW"));
			F_HIGH=Double.parseDouble(prop.getProperty("F_HIGH"));
			ALPHA=Integer.parseInt(prop.getProperty("ALPHA"));
			CUTOFF=Integer.parseInt(prop.getProperty("CUTOFF"));
			PYRAMID_LEVEL=Integer.parseInt(prop.getProperty("PYRAMID_LEVEL"));
			AMPLIFICATION_FACTOR=Integer.parseInt(prop.getProperty("AMPLIFICATION_FACTOR"));
	}

}

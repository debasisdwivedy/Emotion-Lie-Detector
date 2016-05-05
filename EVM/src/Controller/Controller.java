package Controller;

import org.opencv.core.Core;
import Utils.VideoFrame;

public class Controller {
	
	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
    	String mediaFilename="";
    	String flag="";
    	if (args.length == 0) {
			System.out.println("Please pass the required arguments");
			System.exit(0);
		} else {
			mediaFilename = args[0];
			flag=args[1];
		}
    	new VideoFrame(mediaFilename,flag);
    }

}

package Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import Model.ColorAmplification;
import Model.MotionAmplification;
import View.View;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.ToolFactory;

public class VideoFrame {
	private String mediaFilename;
	
	public VideoFrame(String mediaFilename,String operation)
	{
		this.mediaFilename=mediaFilename;
		if("pulse".equalsIgnoreCase(operation))
    	{
    	  IMediaReader video = ToolFactory.makeReader(mediaFilename);
    	  video.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
    	  video.addListener(new View(new ColorAmplification("Resources/Config.properties")));
    	  video.addListener(ToolFactory.makeViewer(IMediaViewer.Mode.FAST_VIDEO_ONLY, false, JFrame.EXIT_ON_CLOSE));
          while (video.readPacket() == null) {}
          ArrayList<Integer> frequecyComponent=ColorAmplification.BPM;
          int temp=0;
          for(int i=0;i<frequecyComponent.size();i++)
          {
        	  temp=temp+frequecyComponent.get(i);
          }
          System.out.println("BPM is :"+temp/frequecyComponent.size());
          System.exit(0);
    	}
    	else
    	{
    	  IMediaReader video = ToolFactory.makeReader(mediaFilename);
    	  video.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
    	  video.addListener(new View(new MotionAmplification("Resources/Config.properties")));
    	  video.addListener(ToolFactory.makeViewer(IMediaViewer.Mode.FAST_VIDEO_ONLY, false, JFrame.EXIT_ON_CLOSE));
          while (video.readPacket() == null) {}
          System.exit(0);
    	}
		
	}

}

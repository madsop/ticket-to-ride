package ttr.gui;

import ttr.data.Konstantar;
import ttr.utgaave.GameVersion;

import javax.swing.*;

import com.google.inject.Inject;

import java.awt.*;
import java.net.URL;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -2629995315390856995L;
	private final Image bildet;

	@Inject
	public ImagePanel(GameVersion spel){
		URL bakgrunnsbildeURL = spel.getBakgrunnsbildet();
		bildet = new ImageIcon(bakgrunnsbildeURL).getImage();
	}

	@Override
	public void paintComponent(Graphics g) {
		int imageHeight = Konstantar.VINDUSSTORLEIK.height * 2/7;
		int imageWidth = computeImageWidth(imageHeight);
		this.setPreferredSize(new Dimension(imageWidth + 50, imageHeight + 50));
		try {
			g.drawImage(bildet, 0, 25, imageWidth, imageHeight, null);
		}
		catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	private int computeImageWidth(int imageHeight) {
		double bilderatio = (double) bildet.getHeight(null) / (double) bildet.getWidth(null);
		return (int) ((imageHeight / bilderatio) * 1.3);
	}
}

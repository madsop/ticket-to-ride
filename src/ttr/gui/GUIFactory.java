package ttr.gui;

import com.google.inject.Inject;

import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;

public class GUIFactory {
	private Meldingspanel messagePanel;
	private Hogrepanelet right;

	@Inject
    public GUIFactory(Meldingspanel messagePanel, Hogrepanelet right) {
		this.messagePanel = messagePanel;
		this.right = right;
	}
	
	public GUI createGUI(ImagePanel imagePanel) {
		return new GUI(imagePanel, messagePanel, right);
	}
}

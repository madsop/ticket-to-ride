package ttr.gui;

import ttr.data.MeldingarModell;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Core;
import ttr.oppdrag.MissionHandler;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel {
	private static final long serialVersionUID = -1540067881602979318L;
	private final Meldingspanel messagePanel;
    private final Hogrepanelet right;

    public GUI(ImagePanel imagePanel, Meldingspanel messagePanel, Hogrepanelet right) {
        this.messagePanel = messagePanel;
        this.right = right;

        GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gridBagConstraints = setUpGridBagConstraints();
		gridBagConstraints.gridy = 0;

		gridBagConstraints.gridx = 0;
		add(imagePanel,gridBagConstraints);

		gridBagConstraints.gridx = 1;
        right.byggHogrepanelet();
		add(right,gridBagConstraints);

		gridBagConstraints.gridx = 2;
		add(messagePanel,gridBagConstraints);
	}

	private GridBagConstraints setUpGridBagConstraints() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		return gridBagConstraints;
	}

    public void setHovud(MissionHandler missionHandler, Core core){ //TODO liker ikkje denne heller
        right.addListeners(missionHandler, core);
        core.addPropertyChangeListener(right);
    }

    public MeldingarModell getMessagesModel(){ //TODO få bort denne?
        return messagePanel.getMeldingarModell();
    }

	public void showWhoseTurnItIs(String myPlayerName, String whoseTurnText) {
        right.setPlayerName("Eg er " + myPlayerName + ", og det er " + whoseTurnText + ".");
	}	

    public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog(this, "Det er ikkje noko kort der, ser du vel.");
		right.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
	}

	public void setRemainingTrains(int position, int numberOfTrains) { right.setRemainingTrains(position, numberOfTrains); }
	public void displayGraphicallyThatItIsMyTurn() { right.displayGraphicallyThatItIsMyTurn();	}

	//TODO det er vel denne som gir syklar
	public void receiveMessage(String message) { messagePanel.getMeldingarModell().newRemoteMessage(message);}

	public void addChatListener(String myPlayerName, ArrayList<IPlayer> players) { messagePanel.addChatListener(myPlayerName, players); }
}
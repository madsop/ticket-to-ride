package ttr.gui;

import ttr.data.Colour;
import ttr.data.MeldingarModell;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel {
	private static final long serialVersionUID = -1540067881602979318L;
	private final Meldingspanel messagePanel;
    private final Hogrepanelet right;
    private final MissionChooser missionChooser;

    public GUI(ImagePanel imagePanel, MissionChooser missionChooser, Meldingspanel messagePanel, Hogrepanelet right){
        this.missionChooser = missionChooser;
        this.messagePanel = messagePanel;
        this.right = right;

        GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gridBagConstraints = setUpGridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
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
        messagePanel.prepareChat(core.findPlayerInAction(), core.getSpelarar());
        right.addListeners(missionHandler, core, this);
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

    public void drawCardsOnTable(int plass, Colour farge){ right.teiknOppKortPåBordet(plass, farge); }
    public ArrayList<Mission> chooseMissions(ArrayList<Mission> missions) { return missionChooser.setUpMissionChooser(missions); }

	public void setRemainingTrains(int position, int numberOfTrains) { right.setRemainingTrains(position, numberOfTrains); }
	public void displayGraphicallyThatItIsMyTurn() { right.displayGraphicallyThatItIsMyTurn();	}

	public void receiveMessage(String message) { messagePanel.getMeldingarModell().nyMelding(message);}

}
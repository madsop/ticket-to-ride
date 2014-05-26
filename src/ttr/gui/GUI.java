package ttr.gui;

import ttr.data.Colour;
import ttr.data.MeldingarModell;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel {
	private static final long serialVersionUID = -1540067881602979318L;
	private final Meldingspanel messagePanel;
    private Hogrepanelet right;
    private final MissionChooser missionChooser;


    public GUI(ImagePanel imagePanel, MissionChooser missionChooser, Meldingspanel messagePanel, Hogrepanelet right){
        this.missionChooser = missionChooser;
        this.messagePanel = messagePanel;
        this.right = right;

        GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		add((JPanel) imagePanel,gridBagConstraints);

		gridBagConstraints.gridx = 1;
        right.byggHogrepanelet();
		add(right,gridBagConstraints);

		gridBagConstraints.gridx = 2;
		add(messagePanel,gridBagConstraints);
	}

    public void setHovud(Core core){
        messagePanel.prepareChat(core);
        right.addListeners(core, this);
    }

    public MeldingarModell getMessagesModel(){ //TODO få bort denne?
        return messagePanel.getMeldingarModell();
    }

	public void showWhoseTurnItIs(String myPlayerName, String whoseTurnText) {
        right.getSpelarnamn().setText("Eg er " + myPlayerName + ", og det er " + whoseTurnText + ".");
	}

    public JTextField getPlayerNameJTextField() { return right.getSpelarnamn(); }
    public JLabel[] getRemainingTrainsLabel() { return right.getTogAtt(); }
    public void drawCardsOnTable(int plass, Colour farge){ right.teiknOppKortPåBordet(plass, farge); }
    public JButton[] getCardButtons(){ return right.getKortButtons(); }

    public ArrayList<Mission> chooseMissions(ArrayList<Mission> missions) { return missionChooser.setUpMissionChooser(missions); }

    public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog(this, "Det er ikkje noko kort der, ser du vel.");
		getCardButtons()[positionOnTable].setBackground(Color.GRAY); //TODO dette e rjo litt ekkelt
		getCardButtons()[positionOnTable].setText("Tom");
	}
}
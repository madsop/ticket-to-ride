package ttr.gui;

import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.gui.hogresida.IHogrepanelet;
import ttr.gui.hogresida.IMeldingspanel;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel implements IGUI {
	private static final long serialVersionUID = -1540067881602979318L;
	private final IMeldingspanel messagePanel;
    private IHogrepanelet right;
    private final MissionChooser missionChooser;


    public GUI(ImagePanel imagePanel, MissionChooser missionChooser, IMeldingspanel messagePanel, IHogrepanelet right){
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
		add((JPanel) right,gridBagConstraints);

		gridBagConstraints.gridx = 2;
		add((JPanel) messagePanel,gridBagConstraints);
	}

    public void setHovud(Core core){
        messagePanel.prepareChat(core);
        right.addListeners(core, this);
    }

    public IMeldingarModell getMessagesModel(){ //TODO få bort denne?
        return messagePanel.getMeldingarModell();
    }

	public void showWhoseTurnItIs(String myPlayerName, String whoseTurnText) {
        right.getSpelarnamn().setText("Eg er " + myPlayerName + ", og det er " + whoseTurnText + ".");
	}

    public JTextField getPlayerNameJTextField() { return right.getSpelarnamn(); }
    public JLabel[] getRemainingTrainsLabel() { return right.getTogAtt(); }
    public void drawCardsOnTable(int plass, Farge farge){ right.teiknOppKortPåBordet(plass, farge); }
    public JButton[] getCardButtons(){ return right.getKortButtons(); }

    public ArrayList<Mission> chooseMissions(ArrayList<Mission> missions) { return missionChooser.setUpMissionChooser(missions); }

    public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog(this, "Det er ikkje noko kort der, ser du vel.");
		getCardButtons()[positionOnTable].setBackground(Color.GRAY); //TODO dette e rjo litt ekkelt
		getCardButtons()[positionOnTable].setText("Tom");
	}
}
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
    private IHogrepanelet hogre;
    private final MissionChooser oppdragsveljar;


    public GUI(IBildePanel bp, MissionChooser oppdragsveljar, IMeldingspanel meldingspanel, IHogrepanelet hogre){
        this.oppdragsveljar = oppdragsveljar;
        this.messagePanel = meldingspanel;
        this.hogre = hogre;

        GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		add((JPanel) bp,c);

		c.gridx = 1;
        hogre.byggHogrepanelet();
		add((JPanel) hogre,c);

		c.gridx = 2;
		add((JPanel) messagePanel,c);
	}

    public void setHovud(Core hovud){
        messagePanel.prepareChat(hovud);
        hogre.addListeners(hovud);
    }

    public IMeldingarModell getMeldingarModell(){
        return messagePanel.getMeldingarModell();
    }

	public void visKvenDetErSinTur(String sinTurNo, boolean nett, String minSpelar) {
		if (nett){
            hogre.getSpelarnamn().setText("Eg er " + minSpelar + ", og det er " + (minSpelar.equals(sinTurNo) ? "min tur." : sinTurNo + " sin tur."));
        }
		else{
            hogre.getSpelarnamn().setText("Eg er " + sinTurNo + ", og det er min tur.");
        }
	}

    public JTextField getSpelarnamn() { return hogre.getSpelarnamn(); }
    public JLabel[] getTogAtt() { return hogre.getTogAtt(); }
    public void teiknOppKortPåBordet(int plass, Farge farge){ hogre.teiknOppKortPåBordet(plass, farge); }
    public JButton[] getKortButtons(){ return hogre.getKortButtons(); }


    public void createJFrame(String tittel, JPanel panel) {
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setBackground(Color.DARK_GRAY);
		frame.setTitle(tittel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    public String showInputDialog(String string) { return JOptionPane.showInputDialog(this, string); }

    public ArrayList<Mission> velOppdrag(ArrayList<Mission> oppdrag) { return oppdragsveljar.setUpOppdragsveljar(oppdrag); }

    public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog(this, "Det er ikkje noko kort der, ser du vel.");
		getKortButtons()[positionOnTable].setBackground(Color.GRAY);
		getKortButtons()[positionOnTable].setText("Tom");
	}
}
package ttr.gui;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.data.MeldingarModell;
import ttr.kjerna.IHovud;
import ttr.struktur.IOppdrag;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel implements IGUI {

    // Faktisk GUI
	private final JFrame frame;

    // Hovud
	private IHovud hovud;

    // Interne klassar
    private final Meldingspanel meldingsboks;
    private Hogrepanelet hogre;
    private final Oppdragsveljar oppdragsveljar;


	public GUI(JFrame frame, ISpelUtgaave spel, boolean nettv) {
        this.frame = frame;

        GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;

		BildePanel bp = new BildePanel(spel);
        
        oppdragsveljar = new Oppdragsveljar(spel,frame);

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(bp,c);

		c.gridx = 1;
        byggHogrepanel();
		add(hogre,c);

		c.gridx = 2;
        meldingsboks = new Meldingspanel(nettv);
		add(meldingsboks,c);

		frame.setPreferredSize(new Dimension(Konstantar.BREIDDE, Konstantar.HOGDE));
	}

    public void setHovud(IHovud hovud){
        this.hovud = hovud;
        meldingsboks.setHovud(hovud);
        hogre.addListeners(hovud);
    }

	/** Sett opp panelet på høgre side av skjermen (altså GUI-et) */
    void byggHogrepanel() {
		hogre = new Hogrepanelet(this,frame);
        hogre.byggHogrepanelet();
	}

    public MeldingarModell getMeldingarModell(){
        return meldingsboks.getMeldingarModell();
    }

	public void visKvenDetErSinTur(String sinTurNo, boolean nett, String minSpelar) {
		if (hovud==null){hogre.getSpelarnamn().setText(""); return;}

		if (nett){
            String tekst = "Eg er " + minSpelar + ", og det er ";
            tekst += minSpelar.equals(sinTurNo) ? "min tur." : sinTurNo+ " sin tur.";
            hogre.getSpelarnamn().setText(tekst);
        }
		else{
            hogre.getSpelarnamn().setText("Eg er " + sinTurNo + ", og det er min tur.");
        }
	}

    public JTextField getSpelarnamn() { return hogre.getSpelarnamn(); }
    public JLabel[] getTogAtt() { return hogre.getTogAtt(); }
    public void teiknOppKortPåBordet(int plass, Farge farge){ hogre.teiknOppKortPåBordet(plass, farge); }
    public JButton[] getKortButtons(){ return hogre.getKortButtons(); }


	/**
	 * Lager ei ramme (ein JFrame)
	 * @param tittel - kva tittelen på ramma skal vera
	 * @param panel - eit JPanel med alt som skal vises fram 
	 */
    public void lagRamme(String tittel, JPanel panel) {
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setBackground(Color.DARK_GRAY);
		frame.setTitle(tittel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    public String showInputDialog(String string) { return JOptionPane.showInputDialog(this, string); }

    public ArrayList<IOppdrag> velOppdrag(ArrayList<IOppdrag> oppdrag) { return oppdragsveljar.velOppdrag(oppdrag); }

}
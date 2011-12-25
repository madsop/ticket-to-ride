package ttr.gui;

import ttr.Main;
import ttr.bord.Bord;
import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.data.MeldingarModell;
import ttr.kjerna.Hovud;
import ttr.kjerna.IHovud;
import ttr.kjerna.Oppdragshandsamar;
import ttr.nettverk.InitialiserNettverk;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GUI extends JPanel implements  IGUI {

    // Faktisk GUI
	private final JFrame frame;
    private JButton ok;
    private ArrayList<JCheckBox> jcb;
    private JDialog jd;

    // Hovud
	private IHovud hovud;
	public IHovud getHovud() {
		return hovud;
	}

    // Interne klassar
    private Meldingspanel meldingsboks;
    private Hogrepanelet hogre;


	private final ISpelUtgaave spel;
    private ArrayList<IOppdrag> valde;
	private ArrayList<IOppdrag> oppdragEinKanVeljeNyeOppdragFrå;

	/**
	 * Opprettar eit GUI-objekt
	 * @param frame - ramma GUI-et lages inni
	 * @throws RemoteException 
	 */
	public GUI(JFrame frame, String hostAddress, ISpelUtgaave spel) throws RemoteException {

		this.frame = frame;
        GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;


		this.spel = spel;
        BildePanel bp = new BildePanel(spel);

        int nett = JOptionPane.showConfirmDialog(this, "Vil du spela eit nettverksspel?");
        boolean nettv;

        nettv = nett == JOptionPane.YES_OPTION;

        byggMeldingsboks(nettv);
        IBord bord = new Bord(this,nettv);
        hovud = new Hovud(this,bord,nettv,spel);            // TODO
        meldingsboks.setHovud(hovud);

		byggHogrepanel();

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(bp,c);

		c.gridx = 1;
		add(hogre,c);

		c.gridx = 2;
		add(meldingsboks,c);


		settIGangSpelet(nett,hostAddress);

		frame.setPreferredSize(new Dimension(Konstantar.BREIDDE, Konstantar.HOGDE));

		/*if ( (hovud.isNett() && hovud.getSpelarar().size()+1 < Konstantar.MAKS_ANTAL_SPELARAR) || (!hovud.isNett() && hovud.getSpelarar().size() < Konstantar.MAKS_ANTAL_SPELARAR)){
			togAtt[togAtt.length-1].setText("");
		}*/
	}
    
    private void settIGangSpelet(int nett, String hostAddress) throws RemoteException {
        if (nett == JOptionPane.YES_OPTION) {
            InitialiserNettverk nettverk = new InitialiserNettverk(this, hostAddress,hovud);
            nettverk.initialiserSpel(); // InitialiserNettverk
            Oppdragshandsamar.trekkOppdrag(this, hovud.getMinSpelar(), true);

            for (ISpelar s : hovud.getSpelarar()){
                for (IOppdrag o : s.getOppdrag()){
                    s.trekt(o.getOppdragsid());
                }
            }

            Main.getFrame().setTitle(Main.getFrame().getTitle() +" - " +hovud.getMinSpelar());
        }
        else {
            for (ISpelar s : hovud.getSpelarar()) {
                Oppdragshandsamar.trekkOppdrag(this, s, true);
            }
            // ??
        }
    }

	/**
	 * Sett opp panelet på høgre side av skjermen (altså GUI-et)
	 */
	private void byggHogrepanel() {
		hogre = new Hogrepanelet(hovud,this,frame);
        hogre.byggHogrepanelet();
	}

	private void byggMeldingsboks(boolean nett){
		meldingsboks = new Meldingspanel(nett);
        meldingsboks.createModell();

	}

    public MeldingarModell getMeldingarModell(){
        return meldingsboks.getMeldingarModell();
    }

	/**
	 * Viser namnet til den spelaren det er sin tur
	 * @param spelarnamn
	 */
	public void setSpelarnamn(String spelarnamn) {
		if (hovud==null){hogre.getSpelarnamn().setText(""); return;}


		if (hovud.isNett()){
			try {
                String tekst = "Eg er " + hovud.getMinSpelar().getNamn() + ", og det er ";
				if (hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())){
                    tekst += "min tur.";
				}
				else{
                    tekst += spelarnamn + " sin tur.";
				}
                hogre.getSpelarnamn().setText(tekst);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
                hogre.getSpelarnamn().setText("Eg er " + hovud.getKvenSinTur().getNamn() + ", og det er min tur.");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

    public JTextField getSpelarnamn() {
        return hogre.getSpelarnamn();
    }                       
    public JLabel[] getTogAtt() {
        return hogre.getTogAtt();
    }
    public void setKortPaaBordet(int plass,Farge farge){
        hogre.teiknOppKortPåBordet(plass, farge);
    }
    public JButton[] getKortButtons(){
        return hogre.getKortButtons();
    }

	/**
	 * Lar spelaren fjerne oppdrag. Funkar iallfall nesten.
	 * @param oppdrag
	 * @return dei valde oppdraga.
	 */
    public ArrayList<IOppdrag> velOppdrag(ArrayList<IOppdrag> oppd) {
		this.oppdragEinKanVeljeNyeOppdragFrå = oppd;
        JPanel vel = new JPanel();
		GridLayout gl = new GridLayout(0, 2);

		vel.setLayout(gl);
		//		vel.setPreferredSize(new Dimension(600,500));

		ArrayList<JTextField> jtf = new ArrayList<JTextField>();
		jcb = new ArrayList<JCheckBox>();

		for (int i = 0; i < oppdragEinKanVeljeNyeOppdragFrå.size(); i++) {
			IOppdrag o = oppdragEinKanVeljeNyeOppdragFrå.get(i);
			Object[] d = o.getDestinasjonar().toArray();
			jtf.add(new JTextField(d[0] +" - " +d[1] + " (" +o.getVerdi() +")"));
			jcb.add(new JCheckBox());
			jcb.get(i).addActionListener(new okListener());
			jcb.get(i).setSelected(false);
			vel.add(jtf.get(i));
			vel.add(jcb.get(i));
		}

		valde = new ArrayList<IOppdrag>();
		ok = new JButton("OK");
		ok.addActionListener(new okListener());
		vel.add(ok);
		ok.setEnabled(false);

		URL c = spel.getBakgrunnsbildet();
		ImageIcon b = new ImageIcon(c);

		JLabel a = new JLabel(b);
		JScrollPane jsp = new JScrollPane();
		jsp.setPreferredSize(new Dimension(b.getIconWidth()+50,b.getIconHeight()-200));
		jsp.getViewport().add(a);

		JPanel heile = new JPanel();
		heile.add(vel);
		heile.add(jsp);

		jd = new JDialog(frame,"Vel oppdragEinKanVeljeNyeOppdragFrå",true);
		jd.setContentPane(heile);
		jd.pack();
		jd.setVisible(true);

		return valde;
	}

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

    @Override
    public String showInputDialog(String string) {
        return JOptionPane.showInputDialog(this, string);
    }


    public void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException{
		String melding;
		if (kort) {
			melding = hovud.getKvenSinTur().getNamn() +" trakk inn " +f;
		}
		else {
			melding = hovud.getKvenSinTur().getNamn() +" trakk oppdragEinKanVeljeNyeOppdragFrå.";
		}
		if (hovud.isNett()){
			hovud.getMinSpelar().faaMelding(melding);
		}

		for (ISpelar s : hovud.getSpelarar()){
			if (hovud.isNett() || hovud.getKvenSinTur()==s){
				if (!tilfeldig){
					s.faaMelding(melding);
				}
				else if(kort && tilfeldig){
					s.faaMelding(hovud.getKvenSinTur().getNamn() +" trakk tilfeldig");
				}
			}
		}
	}



	public void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException{
		if (vert.getNamn().equals(hovud.getMinSpelar().getNamn())){
			for (ISpelar s : hovud.getSpelarar()){
				// metode for å legge kortet vert nettopp trakk på plass i på bordet hos spelar s
				s.setPaaBordet(nyFarge,i);
			}
		}
		else {
			hovud.getMinSpelar().setPaaBordet(nyFarge, i);
			for (ISpelar s : hovud.getSpelarar()){
				if (!vert.getNamn().equals(s.getNamn())){
					s.setPaaBordet(nyFarge, i);
				}
			}
		}
	}

	/**
	 * Blir kalla når spelaren har vald oppdragEinKanVeljeNyeOppdragFrå, og (freistar å/) trykker ok.
	 * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
	 * @author mads
	 *
	 */
	private class okListener implements ActionListener {
		public void gjer(int i, ActionEvent arg0){
			if (arg0.getSource() == jcb.get(i)) {
				if (valde.contains(oppdragEinKanVeljeNyeOppdragFrå.get(i))){
					valde.remove(oppdragEinKanVeljeNyeOppdragFrå.get(i));
					jcb.get(i).setSelected(false);
				}
				else{
					valde.add(oppdragEinKanVeljeNyeOppdragFrå.get(i));
					jcb.get(i).setSelected(true);
					ok.setEnabled(true);
				}
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == ok) {
				if (valde.size() >= oppdragEinKanVeljeNyeOppdragFrå.size()-2) {
					jd.dispose();
					return;
				}
			}

			for (int i = 0; i < oppdragEinKanVeljeNyeOppdragFrå.size(); i++) {
				gjer(i,arg0);
			}

			int count = 0;
			for (int i = 0; i < jcb.size(); i++) {
				if (valde.contains(oppdragEinKanVeljeNyeOppdragFrå.get(i))) {
					count++;
				}
			}
			if (count < oppdragEinKanVeljeNyeOppdragFrå.size() - 2) {
				ok.setEnabled(false);
			}
		}
	}

}
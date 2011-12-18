package ttr.gui;

import ttr.Hovud;
import ttr.IHovud;
import ttr.Main;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.nettverk.InitialiserNettverk;
import ttr.spelar.ISpelar;
import ttr.struktur.Oppdrag;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GUI extends JPanel implements  IGUI {

	// Oppsettet rundt
	private final JFrame frame;
	private IHovud hovud;
	public IHovud getHovud() {
		return hovud;
	}
    public JFrame getFrame(){
        return frame;
    }

    private final BildePanel bp;
	private Meldingspanel meldingsboks;
    private Hogrepanelet hogre;

    // Oppdraga
	private JButton ok;
	private int oppdragstr;


	private final ISpelUtgaave spel;

    private ArrayList<Oppdrag> valde;
	private ArrayList<JCheckBox> jcb;
	private JDialog jd;
	private ArrayList<Oppdrag> oppdrag;

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
		bp = new BildePanel();

        int nett = JOptionPane.showConfirmDialog(this, "Vil du spela eit nettverksspel?");
        boolean nettv;

        nettv = nett == JOptionPane.YES_OPTION;

        byggMeldingsboks(nettv);
        hovud = new Hovud(this, nettv,spel);
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


		if (nett == JOptionPane.YES_OPTION) {
            InitialiserNettverk nettverk = new InitialiserNettverk(this, hostAddress);
			nettverk.initialiserSpel(); // InitialiserNettverk
			trekkOppdrag(hovud.getMinSpelar(),true);

			for (ISpelar s : hovud.getSpelarar()){
				for (Oppdrag o : s.getOppdrag()){
					s.trekt(o.getOppdragsid());
				}
			}

			Main.getFrame().setTitle(Main.getFrame().getTitle() +" - " +hovud.getMinSpelar());
		}
		else {
			for (ISpelar s : hovud.getSpelarar()) {
				trekkOppdrag(s,true);
			}
			// ??
		}

		frame.setPreferredSize(new Dimension(Konstantar.BREIDDE, Konstantar.HOGDE));

		/*if ( (hovud.isNett() && hovud.getSpelarar().size()+1 < Konstantar.MAKS_ANTAL_SPELARAR) || (!hovud.isNett() && hovud.getSpelarar().size() < Konstantar.MAKS_ANTAL_SPELARAR)){
			togAtt[togAtt.length-1].setText("");
		}*/
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
				if (hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())){
                    hogre.getSpelarnamn().setText("Eg er " + hovud.getMinSpelar().getNamn() + ", og det er min tur.");
				}
				else{
                    hogre.getSpelarnamn().setText("Eg er " + hovud.getMinSpelar().getNamn() + ", og det er " + spelarnamn + " sin tur.");
				}
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
        hogre.setKortPaaBordet(plass,farge);
    }
    public JButton[] getKortButtons(){
        return hogre.getKortButtons();
    }

	/**
	 * Lar spelaren fjerne oppdrag. Funkar iallfall nesten.
	 * @param oppdrag
	 * @return dei valde oppdraga.
	 */
    private ArrayList<Oppdrag> velOppdrag(ArrayList<Oppdrag> oppd) {
		this.oppdrag = oppd;
		oppdragstr = oppdrag.size() - 2;
        JPanel vel = new JPanel();
		GridLayout gl = new GridLayout(0, 2);

		vel.setLayout(gl);
		//		vel.setPreferredSize(new Dimension(600,500));

		ArrayList<JTextField> jtf = new ArrayList<JTextField>();
		jcb = new ArrayList<JCheckBox>();

		for (int i = 0; i < oppdrag.size(); i++) {
			Oppdrag o = oppdrag.get(i);
			Object[] d = o.getDestinasjonar().toArray();
			jtf.add(new JTextField(d[0] +" - " +d[1] + " (" +o.getVerdi() +")"));
			jcb.add(new JCheckBox());
			jcb.get(i).addActionListener(new okListener());
			jcb.get(i).setSelected(false);
			vel.add(jtf.get(i));
			vel.add(jcb.get(i));
		}

		valde = new ArrayList<Oppdrag>();
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

		jd = new JDialog(frame,"Vel oppdrag",true);
		jd.setContentPane(heile);
		jd.pack();
		jd.setVisible(true);

		return valde;
	}

    @SuppressWarnings("serial")
	private class BildePanel extends JPanel {
		//		ImageIcon a = new ImageIcon("/home/mads/Dropbox/Programmering/Eclipse-workspace/TTR/src/ttr/nordic/nordic_map.jpg");
		//		Image b = a.getImage();
        final URL c = spel.getBakgrunnsbildet();
		final Image b = new ImageIcon(c).getImage();

		@Override
		public void paintComponent(Graphics g) {
            double bilderatio = (double) b.getHeight(null) / (double) b.getWidth(null);
            /*
         Teiknar bakgrunnsbildet.
         */
            int bildehogde = Konstantar.HOGDE;
            int br = (int) ((bildehogde / bilderatio) * 1.3);
			try {
				g.drawImage(b, 0, 25, br, bildehogde, null);
				bp.setPreferredSize(new Dimension(br +50, bildehogde +50));
			}
			catch (NullPointerException npe) {
				npe.printStackTrace();
			}	
		}
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
			melding = hovud.getKvenSinTur().getNamn() +" trakk oppdrag."; 
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
	 * 
	 * @param s - kva for spelar som skal få oppdraga
	 * @param start - er det i byrjinga av spelet? (dvs. fem eller tre oppdrag)
	 * @param talPaaOppdrag
	 */
    public void trekkOppdrag(ISpelar s, boolean start) throws RemoteException{
		int talPaaOppdrag;
		if (start){
			talPaaOppdrag = Konstantar.ANTAL_STARTOPPDRAG;
		}
		else {
			talPaaOppdrag = Konstantar.ANTAL_VELJEOPPDRAG;
		}
		ArrayList<Oppdrag> oppdrag = new ArrayList<Oppdrag>();
		for (int i = 0; i < talPaaOppdrag; i++) {
				Oppdrag opp = s.trekkOppdragskort();
				oppdrag.add(opp);
		}
		ArrayList<Oppdrag> k = new ArrayList<Oppdrag>();
		while (k.size() < talPaaOppdrag-2){
			k = velOppdrag(oppdrag);
    	}
		oppdrag = k;
        for (Oppdrag anOppdrag : oppdrag) {
                s.faaOppdrag(anOppdrag);
        }

	}


	/**
	 * Blir kalla når spelaren har vald oppdrag, og (freistar å/) trykker ok.
	 * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
	 * @author mads
	 *
	 */
	private class okListener implements ActionListener {
		public void gjer(int i, ActionEvent arg0){
			if (arg0.getSource() == jcb.get(i)) {
				if (valde.contains(oppdrag.get(i))){
					valde.remove(oppdrag.get(i));
					jcb.get(i).setSelected(false);
				}
				else{
					valde.add(oppdrag.get(i));
					jcb.get(i).setSelected(true);
					ok.setEnabled(true);
				}
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == ok) {
				if (valde.size() >= oppdragstr) {
					jd.dispose();
					return;
				}
			}
			for (int i = 0; i < oppdragstr+2; i++) {
				gjer(i,arg0);
			}

			int count = 0;
			for (int i = 0; i < jcb.size(); i++) {
				if (valde.contains(oppdrag.get(i))) {
					count++;
				}
			}
			if (count < oppdragstr) {
				ok.setEnabled(false);
			}
		}
	}

}
package ttr.gui;

import ttr.*;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.nettverk.InitialiserNettverk;
import ttr.spelar.ISpelar;
import ttr.struktur.Oppdrag;
import ttr.struktur.Rute;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

//import java.rmi.Naming;
//import java.rmi.registry.LocateRegistry;


public class GUI extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	// Oppsettet rundt
	private final JFrame frame;
	private Hovud hovud;
	public Hovud getHovud() {
		return hovud;
	}

    private final BildePanel bp;
	private JPanel hogre, meldingsboks;

    // Oppdraga
	private JButton ok;
	private int oppdragstr;

	// Brukargrensesnittet - på høgresida
	private JTextField spelarnamn;
	public JTextField getSpelarnamn() {
		return spelarnamn;
	}

	private JButton trekkOppdrag, bygg, visBygde, visMineKort, visMineOppdrag;
	private JButton[] kortButtons;
	private JButton kortBunke;
	private JLabel[] togAtt;
	private JList<MeldingarModell> meldingar;
    private MeldingarModell meldingarmodell;
	private JTextField chat;
	private static final String starttekst = "Prat her!";
	private final ISpelUtgaave spel;

	public JButton[] getKortButtons(){
		return kortButtons;
	}

	public JLabel[] getTogAtt() {
		return togAtt;
	}

	private final gjerNokoListener gNL;

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

		gNL = new gjerNokoListener();

		this.spel = spel;
		bp = new BildePanel();

		byggHogrepanel();
		byggMeldingsboks();

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(bp,c);

		c.gridx = 1;
		add(hogre,c);

		c.gridx = 2;
		add(meldingsboks,c);

		int nett = JOptionPane.showConfirmDialog(this, "Vil du spela eit nettverksspel?"); 
		boolean nettv;

        nettv = nett == JOptionPane.YES_OPTION;

		hovud = new Hovud(this, nettv,spel);

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
		hogre = new JPanel();
        GridBagLayout hogregbl = new GridBagLayout();
		hogre.setLayout(hogregbl);
		GridBagConstraints d;
		d = new GridBagConstraints();
		d.ipadx = 20;
		d.ipady = 0;
		d.insets = Konstantar.INSETS;

		hogregbl.setConstraints(hogre, d);
		d.anchor = GridBagConstraints.WEST;

		hogre.setPreferredSize(new Dimension(Konstantar.hogrebreidde,Konstantar.HOGDE));
		hogre.setBackground(Color.GRAY);	

		spelarnamn = new JTextField("Denne spelaren sin tur");
		spelarnamn.setEditable(false);
		spelarnamn.setMinimumSize(Konstantar.SPELARNAMNDIM);

		hogre.setPreferredSize(new Dimension(Konstantar.hogrebreidde,Konstantar.HOGDE));


		d.gridwidth = 3;
		d.gridx = 0;
		d.gridy = 0;
		hogre.add(spelarnamn,d);

		d.gridwidth = 1;
		d.gridy = 1;
		trekkOppdrag = new JButton("Trekk oppdrag");
		trekkOppdrag.setMinimumSize(Konstantar.KNAPP);
		trekkOppdrag.addActionListener(gNL);
		hogre.add(trekkOppdrag,d);

		d.gridx = 1;
		bygg = new JButton("Bygg ei togrute");
		bygg.setMinimumSize(Konstantar.KNAPP);
		bygg.addActionListener(gNL);
		hogre.add(bygg,d);

		d.gridy = 2;
		d.gridx = 0;
		visBygde = new JButton("Vis bygde ruter");
		visBygde.setMinimumSize(Konstantar.KNAPP);
		visBygde.addActionListener(gNL);
		hogre.add(visBygde,d);

		d.gridy = 3;
		d.gridx = 0;
		visMineOppdrag = new JButton("Vis mine oppdrag");
		visMineOppdrag.setMinimumSize(Konstantar.KNAPP);
		visMineOppdrag.addActionListener(gNL);
		hogre.add(visMineOppdrag,d);

		d.gridx = 1;
		visMineKort = new JButton("Vis mine kort");
		visMineKort.setMinimumSize(Konstantar.KNAPP);
		visMineKort.addActionListener(gNL);
		hogre.add(visMineKort,d);

		togAtt = new JLabel[4];

		d.gridy = 4;
		d.gridx = 0;
		togAtt[0] = new JLabel("Tog att:");
		togAtt[0].setFont(Konstantar.TOGTALFONT);
		hogre.add(togAtt[0],d);

		d.gridy = 5;
		togAtt[1] = new JLabel("40");
		togAtt[1].setForeground(Color.WHITE);
		togAtt[1].setFont(Konstantar.TOGTALFONT);
		hogre.add(togAtt[1],d);

		d.gridx = 1;
		togAtt[2] = new JLabel("40");
		togAtt[2].setForeground(Color.BLACK);
		togAtt[2].setFont(Konstantar.TOGTALFONT);
		hogre.add(togAtt[2],d);

		d.gridy = 6;
		d.gridx = 0;
		togAtt[3] = new JLabel("40");
		togAtt[3].setForeground(Color.PINK);
		togAtt[3].setFont(Konstantar.TOGTALFONT);
		hogre.add(togAtt[3],d);

		visFargekorta(d);
	}

	private void byggMeldingsboks(){
		meldingsboks = new JPanel();

		meldingsboks.setBackground(Color.WHITE);	

		//meldingsboks.setPreferredSize(new Dimension(150,Konstantar.HOGDE));

		meldingarmodell = new MeldingarModell();
		meldingarmodell.addPropertyChangeListener(this);
        //noinspection unchecked,unchecked
        meldingar = new JList(meldingarmodell);


        JScrollPane mp = new JScrollPane();
		mp.setPreferredSize(new Dimension(Konstantar.MELDINGSPANELBREIDDE, Konstantar.HOGDE - Konstantar.DIFF));
		mp.getViewport().add(meldingar);
		meldingsboks.add(mp);

		meldingsboks.setPreferredSize(new Dimension(Konstantar.MELDINGSPANEL,Konstantar.HOGDE));

		chat = new JTextField(starttekst);
		chat.addKeyListener(new ChatListener());
		chat.setPreferredSize(Konstantar.CHATDIM);
		meldingsboks.add(chat);

		meldingarmodell.nyMelding("Spelet startar. Velkommen!");

	}

	/**
	 * Teiknar fargekorta, gir dei fargar, legg til listeners osv
	 * @param d
	 */
	private void visFargekorta(GridBagConstraints d) {

		// Fargekorta på bordet
		d.gridy = 7;
		kortBunke = new JButton("Tilfeldig");
		kortBunke.setMinimumSize(Konstantar.KORTKNAPP);
		kortBunke.addActionListener(gNL);
		hogre.add(kortBunke,d);

		kortButtons = new JButton[5];

		int tel = 0;
		d.gridx = 1;
		kortButtons[tel]= new JButton("kort 1");
		kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
		kortButtons[tel].setBackground(Color.BLACK);
		kortButtons[tel].addActionListener(gNL);
		hogre.add(kortButtons[tel],d);

		tel++;
		d.gridy++;
		d.gridx = 0;
		kortButtons[tel]= new JButton("kort 2");
		kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
		kortButtons[tel].addActionListener(gNL);
		hogre.add(kortButtons[tel],d);

		tel++;
		d.gridx = 1;
		kortButtons[tel] = new JButton("kort 3");
		kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
		kortButtons[tel].addActionListener(gNL);
		hogre.add(kortButtons[tel],d);

		tel++;
		d.gridy++;
		d.gridx = 0;
		kortButtons[tel] = new JButton("kort 4");
		kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
		kortButtons[tel].addActionListener(gNL);
		hogre.add(kortButtons[tel],d);

		tel++;
		d.gridx = 1;
		kortButtons[tel] = new JButton("kort 5");
		kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
		kortButtons[tel].addActionListener(gNL);
		hogre.add(kortButtons[tel],d);
	}

	public MeldingarModell getMeldingarModell(){
		return meldingarmodell;
	}

	/**
	 * Viser namnet til den spelaren det er sin tur
	 * @param spelarnamn
	 */
	public void setSpelarnamn(String spelarnamn) {
		if (hovud==null){this.spelarnamn.setText(""); return;}
		if (hovud.isNett()){
			try {
				if (hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())){
					this.spelarnamn.setText("Eg er " +hovud.getMinSpelar().getNamn() +", og det er min tur.");
				}
				else{
					this.spelarnamn.setText("Eg er " +hovud.getMinSpelar().getNamn() +", og det er " +spelarnamn +" sin tur.");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				this.spelarnamn.setText("Eg er " +hovud.getKvenSinTur().getNamn() +", og det er min tur.");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tilegnar GUI-et ei hovudklasse
	 * @param hovud
	 */
	public void setHovud(Hovud hovud) {
		this.hovud = hovud;
	}

	/**
	 * Teiknar opp eit kort på bordet
	 * @param plass - kva for ein av plassane på bordet
	 * @param farge - i kva farge
	 */
	public void setKortPaaBordet(int plass, Farge farge) {
		kortButtons[plass].setForeground(Color.BLACK);
		kortButtons[plass].setBackground(Konstantar.fargeTilColor(farge));
		String ret;
		switch (farge) {
		case blå:
			ret = "Blå";
			break;
		case gul:
			ret =  "Gul";
			break;
		case raud:
			ret = "Raud";
			break;
		case grønn:
			ret = "Grønn";
			break;
		case kvit:
			ret = "Kvit";
			break;
		case lilla:
			ret = "Lilla";
			break;
		case oransje:
			ret = "Oransje";
			break;
		case svart:
			ret = "Svart";
			kortButtons[plass].setForeground(Color.WHITE);
			break;
		case valfri:
			ret = "Joker";
			break;
		default:
			ret = "Ops";
			break;
		}
		kortButtons[plass].setText(ret);
	}

	/**
	 * Lar spelaren fjerne oppdrag. Funkar iallfall nesten.
	 * @param oppdrag
	 * @return dei valde oppdraga.
	 */
	public ArrayList<Oppdrag> velOppdrag(ArrayList<Oppdrag> oppd) {
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
	class BildePanel extends JPanel {
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
    void lagRamme(String tittel, JPanel panel) {
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setBackground(Color.DARK_GRAY);
		frame.setTitle(tittel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException{
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

	/**
	 * Lager ein knapp for å trekkje inn kort
	 * @param i plassen på bordet
	 * @throws RemoteException 
	 */
    void kortButton(int i) throws RemoteException {
		Farge f = hovud.getBord().getPaaBordet()[i];
		if (hovud.getKvenSinTur().getValdAllereie()) {
			if (f == Farge.valfri) {
				JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
				return;
			}
			else if (f==null){return;}
			else {
				hovud.getKvenSinTur().faaKort(f);
				sendKortMelding(true,false,f);		
				hovud.getBord().getTilfeldigKortFråBordet(i, true);
				hovud.nesteSpelar();
			}
		}
		else {
			if (f==null){return;}
			hovud.getKvenSinTur().faaKort(f);
			sendKortMelding(true,false,f);
			hovud.getBord().getTilfeldigKortFråBordet(i, true);
			if (f == Farge.valfri) {
				hovud.nesteSpelar();
			}
			hovud.getKvenSinTur().setEinVald(true);
		}
		ISpelar vert = null;
		if (hovud.isNett()){
			if (hovud.getMinSpelar().getSpelarNummer()==0) {
				vert = hovud.getMinSpelar();
			}
		}
		for (ISpelar s : hovud.getSpelarar()) {
			if (!hovud.isNett()){
				s.getTilfeldigKortFråBordet(i);
			}
			else {
				if (s.getSpelarNummer()==0) {
					vert = s;
				}
			}
		}
		if (hovud.isNett() && vert!=null){
			Farge nyFarge = vert.getTilfeldigKortFråBordet(i);
			while (vert.sjekkJokrar()) {
				vert.leggUtFem();
				int[] paaSomInt = vert.getPaaBordetInt();

				for (int plass = 0; plass < hovud.getBord().getPaaBordet().length; plass++){
					nyFarge = Konstantar.FARGAR[paaSomInt[plass]];
					hovud.getMinSpelar().setPaaBordet(nyFarge,plass);
					nyPaaPlass(vert, nyFarge, plass);
				}
			}
			nyPaaPlass(vert, nyFarge, i);
		}
	}		

	void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException{
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
    void trekkOppdrag(ISpelar s, boolean start) throws RemoteException{
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
	 * Blir kalla av JButton-ane på høgrepanelet. Er eigentleg koplinga mellom GUI og kjerne.
	 * @author mads
	 *
	 */
    private class gjerNokoListener implements ActionListener {
		@SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent arg0) {

			/*			try {
				System.out.println("minSpelar: " +hovud.getMinSpelar().getNamn() +", kvenSinTur: " +hovud.getKvenSinTur().getNamn());
				System.out.println(hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn()));
			} catch (RemoteException e2) {
				e2.printStackTrace();
			}
			 */			try {
				 if (hovud.isNett() && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
					 if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
						 JOptionPane.showMessageDialog(Hovud.getGui(), "Det er ikkje din tur!");
						 return;
					 }
				 }
			 } catch (HeadlessException e2) {
				 e2.printStackTrace();
			 } catch (RemoteException e2) {
				 e2.printStackTrace();
			 }

			 if (arg0.getSource() == trekkOppdrag) { 
				 try {
					 sendKortMelding(false, false, null);
				 } 
				 catch (RemoteException e) {
					 e.printStackTrace();
				 }
				 if (hovud.isNett()){
					 try {
						 trekkOppdrag(hovud.getMinSpelar(),false);
					 } catch (RemoteException e) {
						 e.printStackTrace();
					 }
				 }
				 else { 
					 try {
						 trekkOppdrag(hovud.getKvenSinTur(),false);
					 } catch (RemoteException e) {
						 e.printStackTrace();
					 }
				 }
				 try {
					 hovud.nesteSpelar();
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
			 else if (arg0.getSource() == bygg) {
				 Rute[] ruterArray = null;
				 try {
					 ruterArray = hovud.finnFramRuter();
				 } catch (RemoteException e1) {
					 e1.printStackTrace();
				 }

				 Rute ubygdeRuter = (Rute) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
						 JOptionPane.QUESTION_MESSAGE,null,ruterArray,ruterArray[1]);

				 Rute bygd = null;
                 for (Rute aRuterArray : ruterArray) {
                     if (aRuterArray == ubygdeRuter) {
                         bygd = aRuterArray;
                     }
                 }

				 if (bygd!=null) {
					 int[] spelarensKort = null;
					 try {
						 spelarensKort = hovud.getKvenSinTur().getKort();
					 } catch (RemoteException e) {
						 e.printStackTrace();
					 }
					 int kortKrevd = bygd.getLengde()-bygd.getAntaljokrar();
					 Farge ruteFarge = bygd.getFarge();
					 int krevdJokrar = bygd.getAntaljokrar();

					 int plass = Konstantar.finnPosisjonForFarg(ruteFarge);

					 int harjokrar = spelarensKort[spelarensKort.length-1];
					 if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
						 if (bygd.isTunnel()) {
							 try {
								 hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
							 }
							 catch (RemoteException re){
								 re.printStackTrace();
							 }
						 }
						 else {
							 try {
								 hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
							 }
							 catch (RemoteException re){
								 re.printStackTrace();
							 }
						 }
					 }
					 else if (krevdJokrar <= harjokrar && (kortKrevd <= ( (harjokrar-krevdJokrar) + spelarensKort[plass]) ) ){
						 try {
							 if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+krevdJokrar) {
								 if (bygd.isTunnel()) {
									 hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
								 }
								 else {
									 hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
								 }
							 }
							 else {
								 JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
							 }
						 } catch (HeadlessException e) {
							 e.printStackTrace();
						 } catch (RemoteException e) {
							 e.printStackTrace();
						 }
					 }
					 else {
						 JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
					 }
				 }
			 }
			 else if (arg0.getSource() == visMineKort) {
				 // vis korta mine
				 JPanel korta = new JPanel();

				 ISpelar visSine;
				 if (hovud.isNett()) {
					 visSine = hovud.getMinSpelar();
				 }
				 else {
					 visSine = hovud.getKvenSinTur();
				 }
				 String[] kort = new String[Konstantar.ANTAL_FARGAR];

				 for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
					 try {
						 kort[i] = Konstantar.FARGAR[i] +": " +visSine.getKort()[i];
					 } catch (RemoteException e) {
						 kort[i] = "";
						 e.printStackTrace();
					 }
				 }
				 JLabel[] oppdr = new JLabel[kort.length];

				 try {
					 korta.add(new JLabel(visSine.getNamn()));
				 } catch (RemoteException e1) {
					 e1.printStackTrace();
				 }

				 for (int i = 0; i < oppdr.length; i++) {
					 oppdr[i] = new JLabel();
					 oppdr[i].setText(kort[i]);
					 try {
						 if (visSine.getKort()[i] != 0) {
							 oppdr[i].setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
						 }
						 else {
							 oppdr[i].setForeground(Color.LIGHT_GRAY);
						 }
					 } catch (RemoteException e) {
						 e.printStackTrace();
					 }
					 korta.add(oppdr[i]);
				 }
				 try {
					 lagRamme("Viser korta til " +visSine.getNamn(), korta);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }

			 else if (arg0.getSource() == visMineOppdrag) {
				 // vis oppdraga mine
				 JPanel oppdraga = new JPanel();

				 ISpelar visSine;
				 if (hovud.isNett()) {
					 visSine = hovud.getMinSpelar();
				 }
				 else {
					 visSine = hovud.getKvenSinTur();
				 }
				 String oppdrg = "";
				 try {
					 oppdrg = visSine.getNamn() +": ";
				 } catch (RemoteException e1) {
					 e1.printStackTrace();
				 }

				 try {
					 for (int i = 0; i < visSine.getAntalOppdrag(); i++) {
						 Oppdrag o = visSine.getOppdrag().get(i);
						 oppdrg += o;
						 if (visSine.erOppdragFerdig(o.getOppdragsid())){
							 oppdrg += " (OK)";
						 }
						 if (i == visSine.getAntalOppdrag()-1){
							 oppdrg += ".";
						 }
						 else{
							 oppdrg += ", ";
						 }
					 }
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
				 JLabel oppdr = new JLabel(oppdrg);
				 oppdraga.add(oppdr);
				 try {
					 lagRamme("Viser oppdraga til " +visSine.getNamn(), oppdraga);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }

			 else if (arg0.getSource() == visBygde) {
				 JPanel bygde = new JPanel();

				 if (hovud.getAlleBygdeRuter().size()>0) {
					 @SuppressWarnings("unchecked") JList<Rute> bygd = new JList(hovud.getAlleBygdeRuter().toArray());

					 /*	if (hovud.isNett()){
						 // Finn spel-verten
						 for (int i = 0; i < bygd.getModel().getSize(); i++){
							 for (int j = 0; j < hovud.getSpelarar().size()+1; j++)
							 try {
								if (hovud.getAlleBygdeRuter().get(i).getBygdAv().getSpelarNummer()==j){
									bygd.set
								 }
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						 }
					 }
					  */

					 bygde.add(bygd);
					 lagRamme("Desse rutene er bygd",bygde);
				 }
				 else {
					 JOptionPane.showMessageDialog(frame, "Det er ikkje bygd noka rute enno. Bli den første!");
				 }
			 }

			 else if (arg0.getSource() == kortBunke) {
				 Farge f = null;
				 try {
					 f = hovud.getKvenSinTur().trekkFargekort();
				 } catch (RemoteException e1) {
					 e1.printStackTrace();
				 }
				 JPanel p = new JPanel();
				 JLabel woho = new JLabel("Du trakk eit kort av farge " +f);
				 try {
					 if (f==null){return;}
					 hovud.getKvenSinTur().faaKort(f);
					 sendKortMelding(true,true,f);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
				 p.add(woho);
				 //				 lagRamme("Du trakk inn kort", p);
				 try {
					 if (hovud.getKvenSinTur().getValdAllereie()) {
						 try {
							 hovud.nesteSpelar();
						 } catch (RemoteException e) {
							 e.printStackTrace();
						 }
					 }
					 else {
						 hovud.getKvenSinTur().setEinVald(true);
					 }
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }

			 else if (arg0.getSource() == kortButtons[0]) {
				 try {
					 kortButton(0);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
			 else if (arg0.getSource() == kortButtons[1]) {
				 try {
					 kortButton(1);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
			 else if (arg0.getSource() == kortButtons[2]) {
				 try {
					 kortButton(2);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
			 else if (arg0.getSource() == kortButtons[3]) {
				 try {
					 kortButton(3);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
			 else if (arg0.getSource() == kortButtons[4]) {
				 try {
					 kortButton(4);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
			 }
		}
	}

	/**
	 * Blir kalla når spelaren har vald oppdrag, og (freistar å/) trykker ok.
	 * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
	 * @author mads
	 *
	 */
	class okListener implements ActionListener {
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
	private class ChatListener implements KeyListener{
		public void keyPressed(KeyEvent arg0) {	}

		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
				String melding = "";
				if (hovud.isNett()){
					try {
						melding = hovud.getMinSpelar().getNamn();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						melding = hovud.getKvenSinTur().getNamn();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				melding += ": " +chat.getText();
				if (hovud.isNett()){
					meldingarmodell.nyMelding(melding);
				}

				for (ISpelar s : hovud.getSpelarar()){
					try {
						s.faaMelding(melding);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				chat.setText("");
			}
			else if (chat.getText().contains(starttekst)){
				chat.setText(String.valueOf(arg0.getKeyChar()));
			}
		}
		public void keyTyped(KeyEvent arg0) {}
	}


	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(MeldingarModell.MELDINGAR_PROPERTY)){
			meldingarmodell = new MeldingarModell(meldingarmodell.getMeldingar());
			meldingarmodell.addPropertyChangeListener(this);
            //noinspection unchecked
            meldingar.setModel(meldingarmodell);
			meldingar.setSelectedIndex(meldingarmodell.getSize()-1);
			meldingar.ensureIndexIsVisible(meldingar.getSelectedIndex());
		}
	}
}
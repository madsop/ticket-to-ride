package backup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.rmi.Naming;
//import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;

	// Oppsettet rundt
	private JFrame frame;
	private Hovud hovud;
	public Hovud getHovud() {
		return hovud;
	}

	private GridBagLayout gbl, hogregbl;
	private BildePanel bp;
	private JPanel hogre;
	private double bilderatio;

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
	
	public JButton[] getKortButtons(){
		return kortButtons;
	}

	public JLabel[] getTogAtt() {
		return togAtt;
	}

	private gjerNokoListener gNL;

	private Nettverk nettverk;

	// Vel oppdrag-variablane
	private JPanel vel;
	private ArrayList<Oppdrag> valde;
	private ArrayList<JCheckBox> jcb;
	private JDialog jd;
	private ArrayList<Oppdrag> oppdrag;

	/**
	 * Opprettar eit GUI-objekt
	 * @param frame - ramma GUI-et lages inni
	 * @throws RemoteException 
	 */
	public GUI(JFrame frame, String hostAddress, SpelUtgaave spel) throws RemoteException {

		this.frame = frame;
		gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		setPreferredSize(new Dimension(Konstantar.BREIDDE,Konstantar.HOGDE));

		gNL = new gjerNokoListener();

		bp = new BildePanel();
		bp.setPreferredSize(new Dimension(Konstantar.BREIDDE-Konstantar.hogrebreidde, Konstantar.HOGDE));

		byggHogrepanel();

		c.ipadx = 0;
		c.ipady = 0;
		add(bp,c);

		c.ipadx = 1;
		c.ipady = 0;
		add(hogre,c);

		int nett = JOptionPane.showConfirmDialog(this, "Vil du spela eit nettverksspel?"); 
		boolean nettv;		

		if (nett == JOptionPane.YES_OPTION) {
			nettv = true;
		}
		else {
			nettv = false;
		}

		hovud = new Hovud(this, nettv,spel);

		if (nett == JOptionPane.YES_OPTION) {
			nettverk = new Nettverk(this, hostAddress);
			nettverk.initialiserSpel(); // Nettverk
			trekkOppdrag(hovud.getMinSpelar(),true,Konstantar.ANTAL_STARTOPPDRAG);
			Main.getFrame().setTitle(Main.getFrame().getTitle() +" - " +hovud.getMinSpelar());
		}
		else {
			for (Spelar s : hovud.getSpelarar()) {
				trekkOppdrag(s,true,Konstantar.ANTAL_STARTOPPDRAG);
			}
			// ??
		}

		repaint();

	}



	/**
	 * Sett opp panelet på høgre side av skjermen (altså GUI-et)
	 */
	private void byggHogrepanel() {
		hogre = new JPanel();
		hogregbl = new GridBagLayout();
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
		togAtt[3].setForeground(Color.BLUE);
		togAtt[3].setFont(Konstantar.TOGTALFONT);
		hogre.add(togAtt[3],d);

		visFargekorta(d);
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

	/**
	 * Viser namnet til den spelaren det er sin tur
	 * @param spelarnamn
	 */
	public void setSpelarnamn(String spelarnamn) {
		this.spelarnamn.setText(spelarnamn);
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
			ret = "Rosa";
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
		//	Thread t = new Thread();
		oppdragstr = oppdrag.size() - 2;
		vel = new JPanel();

		ArrayList<JTextField> jtf = new ArrayList<JTextField>();
		jcb = new ArrayList<JCheckBox>();

		for (int i = 0; i < oppdrag.size(); i++) {
			Oppdrag o = oppdrag.get(i);
			Set<Destinasjon> d = o.getDestinasjonar();
			Object[] d1;
			d1 = d.toArray();
			jtf.add(new JTextField(d1[0] +" - " +d1[1]));
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
		jd = new JDialog(frame,"Vel oppdrag",true);
		jd.setContentPane(vel);
		jd.pack();
		jd.setVisible(true);

		return valde;
	}

	/**
	 * Teiknar bakgrunnsbildet.
	 * @author mads
	 *
	 */
	@SuppressWarnings("serial")
	class BildePanel extends JPanel {
		ImageIcon a = new ImageIcon("/home/mads/Dropbox/Programmering/Eclipse-workspace/TTR/src/ttr/nordic/nordic_map.jpg");
		Image b = a.getImage();

		public void paintComponent(Graphics g) {
			bilderatio = (double)b.getHeight(null) / (double)b.getWidth(null);
			int bildehogde = Konstantar.HOGDE;
			int br = (int) ((bildehogde / bilderatio)*1.3);
			try {
				//g.drawImage(b, 0, 0, br, bildehogde, null);
				System.out.println(bp.getHeight());
				System.out.println(bp.getWidth());
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

	/**
	 * Lager ein knapp for å trekkje inn kort
	 * @param i plassen på bordet
	 * @throws RemoteException 
	 */
	public void kortButton(int i) throws RemoteException {
		Farge f = hovud.getBord().getPaaBordet()[i];
		if (hovud.getKvenSinTur().getValdAllereie()) {
			if (f == Farge.valfri) {
				JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
				return;
			}
			else {
				hovud.getKvenSinTur().faaKort(f);
				hovud.getBord().getTilfeldigKortFråBordet(i, true);
				hovud.nesteSpelar();
			}
		}
		else {
			hovud.getKvenSinTur().faaKort(f);
			hovud.getBord().getTilfeldigKortFråBordet(i, true);
			if (f == Farge.valfri) {
				hovud.nesteSpelar();
			}
			else {
				hovud.getKvenSinTur().setEinVald(true);
			}
		}
		for (Spelar s : hovud.getSpelarar()) {
			s.getTilfeldigKortFråBordet(i, true);
		}
	}

	public void trekkOppdrag(Spelar s, boolean start, int talPaaOppdrag) {
		ArrayList<Oppdrag> oppdrag = new ArrayList<Oppdrag>();
		for (int i = 0; i < talPaaOppdrag; i++) {
			try {
				Oppdrag o = s.trekkOppdragskort();
				oppdrag.add(o);

			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		oppdrag = velOppdrag(oppdrag);
		for (int i = 0; i < oppdrag.size(); i++) {
			try {
				s.faaOppdrag(oppdrag.get(i));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Blir kalla av JButton-ane på høgrepanelet. Er eigentleg koplinga mellom GUI og kjerne.
	 * @author mads
	 *
	 */
	class gjerNokoListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			/*			try {
				System.out.println("minSpelar: " +hovud.getMinSpelar().getNamn() +", kvenSinTur: " +hovud.getKvenSinTur().getNamn());
				System.out.println(hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn()));
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
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
				 trekkOppdrag(hovud.getKvenSinTur(),false,3);
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
				 for (int i = 0; i < ruterArray.length; i++) {
					 if (ruterArray[i] == ubygdeRuter) {
						 bygd = ruterArray[i];
					 }
				 }

				 if (bygd!=null) {
					 int[] spelarensKort = null;
					 try {
						 spelarensKort = hovud.getKvenSinTur().getKort();
					 } catch (RemoteException e) {
						 e.printStackTrace();
					 }
					 int kortKrevd = bygd.getLengde();
					 Farge ruteFarge = bygd.getFarge();
					 int krevdJokrar = bygd.getAntaljokrar();

					 int plass = -1;
					 for (int i = 0; i < Konstantar.FARGAR.length; i++) {
						 if (ruteFarge == Konstantar.FARGAR[i]) {
							 plass = i;
						 }
					 }

					 if (krevdJokrar <= spelarensKort[spelarensKort.length-1] && kortKrevd <= spelarensKort[plass]) {
						 try {
							 if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd) {
								 if (bygd.isTunnel()) {
									 hovud.byggTunnel(bygd, plass, spelarensKort, kortKrevd, krevdJokrar);
								 }
								 else {
									 hovud.bygg(bygd, plass, spelarensKort, kortKrevd, krevdJokrar);
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

				 Spelar visSine = null;
				 if (hovud.isNett()) {
					 visSine = hovud.getMinSpelar();
				 }
				 else {
					 visSine = hovud.getKvenSinTur();
				 }
				 String[] kort = new String[Konstantar.FARGAR.length];

				 for (int i = 0; i < Konstantar.FARGAR.length; i++) {
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

				 Spelar visSine = null;
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
						 oppdrg += ", " +o;
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
					 JList bygd = new JList(hovud.getAlleBygdeRuter().toArray());
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
					 hovud.getKvenSinTur().faaKort(f);
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }
				 p.add(woho);
				 lagRamme("Du trakk inn kort", p);
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
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == ok) {
				if (valde.size() >= oppdragstr) {
					jd.dispose();
					return;
				}
			}
			for (int i = 0; i < oppdragstr+2; i++) {
				if (arg0.getSource() == jcb.get(i)) {
					if (!jcb.get(i).isSelected()) {
						if (!valde.contains(oppdrag.get(i))) {
							jcb.get(i).setSelected(true);
							valde.add(oppdrag.get(i));
							ok.setEnabled(true);
						}
					}
					else {
						if(valde.contains(oppdrag.get(i))) {
							jcb.get(i).setSelected(false);
							valde.remove(oppdrag.get(i));
						}
					}
				}
			}

			int count = 0;
			for (int i = 0; i < jcb.size(); i++) {
				if (jcb.get(i).isSelected() == true) {
					count++;
				}
			}
			if (count < oppdragstr) {
				ok.setEnabled(false);
			}
		}
	}

}
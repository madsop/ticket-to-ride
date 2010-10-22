package ttr.copy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 * @author mads
 *
 */
public class SpelarImpl extends UnicastRemoteObject implements Spelar {
	private static final long serialVersionUID = 1L;
	private Hovud hovud;

	private static int spelarteljar = 0;
	private int spelarNummer;
	private String namn;

	private int[] kort;
	private ArrayList<Oppdrag> oppdrag;
	private ArrayList<Rute> bygdeRuter; // Delvis unaudsynt pga. bygdRuteMatrise
	private boolean[][] bygdRuteMatrise;
	
	private boolean einValdAllereie = false;
	
	/*
	 * Get & set herifrå
	 */
	
	public void setEinVald(boolean b) throws RemoteException {
		einValdAllereie = b;
	}
	public int getSpelarNummer() throws RemoteException {
		return spelarNummer;
	}
	public boolean getValdAllereie() throws RemoteException {
		return einValdAllereie;
	}
	public Hovud getHovud() throws RemoteException{
		return hovud;
	}
	
	// Ad hoc-metodar
	public ArrayList<Rute> getBygdeRuter() throws RemoteException  {
		return bygdeRuter;
	}
	public int getAntalOppdrag()  throws RemoteException {
		return oppdrag.size();
	}
	public void setTogAtt(int plass, int tog) throws RemoteException {
		Hovud.getGui().getTogAtt()[plass].setText(String.valueOf(tog));
	}
	public void setSpelarNummer(int nummer) throws RemoteException {
		spelarNummer = nummer; 
	}
	public int getSpelarteljar() throws RemoteException {
		return spelarteljar;
	}
	public void setSpelarteljar(int teljar) throws RemoteException {
		spelarteljar = teljar;
	}
	public int getBygdeRuterStr() throws RemoteException {
		return bygdeRuter.size();
	}
	public int getBygdeRuterId(int j) throws RemoteException {
		return bygdeRuter.get(j).getRuteId();
	}

	
	/**
	 * Konstruktør. Lagar ein ny spelar og koplar han til eit hovud.
	 * @param hovud
	 * @param namn
	 * @throws RemoteException
	 */
	public SpelarImpl (Hovud hovud, String namn) throws RemoteException{
		super();
		this.hovud = hovud;
		this.namn = namn;
		kort = new int[Konstantar.ANTAL_FARGAR];
		oppdrag = new ArrayList<Oppdrag>();
		bygdeRuter = new ArrayList<Rute>();
		bygdRuteMatrise = new boolean[Konstantar.ANTAL_DESTINASJONAR][Konstantar.ANTAL_DESTINASJONAR];
		initialiserMatrise();
		
		for (int i = 0; i < Konstantar.ANTAL_STARTKORT; i++) {
			kort[i] = 0;
		}
		
		// Gir spelaren fargekort i byrjinga
		for (int i = 0; i < Konstantar.ANTAL_STARTKORT; i++) {
			Farge trekt = trekkFargekort();
			int plass = -1;
			for (int j = 0; j < Konstantar.FARGAR.length; j++) {
				if (trekt == Konstantar.FARGAR[j]) {
					plass = j;
				}
			}
			if (plass >= 0) {
				kort[plass]++;
			}
		}
	}
	
	public void faaOppdrag(Oppdrag oppdrag)  throws RemoteException {
		if (!this.oppdrag.contains(oppdrag)) {
			this.oppdrag.add(oppdrag);
		}
	}
	public void faaKort(Farge farge) throws RemoteException  {
		int tel = 0;
		for (int i = 0; i < Konstantar.FARGAR.length; i++) {
			if (farge == Konstantar.FARGAR[i]) {
				tel = i;
			}
		}
		kort[tel]++;
	}
	
	public ArrayList<Oppdrag> getOppdrag() throws RemoteException  {
		return oppdrag;
	}
	
	public int[] getKort() throws RemoteException  {
		return kort;
	}
	
	/**
	 * 
	 */
	public ArrayList<Oppdrag> velOppdrag(ArrayList<Oppdrag> oppdrag) throws RemoteException  {
		ArrayList<Oppdrag> valde = new ArrayList<Oppdrag>();
		Hovud.getGui().velOppdrag(oppdrag);
		return valde;
	}

	/**
	 * Oppretter ei #destinasjonar*#destinasjonar med alle verdiar false.
	 */
	private void initialiserMatrise() {
		for (int y = 0; y < Konstantar.ANTAL_DESTINASJONAR; y++) {
			for (int x = 0; x < Konstantar.ANTAL_DESTINASJONAR; x++) {
				bygdRuteMatrise[y][x] = false;
			}
		}
	}
	
	/**
	 * @return kva heiter spelaren?
	 */
	public String getNamn()  throws RemoteException {
		return namn;
	}

	/**
	 * Finn ein måte å gjera denne på - dvs sjekke om oppdrag er fullførte eller ikkje.
	 * @return antal oppdragspoeng spelaren har
	 */
	public int getOppdragspoeng() throws RemoteException  {
		int ret = 0;
	/*	for (int i = 0; i < gjenverandeOppdrag.size(); i++) {
			ret -= gjenverandeOppdrag.get(i).getVerdi();
		}
		for (int i = 0; i < fullførteOppdrag.size(); i++) {
			ret += fullførteOppdrag.get(i).getVerdi();
		}
		*/
		return ret;
	}
	
	/**
	 * @return reknar ut kor mange poeng spelaren får på grunn av bygde ruter
	 */
	public int getRutepoeng() throws RemoteException  {
		int ret = 0;
		for (int i = 0; i < bygdeRuter.size(); i++) {
			ret += bygdeRuter.get(i).getVerdi();
		}
		return ret;
	}
	
	/**
	 * @return kor mange tog har spelaren igjen?
	 */
	public int getGjenverandeTog()  throws RemoteException {
		int brukteTog = 0;
		for (int i = 0; i < bygdeRuter.size(); i++) {
			brukteTog += bygdeRuter.get(i).getLengde();
		}
		return Konstantar.ANTAL_TOG - brukteTog;
	}
	
	/**
	 * @return trekk eit oppdrag frå kortbunken
	 */
	public Oppdrag trekkOppdragskort() throws RemoteException  {
		Oppdrag trekt;
		if (hovud.getAntalGjenverandeOppdrag() > 0) {
			trekt = hovud.getOppdrag();
			//System.out.println(trekt.getDestinasjonar().toArray()[1]);
		}
		else { 
			trekt = null; 
		}
		return trekt;
	}
	
	/**
	 * @return eit tilfeldig fargekort frå toppen av stokken
	 */
	public Farge trekkFargekort() throws RemoteException {
		Farge trekt;
		if (hovud.getBord().getAntalFargekortPåBordet() > 0) {
			trekt = hovud.getBord().getTilfeldigKortFråBordet(0, false);
		}
		else {
			trekt = null;
		}
		return trekt;
	}
	
	/**
	 * Byggjer ei rute
	 * @param rute - kva for rute?
	 * @param farge - i kva for farge?
	 */
	public void bygg(Rute rute, Farge farge) throws RemoteException  {
		rute.setBygdAv(this);
		// Fjern kort frå spelaren og legg dei i stokken eller ved sida av?
		bygdeRuter.add(rute);
		// Sjekk for fullførde oppdrag?
		
		// Fyller matrisa med ei rute frå d1 til d2 (og motsett):
		// Må først iterere over mengda med destinasjonar for å få dei ut
		Iterator<Destinasjon> mengdeIterator = rute.getDestinasjonar().iterator();
		// Er det veldig hårate å gå ut ifrå at det alltid finst to destinasjonar og hard-kode det?
		int destinasjon1 = mengdeIterator.next().ordinal();
		int destinasjon2 = mengdeIterator.next().ordinal();
		bygdRuteMatrise[destinasjon1][destinasjon2] = true;
		bygdRuteMatrise[destinasjon2][destinasjon1] = true;
		transitivTillukking();
	}

	/**
	 * Bør testes
	 */
	private void transitivTillukking() {
		// Dette er ein implementasjon av Warshallalgoritma, side 553 i Rosen [DiskMat]
		// Køyretida er på (u)behagelege 2n³, som er tilnærma likt optimalt
		int n = bygdRuteMatrise.length;
		
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					bygdRuteMatrise[i][j] = bygdRuteMatrise[i][j] || (bygdRuteMatrise[i][k] && bygdRuteMatrise[k][j]);
				}
			}
		}
	}
	
	/**
	 * Enkel toString. Returnerer berre namn.
	 */
	public String toString() {
		return namn;
	}
	
	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public void registrerKlient(Spelar s) throws RemoteException {
		boolean cont = false;
		for (Spelar p : hovud.getSpelarar()) {
			if (p == s) {
				cont = true;
			}
		}
		if (!cont) {
			hovud.getSpelarar().add(s);
		}
		else{
			throw new RemoteException("Denne motspelaren er allereie lagt til!");
		}
	}

	public void settSinTur(Spelar s) throws RemoteException {
		hovud.settSinTur(s);
	}

	public Farge getTilfeldigKortFråBordet(int i, boolean b) throws RemoteException {
		Farge f = hovud.getBord().getTilfeldigKortFråBordet(i, b);
		hovud.getBord().getPaaBordet()[i] = f;
		return f;
	}
	
	public void nybygdRute(int ruteId, Spelar byggjandeSpelar) throws RemoteException {
		Rute vald = null;
		for (Rute r : Hovud.getRuter()) {
			if (r.getRuteId() == ruteId) {
				vald = r;
			}
		}
		
		if (vald.getBygdAv() == null) {
			vald.setBygdAv(byggjandeSpelar);
			if (!hovud.getAlleBygdeRuter().contains(vald)) {
				hovud.getAlleBygdeRuter().add(vald);
			}
		}
	}
	
	public int[] getPaaBordetInt() throws RemoteException {
		int[] bord = new int[Konstantar.ANTAL_KORT_PÅ_BORDET];
		
		for (int i = 0; i < hovud.getBord().getPaaBordet().length; i++) {
			for (int f = 0; f < Konstantar.FARGAR.length; f++) {
				if (hovud.getBord().getPaaBordet()[i] == Konstantar.FARGAR[f]) {
					bord[i] = f;
				}
			}
		}
		
		return bord;
	}	
	
	public void setPaaBord(Farge[] f) {
		hovud.getBord().setPaaBordet(f);
	}
}
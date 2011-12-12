package ttr;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.gui.Konstantar;

import javax.swing.*;
import java.awt.*;
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
		bygdRuteMatrise = new boolean[Destinasjon.values().length][Destinasjon.values().length];
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
	

	public void faaOppdragInt(int oppdragsid)  throws RemoteException {
		boolean muleg = true;
		for (Oppdrag o : oppdrag){
			if (o.getOppdragsid()==oppdragsid){
				muleg=false;
			}
		}
		
		if(muleg){
			Oppdrag o = null;
			for (Oppdrag opp : hovud.getSpel().getOppdrag()){
				if (opp.getOppdragsid()==oppdragsid){
					o=opp;
				}
			}
			this.oppdrag.add(o);
		}
	}

	
	public void faaOppdrag(Oppdrag o) throws RemoteException{
		if(!this.oppdrag.contains(o)){
			this.oppdrag.add(o);
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
		for (int y = 0; y < Destinasjon.values().length; y++) {
			for (int x = 0; x < Destinasjon.values().length; x++) {
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
        for (Oppdrag anOppdrag : oppdrag) {
            Destinasjon d1 = (Destinasjon) anOppdrag.getDestinasjonar().toArray()[0];
            int d = d1.ordinal();
            Destinasjon d2 = (Destinasjon) anOppdrag.getDestinasjonar().toArray()[1];
            int e = d2.ordinal();
            if (bygdRuteMatrise[d][e] || bygdRuteMatrise[e][d]) {
                ret += anOppdrag.getVerdi();
            } else {
                ret -= anOppdrag.getVerdi();
            }
        }
		
		return ret;
	}
	
	public boolean erOppdragFerdig(int oppdragsid) throws RemoteException{
		Oppdrag o = null;
		for (Oppdrag opp : oppdrag){
			if (opp.getOppdragsid() == oppdragsid){
				o = opp;
			}
		}
		if (o==null){
			return false;
		}
		
		Destinasjon d1 = (Destinasjon) o.getDestinasjonar().toArray()[0];
		int d = d1.ordinal();
		Destinasjon d2 = (Destinasjon) o.getDestinasjonar().toArray()[1];
		int e = d2.ordinal();
        return bygdRuteMatrise[d][e] || bygdRuteMatrise[e][d];
	}
	
	public int getAntalFullfoerteOppdrag() throws RemoteException{
		int antal = 0;
		for (Oppdrag o : oppdrag){
			Destinasjon d1 = (Destinasjon) o.getDestinasjonar().toArray()[0];
			int d = d1.ordinal();
			Destinasjon d2 = (Destinasjon) o.getDestinasjonar().toArray()[1];
			int e = d2.ordinal();
			if (bygdRuteMatrise[d][e] || bygdRuteMatrise[e][d]){
				antal++;
			}
		}
		return antal;
	}
	
	/**
	 * @return reknar ut kor mange poeng spelaren får på grunn av bygde ruter
	 */
	public int getRutepoeng() throws RemoteException  {
		int ret = 0;
        for (Rute aBygdeRuter : bygdeRuter) {
            ret += aBygdeRuter.getVerdi();
        }
		return ret;
	}
	
	/**
	 * @return kor mange tog har spelaren igjen?
	 */
	public int getGjenverandeTog()  throws RemoteException {
		int brukteTog = 0;
        for (Rute aBygdeRuter : bygdeRuter) {
            brukteTog += aBygdeRuter.getLengde();
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
	public int trekkOppdragskortInt() throws RemoteException{
		Oppdrag o = trekkOppdragskort();
		return o.getOppdragsid();
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
	public void bygg(Rute rute) throws RemoteException  {
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
	@Override
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
//			throw new RemoteException("Denne motspelaren er allereie lagt til!");
		}
	}

	public void settSinTur(Spelar s) throws RemoteException {
		hovud.settSinTur(s);
	}

	public Farge getTilfeldigKortFråBordet(int i) throws RemoteException {
		Farge f = hovud.getBord().getTilfeldigKortFråBordet(i, true);
		if (f == null){
			JOptionPane.showMessageDialog(Hovud.getGui(), "Det er ikkje noko kort der, ser du vel.");
			Hovud.getGui().getKortButtons()[i].setBackground(Color.GRAY);
			Hovud.getGui().getKortButtons()[i].setText("Tom");
			
			int l = -1;
			for (int j = 0; j < Konstantar.FARGAR.length; j++){
				if (f == Konstantar.FARGAR[j]){
					l = j;
				}
			}
			if (l >= 0 && l < Konstantar.FARGAR.length){
				kort[l]--;
			}
			
			return null;
		}
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

        assert vald != null;
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
	public void setPaaBordet(Farge f, int i) throws RemoteException{
		// Har no finni fargen f og kva for plass denne har i farge-tabellen. 
		// Bør no få lagt ut eit kort på bordet i denne fargen.
		hovud.getBord().setEinPaaBordet(f, i);
	}
	
	public void leggUtFem() {
		hovud.getBord().leggUtFem();
	}
	public ArrayList<Spelar> getSpelarar() {
		return hovud.getSpelarar();
	}	
	public boolean sjekkJokrar() throws RemoteException{
		return hovud.getBord().sjekkJokrar();
	}
	
	public void trekt(int oppdragsid) throws RemoteException {
		// Finn oppdrag
		for (int i = 0; i < hovud.getAntalGjenverandeOppdrag(); i++){
			if (hovud.getGjenverandeOppdrag().get(i).getOppdragsid() == oppdragsid){
				hovud.getGjenverandeOppdrag().remove(i);
			}
		}	
	}
	public void leggIStokken(int tabellplass, int kormange) throws RemoteException {
		hovud.getBord().getIgjenAvFargekort()[tabellplass] += kormange;
	}
	
	public void visSpeletErFerdigmelding(String melding) throws RemoteException {
		JOptionPane.showMessageDialog(Hovud.getGui(), melding);
	}
	public void faaMelding(String melding) throws RemoteException{
		Hovud.getGui().getMeldingarModell().nyMelding(melding);
	}
}
package ttr.spelar;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 */
 public class SpelarImpl extends UnicastRemoteObject implements ISpelar {
	private IHovud hovud;
    private IBord bord;

	private static int spelarteljar = 0;
	private int spelarNummer;
	private String namn;
    private ISpelarOppdragshandsamar spelarOppdragshandsamar;
    private IKorthandsamar korthandsamar;
    private ArrayList<Rute> bygdeRuter; // Delvis unaudsynt pga. harEgBygdMellomAogB

	private boolean einValdAllereie;


    public SpelarImpl (IHovud hovud, String namn, IBord bord) throws RemoteException{
        super();
        this.hovud = hovud;
        this.bord = bord;
        this.namn = namn;
        einValdAllereie = false;
        bygdeRuter = new ArrayList<Rute>();

        korthandsamar = new Korthandsamar(hovud);
        spelarOppdragshandsamar = new SpelarOppdragshandsamar(hovud); 

    }
    
	public void setEittKortTrektInn(boolean b) throws RemoteException {
		einValdAllereie = b;
	}
	public int getSpelarNummer() throws RemoteException {
		return spelarNummer;
	}
	public boolean getValdAllereie() throws RemoteException {
		return einValdAllereie;
	}

	public void setTogAtt(int plass, int tog) throws RemoteException {
		hovud.getGui().getTogAtt()[plass].setText(String.valueOf(tog));
	}
	public void setSpelarNummer(int nummer) throws RemoteException { spelarNummer = nummer; }
	public int getSpelarteljar() throws RemoteException { return spelarteljar; }
	public void setSpelarteljar(int teljar) throws RemoteException { spelarteljar = teljar; }
	public int getBygdeRuterStr() throws RemoteException { return bygdeRuter.size(); }
	public int getBygdeRuterId(int j) throws RemoteException { return bygdeRuter.get(j).getRuteId(); }

	public String getNamn()  throws RemoteException { return namn; }

	public int getGjenverandeTog()  throws RemoteException {
		int brukteTog = 0;
        for (Rute aBygdeRuter : bygdeRuter) {
            brukteTog += aBygdeRuter.getLengde();
        }
		return Konstantar.ANTAL_TOG - brukteTog;
	}

	public void bygg(Rute rute) throws RemoteException  {
		rute.setBygdAv(this);
		// Fjern kort frå spelaren og legg dei i stokken eller ved sida av?
		bygdeRuter.add(rute);

        spelarOppdragshandsamar.bygg(rute);
	}

    @Override
	public String toString() { return namn; }
	
	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public void registrerKlient(ISpelar nyMotspelar) throws RemoteException {
		boolean cont = false;
		for (ISpelar eksisterandeSpelar : hovud.getSpelarar()) {
			if (nyMotspelar == eksisterandeSpelar) {
				cont = true;
			}
		}
		if (!cont) {
			hovud.getSpelarar().add(nyMotspelar);
		}
		else {
//			throw new RemoteException("Denne motspelaren er allereie lagt til!");
		}
	}

	
	public void nybygdRute(int ruteId, ISpelar byggjandeSpelar) throws RemoteException {
		Rute vald = null;
		for (Rute r : hovud.getRuter()) {
			if (r.getRuteId() == ruteId) {
				vald = r;
			}
		}

        assert vald != null;
        vald.setBygdAv(byggjandeSpelar);
        if (!hovud.getAlleBygdeRuter().contains(vald)) {
            hovud.getAlleBygdeRuter().add(vald);
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

	
	public void visSpeletErFerdigmelding(String melding) throws RemoteException {
		JOptionPane.showMessageDialog((Component) hovud.getGui(), melding);
	}


    // FASADE
    public void settSinTur(ISpelar s) throws RemoteException { hovud.settSinTur(s); }
    public ArrayList<ISpelar> getSpelarar() { return hovud.getSpelarar(); }
    public void faaMelding(String melding) throws RemoteException{ hovud.getGui().getMeldingarModell().nyMelding(melding); }

    // Oppdrag
    public int getAntalFullfoerteOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalFullfoerteOppdrag(); }
    public int getOppdragspoeng() throws RemoteException { return spelarOppdragshandsamar.getOppdragspoeng(); }
    public ArrayList<IOppdrag> getOppdrag() throws RemoteException { return spelarOppdragshandsamar.getOppdrag(); }
    public void faaOppdrag(IOppdrag o) throws RemoteException { spelarOppdragshandsamar.faaOppdrag(o); }
    public int getAntalOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalOppdrag(); }
    public IOppdrag trekkOppdragskort() throws RemoteException { return spelarOppdragshandsamar.trekkOppdragskort(); }
    public boolean erOppdragFerdig(int oppdragsid) throws RemoteException { return spelarOppdragshandsamar.erOppdragFerdig(oppdragsid); }
    public void trekt(int oppdragsid) throws RemoteException { spelarOppdragshandsamar.trekt(oppdragsid); }
    
    // Kort
    public int[] getKort() throws RemoteException { return korthandsamar.getKort(); }
    public void faaKort(Farge farge) throws RemoteException { korthandsamar.faaKort(farge);}
    public Farge getTilfeldigKortFråBordet(int i) throws RemoteException { return korthandsamar.getTilfeldigKortFråBordet(i); }
    public Farge trekkFargekort() throws RemoteException { return korthandsamar.trekkFargekort(); }

    // Bord
    public void leggUtFem() { bord.leggUtFem(); }
    public void leggIStokken(int tabellplass, int kormange) throws RemoteException {
        bord.getFargekortaSomErIgjenIBunken()[tabellplass] += kormange;
    }
    public void setPaaBord(Farge[] f) { bord.setPaaBordet(f); }
    public void setPaaBordet(Farge f, int i) throws RemoteException{
        // Har no finni fargen f og kva for plass denne har i farge-tabellen.
        // Bør no få lagt ut eit kort på bordet i denne fargen.
        bord.setEinPaaBordet(f, i);
    }
    public boolean sjekkJokrar() throws RemoteException{ return bord.sjekkOmJokrarPaaBordetErOK(); }
}
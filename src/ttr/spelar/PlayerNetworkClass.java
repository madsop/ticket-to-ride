package ttr.spelar;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.kjerna.IHovud;
import ttr.oppdrag.Mission;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 */
 public class PlayerNetworkClass extends PlayerImpl implements ISpelar  {
	private static final long serialVersionUID = -3600106049579247030L;
    private IKorthandsamar korthandsamar;
    
	public PlayerNetworkClass(IHovud hovud, String namn, IBord bord) throws RemoteException {
		super(hovud, namn, bord);
        korthandsamar = new Korthandsamar(hovud);
	}
	
	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public void registrerKlient(ISpelar nyMotspelar) {
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
	
	// FASADE
    public void settSinTur(ISpelar s) throws RemoteException { hovud.settSinTur(s); }
    public ArrayList<ISpelar> getSpelarar() { return hovud.getSpelarar(); }
    public void faaMelding(String melding) throws RemoteException{ hovud.getGui().getMeldingarModell().nyMelding(melding); }

    // Oppdrag
    public int getAntalFullfoerteOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalFullfoerteOppdrag(); }
    public int getOppdragspoeng() throws RemoteException { return spelarOppdragshandsamar.getOppdragspoeng(); }
    public ArrayList<Mission> getOppdrag() throws RemoteException { return spelarOppdragshandsamar.getOppdrag(); }
    public void faaOppdrag(Mission o) throws RemoteException { spelarOppdragshandsamar.retrieveMission(o); }
    public int getAntalOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalOppdrag(); }
    public Mission trekkOppdragskort() throws RemoteException { return spelarOppdragshandsamar.trekkOppdragskort(); }
    public boolean isMissionAccomplished(int oppdragsid) throws RemoteException { return spelarOppdragshandsamar.isMissionAccomplished(oppdragsid); }
    public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException { spelarOppdragshandsamar.removeChosenMissionFromDeck(oppdragsid); }
    
    // Kort
    public int[] getKort() throws RemoteException { return korthandsamar.getKort(); }
    public void receiveCard(Farge farge) throws RemoteException { korthandsamar.receiveCard(farge);}
    public Farge getTilfeldigKortFråBordet(int i) throws RemoteException { return korthandsamar.getRandomCardFromTheDeck(i); }
    public Farge trekkFargekort() throws RemoteException { return korthandsamar.trekkFargekort(); }

    // Bord
    public void leggUtFem() { bord.leggUtFem(); }
    public void leggIStokken(int tabellplass, int kormange) throws RemoteException {
        bord.getFargekortaSomErIgjenIBunken()[tabellplass] += kormange;
    }
    public void setPaaBord(Farge[] f) { bord.setPaaBordet(f); }
    public void putCardOnTable(Farge f, int i) throws RemoteException{
        // Har no finni fargen f og kva for plass denne har i farge-tabellen.
        // Bør no få lagt ut eit kort på bordet i denne fargen.
        bord.setEinPaaBordet(f, i);
    }
    public boolean areThereTooManyJokersOnTable() throws RemoteException{ return bord.areThereTooManyJokersOnTable(); }

	public ISpelar getThisAsISpelar() {
		return this;
	}
}
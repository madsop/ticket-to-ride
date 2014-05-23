package ttr.spelar;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.kjerna.IHovud;
import ttr.oppdrag.Mission;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 */
 public class PlayerNetworkClass extends PlayerImpl implements PlayerAndNetworkWTF  {
	private static final long serialVersionUID = -3600106049579247030L;
    private CardHandler korthandsamar;
    
	public PlayerNetworkClass(IHovud hovud, String namn, Table bord) throws RemoteException {
		super(hovud, namn, bord);
        korthandsamar = new CardHandlerImpl(hovud);
	}
	
	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public void registrerKlient(PlayerAndNetworkWTF nyMotspelar) {
		boolean cont = false;
		for (PlayerAndNetworkWTF eksisterandeSpelar : hovud.getSpelarar()) {
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
    public void settSinTur(PlayerAndNetworkWTF s) throws RemoteException { hovud.settSinTur(s); }
    public ArrayList<PlayerAndNetworkWTF> getSpelarar() { return hovud.getSpelarar(); }
    public void receiveMessage(String message) throws RemoteException{ hovud.receiveMessage(message); }
	public void showGameOverMessage(String points) throws RemoteException { hovud.showGameOverMessage(points); }

    // Oppdrag
    public int getAntalFullfoerteOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalFullfoerteOppdrag(); }
    public int getOppdragspoeng() throws RemoteException { return spelarOppdragshandsamar.getOppdragspoeng(); }
    public ArrayList<Mission> getOppdrag() throws RemoteException { return spelarOppdragshandsamar.getOppdrag(); }
    public void receiveMission(Mission o) throws RemoteException { spelarOppdragshandsamar.retrieveMission(o); }
    public int getAntalOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalOppdrag(); }
    public Mission trekkOppdragskort() throws RemoteException { return spelarOppdragshandsamar.trekkOppdragskort(); }
    public boolean isMissionAccomplished(int oppdragsid) throws RemoteException { return spelarOppdragshandsamar.isMissionAccomplished(oppdragsid); }
    public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException { spelarOppdragshandsamar.removeChosenMissionFromDeck(oppdragsid); }
    
    // Kort
    public void receiveCard(Farge farge) throws RemoteException { korthandsamar.receiveCard(farge);}
    public Farge getRandomCardFromTheDeck(int i) throws RemoteException { return korthandsamar.getRandomCardFromTheDeck(i); }
    public Farge trekkFargekort() throws RemoteException { return korthandsamar.drawRandomCardFromTheDeck(); }

	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException { return korthandsamar.getNumberOfCardsLeftInColour(colour); }
	public int getNumberOfRemainingJokers() throws RemoteException { return korthandsamar.getNumberOfCardsLeftInColour(Farge.valfri); }

	public void decrementCardsAt(Farge colour, int number) throws RemoteException { korthandsamar.decrementCardsAt(colour, number); }

    // Bord
    public void leggUtFem() { bord.layFiveCardsOutOnTable(); }
    public void leggIStokken(Farge colour, int number) throws RemoteException { bord.addCardsToDeck(colour, number); }
    
    public void setPaaBord(Farge[] f) { bord.setPaaBordet(f); }
    public void putCardOnTable(Farge f, int i) throws RemoteException{
        // Har no finni fargen f og kva for plass denne har i farge-tabellen.
        // Bør no få lagt ut eit kort på bordet i denne fargen.
        bord.setEinPaaBordet(f, i);
    }
    public boolean areThereTooManyJokersOnTable() throws RemoteException{ return bord.areThereTooManyJokersOnTable(); }

	public PlayerAndNetworkWTF getThisAsISpelar() {
		return this;
	}
}
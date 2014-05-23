package ttr.spelar;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 */
 public class PlayerNetworkClass extends PlayerImpl implements PlayerAndNetworkWTF  {
	private static final long serialVersionUID = -3600106049579247030L;
    private CardHandler korthandsamar;
    
	public PlayerNetworkClass(Core hovud, String namn, Table bord) throws RemoteException {
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
    public Collection<Mission> getOppdrag() throws RemoteException { return spelarOppdragshandsamar.getOppdrag(); }
    public void receiveMission(Mission mission) throws RemoteException { spelarOppdragshandsamar.retrieveMission(mission); }
    public int getAntalOppdrag() throws RemoteException { return spelarOppdragshandsamar.getAntalOppdrag(); }
    public Mission trekkOppdragskort() throws RemoteException { return hovud.trekkOppdragskort(); }
    public boolean isMissionAccomplished(Mission mission) throws RemoteException { return spelarOppdragshandsamar.isMissionAccomplished(mission); }
    public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException { hovud.removeChosenMissionFromDeck(oppdragsid); }
    
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
    public void putCardOnTable(Farge colour, int position) throws RemoteException{ bord.putOneCardOnTable(colour, position); }
    public boolean areThereTooManyJokersOnTable() throws RemoteException{ return bord.areThereTooManyJokersOnTable(); }

	public PlayerAndNetworkWTF getThisAsISpelar() {
		return this;
	}
}
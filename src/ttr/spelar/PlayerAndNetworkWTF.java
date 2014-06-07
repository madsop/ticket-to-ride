package ttr.spelar;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.gui.SwingUtils;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Ein spelar. All nettverkskommunikasjon går via denne, så litt hårate klasse med ein del ad hoc-metodar.
 */
 public class PlayerAndNetworkWTF extends PlayerImpl implements IPlayer {
	private static final long serialVersionUID = -3600106049579247030L;
    private CardHandler korthandsamar;
    
	public PlayerAndNetworkWTF(Core hovud, String namn, Table bord) throws RemoteException {
		super(hovud, namn, bord);
        korthandsamar = new CardHandler(hovud);
	}
	
	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public void registrerKlient(IPlayer host) {
		boolean cont = false;
		for (IPlayer eksisterandeSpelar : hovud.getSpelarar()) {
			if (host == eksisterandeSpelar) {
				cont = true;
			}
		}
		if (!cont) {
			hovud.getSpelarar().add(host);
			playerCounter++;
		}
		else {
			//	throw new RemoteException("Denne motspelaren er allereie lagt til!");
		}
	}
	
	// FASADE
    public void settSinTur(IPlayer sinTur) throws RemoteException { hovud.settSinTur(sinTur); }
    public ArrayList<IPlayer> getSpelarar() { return hovud.getSpelarar(); }
    public void receiveMessage(String message){ hovud.receiveMessage(message); }
	public void showGameOverMessage(String points) { SwingUtils.showMessageDialog(points); }

    // Oppdrag
    public int getAntalFullfoerteOppdrag() { return playerMissionHandler.getNumberOfFulfilledMissions(); }
    public int getOppdragspoeng() { return playerMissionHandler.getMissionPoints(); }
    public Collection<Mission> getOppdrag() { return playerMissionHandler.getMissions(); }
    public void receiveMission(Mission mission) { playerMissionHandler.retrieveMission(mission); }
    public Mission trekkOppdragskort() { return hovud.drawMission(); }
    public boolean isMissionAccomplished(Mission mission) { return playerMissionHandler.isMissionAccomplished(mission); }
    public void removeChosenMissionFromDeck(Mission mission) { hovud.removeChosenMissionFromDeck(mission); }
    
    // Kort
    public void receiveCard(Colour farge) { korthandsamar.receiveCard(farge);}
    public Colour getRandomCardFromTheDeck(int i) { return hovud.getRandomCardFromTheDeck(i); }
    public Colour trekkFargekort() { return hovud.getTable().getRandomCardFromTheDeck(); }

	public int getNumberOfCardsLeftInColour(Colour colour) { return korthandsamar.getNumberOfCardsLeftInColour(colour); }
	public int getNumberOfRemainingJokers() { return korthandsamar.getNumberOfCardsLeftInColour(Colour.valfri); }

	public void decrementCardsAt(Colour colour, int number) { korthandsamar.useCards(colour, number); }

    // Bord
    public void leggUtFem() { bord.layFiveCardsOutOnTable(); }
	public void updateDeckOnTable(Colour colour, int kortKrevd, int krevdJokrar, int jokrar) { bord.updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar); }
    public void setPaaBord(Colour[] f) { bord.putCardsOnTable(f); }
    public void putCardOnTable(Colour colour, int position){ bord.putOneCardOnTable(colour, position); }
    public boolean areThereTooManyJokersOnTable(){ return bord.areThereTooManyJokersOnTable(); }

	public IPlayer getThisAsISpelar() {	return this; }

	public void nybygdRute(Route route, IPlayer byggjandeSpelar) { hovud.nybygdRute(route, byggjandeSpelar); }
	public Colour[] getCardsOnTable() { return hovud.getTable().getCardsOpenOnTable(); }
	
	public void setRemainingTrains(int position, int numberOfTrains) { hovud.displayNumberOfRemainingTrains(position, numberOfTrains); }
}
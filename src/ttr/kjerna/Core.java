package ttr.kjerna;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.bygg.ByggjandeInfo;
import ttr.communicationWithPlayers.CommunicationWithPlayers;
import ttr.data.Colour;
import ttr.gui.GUI;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;
import ttr.rute.Route;
import ttr.rute.RouteHandler;
import ttr.spelar.IPlayer;
import ttr.turhandsamar.TurHandsamar;
import ttr.utgaave.GameVersion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public abstract class Core {
	private final GameVersion gameVersion;
	protected final Table table;
	protected ArrayList<IPlayer> players;
	protected final GUI gui;

	protected IPlayer playerInTurn;
	protected IPlayer myPlayer;
	protected CommunicationWithPlayers communicationWithPlayers;
	protected MissionHandler missionHandler;
	private RouteHandler routeHandler;
	protected TurHandsamar turhandsamar;
	private ByggHjelpar buildingHelper;
	private PropertyChangeSupport propertyChangeSupport;

	public Core(GUI gui, Table table, GameVersion gameVersion, ByggHjelpar buildingHelper, MissionHandler missionHandler, RouteHandler routeHandler) throws RemoteException {
		this.gui = gui;
		this.gameVersion = gameVersion;
		this.table = table;
		this.buildingHelper = buildingHelper;
		this.missionHandler = missionHandler;
		this.routeHandler = routeHandler;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		players = new ArrayList<>();
		createTable();
	}

	public abstract void settIGangSpelet(String hostAddress) throws RemoteException;
	public abstract void orientOtherPlayers(int positionOnTable) throws RemoteException;
	public abstract IPlayer findPlayerInAction();
	protected abstract String getWhoseTurnText() throws RemoteException;
	protected abstract void messageUsersInNetworkGame(Route bygd, IPlayer byggjandeSpelar) throws RemoteException;
	protected abstract void createTable() throws RemoteException;

	public void setMinSpelar(IPlayer iPlayer){
		myPlayer = iPlayer;
	}

	public IPlayer getMinSpelar() {
		return myPlayer;
	}

	public Table getTable() {
		return table;
	}

	public IPlayer getKvenSinTur() {
		return playerInTurn;
	}

	public ArrayList<IPlayer> getSpelarar() {
		return players;
	}

	public void settSinTur(IPlayer host) throws RemoteException {
		playerInTurn = host;
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
	}

	public void nesteSpelar() throws RemoteException {
		playerInTurn = turhandsamar.nextPlayer(playerInTurn,myPlayer);
		markIfItIsMyTurn();
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
		playerInTurn.setEittKortTrektInn(false);

		communicationWithPlayers.sjekkOmFerdig(gui.getMessagesModel(),playerInTurn,gameVersion.toString(),myPlayer);
	}

	private void markIfItIsMyTurn() throws RemoteException {
		if (playerInTurn.getNamn().equals(myPlayer.getNamn())) {
			gui.displayGraphicallyThatItIsMyTurn();
		}
	}

	public void bygg(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = buildingHelper.buildRoute(bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	//TODO kanskje byggTunnel og bygg bør smelte saman?
	public void byggTunnel(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = buildingHelper.buildTunnel(table, bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	private void hjelpemetodeBygg(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokrar) throws RemoteException {
		routeHandler.newRoute(bygd);
		messageUsersInNetworkGame(bygd, byggjandeSpelar);
//		gui.setRemainingTrains(byggjandeSpelar.getSpelarNummer() + 1, byggjandeSpelar.getGjenverandeTog());
		//TODO fyr property change på eit fint vis
		propertyChangeSupport.firePropertyChange("remainingTrains" + byggjandeSpelar.getSpelarNummer() + 1, "", byggjandeSpelar.getGjenverandeTog());
		table.updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.receiveMessage(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		communicationWithPlayers.updateOtherPlayers(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);
		nesteSpelar();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}


	/** FORWARDING HERIRÅ OG NED **/
	public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		gui.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
	}

	public void displayNumberOfRemainingTrains(int position, int numberOfTrains) {
		gui.setRemainingTrains(position, numberOfTrains);
	}

	public void receiveMessage(String message) {
		gui.receiveMessage(message);
	}

	public Mission drawMission()  { //TODO flytt vidare inn i oppdragshandsamar
		return missionHandler.trekkOppdragskort();
	}

	public void removeChosenMissionFromDeck(Mission mission) { //TODO flytt vidare inn i oppdragshandsamar?
		missionHandler.removeChosenMissionFromDeck(mission);
	}

	public void nybygdRute(Route route, IPlayer byggjandeSpelar) { //TODO vidare ned til rutehandsamar
		route.setBuiltBy(byggjandeSpelar);
		routeHandler.getBuiltRoutes().add(route);
	}

	public Set<Route> findRoutesNotYetBuilt() throws RemoteException {
		return routeHandler.findRoutesNotYetBuilt(players);
	}

	public Set<Route> getAllBuiltRoutes() {
		return routeHandler.getBuiltRoutes();
	}

	//TODO denne bør vel splittast i to metodar, ein for fargekort og ein for oppdrag
	public void sendMessageAboutCard(boolean isColourCard, boolean isRandom, Colour colour) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(isColourCard, isRandom, colour, playerInTurn.getNamn(), this);
	}

	public void trekkOppdrag() throws RemoteException {
		sendMessageAboutCard(false, false, Colour.blå);
		missionHandler.trekkOppdrag(gui, findPlayerInAction(), false);
		nesteSpelar();
	}

	public Colour getRandomCardFromTheDeck(int positionOnTable) {
		Colour colourOfTheRandomCard = table.getRandomCardFromTheDeckAndPutOnTable(positionOnTable);

		if (colourOfTheRandomCard == null){
			displayGraphicallyThatThereIsNoCardHere(positionOnTable);
			return null;
		}

		table.putOneCardOnTable(colourOfTheRandomCard, positionOnTable);
		return colourOfTheRandomCard;
	}



	//TODO alt under her høyrer saman og bør jo helst finne seg si eiga klasse
	public void createButtonForRetrievingCardFromTable(int positionOnTable) throws RemoteException {
		CardListener cardListener = new CardListener();
		cardListener.retrieveOneCardFromTheTable(positionOnTable, findPlayerInAction());
		orientOtherPlayers(positionOnTable);
	}

	private class CardListener {
		void retrieveOneCardFromTheTable(int positionOnTable, IPlayer kvenSinTur) throws RemoteException {
			Colour colour = table.getCardFromTable(positionOnTable);
			if (colour == null) { return; }
			if (!kvenSinTur.hasAlreadyDrawnOneCard()) {
				retrieveFirstCard(positionOnTable, kvenSinTur, colour);
			}
			else {
				retrieveSecondCard(positionOnTable, kvenSinTur, colour);
			}
		}

		private void retrieveFirstCard(int positionOnTable, IPlayer kvenSinTur, Colour colour) throws RemoteException {
			kvenSinTur.receiveCard(colour);
			putRandomCardFromTheDeckOnTable(positionOnTable, colour);
			if (colour == Colour.valfri) {
				nesteSpelar();
			}
			kvenSinTur.setEittKortTrektInn(true);
		}

		private void retrieveSecondCard(int positionOnTable, IPlayer kvenSinTur, Colour colour) throws RemoteException {
			if (colour == Colour.valfri) {
				JOptionPane.showMessageDialog(null, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
				return;
			}
			kvenSinTur.receiveCard(colour);
			putRandomCardFromTheDeckOnTable(positionOnTable, colour);
			nesteSpelar();
		}

		private void putRandomCardFromTheDeckOnTable(int positionOnTable, Colour colour) throws RemoteException {
			sendMessageAboutCard(true,false,colour);
			table.getRandomCardFromTheDeckAndPutOnTable(positionOnTable);
		}
	}
}
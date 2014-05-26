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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

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

	public Core(GUI gui, Table table, GameVersion gameVersion) throws RemoteException {
		this.gui = gui;
		this.gameVersion = gameVersion;
		this.table = table;
		players = new ArrayList<>();
		routeHandler = new RouteHandler(gameVersion);
		missionHandler = new MissionHandler(gameVersion.getOppdrag());		// Legg til oppdrag
		buildingHelper = new ByggHjelpar(gui);
		createTable();
	}

	public abstract void settIGangSpelet(String hostAddress) throws RemoteException;
	public abstract void orientOtherPlayers(int positionOnTable) throws RemoteException;

	public void setMinSpelar(IPlayer iPlayer){
		myPlayer = iPlayer;
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

	public IPlayer getMinSpelar() {
		return myPlayer;
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
	
	public abstract IPlayer findPlayerInAction();
	protected abstract String getWhoseTurnText() throws RemoteException;

	private void markIfItIsMyTurn() throws RemoteException {
		if (playerInTurn.getNamn().equals(myPlayer.getNamn())) {
			gui.displayGraphicallyThatItIsMyTurn();
		}
	}
	
	public void bygg(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = buildingHelper.bygg(bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	//TODO kanskje byggTunnel og bygg bør smelte saman...
	public void byggTunnel(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = buildingHelper.byggTunnel(table, bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	protected abstract void createTable() throws RemoteException;

	private void hjelpemetodeBygg(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokrar) throws RemoteException {
		routeHandler.newRoute(bygd);
		messageUsersInNetworkGame(bygd, byggjandeSpelar);
		gui.setRemainingTrains(byggjandeSpelar.getSpelarNummer() + 1, byggjandeSpelar.getGjenverandeTog());
		table.updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.receiveMessage(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		communicationWithPlayers.updateOtherPlayers(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);
		nesteSpelar();
	}

	protected abstract void messageUsersInNetworkGame(Route bygd, IPlayer byggjandeSpelar) throws RemoteException;


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

	public void sendMessageAboutCard(boolean isColourCard, boolean isRandom, Colour colour) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(isColourCard, isRandom, colour, playerInTurn.getNamn(), this);
	}

	public MissionHandler getMissionHandler() {
		return missionHandler; //TODO denne må bort etter kvart som DI kjem på plass
	}
}
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
import ttr.rute.RouteHandlerImpl;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.turhandsamar.TurHandsamar;
import ttr.utgaave.GameVersion;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public abstract class Core {
	private final GameVersion gameVersion;
	protected final Table table;
	protected ArrayList<PlayerAndNetworkWTF> players;
	protected final GUI gui;

	protected PlayerAndNetworkWTF kvenSinTur;
	protected PlayerAndNetworkWTF minSpelar;
	protected CommunicationWithPlayers communicationWithPlayers;
	private MissionHandler oppdragshandsamar;
	private RouteHandler rutehandsamar;
	protected TurHandsamar turhandsamar;
	private ByggHjelpar bygghjelpar;

	public Core(GUI gui, Table table, GameVersion gameVersion) throws RemoteException {
		this.gui = gui;
		this.gameVersion = gameVersion;
		this.table = table;
		players = new ArrayList<>();
		rutehandsamar = new RouteHandlerImpl(gameVersion);
		oppdragshandsamar = new MissionHandler(gameVersion.getOppdrag());		// Legg til oppdrag
		bygghjelpar = new ByggHjelpar(gui);
		createTable();
	}

	public abstract void settIGangSpelet(String hostAddress) throws RemoteException;
	public abstract void orientOtherPlayers(int positionOnTable) throws RemoteException;

	public void setMinSpelar(PlayerAndNetworkWTF spelar){
		minSpelar = spelar;
	}

	public Table getTable() {
		return table;
	}

	public PlayerAndNetworkWTF getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<PlayerAndNetworkWTF> getSpelarar() {
		return players;
	}

	public PlayerAndNetworkWTF getMinSpelar() {
		return minSpelar;
	}
	
	public void settSinTur(PlayerAndNetworkWTF spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
	}

	public void nesteSpelar() throws RemoteException {
		kvenSinTur = turhandsamar.nextPlayer(kvenSinTur,minSpelar);
		markIfItIsMyTurn();
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
		kvenSinTur.setEittKortTrektInn(false);

		communicationWithPlayers.sjekkOmFerdig(gui.getMessagesModel(),kvenSinTur,gameVersion.toString(),minSpelar,rutehandsamar.getRoutes());
	}
	
	public abstract PlayerAndNetworkWTF findPlayerInAction();
	protected abstract String getWhoseTurnText() throws RemoteException;

	private void markIfItIsMyTurn() throws RemoteException {
		if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
			gui.getPlayerNameJTextField().setBackground(Color.YELLOW);
		}
	}
	
	public void bygg(Route bygd, Colour colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd,colour,kortKrevd,krevdJokrar,findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	//TODO kanskje byggTunnel og bygg bør smelte saman...
	public void byggTunnel(Route bygd, Colour colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.byggTunnel(table, bygd, colour, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Colour f) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(kort, tilfeldig, f, kvenSinTur.getNamn(), this);
	}

	protected abstract void createTable() throws RemoteException;

	private void hjelpemetodeBygg(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokrar) throws RemoteException {
		rutehandsamar.newRoute(bygd);
		messageUsersInNetworkGame(bygd, byggjandeSpelar);
		gui.getRemainingTrainsLabel()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
		table.updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.getMessagesModel().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		communicationWithPlayers.updateOtherPlayers(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);
		nesteSpelar();
	}

	protected abstract void messageUsersInNetworkGame(Route bygd, PlayerAndNetworkWTF byggjandeSpelar) throws RemoteException;

	/** GUI BLOCK **/
	public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		gui.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
	}

	public void displayNumberOfRemainingTrains(int position, int numberOfTrains) {
		gui.getRemainingTrainsLabel()[position].setText(String.valueOf(numberOfTrains));
	}
	
	public void receiveMessage(String message) {
		gui.getMessagesModel().nyMelding(message);
	}
	

	public Mission missionHandler_trekkOppdragskort() throws RemoteException  { //TODO flytt vidare inn i oppdragshandsamar
		return oppdragshandsamar.trekkOppdragskort();
	}

	public void missionHandler_removeChosenMissionFromDeck(Mission mission) throws RemoteException { //TODO flytt vidare inn i oppdragshandsamar?
		oppdragshandsamar.removeChosenMissionFromDeck(mission);
	}

	public void routeHandler_nybygdRute(Route route, PlayerAndNetworkWTF byggjandeSpelar) { //TODO vidare ned til rutehandsamar
		route.setBuiltBy(byggjandeSpelar);
		rutehandsamar.getBuiltRoutes().add(route);
	}
	
	public Set<Route> findRoutesNotYetBuilt() throws RemoteException {
		return rutehandsamar.findRoutesNotYetBuilt(players);
	}

	public Set<Route> routeHandler_getAlleBygdeRuter() {
		return rutehandsamar.getBuiltRoutes();
	}
}
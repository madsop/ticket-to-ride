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

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public abstract class Core {
	private final GameVersion gameVersion;
	protected final Table table;
	protected ArrayList<IPlayer> players;
	protected final GUI gui;

	protected IPlayer kvenSinTur;
	protected IPlayer minSpelar;
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
		rutehandsamar = new RouteHandler(gameVersion);
		oppdragshandsamar = new MissionHandler(gameVersion.getOppdrag());		// Legg til oppdrag
		bygghjelpar = new ByggHjelpar(gui);
		createTable();
	}

	public abstract void settIGangSpelet(String hostAddress) throws RemoteException;
	public abstract void orientOtherPlayers(int positionOnTable) throws RemoteException;

	public void setMinSpelar(IPlayer iPlayer){
		minSpelar = iPlayer;
	}

	public Table getTable() {
		return table;
	}

	public IPlayer getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<IPlayer> getSpelarar() {
		return players;
	}

	public IPlayer getMinSpelar() {
		return minSpelar;
	}
	
	public void settSinTur(IPlayer host) throws RemoteException {
		kvenSinTur = host;
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
	}

	public void nesteSpelar() throws RemoteException {
		kvenSinTur = turhandsamar.nextPlayer(kvenSinTur,minSpelar);
		markIfItIsMyTurn();
		gui.showWhoseTurnItIs(findPlayerInAction().getNamn(), getWhoseTurnText());
		kvenSinTur.setEittKortTrektInn(false);

		communicationWithPlayers.sjekkOmFerdig(gui.getMessagesModel(),kvenSinTur,gameVersion.toString(),minSpelar);
	}
	
	public abstract IPlayer findPlayerInAction();
	protected abstract String getWhoseTurnText() throws RemoteException;

	private void markIfItIsMyTurn() throws RemoteException {
		if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
			gui.getPlayerNameJTextField().setBackground(Color.YELLOW);
		}
	}
	
	public void bygg(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	//TODO kanskje byggTunnel og bygg bør smelte saman...
	public void byggTunnel(Route bygd, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.byggTunnel(table, bygd, kortKrevd, krevdJokrar, findPlayerInAction());
		if (byggjandeInfo == null) { return; }		 // TODO betre tilbakemelding her
		hjelpemetodeBygg(bygd, byggjandeInfo.colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Colour f) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(kort, tilfeldig, f, kvenSinTur.getNamn(), this);
	}

	protected abstract void createTable() throws RemoteException;

	private void hjelpemetodeBygg(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokrar) throws RemoteException {
		rutehandsamar.newRoute(bygd);
		messageUsersInNetworkGame(bygd, byggjandeSpelar);
		gui.getRemainingTrainsLabel()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
		table.updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.getMessagesModel().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		communicationWithPlayers.updateOtherPlayers(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);
		nesteSpelar();
	}

	protected abstract void messageUsersInNetworkGame(Route bygd, IPlayer byggjandeSpelar) throws RemoteException;

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
	

	public Mission missionHandler_trekkOppdragskort()  { //TODO flytt vidare inn i oppdragshandsamar
		return oppdragshandsamar.trekkOppdragskort();
	}

	public void missionHandler_removeChosenMissionFromDeck(Mission mission) { //TODO flytt vidare inn i oppdragshandsamar?
		oppdragshandsamar.removeChosenMissionFromDeck(mission);
	}

	public void routeHandler_nybygdRute(Route route, IPlayer byggjandeSpelar) { //TODO vidare ned til rutehandsamar
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
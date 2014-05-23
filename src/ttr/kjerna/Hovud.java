package ttr.kjerna;

import ttr.bord.IBord;
import ttr.bygg.ByggHjelpar;
import ttr.bygg.IByggHjelpar;
import ttr.bygg.ByggjandeInfo;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.rute.Route;
import ttr.rute.RouteHandler;
import ttr.rute.RouteHandlerImpl;
import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public class Hovud implements IHovud {

	private final ISpelUtgaave gameVersion;
	private final IBord table;
	private ArrayList<ISpelar> players;
	private final IGUI gui;

	private final boolean isNetworkGame;

	private ISpelar kvenSinTur;
	private ISpelar minSpelar;
	private IKommunikasjonMedSpelarar communicationWithPlayers;
	private MissionHandler oppdragshandsamar;
	private RouteHandler rutehandsamar;
	private ITurhandsamar turhandsamar;
	private IByggHjelpar bygghjelpar;

	public Hovud(IGUI gui, IBord table, boolean isNetworkGame, ISpelUtgaave gameVersion) throws RemoteException {
		this.gui = gui;
		this.isNetworkGame = isNetworkGame;
		this.gameVersion = gameVersion;
		this.table = table;
		players = new ArrayList<>();
		LagBrettet(isNetworkGame);
	}

	public void settIGangSpelet(boolean nett, String hostAddress) throws RemoteException {
		if (nett) {
			startNetworkGame(hostAddress);
		}
		else {
			startLocalGame();
		}
	}

	public ArrayList<Mission> getGjenverandeOppdrag() {
		return oppdragshandsamar.getRemainingMissions()   ;
	}

	public Set<Route> getRuter() {
		return rutehandsamar.getRoutes();
	}

	public Set<Route> getAlleBygdeRuter() {
		return rutehandsamar.getBuiltRoutes();
	}

	public void setMinSpelar(ISpelar spelar){
		minSpelar = spelar;
	}

	public IBord getBord() {
		return table;
	}
	public boolean isNett() {
		return isNetworkGame;
	}

	public ISpelar getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<ISpelar> getSpelarar() {
		return players;
	}
	
	public int getAntalGjenverandeOppdrag() {
		return oppdragshandsamar.getNumberOfRemainingMissions();
	}

	public Mission getOppdrag() {
		return oppdragshandsamar.getMissionAndRemoveItFromDeck();
	}

	public ISpelar getMinSpelar() {
		return minSpelar;
	}

	public void settSinTur(ISpelar spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(),isNetworkGame,minSpelar.getNamn()); //todo minspelar er null init...
	}

	public void nesteSpelar() throws RemoteException {
		kvenSinTur = turhandsamar.nextPlayer(kvenSinTur,minSpelar);

		markIfItIsMyTurn();
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(),isNetworkGame,minSpelar.getNamn());
		kvenSinTur.setEittKortTrektInn(false);

		communicationWithPlayers.sjekkOmFerdig(gui.getMeldingarModell(),kvenSinTur,gameVersion.toString(),minSpelar,rutehandsamar.getRoutes());
	}

	private void markIfItIsMyTurn() throws RemoteException {
		if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
			gui.getSpelarnamn().setBackground(Color.YELLOW);
		}
	}

	public Set<Route> findRoutesNotYetBuilt() throws RemoteException {
		return rutehandsamar.findRoutesNotYetBuilt(players);
	}

	public void bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd,colour,kortKrevd,krevdJokrar,minSpelar,kvenSinTur);
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void byggTunnel(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.byggTunnel(table, bygd, colour, kortKrevd, krevdJokrar, minSpelar, kvenSinTur);
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge f) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(kort,tilfeldig,f,kvenSinTur.getNamn(),isNetworkGame,this);
	}

	public void newCardPlacedOnTableInNetworkGame(ISpelar vert, Farge nyFarge, int i) throws RemoteException {
		communicationWithPlayers.newCardPlacedOnTableInNetworkGame(vert,nyFarge,i,this);
	}

	private void LagBrettet(boolean nett) throws RemoteException {
		rutehandsamar = new RouteHandlerImpl(gameVersion);
		oppdragshandsamar = new MissionHandlerImpl(gameVersion.getOppdrag());		// Legg til oppdrag
		bygghjelpar = new ByggHjelpar(gui,nett);
		communicationWithPlayers = new KommunikasjonMedSpelarar(nett,players); // TODO dependency injection?

		if (!nett) {
			createPlayersAndSetUpForLocalGame();
		}         // else er det nettverksspel og handterast seinare
		turhandsamar = new TurHandsamar(players,nett);
	}

	private void startNetworkGame(String hostAddress) throws RemoteException {
		InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		nettverk.initialiseNetworkGame();
		MissionHandlerImpl.trekkOppdrag(gui, minSpelar, true);

		givePlayersMissions();
	}

	private void givePlayersMissions() throws RemoteException {
		for (ISpelar player : players){
			for (Mission mission : player.getOppdrag()){
				player.removeChosenMissionFromDeck(mission.getMissionId());
			}
		}
	}

	private void startLocalGame() throws RemoteException {
		for (ISpelar player : players) {
			MissionHandlerImpl.trekkOppdrag(gui, player, true);
		}
		// ??
	}

	private void createPlayersAndSetUpForLocalGame() throws RemoteException {
		players = communicationWithPlayers.createPlayersForLocalGame(this,table); //todo playes må komme inn i arraylista her på eit vis
		if (minSpelar == null) { setMinSpelar(players.get(0)); }
		settSinTur(players.get(0));
	}

	private void hjelpemetodeBygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int jokrar) throws RemoteException{
		rutehandsamar.newRoute(bygd);

		messageUsersInNetworkGame(bygd, byggjandeSpelar);
		gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
		updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.getMeldingarModell().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());

		communicationWithPlayers.oppdaterAndreSpelarar(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);

		nesteSpelar();

	}

	private void messageUsersInNetworkGame(Route builtRoute, ISpelar buildingPlayer) throws RemoteException {
		if (isNetworkGame) {
			for (ISpelar player : players) {
				player.nybygdRute(builtRoute.getRouteId(),buildingPlayer);
				player.setTogAtt(buildingPlayer.getSpelarNummer()+1, buildingPlayer.getGjenverandeTog());
			}
		}
	}

	private void updateDeckOnTable(Farge colour, int kortKrevd, int krevdJokrar, int jokrar) {
		table.addCardsToDeck(colour, kortKrevd-(jokrar-krevdJokrar));
		table.addJokersToDeck(jokrar);
	}

	public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		gui.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
	}

	public void displayNumberOfRemainingTrains(int position, int numberOfTrains) {
		gui.getTogAtt()[position].setText(String.valueOf(numberOfTrains));
	}

	public void showGameOverMessage(String message) {
		JOptionPane.showMessageDialog((Component) gui, message);
	}
	
	public void receiveMessage(String message) {
		gui.getMeldingarModell().nyMelding(message);
	}
}
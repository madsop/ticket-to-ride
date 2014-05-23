package ttr.kjerna;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.bygg.IByggHjelpar;
import ttr.bygg.ByggjandeInfo;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.rute.Route;
import ttr.rute.RouteHandler;
import ttr.rute.RouteHandlerImpl;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.utgaave.GameVersion;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public abstract class CoreImpl implements Core {

	private final GameVersion gameVersion;
	protected final Table table;
	protected ArrayList<PlayerAndNetworkWTF> players;
	protected final IGUI gui;

	protected PlayerAndNetworkWTF kvenSinTur;
	protected PlayerAndNetworkWTF minSpelar;
	protected CommunicationWithPlayers communicationWithPlayers;
	private MissionHandler oppdragshandsamar;
	private RouteHandler rutehandsamar;
	protected ITurhandsamar turhandsamar;
	private IByggHjelpar bygghjelpar;

	public CoreImpl(IGUI gui, Table table, GameVersion gameVersion) throws RemoteException {
		this.gui = gui;
		this.gameVersion = gameVersion;
		this.table = table;
		players = new ArrayList<>();
		LagBrettet();
	}

	public abstract void settIGangSpelet(String hostAddress) throws RemoteException;

	public ArrayList<Mission> getGjenverandeOppdrag() {
		return oppdragshandsamar.getRemainingMissions()   ;
	}

	public Set<Route> getRuter() {
		return rutehandsamar.getRoutes();
	}

	public Set<Route> getAlleBygdeRuter() {
		return rutehandsamar.getBuiltRoutes();
	}

	public void setMinSpelar(PlayerAndNetworkWTF spelar){
		minSpelar = spelar;
	}

	public Table getTable() {
		return table;
	}
	public abstract boolean isNetworkGame();

	public PlayerAndNetworkWTF getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<PlayerAndNetworkWTF> getSpelarar() {
		return players;
	}
	
	public int getAntalGjenverandeOppdrag() {
		return oppdragshandsamar.getNumberOfRemainingMissions();
	}

	public Mission getOppdrag() {
		return oppdragshandsamar.getMissionAndRemoveItFromDeck();
	}

	public PlayerAndNetworkWTF getMinSpelar() {
		return minSpelar;
	}

	public void settSinTur(PlayerAndNetworkWTF spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(), isNetworkGame(), minSpelar.getNamn()); //todo minspelar er null init...
	}

	public void nesteSpelar() throws RemoteException {
		kvenSinTur = turhandsamar.nextPlayer(kvenSinTur,minSpelar);

		markIfItIsMyTurn();
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(), isNetworkGame(), minSpelar.getNamn());
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

	public abstract PlayerAndNetworkWTF findPlayerInAction();
	
	public void bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd,colour,kortKrevd,krevdJokrar,findPlayerInAction());
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void byggTunnel(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.byggTunnel(table, bygd, colour, kortKrevd, krevdJokrar, findPlayerInAction());
		hjelpemetodeBygg(bygd, colour, kortKrevd, krevdJokrar, byggjandeInfo.byggjandeSpelar, byggjandeInfo.jokrar);
	}

	public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge f) throws RemoteException {
		communicationWithPlayers.sendMessageAboutCard(kort, tilfeldig, f, kvenSinTur.getNamn(), this);
	}

	public void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF vert, Farge nyFarge, int i) throws RemoteException {
		communicationWithPlayers.newCardPlacedOnTableInNetworkGame(vert, nyFarge, i, this);
	}

	private void LagBrettet() throws RemoteException {
		rutehandsamar = new RouteHandlerImpl(gameVersion);
		oppdragshandsamar = new MissionHandlerImpl(gameVersion.getOppdrag());		// Legg til oppdrag
		bygghjelpar = new ByggHjelpar(gui);
		createTable();
	}
	
	protected abstract void createTable() throws RemoteException;

	private void hjelpemetodeBygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokrar) throws RemoteException{
		rutehandsamar.newRoute(bygd);

		messageUsersInNetworkGame(bygd, byggjandeSpelar);
		gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
		updateDeckOnTable(colour, kortKrevd, krevdJokrar, jokrar);
		gui.getMeldingarModell().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());

		communicationWithPlayers.oppdaterAndreSpelarar(colour, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);

		nesteSpelar();

	}

	private void messageUsersInNetworkGame(Route builtRoute, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		if (isNetworkGame()) {
			for (PlayerAndNetworkWTF player : players) {
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
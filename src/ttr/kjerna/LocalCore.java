package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.communicationWithPlayers.CommunicationWithPlayersLocal;
import ttr.gui.GUI;
import ttr.oppdrag.MissionHandler;
import ttr.rute.Route;
import ttr.rute.RouteHandler;
import ttr.spelar.IPlayer;
import ttr.turhandsamar.TurHandsamarLocal;
import ttr.utgaave.GameVersion;

public class LocalCore extends Core {
	public LocalCore(GUI gui, Table table, GameVersion gameVersion, ByggHjelpar buildingHelper, MissionHandler missionHandler, RouteHandler routeHandler) throws RemoteException {
		super(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		for (IPlayer player : players) {
			missionHandler.trekkOppdrag(gui, player, true);
		}
		// ??
	}

	public IPlayer findPlayerInAction() {
		return playerInTurn;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersLocal(players); // TODO dependency injection?
		createPlayersAndSetUpForLocalGame();
		turhandsamar = new TurHandsamarLocal(players);	
	}

	private void createPlayersAndSetUpForLocalGame() throws RemoteException {
		players = communicationWithPlayers.createPlayersForLocalGame(this,table); //todo playes må komme inn i arraylista her på eit vis
		if (myPlayer == null) {setMinSpelar(players.get(0)); }
		settSinTur(players.get(0));
		gui.addChatListener(myPlayer, players);
	}

	protected void messageUsersInNetworkGame(Route bygd, IPlayer byggjandeSpelar) { }

	protected String getWhoseTurnText() {
		return "min tur";
	}

	public void orientOtherPlayers(int positionOnTable) throws RemoteException {
		for (IPlayer player : getSpelarar()) {
			player.getRandomCardFromTheDeck(positionOnTable); // TODO kva er det her, og korfor? 
		}
	}
}
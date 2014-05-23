package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.communicationWithPlayers.CommunicationWithPlayersLocal;
import ttr.gui.IGUI;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.turhandsamar.TurHandsamarLocal;
import ttr.utgaave.GameVersion;

public class LocalCore extends Core {
	public LocalCore(IGUI gui, Table table, GameVersion gameVersion) throws RemoteException {
		super(gui, table, gameVersion);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		for (PlayerAndNetworkWTF player : players) {
			MissionHandlerImpl.trekkOppdrag(gui, player, true);
		}
		// ??
	}

	public PlayerAndNetworkWTF findPlayerInAction() {
		return kvenSinTur;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersLocal(players); // TODO dependency injection?
		createPlayersAndSetUpForLocalGame();
		turhandsamar = new TurHandsamarLocal(players);	
	}

	private void createPlayersAndSetUpForLocalGame() throws RemoteException {
		players = communicationWithPlayers.createPlayersForLocalGame(this,table); //todo playes må komme inn i arraylista her på eit vis
		if (minSpelar == null) {setMinSpelar(players.get(0)); }
		settSinTur(players.get(0));
	}

	protected void messageUsersInNetworkGame(Route bygd, PlayerAndNetworkWTF byggjandeSpelar) throws RemoteException { }

	@Override
	protected String getWhoseTurnText() {
		return "min tur";
	}

	@Override
	public void orientOtherPlayers(int positionOnTable) throws RemoteException {
		for (PlayerAndNetworkWTF player : getSpelarar()) {
			player.getRandomCardFromTheDeck(positionOnTable); // TODO kva er det her, og korfor? 
		}
	}
}
package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.gui.IGUI;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.utgaave.GameVersion;

public class LocalCore extends CoreImpl {
	public LocalCore(IGUI gui, Table table, GameVersion gameVersion) throws RemoteException  {
		super(gui, table, gameVersion);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		for (PlayerAndNetworkWTF player : players) {
			MissionHandlerImpl.trekkOppdrag(gui, player, true);
		}
		// ??
	}

	public boolean isNetworkGame() {
		return false;
	}

	public PlayerAndNetworkWTF findPlayerInAction() {
		return kvenSinTur;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersLocal(players); // TODO dependency injection?
		createPlayersAndSetUpForLocalGame();
		turhandsamar = new TurHandsamar(players,false);	
	}

	private void createPlayersAndSetUpForLocalGame() throws RemoteException {
		players = communicationWithPlayers.createPlayersForLocalGame(this,table); //todo playes må komme inn i arraylista her på eit vis
		if (minSpelar == null) {setMinSpelar(players.get(0)); }
		settSinTur(players.get(0));
	}
}
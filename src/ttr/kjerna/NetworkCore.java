package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.communicationWithPlayers.CommunicationWithPlayersNetwork;
import ttr.gui.IGUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.utgaave.GameVersion;

public class NetworkCore extends CoreImpl {
	public NetworkCore(IGUI gui, Table table, GameVersion gameVersion) throws RemoteException  {
		super(gui, table, gameVersion);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		nettverk.initialiseNetworkGame();
		MissionHandlerImpl.trekkOppdrag(gui, minSpelar, true);

		givePlayersMissions();
	}

	private void givePlayersMissions() throws RemoteException {
		for (PlayerAndNetworkWTF player : players){
			for (Mission mission : player.getOppdrag()){
				player.removeChosenMissionFromDeck(mission.getMissionId());
			}
		}
	}

	public boolean isNetworkGame() {
		return true;
	}

	public PlayerAndNetworkWTF findPlayerInAction() {
		return minSpelar;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersNetwork(players);
		turhandsamar = new TurHandsamar(players,true);		
	}
}

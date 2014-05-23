package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.gui.IGUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.utgaave.GameVersion;

public class NetworkCore extends CoreImpl {
	public NetworkCore(IGUI gui, Table table, GameVersion gameVersion) throws RemoteException  {
		super(gui, table, gameVersion);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		nettverk.initialiseNetworkGame();
		MissionHandlerImpl.trekkOppdrag(gui, minSpelar, true);

		givePlayersMissions(); //TODO bør denne metoden flyttast hit?
	}

	public boolean isNetworkGame() {
		return true;
	}
}

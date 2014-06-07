package ttr.kjerna;

import java.rmi.RemoteException;

import com.google.inject.Inject;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.gui.GUI;
import ttr.oppdrag.MissionHandler;
import ttr.rute.RouteHandler;
import ttr.utgaave.GameVersion;

public class CoreFactory {
	private Table table;
	private ByggHjelpar buildingHelper;

	@Inject
	public CoreFactory(Table table, ByggHjelpar buildingHelper) {
		this.table = table;
		this.buildingHelper = buildingHelper;
	}

	public Core createCore(GUI gui, MissionHandler missionHandler, GameVersion gameVersion, boolean isNetworkGame) throws RemoteException {
		RouteHandler routeHandler = new RouteHandler(gameVersion);

		return isNetworkGame ? 
				new NetworkCore(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler) : 
					new LocalCore(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler);

	}

}

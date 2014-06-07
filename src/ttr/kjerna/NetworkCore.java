package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.communicationWithPlayers.CommunicationWithPlayersNetwork;
import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;
import ttr.rute.Route;
import ttr.rute.RouteHandler;
import ttr.spelar.IPlayer;
import ttr.turhandsamar.TurHandsamarNetwork;
import ttr.utgaave.GameVersion;

public class NetworkCore extends Core {
	public NetworkCore(GUI gui, Table table, GameVersion gameVersion, ByggHjelpar buildingHelper, MissionHandler missionHandler, RouteHandler routeHandler) throws RemoteException  {
		super(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		nettverk.initialiseNetworkGame();
		missionHandler.trekkOppdrag(gui, myPlayer, true);

		givePlayersMissions();

		gui.addChatListener(myPlayer, players);
	}

	private void givePlayersMissions() throws RemoteException {
		for (IPlayer player : players){
			for (Mission mission : player.getOppdrag()){
				player.removeChosenMissionFromDeck(mission);
			}
		}
	}

	public IPlayer findPlayerInAction() {
		return myPlayer;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersNetwork(players);
		turhandsamar = new TurHandsamarNetwork(players);
	}

	protected void messageUsersInNetworkGame(Route builtRoute, IPlayer buildingPlayer) throws RemoteException {
		for (IPlayer player : players) {
			player.nybygdRute(builtRoute,buildingPlayer);
			player.setRemainingTrains(buildingPlayer.getSpelarNummer()+1, buildingPlayer.getGjenverandeTog());
		}
	}

	protected String getWhoseTurnText() throws RemoteException {
		return (myPlayer.getNamn().equals(playerInTurn.getNamn()) ? "min tur." : playerInTurn.getNamn() + " sin tur.");
	}

	public void orientOtherPlayers(int positionOnTable) throws RemoteException {
		IPlayer host = findHost();
		
		if (host != null){
			Colour newColour = host.getRandomCardFromTheDeck(positionOnTable);
			while (host.areThereTooManyJokersOnTable()) {
				placeNewCardsOnTable(host);
				newColour = host.getCardsOnTable()[positionOnTable];
			}
			newCardPlacedOnTableInNetworkGame(host, newColour, positionOnTable);
		}
	}

	private IPlayer findHost() throws RemoteException {
		IPlayer host = null;
		if (myPlayer.getSpelarNummer()==0) {
			host = myPlayer; // TODO forsvinn ikkje denne uansett i løpet av for-løkka under?
		}
		for (IPlayer player : players) {
			if (player.getSpelarNummer()==0) {
				return player;
			}
		}
		return host;
	}

	private void placeNewCardsOnTable(IPlayer host) throws RemoteException {
		host.leggUtFem();
		Colour[] cardsOnTable = host.getCardsOnTable();

		for (int plass = 0; plass < Konstantar.ANTAL_KORT_PÅ_BORDET; plass++){
			myPlayer.putCardOnTable(cardsOnTable[plass],plass);
			newCardPlacedOnTableInNetworkGame(host, cardsOnTable[plass], plass);
		}
	}

	private void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour newColour, int position) throws RemoteException {
		communicationWithPlayers.newCardPlacedOnTableInNetworkGame(host, newColour, position, this);
	}
}
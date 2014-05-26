package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.communicationWithPlayers.CommunicationWithPlayersNetwork;
import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandler;
import ttr.rute.Route;
import ttr.spelar.IPlayer;
import ttr.turhandsamar.TurHandsamarNetwork;
import ttr.utgaave.GameVersion;

public class NetworkCore extends Core {
	public NetworkCore(GUI gui, Table table, GameVersion gameVersion) throws RemoteException  {
		super(gui, table, gameVersion);
	}

	public void settIGangSpelet(String hostAddress) throws RemoteException {
		InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		nettverk.initialiseNetworkGame();
		MissionHandler.trekkOppdrag(gui, minSpelar, true);

		givePlayersMissions();
	}

	private void givePlayersMissions() throws RemoteException {
		for (IPlayer player : players){
			for (Mission mission : player.getOppdrag()){
				player.removeChosenMissionFromDeck(mission);
			}
		}
	}

	public IPlayer findPlayerInAction() {
		return minSpelar;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersNetwork(players);
		turhandsamar = new TurHandsamarNetwork(players);		
	}

	protected void messageUsersInNetworkGame(Route builtRoute, IPlayer buildingPlayer) throws RemoteException {
		for (IPlayer player : players) {
			player.nybygdRute(builtRoute,buildingPlayer);
			player.setTogAtt(buildingPlayer.getSpelarNummer()+1, buildingPlayer.getGjenverandeTog());
		}
	}

	@Override
	protected String getWhoseTurnText() throws RemoteException {
		return (minSpelar.getNamn().equals(kvenSinTur.getNamn()) ? "min tur." : kvenSinTur.getNamn() + " sin tur.");
	}

	@Override
	public void orientOtherPlayers(int positionOnTable) throws RemoteException {
		IPlayer host = findHost();
		
		if (host != null){
			Colour newColour = host.getRandomCardFromTheDeck(positionOnTable);
			while (host.areThereTooManyJokersOnTable()) {
				newColour = placeNewCardsOnTable(host);
			}
			newCardPlacedOnTableInNetworkGame(host, newColour, positionOnTable);
		}
	}

	private IPlayer findHost() throws RemoteException {
		IPlayer host = null;
		if (minSpelar.getSpelarNummer()==0) {
			host = minSpelar; // TODO forsvinn ikkje denne uansett i løpet av for-løkka under?
		}
		for (IPlayer player : players) {
			if (player.getSpelarNummer()==0) {
				return player;
			}
		}
		return host;
	}

	private Colour placeNewCardsOnTable(IPlayer host) throws RemoteException {
		Colour newColour = null;
		host.leggUtFem();
		Colour[] cardsOnTable = host.getCardsOnTable();

		for (int plass = 0; plass < Konstantar.ANTAL_KORT_PÅ_BORDET; plass++){
			newColour = cardsOnTable[plass];
			minSpelar.putCardOnTable(newColour,plass);
			newCardPlacedOnTableInNetworkGame(host, newColour, plass);
		}
		return newColour;
	}

	private void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour newColour, int i) throws RemoteException {
		communicationWithPlayers.newCardPlacedOnTableInNetworkGame(host, newColour, i, this);
	}
}
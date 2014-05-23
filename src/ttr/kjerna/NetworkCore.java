package ttr.kjerna;

import java.rmi.RemoteException;

import ttr.bord.Table;
import ttr.communicationWithPlayers.CommunicationWithPlayersNetwork;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionHandlerImpl;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.turhandsamar.TurHandsamarNetwork;
import ttr.utgaave.GameVersion;

public class NetworkCore extends Core {
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

	public PlayerAndNetworkWTF findPlayerInAction() {
		return minSpelar;
	}

	protected void createTable() throws RemoteException {
		communicationWithPlayers = new CommunicationWithPlayersNetwork(players);
		turhandsamar = new TurHandsamarNetwork(players);		
	}

	protected void messageUsersInNetworkGame(Route builtRoute, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		for (PlayerAndNetworkWTF player : players) {
			player.nybygdRute(builtRoute,buildingPlayer);
			player.setTogAtt(buildingPlayer.getSpelarNummer()+1, buildingPlayer.getGjenverandeTog());
		}
	}

	@Override
	protected String getWhoseTurnText() throws RemoteException {
		return (minSpelar.equals(kvenSinTur) ? "min tur." : kvenSinTur.getNamn() + " sin tur.");
	}

	@Override
	public void orientOtherPlayers(int positionOnTable) throws RemoteException {
		PlayerAndNetworkWTF host = findHost();
		
		if (host != null){
			Farge newColour = host.getRandomCardFromTheDeck(positionOnTable);
			while (host.areThereTooManyJokersOnTable()) {
				newColour = placeNewCardsOnTable(host);
			}
			newCardPlacedOnTableInNetworkGame(host, newColour, positionOnTable);
		}
	}

	private PlayerAndNetworkWTF findHost() throws RemoteException {
		PlayerAndNetworkWTF host = null;
		if (minSpelar.getSpelarNummer()==0) {
			host = minSpelar; // TODO forsvinn ikkje denne uansett i løpet av for-løkka under?
		}
		for (PlayerAndNetworkWTF player : getSpelarar()) {
			if (player.getSpelarNummer()==0) {
				return player;
			}
		}
		return host;
	}

	private Farge placeNewCardsOnTable(PlayerAndNetworkWTF host) throws RemoteException {
		Farge newColour = null;
		host.leggUtFem();
		Farge[] cardsOnTable = host.getCardsOnTable();

		for (int plass = 0; plass < Konstantar.ANTAL_KORT_PÅ_BORDET; plass++){
			newColour = cardsOnTable[plass];
			getMinSpelar().putCardOnTable(newColour,plass);
			newCardPlacedOnTableInNetworkGame(host, newColour, plass);
		}
		return newColour;
	}

	private void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge newColour, int i) throws RemoteException {
		communicationWithPlayers.newCardPlacedOnTableInNetworkGame(host, newColour, i, this);
	}
}
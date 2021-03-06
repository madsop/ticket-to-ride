package ttr.communicationWithPlayers;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.MeldingarModell;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.oppdrag.PlayerMissionHandler;
import ttr.rute.Route;
import ttr.spelar.IPlayer;
import ttr.spelar.PlayerAndNetworkWTF;

public class CommunicationWithPlayersLocal extends CommunicationWithPlayers {

	public CommunicationWithPlayersLocal(ArrayList<IPlayer> players) {
		super(players);
	}

	public ArrayList<IPlayer> createPlayersForLocalGame(Core hovud, Table bord) {
		int antalSpelarar = addPlayers();
		try {
			return createPlayers(hovud, bord, antalSpelarar);
		} catch (RemoteException ignored) { }
		return new ArrayList<>();
	}
	
	
	@Override
	public void updateOtherPlayers(Colour colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) { }

	@Override
	public void sjekkOmFerdig(MeldingarModell meldingarModell,	IPlayer kvenSinTur, String speltittel, IPlayer minSpelar) throws RemoteException {
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			new GameFinisherLocal(players).finishGame(meldingarModell, speltittel, minSpelar);
		}
	}

	private int addPlayers() {
		int numberOfPlayers = 0;
		Object[] legalNumberOfPlayers = {2,3};
		while ((numberOfPlayers != 2) && (numberOfPlayers != 3)) {
			numberOfPlayers = 2+ JOptionPane.showOptionDialog(null, Infostrengar.KorMangeMed, Infostrengar.AntalSpelarar, 0, 3, null, legalNumberOfPlayers, 2);
		}
		return numberOfPlayers;
	}

	private ArrayList<IPlayer> createPlayers(Core hovud, Table bord, int antalSpelarar) throws HeadlessException, RemoteException {
		players = new ArrayList<>();
		for (int i = 1; i <= antalSpelarar; i++) {
			players.add(new PlayerAndNetworkWTF(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord, new PlayerMissionHandler()));
		}
		return players;
	}
	
	@Override
	protected void localOrNetworkSpecificMessageStuff(IPlayer myPlayer, String melding) { }

	@Override
	public void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour nyFarge, int position, Core hovud) { }

}

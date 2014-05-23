package ttr.communicationWithPlayers;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.spelar.PlayerNetworkClass;

public class CommunicationWithPlayersLocal extends CommunicationWithPlayersImpl {

	public CommunicationWithPlayersLocal(ArrayList<PlayerAndNetworkWTF> spelarar) {
		super(spelarar);
	}

	public ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(Core hovud, Table bord) {
		int antalSpelarar = addPlayers();
		try {
			return createPlayers(hovud, bord, antalSpelarar);
		} catch (RemoteException ignored) { }
		return new ArrayList<>();
	}
	
	
	@Override
	public void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException { }

	@Override
	public void sjekkOmFerdig(IMeldingarModell meldingarModell,	PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter)
					throws RemoteException {
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			orientPlayersThatTheGameIsOver(meldingarModell);

			int[] totalpoeng = new int[players.size()];

			PlayerAndNetworkWTF vinnar = null;
			int vinnarpoeng = 0;
			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			String pointsString = Infostrengar.SpeletErFerdig;

			for (PlayerAndNetworkWTF player : players) {
				PlayerAndNetworkWTF leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell,ruter );
				vinnarpoeng = reknUtPoeng(leiar,ruter);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
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

	private ArrayList<PlayerAndNetworkWTF> createPlayers(Core hovud, Table bord, int antalSpelarar) throws HeadlessException, RemoteException {
		players = new ArrayList<>();
		for (int i = 1; i <= antalSpelarar; i++) {
			players.add(new PlayerNetworkClass(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord));
		}
		return players;
	}
	
	@Override
	protected void orientOthers(IMeldingarModell meldingarModell) { }

	@Override
	protected void orientNetwork(IMeldingarModell meldingarModell, PlayerAndNetworkWTF playerWithMostMissionsAccomplished, int bestNumberOfMissionsAccomplished) 
			throws RemoteException { }

	@Override
	protected void localOrNetworkSpecificMessageStuff(PlayerAndNetworkWTF myPlayer, String melding)
			throws RemoteException { }

	@Override
	public void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge nyFarge, int position, Core hovud) throws RemoteException { }

}

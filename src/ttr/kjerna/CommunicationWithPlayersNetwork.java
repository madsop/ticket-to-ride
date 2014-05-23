package ttr.kjerna;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

public class CommunicationWithPlayersNetwork extends CommunicationWithPlayersImpl {
	public CommunicationWithPlayersNetwork(ArrayList<PlayerAndNetworkWTF> spelarar) {
		super(spelarar);
	}

	@Override
	public void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException {				
		for (PlayerAndNetworkWTF player : players) {
			player.leggIStokken(colour, (kortKrevd-(jokrar-krevdJokrar)));
			player.leggIStokken(Farge.valfri,jokrar);
			player.receiveMessage(byggjandeNamn + " bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		}
	}

	@Override
	protected void orientNetwork(IMeldingarModell meldingarModell, PlayerAndNetworkWTF playerWithMostMissionsAccomplished, int bestNumberOfMissionsAccomplished) 
			throws RemoteException {
		meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
	}

	@Override
	public void sjekkOmFerdig(IMeldingarModell meldingarModell, PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter) throws RemoteException{
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			orientPlayersThatTheGameIsOver(meldingarModell);

			int[] totalpoeng = new int[players.size() + 1];

			PlayerAndNetworkWTF vinnar = null;
			int vinnarpoeng = 0;
			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			String pointsString = Infostrengar.SpeletErFerdig;
			totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar,ruter);
			pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
			vinnar = minSpelar;
			vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];

			for (PlayerAndNetworkWTF player : players) {
				PlayerAndNetworkWTF leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell,ruter );
				vinnarpoeng = reknUtPoeng(leiar,ruter);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
		}
	}
	
	protected void orientOthers(IMeldingarModell meldingarModell) {
		meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);
	}
	
	@Override
	protected void localOrNetworkSpecificMessageStuff(PlayerAndNetworkWTF myPlayer, String melding) throws RemoteException {
		myPlayer.receiveMessage(melding);
	}
	

	public void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge nyFarge, int position, Core hovud) throws RemoteException{
		if (iAmHost(host, hovud)){
			orientPlayersAboutNewCardOnTable(nyFarge, position, hovud.getSpelarar());
		}
		else {
			orientPlayersAndHostAboutNewCardOnTable(host, nyFarge, position, hovud.getSpelarar(), hovud.getMinSpelar());
		}
	}

	private boolean iAmHost(PlayerAndNetworkWTF vert, Core hovud) throws RemoteException {
		return vert.getNamn().equals(hovud.getMinSpelar().getNamn());
	}

	private void orientPlayersAboutNewCardOnTable(Farge nyFarge, int position, Collection<PlayerAndNetworkWTF> players) throws RemoteException {
		for (PlayerAndNetworkWTF player : players){
			// metode for å legge kortet host nettopp trakk på plass i på bordet hos spelar s
			player.putCardOnTable(nyFarge,position);
		}
	}

	private void orientPlayersAndHostAboutNewCardOnTable(PlayerAndNetworkWTF host, Farge nyFarge, int position, ArrayList<PlayerAndNetworkWTF> players, PlayerAndNetworkWTF myPlayer) throws RemoteException {
		myPlayer.putCardOnTable(nyFarge, position);
		for (PlayerAndNetworkWTF player : players){
			if (!host.getNamn().equals(player.getNamn())){
				player.putCardOnTable(nyFarge, position);
			}
		}
	}

	@Override
	public ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(Core hovud,	Table bord) { return null; }
}
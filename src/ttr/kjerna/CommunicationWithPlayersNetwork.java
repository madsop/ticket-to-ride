package ttr.kjerna;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

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
	protected void orientNetwork(IMeldingarModell meldingarModell,
			PlayerAndNetworkWTF playerWithMostMissionsAccomplished,
			int bestNumberOfMissionsAccomplished) throws RemoteException {
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
	
	@Override
	protected void orientPlayersThatTheGameIsOver(IMeldingarModell meldingarModell) throws RemoteException {
		meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);
		for (PlayerAndNetworkWTF player : players){
			player.receiveMessage(Infostrengar.SpeletErFerdig);
		}
	}
	
	@Override
	public void sendMessageAboutCard(boolean card, boolean random, Farge colour, String handlandespelarsNamn, Core hovud) throws RemoteException{
		String melding = handlandespelarsNamn;
		melding += card ? " trakk inn " + colour +"." : " trakk oppdrag.";

		hovud.getMinSpelar().receiveMessage(melding);

		for (PlayerAndNetworkWTF player : hovud.getSpelarar()){
			sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
		}
	}
}
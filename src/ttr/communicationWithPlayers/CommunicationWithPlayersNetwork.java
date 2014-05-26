package ttr.communicationWithPlayers;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.MeldingarModell;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

public class CommunicationWithPlayersNetwork extends CommunicationWithPlayers {
	public CommunicationWithPlayersNetwork(ArrayList<IPlayer> spelarar) {
		super(spelarar);
	}

	public void updateOtherPlayers(Colour colour, int kortKrevd, int numberOfJokers, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException {				
		for (IPlayer player : players) {
			player.putCardsInDeck(colour, (kortKrevd-(numberOfJokers-krevdJokrar)));
			player.putCardsInDeck(Colour.valfri, numberOfJokers);
			player.receiveMessage(byggjandeNamn + " bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
		}
	}

	@Override
	protected void orientNetwork(MeldingarModell meldingarModell, IPlayer playerWithMostMissionsAccomplished, int bestNumberOfMissionsAccomplished) throws RemoteException {
		meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
	}

	@Override
	public void sjekkOmFerdig(MeldingarModell meldingarModell, IPlayer kvenSinTur, String speltittel, IPlayer minSpelar) throws RemoteException {
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			orientPlayersThatTheGameIsOver(meldingarModell);

			int[] totalpoeng = new int[players.size() + 1];

			IPlayer vinnar = null;
			int vinnarpoeng = 0;
			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			String pointsString = Infostrengar.SpeletErFerdig;
			totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar);
			pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
			vinnar = minSpelar;
			vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];

			for (IPlayer player : players) {
				IPlayer leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell);
				vinnarpoeng = reknUtPoeng(leiar);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
		}
	}
	
	protected void orientOthers(MeldingarModell meldingarModell) {
		meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);
	}
	
	@Override
	protected void localOrNetworkSpecificMessageStuff(IPlayer myPlayer, String melding) throws RemoteException {
		myPlayer.receiveMessage(melding);
	}
	

	public void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour nyFarge, int position, Core hovud) throws RemoteException {
		if (iAmHost(host, hovud)){
			orientPlayersAboutNewCardOnTable(nyFarge, position, hovud.getSpelarar());
		}
		else {
			orientPlayersAndHostAboutNewCardOnTable(host, nyFarge, position, hovud.getSpelarar(), hovud.getMinSpelar());
		}
	}

	private boolean iAmHost(IPlayer vert, Core hovud) throws RemoteException {
		return vert.getNamn().equals(hovud.getMinSpelar().getNamn());
	}

	private void orientPlayersAboutNewCardOnTable(Colour nyFarge, int position, ArrayList<IPlayer> arrayList) throws RemoteException {
		for (IPlayer player : arrayList){
			// metode for å legge kortet host nettopp trakk på plass i på bordet hos spelar s
			player.putCardOnTable(nyFarge,position);
		}
	}

	private void orientPlayersAndHostAboutNewCardOnTable(IPlayer host, Colour nyFarge, int position, ArrayList<IPlayer> arrayList, IPlayer myPlayer) throws RemoteException {
		myPlayer.putCardOnTable(nyFarge, position);
		for (IPlayer player : arrayList){
			if (!host.getNamn().equals(player.getNamn())){
				player.putCardOnTable(nyFarge, position);
			}
		}
	}

	@Override
	public ArrayList<IPlayer> createPlayersForLocalGame(Core hovud,	Table bord) { return null; }
}
package ttr.communicationWithPlayers;

import ttr.bord.Table;
import ttr.data.*;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.IPlayer;
import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class CommunicationWithPlayers {
	protected ArrayList<IPlayer> players;

	public CommunicationWithPlayers (ArrayList<IPlayer> players) {
		this.players = players;
	}

	public abstract void updateOtherPlayers(Colour colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
	public abstract ArrayList<IPlayer> createPlayersForLocalGame(Core hovud, Table bord);
	public abstract void sjekkOmFerdig(MeldingarModell meldingarModell, IPlayer kvenSinTur, String speltittel, IPlayer minSpelar) throws RemoteException;
	
	public void sendMessageAboutCard(boolean card, boolean random, Colour colour, String handlandespelarsNamn, Core hovud) throws RemoteException {
		String melding = handlandespelarsNamn + (card ? " trakk inn " + colour +"." : " trakk oppdrag.");

		localOrNetworkSpecificMessageStuff(hovud.getMinSpelar(), melding);
		
		for (IPlayer player : hovud.getSpelarar()) {
			if (hovud.getKvenSinTur()==player) { //TODO eh, hæ?
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
			}
		}
	}

	protected abstract void localOrNetworkSpecificMessageStuff(IPlayer myPlayer, String melding) throws RemoteException;

	protected void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, IPlayer player) throws RemoteException {
		if (!random){
			player.receiveMessage(melding);
		}
		else if(card){
			player.receiveMessage(handlandespelarsNamn + " trakk tilfeldig.");
		}
	}

	public abstract void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour nyFarge, int position, Core hovud) throws RemoteException;
}
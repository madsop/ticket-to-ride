package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.*;
import ttr.rute.IRute;
import ttr.spelar.ISpelar;
import ttr.spelar.PlayerNetworkClass;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public class KommunikasjonMedSpelarar implements IKommunikasjonMedSpelarar {
	private final boolean nett;
	private Collection<ISpelar> players;

	public KommunikasjonMedSpelarar (boolean nett, Collection<ISpelar> spelarar) {
		this.nett = nett;
		this.players = spelarar;
	}

	public void oppdaterAndreSpelarar(int plass, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, IRute bygd) throws RemoteException {
		if (nett) {
			for (ISpelar s : players) {
				s.leggIStokken(plass, (kortKrevd-(jokrar-krevdJokrar)));
				s.leggIStokken(Konstantar.ANTAL_FARGAR-1,jokrar);
				s.faaMelding(byggjandeNamn + " bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getFarge());
			}
		}
	}

	public void createPlayersForLocalGame(IHovud hovud, IBord bord) {
		int antalSpelarar = addPlayers();
		try {
			createPlayers(hovud, bord, antalSpelarar);
		} catch (RemoteException ignored) { }
	}

	private int addPlayers() {
		int numberOfPlayers = 0;
		Object[] legalNumberOfPlayers = {2,3};
		while ((numberOfPlayers != 2) && (numberOfPlayers != 3)) {
			numberOfPlayers = 2+ JOptionPane.showOptionDialog(null, Infostrengar.KorMangeMed, Infostrengar.AntalSpelarar, 0, 3, null, legalNumberOfPlayers, 2);
		}
		return numberOfPlayers;
	}

	private void createPlayers(IHovud hovud, IBord bord, int antalSpelarar) throws HeadlessException, RemoteException {
		players = new ArrayList<>();
		for (int i = 1; i <= antalSpelarar; i++) {
			players.add(new PlayerNetworkClass(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord));
		}
	}

	public void sjekkOmFerdig(IMeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<IRute> ruter) throws RemoteException{
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			String pointsString = Infostrengar.SpeletErFerdig;

			int[] totalpoeng = new int[players.size() + (nett ? 1 : 0)];

			ISpelar vinnar = null;
			int vinnarpoeng = 0;

			if (nett) {meldingarModell.nyMelding(pointsString);}
			for (ISpelar player : players){
				player.faaMelding(pointsString);
			}

			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			if (nett) {
				totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar,ruter);
				pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
				vinnar = minSpelar;
				vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];
			}

			for (ISpelar player : players) {
				ISpelar leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell,ruter );
				vinnarpoeng = reknUtPoeng(leiar,ruter);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
		}
	}

	//TODO Legg inn spelutgaave-spesifikk bonus her - lengst rute for Europe
	private void addGameSpecificBonus(IMeldingarModell meldingarModell,	String speltittel, ISpelar minSpelar, int[] totalpoeng)	throws RemoteException {
		if (speltittel.equals(Nordic.tittel)){
			finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
		}
	}

	private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, ISpelar minSpelar, IMeldingarModell meldingarModell) throws RemoteException {
		ISpelar playerWithMostMissionsAccomplished = getPlayerWithMostMissionsAccomplished(minSpelar);
		int bestNumberOfMissionsAccomplished = playerWithMostMissionsAccomplished.getAntalFullfoerteOppdrag();
		totalpoeng[playerWithMostMissionsAccomplished.getSpelarNummer()] = 10;

		if (nett){
			meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
		}
		for (ISpelar s : players){
			s.faaMelding(playerWithMostMissionsAccomplished.getNamn() +" klarte flest oppdrag, " +bestNumberOfMissionsAccomplished);
		}
	}

	private ISpelar getPlayerWithMostMissionsAccomplished(ISpelar minSpelar) throws RemoteException {
		ISpelar flest = null;
		int flestoppdrag = -1;
		if (nett){
			flestoppdrag = minSpelar.getAntalFullfoerteOppdrag();
			flest = minSpelar;
		}
		for (ISpelar player : players){
			int oppdragspoeng = player.getAntalFullfoerteOppdrag();

			if (oppdragspoeng > flestoppdrag){
				flestoppdrag = oppdragspoeng;
				flest = player;
			}
		}
		return flest;
	}

	private String informTheOthersAboutMyPoints(IMeldingarModell messagesModel, ISpelar myPlayer, int[] totalpoeng) throws RemoteException {
		String pointsStringForMyPlayer = myPlayer.getNamn() + " fekk " + totalpoeng[myPlayer.getSpelarNummer()] + " poeng. ";
		messagesModel.nyMelding(pointsStringForMyPlayer);
		for (ISpelar player : players){
			player.faaMelding(pointsStringForMyPlayer);
		}
		return " " + pointsStringForMyPlayer;
	}


	private void avsluttSpeletMedSuksess(ISpelar vinnar,String pointsString, IMeldingarModell meldingarModell) throws RemoteException {
		String poeng = pointsString;
		String vinnaren = vinnar.getNamn() +" vann spelet, gratulerer!";
		poeng += vinnaren;
		meldingarModell.nyMelding(vinnaren);
		for (ISpelar player : players){
			player.faaMelding(vinnaren);
			player.visSpeletErFerdigmelding(poeng);
		}
		JOptionPane.showMessageDialog(new JPanel(), poeng);
	}

	private ISpelar reknUtPoengOgFinnVinnar(int[] totalpoeng, ISpelar s, int vinnarpoeng, ISpelar vinnar, IMeldingarModell meldingarModell, Set<IRute> ruter) throws RemoteException {
		ISpelar leiarNo = vinnar;

		int spelarensPoeng = reknUtPoeng(s,ruter);

		String sp = s.getNamn() +" fekk " +totalpoeng[s.getSpelarNummer()] +" poeng. ";
		meldingarModell.nyMelding(sp);
		for (ISpelar t : players){
			t.faaMelding(sp);
		}
		if (spelarensPoeng>vinnarpoeng){
			leiarNo = s;
		}
		else if (vinnar != null && spelarensPoeng==vinnarpoeng){
			if (vinnar.getOppdragspoeng() < s.getOppdragspoeng()){
				leiarNo = s;
			}
		}
		return leiarNo;
	}

	private int reknUtPoeng(ISpelar s, Set<IRute> ruter) throws RemoteException {
		int poeng = s.getOppdragspoeng();
		for (int j = 0; j < s.getBygdeRuterSize(); j++) {
			for (IRute r : ruter) {
				if (s.getBygdeRuterId(j) == r.getRuteId()) {
					poeng += r.getVerdi();
				}
			}
		}
		return poeng;
	}

	public void sendKortMelding(boolean card, boolean random, Farge colour, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException{
		String melding = handlandespelarsNamn;
		melding += card ? " trakk inn " + colour +"." : " trakk oppdrag.";

		if (nett){
			hovud.getMinSpelar().faaMelding(melding);
		}

		for (ISpelar s : hovud.getSpelarar()){
			if (nett || hovud.getKvenSinTur()==s){
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, s);
			}
		}
	}

	private void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, ISpelar player) throws RemoteException {
		if (!random){
			player.faaMelding(melding);
		}
		else if(card && random){
			player.faaMelding(handlandespelarsNamn +" trakk tilfeldig.");
		}
	}



	public void nyPaaPlass(ISpelar host, Farge nyFarge, int i, IHovud hovud) throws RemoteException{
		if (iAmHost(host, hovud)){
			for (ISpelar s : hovud.getSpelarar()){
				// metode for å legge kortet host nettopp trakk på plass i på bordet hos spelar s
				s.setPaaBordet(nyFarge,i);
			}
		}
		else {
			hovud.getMinSpelar().setPaaBordet(nyFarge, i);
			for (ISpelar s : hovud.getSpelarar()){
				if (!host.getNamn().equals(s.getNamn())){
					s.setPaaBordet(nyFarge, i);
				}
			}
		}
	}

	private boolean iAmHost(ISpelar vert, IHovud hovud) throws RemoteException {
		return vert.getNamn().equals(hovud.getMinSpelar().getNamn());
	}
}

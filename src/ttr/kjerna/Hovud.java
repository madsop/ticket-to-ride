package ttr.kjerna;

import ttr.bord.IBord;
import ttr.bygg.ByggHjelpar;
import ttr.bygg.IByggHjelpar;
import ttr.bygg.ByggjandeInfo;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.nettverk.InitialiserNettverk;
import ttr.oppdrag.IOppdrag;
import ttr.oppdrag.IOppdragshandsamar;
import ttr.oppdrag.Oppdragshandsamar;
import ttr.rute.IRoute;
import ttr.rute.IRutehandsamar;
import ttr.rute.Rutehandsamar;
import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

// Handterer kven sin tur det er, og oppsett i startfasen av spelet
public class Hovud implements IHovud {

	private final ISpelUtgaave spel;
	private final IBord bord;
	private final ArrayList<ISpelar> players;
	private final IGUI gui;

	private final boolean nett;

	private ISpelar kvenSinTur;
	private ISpelar minSpelar;
	private IKommunikasjonMedSpelarar kommunikasjonMedSpelarar;
	private IOppdragshandsamar oppdragshandsamar;
	private IRutehandsamar rutehandsamar;
	private ITurhandsamar turhandsamar;
	private IByggHjelpar bygghjelpar;

	public Hovud(IGUI gui, IBord bord, boolean nett, ISpelUtgaave spel) throws RemoteException {
		this.gui = gui;
		this.nett = nett;
		this.spel = spel;
		this.bord = bord;
		players = new ArrayList<>();
		LagBrettet(nett);
	}

	public void settIGangSpelet(boolean nett, String hostAddress) throws RemoteException {
		if (nett) {
			startNetworkGame(hostAddress);
		}
		else {
			startLocalGame();
		}
	}

	public ArrayList<IOppdrag> getGjenverandeOppdrag() {
		return oppdragshandsamar.getRemainingMissions()   ;
	}

	public Set<IRoute> getRuter() {
		return rutehandsamar.getRuter();
	}

	public Set<IRoute> getAlleBygdeRuter() {
		return rutehandsamar.getAlleBygdeRuter();
	}

	public void setMinSpelar(ISpelar spelar){
		minSpelar = spelar;
	}

	public IBord getBord() {
		return bord;
	}
	public boolean isNett() {
		return nett;
	}

	public ISpelar getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<ISpelar> getSpelarar() {
		return players;
	}

	public IGUI getGui() {
		return gui;
	}

	public int getAntalGjenverandeOppdrag() {
		return oppdragshandsamar.getNumberOfRemainingMissions();
	}

	public IOppdrag getOppdrag() {
		return oppdragshandsamar.getMissionAndRemoveItFromDeck();
	}

	public ISpelar getMinSpelar() {
		return minSpelar;
	}

	public void settSinTur(ISpelar spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(),nett,minSpelar.getNamn());
	}

	public void nesteSpelar() throws RemoteException {
		kvenSinTur = turhandsamar.nextPlayer(kvenSinTur,minSpelar);

		markIfItIsMyTurn();
		gui.visKvenDetErSinTur(kvenSinTur.getNamn(),nett,minSpelar.getNamn());
		kvenSinTur.setEittKortTrektInn(false);

		kommunikasjonMedSpelarar.sjekkOmFerdig(gui.getMeldingarModell(),kvenSinTur,spel.toString(),minSpelar,rutehandsamar.getRuter());
	}

	private void markIfItIsMyTurn() throws RemoteException {
		if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
			gui.getSpelarnamn().setBackground(Color.YELLOW);
		}
	}

	public Set<IRoute> findRoutesNotYetBuilt() throws RemoteException {
		return rutehandsamar.findRoutesNotYetBuilt(players);
	}

	public void bygg(IRoute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd,plass,kortKrevd,krevdJokrar,minSpelar,kvenSinTur);
		ISpelar byggjandeSpelar = byggjandeInfo.byggjandeSpelar;
		int jokrar = byggjandeInfo.jokrar;
		hjelpemetodeBygg(bygd,plass,kortKrevd,krevdJokrar,byggjandeSpelar,jokrar);
	}

	public void byggTunnel(IRoute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
		ByggjandeInfo byggjandeInfo = bygghjelpar.byggTunnel(bord, bygd, plass, kortKrevd, krevdJokrar, minSpelar, kvenSinTur);
		ISpelar byggjandeSpelar = byggjandeInfo.byggjandeSpelar;
		int jokrar = byggjandeInfo.jokrar;
		hjelpemetodeBygg(bygd,plass,kortKrevd,krevdJokrar,byggjandeSpelar,jokrar);
	}

	public void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException {
		kommunikasjonMedSpelarar.sendKortMelding(kort,tilfeldig,f,kvenSinTur.getNamn(),nett,this);
	}

	public void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException {
		kommunikasjonMedSpelarar.nyPaaPlass(vert,nyFarge,i,this);
	}

	private void LagBrettet(boolean nett) throws RemoteException {
		rutehandsamar = new Rutehandsamar(spel);

		// Legg til oppdrag
		oppdragshandsamar = new Oppdragshandsamar(spel.getOppdrag());

		bygghjelpar = new ByggHjelpar(gui,nett);

		if (!nett) {
			createPlayersAndSetUpForLocalGame();
		}         // else er det nettverksspel og handterast seinare
		kommunikasjonMedSpelarar = new KommunikasjonMedSpelarar(nett,players); // TODO dependency injection?
		turhandsamar = new TurHandsamar(players,nett);
	 }

	 private void startNetworkGame(String hostAddress) throws RemoteException {
		 InitialiserNettverk nettverk = new InitialiserNettverk(gui, hostAddress, this);
		 nettverk.initialiseNetworkGame();
		 Oppdragshandsamar.trekkOppdrag(gui, minSpelar, true);

		 givePlayersMissions();
	 }

	 private void givePlayersMissions() throws RemoteException {
		 for (ISpelar player : players){
			 for (IOppdrag mission : player.getOppdrag()){
				 player.trekt(mission.getOppdragsid());
			 }
		 }
	 }

	 private void startLocalGame() throws RemoteException {
		 for (ISpelar s : players) {
			 Oppdragshandsamar.trekkOppdrag(gui, s, true);
		 }
		 // ??
	 }

	 private void createPlayersAndSetUpForLocalGame() throws RemoteException {
		 kommunikasjonMedSpelarar.createPlayersForLocalGame(this,bord);
		 settSinTur(players.get(0));
	 }

	 private void hjelpemetodeBygg(IRoute bygd,int plass,int kortKrevd,int krevdJokrar,ISpelar byggjandeSpelar,int jokrar) throws RemoteException{
		 rutehandsamar.nyRute(bygd);

		 messageUsersInNetworkGame(bygd, byggjandeSpelar);
		 gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
		 updateDeckOnTable(plass, kortKrevd, krevdJokrar, jokrar);
		 gui.getMeldingarModell().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());

		 kommunikasjonMedSpelarar.oppdaterAndreSpelarar(plass, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);

		 nesteSpelar();

	 }

	 private void messageUsersInNetworkGame(IRoute builtRoute, ISpelar buildingPlayer) throws RemoteException {
		 if (nett) {
			 for (ISpelar s : players) {
				 s.nybygdRute(builtRoute.getRouteId(),buildingPlayer);
				 s.setTogAtt(buildingPlayer.getSpelarNummer()+1, buildingPlayer.getGjenverandeTog());
			 }
		 }
	 }

	 private void updateDeckOnTable(int plass, int kortKrevd, int krevdJokrar, int jokrar) {
		 bord.getFargekortaSomErIgjenIBunken()[plass]+=(kortKrevd-(jokrar-krevdJokrar));
		 bord.getFargekortaSomErIgjenIBunken()[Konstantar.ANTAL_FARGAR-1]+=jokrar;
	 }


}
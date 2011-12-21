package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;
import ttr.spelar.SpelarImpl;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;
import ttr.utgaave.nordic.Nordic;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Hovud implements IHovud {

	// "Variablar"
	private final ISpelUtgaave spel;
	/*public ISpelUtgaave getSpel() {
		return spel;
	}
*/

	private final IBord bord;
	private final Set<Rute> ruter;
	private ArrayList<ISpelar> spelarar;
	private final IGUI gui;

	private final boolean nett;
	private int antalSpelarar;

	// Variablar
	private ISpelar kvenSinTur;
	private ArrayList<Rute> alleBygdeRuter;
	private ISpelar minSpelar;
    private IKommunikasjonMedSpelarar kommunikasjonMedSpelarar;
    private IOppdragshandsamar oppdragshandsamar;

	public Hovud(IGUI gui, IBord bord, boolean nett, ISpelUtgaave spel) throws RemoteException {
		this.gui = gui;
		this.nett = nett;
		this.spel = spel;
        this.bord = bord;
        spelarar = new ArrayList<ISpelar>();
        ruter = spel.getRuter();
		LagBrettet(nett);
	}


    @Override
    public ArrayList<IOppdrag> getGjenverandeOppdrag() {
        return oppdragshandsamar.getGjenverandeOppdrag()   ;
    }

    public Set<Rute> getRuter() {
		return ruter;
	}

	public ArrayList<Rute> getAlleBygdeRuter() {
		return alleBygdeRuter;
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
		return spelarar;
	}

	public IGUI getGui() {
		return gui;
	}

    @Override
    public int getAntalGjenverandeOppdrag() {
        return oppdragshandsamar.getAntalGjenverandeOppdrag();
    }

    @Override
    public IOppdrag getOppdrag() {
        return oppdragshandsamar.getOppdrag();
    }

    /**
	 * Startar spelet.
	 * @throws RemoteException 
	 */
	private void LagBrettet(boolean nett) throws RemoteException {
		alleBygdeRuter = new ArrayList<Rute>();

		// Legg til oppdrag
        oppdragshandsamar = new Oppdragshandsamar(spel.getOppdrag());

		if (!nett) {
			mekkSpelarar();
		}
		else { 
			// Nettverksspel. Handterast manuelt seinare.
		}
        this.kommunikasjonMedSpelarar = new KommunikasjonMedSpelarar(nett,spelarar);
	}

	/**
	 * Sett opp spelet for eit ikkje-nettverks-spel.
	 * @throws RemoteException
	 */
	private void mekkSpelarar() throws RemoteException {
		kommunikasjonMedSpelarar.mekkSpelarar(this);
		settSinTur(spelarar.get(0));
	}

	public ISpelar getMinSpelar() {
		return minSpelar;
	}

	/**
	 * Sett at det er denne spelaren sin tur;
	 * @throws RemoteException 
	 */
	public void settSinTur(ISpelar spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.setSpelarnamn(kvenSinTur.getNamn());
	}

	/** Spelaren er ferdig med sin tur, no er det neste spelar
	 * @throws RemoteException 
	 */
    private void nesteUtanNett(int sp){
		if (sp == spelarar.size()) {
			kvenSinTur = spelarar.get(0);
		}
		else {
			kvenSinTur = spelarar.get(sp);
		}
	}

	private void nesteMedNett() throws RemoteException{
		int no = kvenSinTur.getSpelarNummer();
		ISpelar host = null;
		if (minSpelar.getSpelarNummer() == 0) {
			host = minSpelar;
		}
		else {
			for (ISpelar s : spelarar) {
				if (s.getSpelarNummer() == 0) {
					host = s;
				}
			}
		}

		int neste;
        assert host != null;
        if (no+1 < host.getSpelarteljar()) {
			neste = no+1;
		}
		else {
			neste = 0;
		}

		if (minSpelar.getSpelarNummer() == neste) {
			kvenSinTur = minSpelar;
		}
		else {
			for (ISpelar s : spelarar) {
				if (s.getSpelarNummer() == neste) {
					kvenSinTur = s;
				}
			}
		}

		for (ISpelar s : spelarar) {
			s.settSinTur(kvenSinTur);
		}

		if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
			gui.getSpelarnamn().setBackground(Color.YELLOW);
		}
	}

	public void nesteSpelar() throws RemoteException {
		int sp = 0;
		for (int i = 0; i < spelarar.size(); i++) {
			if (spelarar.get(i) == kvenSinTur) {
				sp = i;
			}
		}
		sp++;

		if (!nett) {
			nesteUtanNett(sp);
		}

		if (nett) {
			nesteMedNett();
		}
		gui.setSpelarnamn(kvenSinTur.getNamn());
		kvenSinTur.setEinVald(false);

		kommunikasjonMedSpelarar.sjekkOmFerdig(gui.getMeldingarModell(),kvenSinTur,spel.getTittel(),minSpelar,ruter);
	}




	/**
	 * Finn alle ubygde ruter.
	 * @return Eit array over alle rutene som ikkje alt er bygd
	 * @throws RemoteException 
	 */
	public Rute[] finnFramRuter() throws RemoteException {
		Set<Rute> ruter = getRuter();
        for (ISpelar aSpelarar1 : spelarar) {
            for (int j = 0; j < aSpelarar1.getBygdeRuterStr(); j++) {
                int ruteId = aSpelarar1.getBygdeRuterId(j);
                for (Rute r : ruter) {
                    if (r.getRuteId() == ruteId && !(alleBygdeRuter.contains(r))) {
                        alleBygdeRuter.add(r);
                    }
                }
            }
        }

		int str = ruter.size();
		Rute[] ruterTemp = new Rute[str];
		Iterator<Rute> it2 = ruter.iterator();
		for (int i = 0; i < str; i++) {
			ruterTemp[i] = it2.next();
		}

		/*	ArrayList<Rute>	bR = new ArrayList<Rute>();
        for (ISpelar aSpelarar : spelarar) {
            for (int j = 0; j < aSpelarar.getBygdeRuter().size(); j++) {
                bR.add(aSpelarar.getBygdeRuter().get(j));
            }
            for (int j = 0; j < aSpelarar.getBygdeRuterStr(); j++) {
                int ruteId = aSpelarar.getBygdeRuterId(j);
                for (Rute r : ruter) {
                    if (r.getRuteId() == ruteId) {
                        bR.add(r);
                    }
                }
            }
        }              */
		 
		Rute[] ruterArray = new Rute[str-alleBygdeRuter.size()];
		int c = 0;

		for (int i = 0; i < str; i++) {
			boolean b = true;
            for (Rute anAlleBygdeRuter : alleBygdeRuter) {
                if (ruterTemp[i] == anAlleBygdeRuter) {
                    b = false;
                }
            }
			if (b) {
				Rute r = ruterTemp[i];
				if (c < ruterArray.length) {
					ruterArray[c] = r;
					c++;
				}

			}
		}
		return ruterArray;
	}

	private int velAntalJokrarDuVilBruke(Rute rute, ISpelar s, Farge valdFarge) throws RemoteException{
		int jokrar = s.getKort()[Konstantar.ANTAL_FARGAR-1];
		int kormange = -1;
		while (kormange < 0 || kormange > jokrar || kormange > rute.getLengde()) {
			String sendinn = "Kor mange jokrar vil du bruke på å byggje ruta? Du har " +jokrar +" jokrar, " +s.getKort()[Konstantar.finnPosisjonForFarg(valdFarge)] + " av fargen du skal byggje, og ruta er " +rute.getLengde() +" tog lang.";
			String km = JOptionPane.showInputDialog(sendinn,0);
			kormange = Integer.parseInt(km);
		}
		return kormange;
	}

	/**
	 * Byggjar ei rute.
	 * @param bygd - kva for rute.
	 * @param plass - kva for farge - i plass i farge-arrayet.
	 * @param spelarensKort - korta spelaren har.
	 * @param kortKrevd - kor mange vanlege kort ruta krev.
	 * @param krevdJokrar - kor mange jokrar som trengs for å byggje ruta.
	 * @throws RemoteException 
	 */
	private Farge valdFarge;
    
    private int byggValfriFarge(ISpelar byggjandeSpelar, int krevdJokrar, int kortKrevd) throws RemoteException {
        ArrayList<Farge> mulegeFargar = new ArrayList<Farge>();
        int ekstrajokrar = byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1]-krevdJokrar;
        System.out.println("ekstrajokrar: " +ekstrajokrar);
        for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
            if (i != Konstantar.ANTAL_FARGAR-1){
                if((byggjandeSpelar.getKort()[i] + ekstrajokrar) >= kortKrevd
                        && ekstrajokrar >= 0){
                    mulegeFargar.add(Konstantar.FARGAR[i]);
                }
            }
            else{
                if (ekstrajokrar >= kortKrevd){
                    mulegeFargar.add(Konstantar.FARGAR[i]);
                }
            }
        }

        if (mulegeFargar.size() > 0){
            int i = -2;
            while (i<0 || i > mulegeFargar.size()){
                i = JOptionPane.showOptionDialog((Component) gui, "Vel farge å byggje i", "Vel farge å byggje i", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));

                if (i==-1){
                    return -1;
                }
            }
            valdFarge = mulegeFargar.get(i);
            return Konstantar.finnPosisjonForFarg(valdFarge);
        }
        return -1;
        //System.out.println("vald farge er " +valdFarge);
    }
    
	public void bygg(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
		ISpelar byggjandeSpelar = nett ? minSpelar : kvenSinTur;

		if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
			plass = byggValfriFarge(byggjandeSpelar,krevdJokrar,kortKrevd);
            if (plass == -1) { return; }
		}
		else {
			valdFarge = bygd.getFarge();
		}

		int jokrar=0;

		// Vel kor mange jokrar du vil bruke
		if (byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] > 0) {
			do {
				jokrar = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar,valdFarge);
			} while(!(byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] >= jokrar 
					&& jokrar>=krevdJokrar && 
					byggjandeSpelar.getKort()[plass] >= kortKrevd-(jokrar-krevdJokrar)));
		}

		byggjandeSpelar.bygg(bygd);

		//Sjekk om spelaren har nok kort.
		if (!(jokrar <= byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] 
		                                          && (kortKrevd-(jokrar-krevdJokrar) <= byggjandeSpelar.getKort()[plass]))){
			if (bygd.getFarge() != Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
				JOptionPane.showMessageDialog((Component) gui, "Synd, men du har ikkje nok kort til det her. ");
			}
		}
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
			if(Konstantar.FARGAR[i]==valdFarge){
				plass = i;
			}
		}
		byggjandeSpelar.getKort()[plass] -= (kortKrevd-(jokrar-krevdJokrar));
		byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] -= jokrar;
		alleBygdeRuter.add(bygd);

		if (nett) {
			for (ISpelar s : spelarar) {
				s.nybygdRute(bygd.getRuteId(),byggjandeSpelar);
				s.setTogAtt(byggjandeSpelar.getSpelarNummer()+1, byggjandeSpelar.getGjenverandeTog());
			}
		}
		gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));		
		bord.getIgjenAvFargekort()[plass]+=(kortKrevd-(jokrar-krevdJokrar));
		bord.getIgjenAvFargekort()[Konstantar.ANTAL_FARGAR-1]+=jokrar;
		gui.getMeldingarModell().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getDestinasjonar().toArray()[0] + " - " +bygd.getDestinasjonar().toArray()[1] + " i farge " + bygd.getFarge());

		kommunikasjonMedSpelarar.oppdaterAndreSpelarar(plass, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);

		nesteSpelar();
	}


	public void byggTunnel(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
		Farge[] treTrekte = new Farge[3];
		int ekstra = 0;
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getTilfeldigKortFråBordet(0, false);
			if (treTrekte[i] == null){
				JOptionPane.showMessageDialog((Component) gui, "Det er ikkje fleire kort igjen på bordet, du må vente til det kjem kort i bunken før du kan prøve å byggje tunnelen.");
				return;
			}
			if (treTrekte[i] == Farge.valfri || treTrekte[i] == bygd.getFarge()) {
				ekstra++;
			}
		}

		int byggLell = JOptionPane.showConfirmDialog((Component) gui, "Du prøver å byggje ei rute som er tunnel. " +
				"Det krev at du snur tre kort. Det vart " +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2] 
				                                 +". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
		if (byggLell == JOptionPane.OK_OPTION) {
			bygg(bygd, plass, kortKrevd+ekstra, krevdJokrar);
		}
	}
}
package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;
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
	private final ArrayList<ISpelar> spelarar;
	private final IGUI gui;

	private final boolean nett;

    // Variablar
	private ISpelar kvenSinTur;
	private ISpelar minSpelar;
    private IKommunikasjonMedSpelarar kommunikasjonMedSpelarar;
    private IOppdragshandsamar oppdragshandsamar;
    private IRutehandsamar rutehandsamar;
    private IByggHjelpar bygghjelpar;



    public Hovud(IGUI gui, IBord bord, boolean nett, ISpelUtgaave spel) throws RemoteException {
		this.gui = gui;
		this.nett = nett;
		this.spel = spel;
        this.bord = bord;
        spelarar = new ArrayList<ISpelar>();
		LagBrettet(nett);
	}


    @Override
    public ArrayList<IOppdrag> getGjenverandeOppdrag() {
        return oppdragshandsamar.getGjenverandeOppdrag()   ;
    }

    @Override
    public Set<Rute> getRuter() {
        return rutehandsamar.getRuter();
    }

    @Override
    public ArrayList<Rute> getAlleBygdeRuter() {
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
        rutehandsamar = new Rutehandsamar(spel);

		// Legg til oppdrag
        oppdragshandsamar = new Oppdragshandsamar(spel.getOppdrag());

        bygghjelpar = new ByggHjelpar(gui,nett);

		if (!nett) {
			mekkSpelarar();
		}         // else er det nettverksspel og handterast seinare
        kommunikasjonMedSpelarar = new KommunikasjonMedSpelarar(nett,spelarar);
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
		int sp = 1;
		for (int i = 0; i < spelarar.size(); i++) {
			if (spelarar.get(i) == kvenSinTur) {
				sp +=i;
			}
		}

		if (!nett) {
			nesteUtanNett(sp);
		}

		if (nett) {
			nesteMedNett();
		}
		gui.setSpelarnamn(kvenSinTur.getNamn());
		kvenSinTur.setEinVald(false);

		kommunikasjonMedSpelarar.sjekkOmFerdig(gui.getMeldingarModell(),kvenSinTur,spel.getTittel(),minSpelar,rutehandsamar.getRuter());
	}

    @Override
    public Rute[] finnFramRuter() throws RemoteException {
        return rutehandsamar.finnFramRuter(spelarar);
    }

    public void bygg(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
        byggjandeInfo byggjandeInfo = bygghjelpar.bygg(bygd,plass,kortKrevd,krevdJokrar,minSpelar,kvenSinTur);
        ISpelar byggjandeSpelar = byggjandeInfo.byggjandeSpelar;
        int jokrar = byggjandeInfo.jokrar;

        rutehandsamar.nyRute(bygd);


        if (nett) {
            for (ISpelar s : spelarar) {
                s.nybygdRute(bygd.getRuteId(),byggjandeSpelar);
                s.setTogAtt(byggjandeSpelar.getSpelarNummer()+1, byggjandeSpelar.getGjenverandeTog());
            }
        }
        gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));
        bord.getFargekortaSomErIgjenIBunken()[plass]+=(kortKrevd-(jokrar-krevdJokrar));
        bord.getFargekortaSomErIgjenIBunken()[Konstantar.ANTAL_FARGAR-1]+=jokrar;
        gui.getMeldingarModell().nyMelding(byggjandeSpelar.getNamn() + "  bygde ruta " +bygd.getDestinasjonar().toArray()[0] + " - " +bygd.getDestinasjonar().toArray()[1] + " i farge " + bygd.getFarge());

        kommunikasjonMedSpelarar.oppdaterAndreSpelarar(plass, kortKrevd, jokrar, krevdJokrar, byggjandeSpelar.getNamn(), bygd);

        nesteSpelar();

    }

    @Override
    public void byggTunnel(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException {
        bygghjelpar.byggTunnel(bord,bygd,plass,kortKrevd,krevdJokrar,minSpelar,kvenSinTur);
    }

}
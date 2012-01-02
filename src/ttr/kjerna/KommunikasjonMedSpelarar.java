package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.data.MeldingarModell;
import ttr.rute.IRute;
import ttr.spelar.ISpelar;
import ttr.spelar.SpelarImpl;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;


public class KommunikasjonMedSpelarar implements IKommunikasjonMedSpelarar {
    private final boolean nett;
    private ArrayList<ISpelar> spelarar;

    public KommunikasjonMedSpelarar (boolean nett, ArrayList<ISpelar> spelarar) {
        this.nett = nett;
        this.spelarar = spelarar;
    }
    
    public void oppdaterAndreSpelarar(int plass, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, IRute bygd) throws RemoteException {
        if (nett) {
            for (ISpelar s : spelarar) {
                s.leggIStokken(plass, (kortKrevd-(jokrar-krevdJokrar)));
                s.leggIStokken(Konstantar.ANTAL_FARGAR-1,jokrar);
                s.faaMelding(byggjandeNamn + " bygde ruta " +bygd.getDestinasjonar().toArray()[0] + " - " +bygd.getDestinasjonar().toArray()[1] + " i farge " + bygd.getFarge());
            }
        }
    }

    /**
     * Sett opp spelet for eit ikkje-nettverks-spel.
     * @throws RemoteException
     */
    public void mekkSpelarar(IHovud hovud, IBord bord) {
        // Legg til spelarar
        int antalSpelarar = 0;
        while ( (antalSpelarar != 2) && (antalSpelarar != 3)) { // Sett antal spelarar
            Object[] val = {2,3};
            antalSpelarar = 2+ JOptionPane.showOptionDialog(null, Infostrengar.KorMangeMed, Infostrengar.AntalSpelarar, 0, 3, null, val, 2);
        }

        //antalSpelarar = 3;
        spelarar = new ArrayList<ISpelar>();
        for (int i = 1; i <= antalSpelarar; i++) { // Opprettar spelarar
            try {
                spelarar.add(new SpelarImpl(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord));
            }
            catch (RemoteException ignored) {

            }
        }
    }


    //TODO: Utrekning av lengst rute / flest oppdrag
    public void sjekkOmFerdig(MeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<IRute> ruter) throws RemoteException{
        if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
            String poeng = Infostrengar.SpeletErFerdig;

            int[] totalpoeng;
            if (!nett){
                totalpoeng = new int[spelarar.size()];
            }
            else {
                totalpoeng = new int[spelarar.size()+1];
            }

            ISpelar vinnar = null;
            int vinnarpoeng = 0;

            if (nett) {meldingarModell.nyMelding(poeng);}
            for (ISpelar s : spelarar){
                s.faaMelding(poeng);
            }

            if (speltittel.equals(Nordic.tittel)){
                finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
            }

            if (nett) {
                totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar,ruter);
                String p = minSpelar.getNamn() + " fekk " + totalpoeng[minSpelar.getSpelarNummer()] + " poeng. ";
                poeng += " " +p;
                meldingarModell.nyMelding(p);
                for (ISpelar s : spelarar){
                    s.faaMelding(p);
                }
                vinnar = minSpelar;
                vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];
            }


            for (ISpelar s : spelarar) {
                ISpelar leiar = reknUtPoengOgFinnVinnar(totalpoeng,s,poeng,vinnarpoeng,vinnar,meldingarModell,ruter );
                vinnarpoeng = reknUtPoeng(leiar,ruter);
            }
            // Legg inn spelutgaave-spesifikk bonus her
            avsluttSpeletMedSuksess(vinnar,poeng,meldingarModell);
        }
    }


    private void avsluttSpeletMedSuksess(ISpelar vinnar,String poeng, MeldingarModell meldingarModell) throws RemoteException {

        assert vinnar != null;
        String vinnaren = vinnar.getNamn() +" vann spelet, gratulerer!";
        poeng += vinnaren;
        meldingarModell.nyMelding(vinnaren);
        for (ISpelar s : spelarar){
            s.faaMelding(vinnaren);
            s.visSpeletErFerdigmelding(poeng);
        }
        JOptionPane.showMessageDialog(new JPanel(), poeng);
    }


    private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, ISpelar minSpelar, MeldingarModell meldingarModell) throws RemoteException{
        int flestoppdrag = -1;
        ISpelar flest = null;
        if (nett){
            flestoppdrag = minSpelar.getAntalFullfoerteOppdrag();
            flest = minSpelar;
        }
        for (ISpelar s : spelarar){
            int oppdragspoeng = s.getAntalFullfoerteOppdrag();

            if (oppdragspoeng > flestoppdrag){
                flestoppdrag = oppdragspoeng;
                flest = s;
            }
        }

        assert flest != null;
        totalpoeng[flest.getSpelarNummer()] = 10;

        if (nett){
            meldingarModell.nyMelding(flest.getNamn() + " klarte flest oppdrag, " + flestoppdrag);
        }
        for (ISpelar s : spelarar){
            s.faaMelding(flest.getNamn() +" klarte flest oppdrag, " +flestoppdrag);
        }
    }


    private ISpelar reknUtPoengOgFinnVinnar(int[] totalpoeng, ISpelar s, String poeng,int vinnarpoeng, ISpelar vinnar, MeldingarModell meldingarModell, Set<IRute> ruter) throws RemoteException {
        ISpelar leiarNo = vinnar;

        int spelarensPoeng = reknUtPoeng(s,ruter);

        String sp = s.getNamn() +" fekk " +totalpoeng[s.getSpelarNummer()] +" poeng. ";
        poeng += " " +sp;
        meldingarModell.nyMelding(sp);
        for (ISpelar t : spelarar){
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
        for (int j = 0; j < s.getBygdeRuterStr(); j++) {
            for (IRute r : ruter) {
                if (s.getBygdeRuterId(j) == r.getRuteId()) {
                    poeng += r.getVerdi();
                }
            }
        }
        return poeng;
    }

    public void sendKortMelding(boolean kort, boolean tilfeldig, Farge f, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException{
        String melding = handlandespelarsNamn;
        melding += kort ? " trakk inn " + f +"." : " trakk oppdrag.";

        if (nett){
            hovud.getMinSpelar().faaMelding(melding);
        }

        for (ISpelar s : hovud.getSpelarar()){
            if (nett || hovud.getKvenSinTur()==s){
                if (!tilfeldig){
                    s.faaMelding(melding);
                }
                else if(kort && tilfeldig){
                    s.faaMelding(handlandespelarsNamn +" trakk tilfeldig.");
                }
            }
        }
    }



    public void nyPaaPlass(ISpelar vert, Farge nyFarge, int i, IHovud hovud) throws RemoteException{
        if (vert.getNamn().equals(hovud.getMinSpelar().getNamn())){
            for (ISpelar s : hovud.getSpelarar()){
                // metode for å legge kortet vert nettopp trakk på plass i på bordet hos spelar s
                s.setPaaBordet(nyFarge,i);
            }
        }
        else {
            hovud.getMinSpelar().setPaaBordet(nyFarge, i);
            for (ISpelar s : hovud.getSpelarar()){
                if (!vert.getNamn().equals(s.getNamn())){
                    s.setPaaBordet(nyFarge, i);
                }
            }
        }
    }
}

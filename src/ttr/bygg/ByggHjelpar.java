package ttr.bygg;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.rute.IRute;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar implements IByggHjelpar {
    private final IGUI gui;
    private Farge valdFarge;
    private final boolean nett;

    public ByggHjelpar(IGUI gui, boolean nett) {
        this.gui = gui;
        this.nett = nett;
    }
    
        /**
         * Byggjar ei rute.
         * @param bygd - kva for rute.
         * @param plass - kva for farge - i plass i farge-arrayet.
         * @param spelarensKort - korta spelaren har.
         * @param kortKrevd - kor mange vanlege kort ruta krev.
         * @param krevdJokrar - kor mange jokrar som trengs for å byggje ruta.
         * @throws java.rmi.RemoteException
         */
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
                    i = JOptionPane.showOptionDialog((Component) gui, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));

                    if (i==-1){
                        return -1;
                    }
                }
                valdFarge = mulegeFargar.get(i);
                return Konstantar.finnPosisjonForFarg(valdFarge);
            }
            return -1;
        }

        public byggjandeInfo bygg(IRute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException {
            ISpelar byggjandeSpelar = nett ? minSpelar : kvenSinTur;

            if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
                plass = byggValfriFarge(byggjandeSpelar,krevdJokrar,kortKrevd);
                if (plass == -1) { return null; }
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
                    JOptionPane.showMessageDialog((Component) gui, Infostrengar.IkkjeNokKort);
                }
            }
            for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
                if(Konstantar.FARGAR[i]==valdFarge){
                    plass = i;
                }
            }
            byggjandeSpelar.getKort()[plass] -= (kortKrevd-(jokrar-krevdJokrar));
            byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] -= jokrar;

            return new byggjandeInfo(byggjandeSpelar,jokrar);
        }


        public byggjandeInfo byggTunnel(IBord bord, IRute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException {
            Farge[] treTrekte = new Farge[3];
            int ekstra = 0;
            for (int i = 0; i < treTrekte.length; i++) {
                treTrekte[i] = bord.getTilfeldigKortFråBordet(0, false);
                if (treTrekte[i] == null){
                    JOptionPane.showMessageDialog((Component) gui, Infostrengar.TomtPåBordet);
                    return null;
                }
                if (treTrekte[i] == Farge.valfri || treTrekte[i] == bygd.getFarge()) {
                    ekstra++;
                }
            }

            int byggLell = JOptionPane.showConfirmDialog((Component) gui, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
                    +treTrekte[1] +" og " +treTrekte[2]
                    +". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
            if (byggLell == JOptionPane.OK_OPTION) {
                return bygg(bygd, plass, kortKrevd+ekstra, krevdJokrar,minSpelar,kvenSinTur);
            }
            return null;
        }

    private int velAntalJokrarDuVilBruke(IRute rute, ISpelar s, Farge valdFarge) throws RemoteException{
        int jokrar = s.getKort()[Konstantar.ANTAL_FARGAR-1];
        int kormange = -1;
        while (kormange < 0 || kormange > jokrar || kormange > rute.getLengde()) {
            String sendinn = Infostrengar.AntalJokrarStart +jokrar +" jokrar, " +s.getKort()[Konstantar.finnPosisjonForFarg(valdFarge)] + " av fargen du skal byggje, og ruta er " +rute.getLengde() +" tog lang.";
            String km = JOptionPane.showInputDialog(sendinn,0);
            kormange = Integer.parseInt(km);
        }
        return kormange;
    }
}

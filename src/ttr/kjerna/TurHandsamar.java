package ttr.kjerna;

import ttr.spelar.ISpelar;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class TurHandsamar implements ITurhandsamar {
    private final ArrayList<ISpelar> spelarar;
    private final boolean nett;
    
    TurHandsamar(ArrayList<ISpelar> spelarar, boolean nett) {
        this.spelarar = spelarar;
        this.nett = nett;
    }


    /** Spelaren er ferdig med sin tur, no er det neste spelar
     * @throws java.rmi.RemoteException
     */
    private ISpelar nesteUtanNett(int sp){
        if (sp == spelarar.size()) {
            return spelarar.get(0);
        }
        else {
            return spelarar.get(sp);
        }
    }

    private ISpelar nesteMedNett(ISpelar kvenSinTur, ISpelar minSpelar) throws RemoteException {
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

        return kvenSinTur;
    }

    public ISpelar nesteSpelar(ISpelar kvenSinTur, ISpelar minSpelar) throws RemoteException{
        int sp = 1;
        for (int i = 0; i < spelarar.size(); i++) {
            if (spelarar.get(i) == kvenSinTur) {
                sp +=i;
            }
        }

        return nett ? nesteMedNett(kvenSinTur, minSpelar) : nesteUtanNett(sp);
    }
}

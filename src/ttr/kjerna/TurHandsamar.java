package ttr.kjerna;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TurHandsamar implements ITurhandsamar {
    private final ArrayList<ISpelar> spelarar;
    private final boolean nett;
    
    TurHandsamar(ArrayList<ISpelar> spelarar, boolean nett) {
        this.spelarar = spelarar;
        this.nett = nett;
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

    private ISpelar nesteMedNett(final ISpelar kvenSinTur, final ISpelar minSpelar) throws RemoteException {
        int no = kvenSinTur.getSpelarNummer();
        ISpelar host = findHost(minSpelar);
        int neste = findNextNumber(no, host.getSpelarteljar());
        ISpelar sinTur = findOutWhoseTurnItIs(minSpelar, neste);
        
        for (ISpelar s : spelarar) {
            s.settSinTur(sinTur);
        }
        return sinTur;
    }
    
	private ISpelar findHost(final ISpelar minSpelar) throws RemoteException {
		if (minSpelar.getSpelarNummer() == 0) {
            return minSpelar;
        }
		for (ISpelar s : spelarar) {
		    if (s.getSpelarNummer() == 0) {
		        return s;
		    }
		}
		return null;
	}

	private int findNextNumber(final int no, int playerCount) {
		if (no+1 < playerCount) {
            return no+1;
        }
        return 0;
	}

	private ISpelar findOutWhoseTurnItIs(final ISpelar myPlayer, int next) throws RemoteException {
		if (myPlayer.getSpelarNummer() == next) {
            return myPlayer;
        }
		for (ISpelar s : spelarar) {
		    if (s.getSpelarNummer() == next) {
		       return s;
		    }
		}
		return null;
	}

    /** Spelaren er ferdig med sin tur, no er det neste spelar
     * @throws java.rmi.RemoteException
     */
    private ISpelar nesteUtanNett(int sp){
    	int id = spelarar.size() == sp ? 0 : sp;
		return spelarar.get(id);
    }
}

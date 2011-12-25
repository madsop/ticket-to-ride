package ttr.kjerna;

import ttr.data.MeldingarModell;
import ttr.spelar.ISpelar;
import ttr.struktur.Rute;

import java.rmi.RemoteException;
import java.util.Set;

public interface IKommunikasjonMedSpelarar {
    public void oppdaterAndreSpelarar(int plass, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Rute bygd) throws RemoteException;
    void mekkSpelarar(IHovud hovud);
    void sjekkOmFerdig(MeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<Rute> ruter) throws RemoteException;
}

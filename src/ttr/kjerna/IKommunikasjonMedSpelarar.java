package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.MeldingarModell;
import ttr.spelar.ISpelar;
import ttr.struktur.Rute;

import java.rmi.RemoteException;
import java.util.Set;

public interface IKommunikasjonMedSpelarar {
    public void oppdaterAndreSpelarar(int plass, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Rute bygd) throws RemoteException;
    void mekkSpelarar(IHovud hovud, IBord bord);
    void nyPaaPlass(ISpelar vert, Farge nyFarge, int i, IHovud hovud) throws RemoteException;
    void sjekkOmFerdig(MeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<Rute> ruter) throws RemoteException;
    void sendKortMelding(boolean kort, boolean tilfeldig, Farge f, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException;
}

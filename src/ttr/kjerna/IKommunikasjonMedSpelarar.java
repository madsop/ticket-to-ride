package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.rute.Route;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IKommunikasjonMedSpelarar {
    public void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
    ArrayList<ISpelar> createPlayersForLocalGame(IHovud hovud, IBord bord);
    void newCardPlacedOnTableInNetworkGame(ISpelar vert, Farge nyFarge, int i, IHovud hovud) throws RemoteException;
    void sjekkOmFerdig(IMeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<Route> ruter) throws RemoteException;
    void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge f, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException;
}
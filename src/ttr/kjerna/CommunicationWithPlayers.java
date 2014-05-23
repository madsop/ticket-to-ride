package ttr.kjerna;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface CommunicationWithPlayers {
    public void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
    ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(Core hovud, Table bord);
    void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF vert, Farge nyFarge, int i, Core hovud) throws RemoteException;
    void sjekkOmFerdig(IMeldingarModell meldingarModell, PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter) throws RemoteException;
    void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge f, String handlandespelarsNamn, Core hovud) throws RemoteException;
}
package ttr.communicationWithPlayers;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.MeldingarModell;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface CommunicationWithPlayers {
    public void updateOtherPlayers(Colour colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
    ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(Core hovud, Table bord);
    void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF vert, Colour nyFarge, int i, Core hovud) throws RemoteException;
    void sjekkOmFerdig(MeldingarModell meldingarModell, PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter) throws RemoteException;
    void sendMessageAboutCard(boolean kort, boolean tilfeldig, Colour f, String handlandespelarsNamn, Core hovud) throws RemoteException;
}
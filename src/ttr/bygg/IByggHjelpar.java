package ttr.bygg;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    ByggjandeInfo bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException;
    ByggjandeInfo byggTunnel(Table bord, Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException;
}

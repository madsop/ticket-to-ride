package ttr.bygg;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.rute.Route;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    ByggjandeInfo bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
    ByggjandeInfo byggTunnel(IBord bord, Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
}

package ttr.bygg;

import ttr.bord.IBord;
import ttr.rute.Route;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    ByggjandeInfo bygg(Route bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
    ByggjandeInfo byggTunnel(IBord bord, Route bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
}

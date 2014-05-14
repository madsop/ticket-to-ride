package ttr.bygg;

import ttr.bord.IBord;
import ttr.rute.IRoute;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    ByggjandeInfo bygg(IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
    ByggjandeInfo byggTunnel(IBord bord, IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
}

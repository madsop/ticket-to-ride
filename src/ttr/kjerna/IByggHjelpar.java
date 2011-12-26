package ttr.kjerna;

import ttr.bord.IBord;
import ttr.spelar.ISpelar;
import ttr.struktur.Rute;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    byggjandeInfo bygg(Rute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
    byggjandeInfo byggTunnel(IBord bord, Rute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
}

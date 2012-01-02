package ttr.bygg;

import ttr.bord.IBord;
import ttr.rute.IRute;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

public interface IByggHjelpar {
    byggjandeInfo bygg(IRute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
    byggjandeInfo byggTunnel(IBord bord, IRute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException;
}

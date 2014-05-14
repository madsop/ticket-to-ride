package ttr.kjerna;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

interface ITurhandsamar {
    ISpelar nextPlayer(ISpelar kvenSinTur, ISpelar minSpelar) throws RemoteException;
}

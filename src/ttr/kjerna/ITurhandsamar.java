package ttr.kjerna;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

interface ITurhandsamar {
    ISpelar nesteSpelar(ISpelar kvenSinTur, ISpelar minSpelar) throws RemoteException;
}

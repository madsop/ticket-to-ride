package ttr.kjerna;

import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;

interface ITurhandsamar {
    PlayerAndNetworkWTF nextPlayer(PlayerAndNetworkWTF kvenSinTur, PlayerAndNetworkWTF minSpelar) throws RemoteException;
}

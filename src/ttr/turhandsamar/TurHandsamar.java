package ttr.turhandsamar;

import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class TurHandsamar {
    protected final ArrayList<PlayerAndNetworkWTF> players;
    
    TurHandsamar(ArrayList<PlayerAndNetworkWTF> spelarar) {
        this.players = spelarar;
    }

    public PlayerAndNetworkWTF nextPlayer(PlayerAndNetworkWTF kvenSinTur, PlayerAndNetworkWTF minSpelar) throws RemoteException{
        return next(kvenSinTur, minSpelar);
    }
    
	protected abstract PlayerAndNetworkWTF next(PlayerAndNetworkWTF minSpelar, PlayerAndNetworkWTF kvenSinTur) throws RemoteException;
}

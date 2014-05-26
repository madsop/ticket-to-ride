package ttr.turhandsamar;

import ttr.spelar.IPlayer;
import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class TurHandsamar {
    protected final ArrayList<IPlayer> players;
    
    TurHandsamar(ArrayList<IPlayer> spelarar) {
        this.players = spelarar;
    }

    public IPlayer nextPlayer(IPlayer kvenSinTur, IPlayer minSpelar) throws RemoteException {
        return next(kvenSinTur, minSpelar);
    }
    
	protected abstract IPlayer next(IPlayer minSpelar, IPlayer kvenSinTur) throws RemoteException;
}
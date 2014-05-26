package ttr.turhandsamar;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ttr.spelar.IPlayer;

public class TurHandsamarNetwork extends TurHandsamar {

	public TurHandsamarNetwork(ArrayList<IPlayer> spelarar) {
		super(spelarar);
	}

	@Override
	protected IPlayer next(IPlayer minSpelar, IPlayer kvenSinTur) throws RemoteException {
		IPlayer host = findHost(minSpelar);
        int nextNumber = findNextNumber(kvenSinTur.getSpelarNummer(), host.getSpelarteljar());
        IPlayer sinTur = setPlayersTurn(minSpelar, nextNumber);
        return sinTur;
	}

	private IPlayer setPlayersTurn(IPlayer minSpelar, int nextNumber) throws RemoteException {
		IPlayer sinTur = findPlayerByPlayerNumber(minSpelar, nextNumber);
        
        for (IPlayer player : players) {
            player.settSinTur(sinTur);
        }
		return sinTur;
	}
    
	private IPlayer findHost(final IPlayer minSpelar) throws RemoteException {
		return findPlayerByPlayerNumber(minSpelar, 0);
	}

	private IPlayer findPlayerByPlayerNumber(final IPlayer myPlayer, int playerNumber) throws RemoteException {
		if (myPlayer.getSpelarNummer() == playerNumber) {
            return myPlayer;
        }
		for (IPlayer player : players) {
		    if (player.getSpelarNummer() == playerNumber) {
		       return player;
		    }
		}
//		return players.stream().filter(x -> x.getSpelarNummer() == playerNumber).findAny().get(); //TODO after decoupling
		return null;
	}


	private int findNextNumber(final int no, int playerCount) {
		return (no+1 < playerCount) ? no+1 : 0;
	}
}
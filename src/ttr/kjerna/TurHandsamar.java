package ttr.kjerna;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TurHandsamar implements ITurhandsamar {
    private final ArrayList<ISpelar> players;
    private final boolean nett;
    
    TurHandsamar(ArrayList<ISpelar> spelarar, boolean nett) {
        this.players = spelarar;
        this.nett = nett;
    }

    public ISpelar nextPlayer(ISpelar kvenSinTur, ISpelar minSpelar) throws RemoteException{
        int idOfNextPlayer = findIdOfNextPlayer(kvenSinTur);
        return nett ? nesteMedNett(kvenSinTur, minSpelar) : nextPlayerUtanNett(idOfNextPlayer);
    }

	private int findIdOfNextPlayer(ISpelar kvenSinTur) {
		return players.indexOf(kvenSinTur) + 1;
	}

    private ISpelar nesteMedNett(final ISpelar kvenSinTur, final ISpelar minSpelar) throws RemoteException {
        int no = kvenSinTur.getSpelarNummer();
        ISpelar host = findHost(minSpelar);
        int next = findNextNumber(no, host.getSpelarteljar());
        ISpelar sinTur = findPlayerByPlayerNumber(minSpelar, next);
        
        for (ISpelar player : players) {
            player.settSinTur(sinTur);
        }
        return sinTur;
    }
    
	private ISpelar findHost(final ISpelar myPlayer) throws RemoteException {
		return findPlayerByPlayerNumber(myPlayer, 0);
	}

	private int findNextNumber(final int no, int playerCount) {
		if (no+1 < playerCount) {
            return no+1;
        }
        return 0;
	}

	private ISpelar findPlayerByPlayerNumber(final ISpelar myPlayer, int playerNumber) throws RemoteException {
		if (myPlayer.getSpelarNummer() == playerNumber) {
            return myPlayer;
        }
		for (ISpelar player : players) {
		    if (player.getSpelarNummer() == playerNumber) {
		       return player;
		    }
		}
//		return players.stream().filter(x -> x.getSpelarNummer() == playerNumber).findAny().get(); //TODO after decoupling
		return null;
	}

    private ISpelar nextPlayerUtanNett(int idOfNextPlayer){
    	int id = players.size() == idOfNextPlayer ? 0 : idOfNextPlayer;
		return players.get(id);
    }
}

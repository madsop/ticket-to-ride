package ttr.kjerna;

import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TurHandsamar implements ITurhandsamar {
    private final ArrayList<PlayerAndNetworkWTF> players;
    private final boolean nett;
    
    TurHandsamar(ArrayList<PlayerAndNetworkWTF> spelarar, boolean nett) {
        this.players = spelarar;
        this.nett = nett;
    }

    public PlayerAndNetworkWTF nextPlayer(PlayerAndNetworkWTF kvenSinTur, PlayerAndNetworkWTF minSpelar) throws RemoteException{
        int idOfNextPlayer = findIdOfNextPlayer(kvenSinTur);
        return nett ? nesteMedNett(kvenSinTur, minSpelar) : nextPlayerUtanNett(idOfNextPlayer);
    }

	private int findIdOfNextPlayer(PlayerAndNetworkWTF kvenSinTur) {
		return players.indexOf(kvenSinTur) + 1;
	}

    private PlayerAndNetworkWTF nesteMedNett(final PlayerAndNetworkWTF kvenSinTur, final PlayerAndNetworkWTF minSpelar) throws RemoteException {
        PlayerAndNetworkWTF host = findHost(minSpelar);
        int next = findNextNumber(kvenSinTur.getSpelarNummer(), host.getSpelarteljar());
        PlayerAndNetworkWTF sinTur = findPlayerByPlayerNumber(minSpelar, next);
        
        for (PlayerAndNetworkWTF player : players) {
            player.settSinTur(sinTur);
        }
        return sinTur;
    }
    
	private PlayerAndNetworkWTF findHost(final PlayerAndNetworkWTF myPlayer) throws RemoteException {
		return findPlayerByPlayerNumber(myPlayer, 0);
	}

	private int findNextNumber(final int no, int playerCount) {
		if (no+1 < playerCount) {
            return no+1;
        }
        return 0;
	}

	private PlayerAndNetworkWTF findPlayerByPlayerNumber(final PlayerAndNetworkWTF myPlayer, int playerNumber) throws RemoteException {
		if (myPlayer.getSpelarNummer() == playerNumber) {
            return myPlayer;
        }
		for (PlayerAndNetworkWTF player : players) {
		    if (player.getSpelarNummer() == playerNumber) {
		       return player;
		    }
		}
//		return players.stream().filter(x -> x.getSpelarNummer() == playerNumber).findAny().get(); //TODO after decoupling
		return null;
	}

    private PlayerAndNetworkWTF nextPlayerUtanNett(int idOfNextPlayer){
    	int id = players.size() == idOfNextPlayer ? 0 : idOfNextPlayer;
		return players.get(id);
    }
}

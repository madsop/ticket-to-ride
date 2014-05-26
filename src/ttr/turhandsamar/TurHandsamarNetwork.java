package ttr.turhandsamar;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ttr.spelar.PlayerAndNetworkWTF;

public class TurHandsamarNetwork extends TurHandsamar {

	public TurHandsamarNetwork(ArrayList<PlayerAndNetworkWTF> spelarar) {
		super(spelarar);
	}

	@Override
	protected PlayerAndNetworkWTF next(PlayerAndNetworkWTF minSpelar, PlayerAndNetworkWTF kvenSinTur) throws RemoteException {
		PlayerAndNetworkWTF host = findHost(minSpelar);
        int nextNumber = findNextNumber(kvenSinTur.getSpelarNummer(), host.getSpelarteljar());
        PlayerAndNetworkWTF sinTur = setPlayersTurn(minSpelar, nextNumber);
        return sinTur;
	}

	private PlayerAndNetworkWTF setPlayersTurn(PlayerAndNetworkWTF minSpelar, int nextNumber) throws RemoteException {
		PlayerAndNetworkWTF sinTur = findPlayerByPlayerNumber(minSpelar, nextNumber);
        
        for (PlayerAndNetworkWTF player : players) {
            player.settSinTur(sinTur);
        }
		return sinTur;
	}
    
	private PlayerAndNetworkWTF findHost(final PlayerAndNetworkWTF myPlayer) {
		return findPlayerByPlayerNumber(myPlayer, 0);
	}

	private PlayerAndNetworkWTF findPlayerByPlayerNumber(final PlayerAndNetworkWTF myPlayer, int playerNumber) {
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


	private int findNextNumber(final int no, int playerCount) {
		if (no+1 < playerCount) {
            return no+1;
        }
        return 0;
	}
}
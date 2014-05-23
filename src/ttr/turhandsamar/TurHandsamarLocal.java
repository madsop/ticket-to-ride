package ttr.turhandsamar;

import java.util.ArrayList;

import ttr.spelar.PlayerAndNetworkWTF;

public class TurHandsamarLocal extends TurHandsamar {

	public TurHandsamarLocal(ArrayList<PlayerAndNetworkWTF> spelarar) {
		super(spelarar);
	}

	@Override
	protected PlayerAndNetworkWTF next(PlayerAndNetworkWTF minSpelar, PlayerAndNetworkWTF kvenSinTur) {
		int id = findIdOfNextPlayer(kvenSinTur);
		return players.get(id);
	}
	
	private int findIdOfNextPlayer(PlayerAndNetworkWTF kvenSinTur) {
		int idOfNextPlayer = players.indexOf(kvenSinTur) + 1;
		return (players.size() == idOfNextPlayer) ? 0 : idOfNextPlayer;
	}

}

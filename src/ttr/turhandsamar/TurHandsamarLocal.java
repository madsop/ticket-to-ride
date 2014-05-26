package ttr.turhandsamar;

import java.util.ArrayList;

import ttr.spelar.IPlayer;

public class TurHandsamarLocal extends TurHandsamar {

	public TurHandsamarLocal(ArrayList<IPlayer> players) {
		super(players);
	}

	@Override
	protected IPlayer next(IPlayer minSpelar, IPlayer kvenSinTur) {
		int id = findIdOfNextPlayer(kvenSinTur);
		return players.get(id);
	}
	
	private int findIdOfNextPlayer(IPlayer kvenSinTur) {
		int idOfNextPlayer = players.indexOf(kvenSinTur) + 1;
		return (players.size() == idOfNextPlayer) ? 0 : idOfNextPlayer;
	}

}

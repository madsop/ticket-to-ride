package ttr.communicationWithPlayers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.spelar.IPlayer;

public class GameFinisherLocal extends GameFinisher {
	public GameFinisherLocal(ArrayList<IPlayer> players) {
		super(players);
	}

	public void finishGame(MeldingarModell meldingarModell, String speltittel, IPlayer minSpelar) throws RemoteException {
		orientPlayersThatTheGameIsOver(meldingarModell);

		int[] totalpoeng = new int[players.size()];

		IPlayer vinnar = null;
		int vinnarpoeng = 0;
		addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

		String pointsString = Infostrengar.SpeletErFerdig;

		for (IPlayer player : players) {
			IPlayer leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell);
			vinnarpoeng = reknUtPoeng(leiar);
		}
		avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
	}

	@Override
	protected void orientOthers(MeldingarModell meldingarModell) { }

	@Override
	protected void orientNetwork(MeldingarModell meldingarModell, IPlayer playerWithMostMissionsAccomplished, int bestNumberOfMissionsAccomplished) { }


}

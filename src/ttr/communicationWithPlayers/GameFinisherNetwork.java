package ttr.communicationWithPlayers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.spelar.IPlayer;

public class GameFinisherNetwork extends GameFinisher {
	public GameFinisherNetwork(ArrayList<IPlayer> players) {
		super(players);
	}

	public void finishGame(MeldingarModell meldingarModell, IPlayer kvenSinTur, String speltittel, IPlayer minSpelar) throws RemoteException {
		orientPlayersThatTheGameIsOver(meldingarModell);

		int[] totalpoeng = new int[players.size() + 1];

		IPlayer vinnar = null;
		int vinnarpoeng = 0;
		addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

		String pointsString = Infostrengar.SpeletErFerdig;
		totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar);
		pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
		vinnar = minSpelar;
		vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];

		for (IPlayer player : players) {
			IPlayer leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell);
			vinnarpoeng = reknUtPoeng(leiar);
		}
		avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
	}


	@Override
	protected void orientNetwork(MeldingarModell meldingarModell, IPlayer playerWithMostMissionsAccomplished, int bestNumberOfMissionsAccomplished) throws RemoteException {
		meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
	}

	
	protected void orientOthers(MeldingarModell meldingarModell) {
		meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);
	}
}

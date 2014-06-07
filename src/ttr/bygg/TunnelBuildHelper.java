package ttr.bygg;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.Infostrengar;
import ttr.rute.Route;

class TunnelBuildHelper {
	
	int checkIfTunnelShouldBeBuiltAndHowManyExtraCardsWillBeNeeded(Table bord, Route bygd) throws HeadlessException {
		Colour[] treTrekte = drawThreeRandomCards(bord);
		int ekstra = computeExtraNeededCards(bygd, treTrekte);

		if (askUserIfBuildAnyway(treTrekte, ekstra) == JOptionPane.OK_OPTION) {
			return ekstra;
		}
		return -1;
	}

	private Colour[] drawThreeRandomCards(Table bord) {
		Colour[] treTrekte = new Colour[3];
		
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getRandomCardFromTheDeck();
			if (treTrekte[i] == null){
				JOptionPane.showMessageDialog(null, Infostrengar.TomtPåBordet);
				return null;
			}
		}
		return treTrekte;
	}

	private int computeExtraNeededCards(Route bygd, Colour[] treTrekte) {
		int extra = 0;
		for (Colour drawnCard : treTrekte) {
			if (drawnCard == Colour.valfri || drawnCard == bygd.getColour()) {
				extra++;
			}
		}
		return extra;
	}
	private int askUserIfBuildAnyway(Colour[] treTrekte, int ekstra) {
		return JOptionPane.showConfirmDialog(null, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2]	+". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
	}
}
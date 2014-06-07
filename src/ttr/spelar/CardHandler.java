package ttr.spelar;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.kjerna.Core;

import java.util.HashMap;
import java.util.Map;

public class CardHandler {
	private Map<Colour, Integer> cards;

	CardHandler(Core hovud) {
		cards = new HashMap<>();
		for (Colour colour : Colour.values()) {
			cards.put(colour, 0);
		}

		//TODO dette bør vel eigentleg ikkje gjerast her
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Colour trekt = hovud.getTable().getRandomCardFromTheDeck();
			cards.put(trekt, cards.get(trekt) + 1); //todo denne feilar som berre pokker..hmm
		}
	}

	public void receiveCard(Colour colour) {
		cards.put(colour, cards.get(colour) + 1);
	}

	public int getNumberOfCardsLeftInColour(Colour colour) {
		return cards.get(colour);
	}

	public void useCards(Colour colour, int number) {
		cards.put(colour, cards.get(colour) - number);
	}

}
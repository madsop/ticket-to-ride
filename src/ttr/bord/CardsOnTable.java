package ttr.bord;

import ttr.data.Colour;
import ttr.data.Konstantar;

public class CardsOnTable {
    private Colour[] cardsOpenOnTable;

    public CardsOnTable() {
    	cardsOpenOnTable = new Colour[Konstantar.ANTAL_KORT_PÅ_BORDET];
	}

	Colour[] getCardsOpenOnTable() {
		return cardsOpenOnTable;
	}

	void setCardsOpenOnTable(Colour[] cardsOpenOnTable) {
		this.cardsOpenOnTable = cardsOpenOnTable;
	}
	
	Colour getCardAt(int position) {
		return cardsOpenOnTable[position];
	}
	
	void putCardOnTable(int position, Colour colour) {
		cardsOpenOnTable[position] = colour;
	}

	boolean areThereTooManyJokersOnTable() {
		int jokrar = 0;
		for (Colour f : cardsOpenOnTable) {
			if (f == Colour.valfri) {
				jokrar++;
			}
		}
        return jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET;
	}
}
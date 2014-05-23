package ttr.bord;

import ttr.data.Farge;
import ttr.data.Konstantar;

public class CardsOnTable {
    private Farge[] cardsOpenOnTable;

    public CardsOnTable() {
    	cardsOpenOnTable = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];
	}

	Farge[] getCardsOpenOnTable() {
		return cardsOpenOnTable;
	}

	void setCardsOpenOnTable(Farge[] cardsOpenOnTable) {
		this.cardsOpenOnTable = cardsOpenOnTable;
	}
	
	Farge getCardAt(int position) {
		return cardsOpenOnTable[position];
	}
	
	void putCardOnTable(int position, Farge colour) {
		cardsOpenOnTable[position] = colour;
	}

	boolean areThereTooManyJokersOnTable() {
		int jokrar = 0;
		for (Farge f : cardsOpenOnTable) {
			if (f == Farge.valfri) {
				jokrar++;
			}
		}
        return jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET;
	}
}
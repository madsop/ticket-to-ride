package ttr.bord;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.inject.Inject;

import ttr.data.Colour;
import ttr.data.Konstantar;

public class CardsOnTable {
    private Colour[] cardsOpenOnTable;
	private PropertyChangeSupport propertyChangeSupport;

	@Inject
    public CardsOnTable() {
    	cardsOpenOnTable = new Colour[Konstantar.ANTAL_KORT_PÅ_BORDET];
    	propertyChangeSupport = new PropertyChangeSupport(this);
	}

	Colour[] getCardsOpenOnTable() {
		return cardsOpenOnTable;
	}

	void setCardsOpenOnTable(Colour[] cardsOpenOnTable) {
		for (int i = 0; i < this.cardsOpenOnTable.length; i++) {
			propertyChangeSupport.firePropertyChange(i + "", this.cardsOpenOnTable[i], cardsOpenOnTable[i]);
		}
		this.cardsOpenOnTable = cardsOpenOnTable;
	}
	
	Colour getCardAt(int position) {
		return cardsOpenOnTable[position];
	}
	
	void putCardOnTable(int position, Colour colour) {
		propertyChangeSupport.firePropertyChange(position + "", cardsOpenOnTable[position], colour);
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

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
}
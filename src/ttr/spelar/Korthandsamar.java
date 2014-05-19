package ttr.spelar;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Korthandsamar extends UnicastRemoteObject implements IKorthandsamar {
	private static final long serialVersionUID = 3899317463384337994L;
	private IHovud hovud;
    private int[] cards;

    Korthandsamar(IHovud hovud) throws RemoteException {
        super();
        this.hovud = hovud;
        faaInitielleFargekort();
    }

    private void faaInitielleFargekort() {
    	initialiseCards();
        for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
            Farge trekt = trekkFargekort();
            int plass = -1;
            for (int colourPosition = 0; colourPosition < Konstantar.FARGAR.length; colourPosition++) {
                if (trekt == Konstantar.FARGAR[colourPosition]) {
                    plass = colourPosition;
                }
            }
            if (plass >= 0) {
                cards[plass]++;
            }
        }
    }
    
	private void initialiseCards() {
		cards = new int[Konstantar.ANTAL_FARGAR];

        for (int i = 0; i < Konstantar.ANTAL_STARTKORT; i++) {
            cards[i] = 0;
        }
	}

    public Farge getRandomCardFromTheDeck(int positionOnTable) {
        Farge colourOfTheRandomCard = hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(positionOnTable, true);

        if (colourOfTheRandomCard == null){
//            tryingToPickNullCard(positionOnTable, colourOfTheRandomCard);
        	displayGraphicallyThatThereIsNoCardHere(positionOnTable);
            return null;
        }

        hovud.getBord().setEinPaaBordet(colourOfTheRandomCard, positionOnTable);
        return colourOfTheRandomCard;
    }

//	private void tryingToPickNullCard(int positionOnTable, Farge colourOfTheRandomCard) {

//		int colourPosition = -1; TODO fjern heile denne når eg byrjar å bli trygg på refaktoriseringa
//		for (Farge farge : Konstantar.FARGAR){
//		    if (colourOfTheRandomCard == farge){
//		        colourPosition = farge.ordinal(); 
//		        System.out.println("THIS can friggin never happen");
//		    }
//		}
//		if (colourPosition >= 0 && colourPosition < Konstantar.FARGAR.length){ //todo bør ikkje denne vera >0 ?
//		    kort[colourPosition]--;
//		}
//	}

	private void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog((Component) hovud.getGui(), "Det er ikkje noko kort der, ser du vel.");
		hovud.getGui().getKortButtons()[positionOnTable].setBackground(Color.GRAY);
		hovud.getGui().getKortButtons()[positionOnTable].setText("Tom");
	}


    public void receiveCard(Farge colour) {
        int position = 0;
        for (int i = 0; i < Konstantar.FARGAR.length; i++) {
            if (colour == Konstantar.FARGAR[i]) {
                position = i;
            }
        }        
        cards[position]++;
    }

    public int[] getKort() {
        return cards;
    }

    /** @return eit tilfeldig fargekort frå toppen av stokken */
    @Override
    public Farge trekkFargekort() {
        if (hovud.getBord().areThereAnyCardsLeftInDeck()) {
            return hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(0, false);
        }
        return null;
    }
}
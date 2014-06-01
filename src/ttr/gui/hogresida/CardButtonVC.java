package ttr.gui.hogresida;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.Konstantar;

public class CardButtonVC extends JButton {
	private static final long serialVersionUID = 2296444830008249938L;
	private int position;

	public CardButtonVC(String string, int position) {
		super(string);
        this.setMinimumSize(Konstantar.KORTKNAPP);
        this.setBackground(Color.BLACK);
        this.position = position;
	}

	void displayGraphicallyThatThereIsNoCardHere() {
		this.setBackground(Color.GRAY);
		this.setText("Tom");
	}

	void drawCardOnTable(Colour colour) {
		this.setForeground(Color.BLACK);
        this.setBackground(Konstantar.fargeTilColor(colour));
        String colourText = getColourText(colour);
        this.setText(colourText);		
	}

	private String getColourText(Colour colour) {
        switch (colour) {
            case blå:
                return "Blå";
            case gul:
                return "Gul";
            case raud:
                return "Raud";
            case grønn:
                return "Grønn";
            case kvit:
                return "Kvit";
            case lilla:
                return "Lilla";
            case oransje:
                return "Oransje";
            case svart:
            	this.setForeground(Color.WHITE);
                return "Svart";
            case valfri:
                return "Joker";
            default:
                return "Ops";
        }
	}
	
	private class CardChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().toString().equals(position + "")) {
				drawCardOnTable((Colour) evt.getNewValue());
			}
		}
		
	}

	public void setThisAsPropertyChangeListener(Table table) {
		table.addPropertyChangeListener(new CardChangeListener());		
	}
}
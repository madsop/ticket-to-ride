package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.listeners.DelegationListener;
import ttr.listeners.WrapperKortListener;
import ttr.oppdrag.MissionHandler;

import javax.swing.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Hogrepanelet extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 5138678205804362548L;
    private JTextField spelarnamn;

    private CardButtonVC[] cardButtons;
    private JLabel[] remainingTrainsLabel;
    private JButton trekkOppdrag,bygg,visBygde,visMineOppdrag,visMineKort, kortBunke;

    public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
    	cardButtons[positionOnTable].displayGraphicallyThatThereIsNoCardHere();
	}

    public void byggHogrepanelet(){
        GridBagConstraints gridBagConstraints = configureLayout();

        spelarnamn = new JTextField("Denne spelaren sin tur");
        spelarnamn.setEditable(false);
        spelarnamn.setMinimumSize(Konstantar.SPELARNAMNDIM);

        this.setPreferredSize(new Dimension(Konstantar.hogrebreidde, Konstantar.HOGDE)); 

        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.add(spelarnamn, gridBagConstraints);
        
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridy = 1;
        trekkOppdrag = new JButton(Infostrengar.trekkOppdragLabel);
        trekkOppdrag.setMinimumSize(Konstantar.KNAPP);
        this.add(trekkOppdrag, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        bygg = new JButton(Infostrengar.byggLabel);
        bygg.setMinimumSize(Konstantar.KNAPP);
        this.add(bygg, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridx = 0;
        visBygde = new JButton(Infostrengar.bygdeRuterLabel);
        visBygde.setMinimumSize(Konstantar.KNAPP);
        this.add(visBygde, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridx = 0;
        visMineOppdrag = new JButton(Infostrengar.visMineOppdragLabel);
        visMineOppdrag.setMinimumSize(Konstantar.KNAPP);
        this.add(visMineOppdrag, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        visMineKort = new JButton(Infostrengar.visMineKortLabel);
        visMineKort.setMinimumSize(Konstantar.KNAPP);
        this.add(visMineKort, gridBagConstraints);

        remainingTrainsLabel = new JLabel[4];

        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridx = 0;
        remainingTrainsLabel[0] = new JLabel(Infostrengar.TogAttLabel);
        remainingTrainsLabel[0].setFont(Konstantar.TOGTALFONT);
        this.add(remainingTrainsLabel[0], gridBagConstraints);

        gridBagConstraints.gridy = 5;
        remainingTrainsLabel[1] = new JLabel(String.valueOf(Konstantar.ANTAL_TOG));
        remainingTrainsLabel[1].setForeground(Color.WHITE);
        remainingTrainsLabel[1].setFont(Konstantar.TOGTALFONT);
        this.add(remainingTrainsLabel[1], gridBagConstraints);

        gridBagConstraints.gridx = 1;
        remainingTrainsLabel[2] = new JLabel(String.valueOf(Konstantar.ANTAL_TOG));
        remainingTrainsLabel[2].setForeground(Color.BLACK);
        remainingTrainsLabel[2].setFont(Konstantar.TOGTALFONT);
        this.add(remainingTrainsLabel[2], gridBagConstraints);

        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridx = 0;
        remainingTrainsLabel[3] = new JLabel(String.valueOf(Konstantar.ANTAL_TOG));
        remainingTrainsLabel[3].setForeground(Color.PINK);
        remainingTrainsLabel[3].setFont(Konstantar.TOGTALFONT);
        this.add(remainingTrainsLabel[3], gridBagConstraints);

        visFargekorta(gridBagConstraints);
    }

	private GridBagConstraints configureLayout() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.insets = Konstantar.INSETS;
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        gridBagLayout.setConstraints(this, gridBagConstraints);        

        this.setPreferredSize(new Dimension(Konstantar.hogrebreidde, Konstantar.HOGDE));
        this.setBackground(Color.GRAY);
        return gridBagConstraints;
	}
    
    public void addListeners(MissionHandler missionHandler, Core core) {
        DelegationListener listener = new DelegationListener(core, visBygde, visMineKort, visMineOppdrag, trekkOppdrag, bygg);
        trekkOppdrag.addActionListener(listener);
        bygg.addActionListener(listener);
        visMineKort.addActionListener(listener);
        visMineOppdrag.addActionListener(listener);
        visBygde.addActionListener(listener);

        WrapperKortListener kortListener = new WrapperKortListener(kortBunke, cardButtons, core);
        kortBunke.addActionListener(kortListener);
        for (CardButtonVC cardButton : cardButtons) {
            cardButton.addActionListener(kortListener);
            cardButton.setThisAsPropertyChangeListener(core.getTable());
        }
    }

    private void visFargekorta(GridBagConstraints gridBagConstraints) {
        // Fargekorta på bordet
        gridBagConstraints.gridy = 7;
        kortBunke = new JButton(Infostrengar.TrekkFråBunkenLabel);
        kortBunke.setMinimumSize(Konstantar.KORTKNAPP);
        this.add(kortBunke, gridBagConstraints);

        cardButtons = new CardButtonVC[5];

        gridBagConstraints.gridx = 1;
        mekkKortButton(0, gridBagConstraints);

        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        mekkKortButton(1, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        mekkKortButton(2, gridBagConstraints);

        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        mekkKortButton(3, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        mekkKortButton(4, gridBagConstraints);
    }

    private void mekkKortButton(int counter, GridBagConstraints gridBagConstraints){
    	cardButtons[counter] = new CardButtonVC("kort " + counter, counter);
    	this.add(cardButtons[counter], gridBagConstraints);
    }

	public void setPlayerName(String newPlayerName) {
		spelarnamn.setText(newPlayerName);		
	}

	public void displayGraphicallyThatItIsMyTurn() {
		spelarnamn.setBackground(Color.YELLOW);
	}
	
	public void setRemainingTrains(int position, int numberOfTrains) {
		remainingTrainsLabel[position].setText(String.valueOf(numberOfTrains));		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		int position = Integer.parseInt(evt.getPropertyName().substring(evt.getPropertyName().length() - 2));
		String numberOfTrains = "" + evt.getNewValue();
		remainingTrainsLabel[position].setText(numberOfTrains);
		//TODO forenkle
	}
}
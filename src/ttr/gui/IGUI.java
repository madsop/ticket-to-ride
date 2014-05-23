package ttr.gui;

import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;

import javax.swing.*;
import java.util.ArrayList;

public interface IGUI {
    public IMeldingarModell getMessagesModel();
    public void showWhoseTurnItIs(String spelarnamn, String minSpelar);
    public JTextField getPlayerNameJTextField();
    public JLabel[] getRemainingTrainsLabel();
    public void drawCardsOnTable(int plass, Farge farge);
    public JButton[] getCardButtons();
    public void setHovud(Core hovud);
    void displayGraphicallyThatThereIsNoCardHere(int positionOnTable);

    ArrayList<Mission> chooseMissions(ArrayList<Mission> oppd);

}
package ttr.gui;

import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.kjerna.Core;
import ttr.oppdrag.Mission;

import javax.swing.*;
import java.util.ArrayList;

public interface IGUI {
    public IMeldingarModell getMeldingarModell();
    public void visKvenDetErSinTur(String spelarnamn, String minSpelar);
    public JTextField getSpelarnamn();
    public JLabel[] getTogAtt();
    public void teiknOppKortPåBordet(int plass, Farge farge);
    public JButton[] getKortButtons();
    void createJFrame(String tittel, JPanel panel);
    public String showInputDialog(String string);                       // TODO: bruk denne og liknande meir
    public void setHovud(Core hovud);
    void displayGraphicallyThatThereIsNoCardHere(int positionOnTable);

    ArrayList<Mission> velOppdrag(ArrayList<Mission> oppd);

}
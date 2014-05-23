package ttr.gui;

import ttr.data.Farge;
import ttr.data.IMeldingarModell;
import ttr.kjerna.IHovud;
import ttr.oppdrag.Mission;

import javax.swing.*;
import java.util.ArrayList;

public interface IGUI {
    public IMeldingarModell getMeldingarModell();
    public void visKvenDetErSinTur(String spelarnamn, boolean nett, String minSpelar);
    public JTextField getSpelarnamn();
    public JLabel[] getTogAtt();
    public void teiknOppKortPåBordet(int plass, Farge farge);
    public JButton[] getKortButtons();
    void lagRamme(String tittel, JPanel panel);
    public String showInputDialog(String string);                       // TODO: bruk denne og liknande meir
    public void setHovud(IHovud hovud);
    void displayGraphicallyThatThereIsNoCardHere(int positionOnTable);

    ArrayList<Mission> velOppdrag(ArrayList<Mission> oppd);

}
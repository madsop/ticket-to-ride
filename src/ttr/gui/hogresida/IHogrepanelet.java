package ttr.gui.hogresida;

import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.kjerna.Core;

import javax.swing.*;

public interface IHogrepanelet {
    JButton[] getKortButtons();

    JTextField getSpelarnamn();

    JLabel[] getTogAtt();

    void byggHogrepanelet();

    void addListeners(Core hovud);

    void teiknOppKortPåBordet(int plass, Farge farge);

    void setGUI(IGUI gui);
}
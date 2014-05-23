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

    void addListeners(Core core, IGUI gui);

    void teiknOppKortPåBordet(int position, Farge colour);
}
package ttr.gui;

import ttr.kjerna.IHovud;
import ttr.data.Farge;
import ttr.data.MeldingarModell;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

public interface IGUI {
    public IHovud getHovud();

    public MeldingarModell getMeldingarModell();
    public void setSpelarnamn(String spelarnamn);
    public JTextField getSpelarnamn();
    public JLabel[] getTogAtt();
    public void setKortPaaBordet(int plass,Farge farge);
    public JButton[] getKortButtons();
    void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException;
    void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException;
    void trekkOppdrag(ISpelar s, boolean start) throws RemoteException;
    void lagRamme(String tittel, JPanel panel);
    public String showInputDialog(String string);                       // TODO: bruk denne og liknande meir

}

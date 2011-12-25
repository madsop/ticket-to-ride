package ttr.gui;

import ttr.data.Farge;
import ttr.data.MeldingarModell;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGUI {
    public MeldingarModell getMeldingarModell();
    public void setSpelarnamn(String spelarnamn);
    public JTextField getSpelarnamn();
    public JLabel[] getTogAtt();
    public void setKortPaaBordet(int plass,Farge farge);
    public JButton[] getKortButtons();
    void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException;
    void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException;
    ArrayList<IOppdrag> velOppdrag(ArrayList<IOppdrag> oppd);
    void lagRamme(String tittel, JPanel panel);
    public String showInputDialog(String string);                       // TODO: bruk denne og liknande meir
    public void setHovud(IHovud hovud);
    public void byggHogrepanel();

}

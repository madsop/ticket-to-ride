package ttr.kjerna;

import ttr.gui.IGUI;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IOppdragshandsamar {
    ArrayList<IOppdrag> getGjenverandeOppdrag();

    int getAntalGjenverandeOppdrag();

    IOppdrag getOppdrag();
}

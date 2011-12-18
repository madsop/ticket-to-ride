package ttr;

import ttr.gui.GUI;
import ttr.spelar.ISpelar;
import ttr.struktur.Oppdrag;
import ttr.struktur.Rute;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud extends Remote {
    public ArrayList<Oppdrag> getGjenverandeOppdrag();

    public Set<Rute> getRuter();

    public ArrayList<Rute> getAlleBygdeRuter();

    public void setMinSpelar(ISpelar spelar);

    public Bord getBord();
    public boolean isNett();


    public ISpelar getKvenSinTur();

    public ArrayList<ISpelar> getSpelarar();

    public GUI getGui();

    public int getAntalGjenverandeOppdrag ();
    public Oppdrag getOppdrag();

    public ISpelar getMinSpelar();
    public void settSinTur(ISpelar spelar) throws RemoteException;
    public void nesteSpelar() throws RemoteException;

    public Rute[] finnFramRuter() throws RemoteException;
    public void bygg(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;

}

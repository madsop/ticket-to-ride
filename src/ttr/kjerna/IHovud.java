package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud {
    public ArrayList<IOppdrag> getGjenverandeOppdrag();

    public Set<Rute> getRuter();

    public ArrayList<Rute> getAlleBygdeRuter();

    public void setMinSpelar(ISpelar spelar);

    public IBord getBord();
    public boolean isNett();

    public void settIGangSpelet(boolean nett, String hostAddress) throws RemoteException;
    public void sendKortMelding(boolean kort, boolean tilfeldig, Farge f) throws RemoteException;
    public void nyPaaPlass(ISpelar vert, Farge nyFarge, int i) throws RemoteException;

    public ISpelar getKvenSinTur();

    public ArrayList<ISpelar> getSpelarar();

    public IGUI getGui();

    public int getAntalGjenverandeOppdrag ();
    public IOppdrag getOppdrag();

    public ISpelar getMinSpelar();
    public void settSinTur(ISpelar spelar) throws RemoteException;
    public void nesteSpelar() throws RemoteException;

    public Rute[] finnFramRuter() throws RemoteException;
    public void bygg(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(Rute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;

}
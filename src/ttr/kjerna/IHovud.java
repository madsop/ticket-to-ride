package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.oppdrag.Mission;
import ttr.rute.IRoute;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud {
    public ArrayList<Mission> getGjenverandeOppdrag();

    public Set<IRoute> getRuter();

    public Set<IRoute> getAlleBygdeRuter();

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
    public Mission getOppdrag();

    public ISpelar getMinSpelar();
    public void settSinTur(ISpelar spelar) throws RemoteException;
    public void nesteSpelar() throws RemoteException;

    public Set<IRoute> findRoutesNotYetBuilt() throws RemoteException;
    public void bygg(IRoute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(IRoute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;

}
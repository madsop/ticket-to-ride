package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.oppdrag.Mission;
import ttr.rute.Route;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud {
    public ArrayList<Mission> getGjenverandeOppdrag();

    public Set<Route> getRuter();

    public Set<Route> getAlleBygdeRuter();

    public void setMinSpelar(ISpelar spelar);

    public IBord getBord();
    public boolean isNett();

    public void settIGangSpelet(boolean nett, String hostAddress) throws RemoteException;
    public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge f) throws RemoteException;
    public void newCardPlacedOnTableInNetworkGame(ISpelar vert, Farge nyFarge, int i) throws RemoteException;

    public ISpelar getKvenSinTur();

    public ArrayList<ISpelar> getSpelarar();

    public IGUI getGui();

    public int getAntalGjenverandeOppdrag ();
    public Mission getOppdrag();

    public ISpelar getMinSpelar();
    public void settSinTur(ISpelar spelar) throws RemoteException;
    public void nesteSpelar() throws RemoteException;

    public Set<Route> findRoutesNotYetBuilt() throws RemoteException;
    public void bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException;

}
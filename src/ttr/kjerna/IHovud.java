package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.oppdrag.IOppdrag;
import ttr.rute.IRute;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud {
    public ArrayList<IOppdrag> getGjenverandeOppdrag();

    public Set<IRute> getRuter();

    public Set<IRute> getAlleBygdeRuter();

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

    public Set<IRute> finnFramRuter() throws RemoteException;
    public void bygg(IRute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(IRute bygd, int plass, int kortKrevd, int krevdJokrar) throws RemoteException;

}
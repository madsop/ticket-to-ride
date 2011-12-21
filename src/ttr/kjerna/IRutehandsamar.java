package ttr.kjerna;

import ttr.spelar.ISpelar;
import ttr.struktur.Rute;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IRutehandsamar {
    ArrayList<Rute> getAlleBygdeRuter();

    Rute[] finnFramRuter(ArrayList<ISpelar> spelarar) throws RemoteException;
    public Set<Rute> getRuter();
    void nyRute(Rute rute);

}

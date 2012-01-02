package ttr.rute;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IRutehandsamar {
    ArrayList<IRute> getAlleBygdeRuter();

    IRute[] finnFramRuter(ArrayList<ISpelar> spelarar) throws RemoteException;
    public Set<IRute> getRuter();
    void nyRute(IRute rute);

}

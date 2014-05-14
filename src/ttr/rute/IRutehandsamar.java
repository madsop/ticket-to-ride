package ttr.rute;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IRutehandsamar {
    Set<IRute> getAlleBygdeRuter();

    Set<IRute> findRoutesNotYetBuilt(ArrayList<ISpelar> spelarar) throws RemoteException;
    public Set<IRute> getRuter();
    void nyRute(IRute rute);

}

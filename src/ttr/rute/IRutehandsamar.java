package ttr.rute;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

public interface IRutehandsamar {
    Set<IRute> getAlleBygdeRuter();

    Set<IRute> findRoutesNotYetBuilt(Collection<ISpelar> spelarar) throws RemoteException;
    public Set<IRute> getRuter();
    void nyRute(IRute rute);

}

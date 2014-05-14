package ttr.rute;

import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

public interface IRutehandsamar {
    Set<IRoute> getAlleBygdeRuter();

    Set<IRoute> findRoutesNotYetBuilt(Collection<ISpelar> spelarar) throws RemoteException;
    public Set<IRoute> getRuter();
    void nyRute(IRoute rute);

}

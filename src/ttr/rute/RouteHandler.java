package ttr.rute;

import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

public interface RouteHandler {
    Set<Route> getBuiltRoutes();

    Set<Route> findRoutesNotYetBuilt(Collection<PlayerAndNetworkWTF> spelarar) throws RemoteException;
    public Set<Route> getRoutes();
    void newRoute(Route rute);

}

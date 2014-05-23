package ttr.oppdrag;

import ttr.rute.Route;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface PlayerMissionHandler extends Remote{
    void bygg(Route rute) throws RemoteException;
    boolean isMissionAccomplished(Mission mission) throws RemoteException;

    int getAntalFullfoerteOppdrag() throws RemoteException;
    int getOppdragspoeng() throws RemoteException;
    Collection<Mission> getOppdrag() throws RemoteException;
    void retrieveMission(Mission o) throws RemoteException;
    int getAntalOppdrag() throws RemoteException;

}

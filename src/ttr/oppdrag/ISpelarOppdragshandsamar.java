package ttr.oppdrag;

import ttr.rute.Route;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISpelarOppdragshandsamar extends Remote{
    void bygg(Route rute) throws RemoteException;
    Mission trekkOppdragskort() throws RemoteException;
    boolean isMissionAccomplished(int oppdragsid) throws RemoteException;

    int getAntalFullfoerteOppdrag() throws RemoteException;
    int getOppdragspoeng() throws RemoteException;
    ArrayList<Mission> getOppdrag() throws RemoteException;
    void retrieveMission(Mission o) throws RemoteException;
    int getAntalOppdrag() throws RemoteException;
    void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException;

}

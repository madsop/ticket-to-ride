package ttr.oppdrag;

import ttr.rute.IRoute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISpelarOppdragshandsamar extends Remote{
    void bygg(IRoute rute) throws RemoteException;
    Mission trekkOppdragskort() throws RemoteException;
    boolean erOppdragFerdig(int oppdragsid) throws RemoteException;

    int getAntalFullfoerteOppdrag() throws RemoteException;
    int getOppdragspoeng() throws RemoteException;
    ArrayList<Mission> getOppdrag() throws RemoteException;
    void faaOppdrag(Mission o) throws RemoteException;
    int getAntalOppdrag() throws RemoteException;
    void trekt(int oppdragsid) throws RemoteException;

}

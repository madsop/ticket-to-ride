package ttr.spelar;

import ttr.kjerna.IHovud;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISpelarOppdragshandsamar extends Remote{
    void bygg(Rute rute) throws RemoteException;
    IOppdrag trekkOppdragskort() throws RemoteException;
    boolean erOppdragFerdig(int oppdragsid) throws RemoteException;

    int getAntalFullfoerteOppdrag() throws RemoteException;
    int getOppdragspoeng() throws RemoteException;
    ArrayList<IOppdrag> getOppdrag() throws RemoteException;
    void faaOppdrag(IOppdrag o) throws RemoteException;
    int getAntalOppdrag() throws RemoteException;
    void trekt(int oppdragsid) throws RemoteException;

}

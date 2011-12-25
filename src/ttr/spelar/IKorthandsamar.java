package ttr.spelar;

import ttr.data.Farge;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IKorthandsamar extends Remote {
    Farge getTilfeldigKortFråBordet(int i) throws RemoteException;

    void faaKort(Farge farge) throws RemoteException;

    int[] getKort() throws RemoteException;

    Farge trekkFargekort() throws RemoteException;
}

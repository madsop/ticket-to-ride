package ttr.copy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Spelar extends Remote{
	public void registrerKlient(Spelar s) throws RemoteException;
	public void bygg(Rute rute, Farge farge) throws RemoteException;
	public Farge trekkFargekort() throws RemoteException;
	public Oppdrag trekkOppdragskort() throws RemoteException;
	public int getGjenverandeTog() throws RemoteException;
	public int getRutepoeng() throws RemoteException;
	public int getOppdragspoeng() throws RemoteException;
	public String getNamn() throws RemoteException;
	public int[] getKort() throws RemoteException;
	public ArrayList<Oppdrag> getOppdrag() throws RemoteException;
	public void faaOppdrag(Oppdrag oppdrag) throws RemoteException;
	public void faaKort(Farge farge) throws RemoteException;
	public int getAntalOppdrag() throws RemoteException;	
	public void setEinVald(boolean b) throws RemoteException;
	public ArrayList<Rute> getBygdeRuter() throws RemoteException;
	public boolean getValdAllereie() throws RemoteException;
	public Hovud getHovud() throws RemoteException;
	public void settSinTur(Spelar s) throws RemoteException;
	public int getSpelarNummer() throws RemoteException;
	public void setSpelarNummer(int nummer) throws RemoteException;
	public int getSpelarteljar() throws RemoteException;
	public void setSpelarteljar(int teljar) throws RemoteException;
	public void setTogAtt(int plass, int tog) throws RemoteException;
	public Farge getTilfeldigKortFråBordet(int i , boolean b) throws RemoteException;
	public void nybygdRute(int ruteId, Spelar byggjandeSpelar) throws RemoteException;
	public int getBygdeRuterStr() throws RemoteException;
	public int getBygdeRuterId(int j) throws RemoteException;
	public int[] getPaaBordetInt() throws RemoteException;
	public void setPaaBord(Farge[] f) throws RemoteException;
}

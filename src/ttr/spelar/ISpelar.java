package ttr.spelar;

import ttr.Hovud;
import ttr.data.Farge;
import ttr.struktur.Oppdrag;
import ttr.struktur.Rute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISpelar extends Remote{
	public void registrerKlient(ISpelar s) throws RemoteException;
	public void bygg(Rute rute) throws RemoteException;
	public Farge trekkFargekort() throws RemoteException;
	public Oppdrag trekkOppdragskort() throws RemoteException;
	public int getGjenverandeTog() throws RemoteException;
	public int getRutepoeng() throws RemoteException;
	public int getOppdragspoeng() throws RemoteException;
	public String getNamn() throws RemoteException;
	public int[] getKort() throws RemoteException;
	public ArrayList<Oppdrag> getOppdrag() throws RemoteException;
	//public void faaOppdragInt(int i) throws RemoteException;
	public void faaOppdrag(Oppdrag o) throws RemoteException;
	public void faaKort(Farge farge) throws RemoteException;
	public int getAntalOppdrag() throws RemoteException;	
	public void setEinVald(boolean b) throws RemoteException;
	public ArrayList<Rute> getBygdeRuter() throws RemoteException;
	public boolean getValdAllereie() throws RemoteException;
	public Hovud getHovud() throws RemoteException;
	public void settSinTur(ISpelar s) throws RemoteException;
	public int getSpelarNummer() throws RemoteException;
	public void setSpelarNummer(int nummer) throws RemoteException;
	public int getSpelarteljar() throws RemoteException;
	public void setSpelarteljar(int teljar) throws RemoteException;
	public void setTogAtt(int plass, int tog) throws RemoteException;
	public Farge getTilfeldigKortFråBordet(int i) throws RemoteException;
	public void nybygdRute(int ruteId, ISpelar byggjandeSpelar) throws RemoteException;
	public int getBygdeRuterStr() throws RemoteException;
	public int getBygdeRuterId(int j) throws RemoteException;
	public int[] getPaaBordetInt() throws RemoteException;
	public void setPaaBord(Farge[] f) throws RemoteException;
	public ArrayList<ISpelar> getSpelarar() throws RemoteException;
	public void setPaaBordet(Farge f, int plass) throws RemoteException;
	public void leggUtFem() throws RemoteException;
	public boolean sjekkJokrar() throws RemoteException;
	public void trekt(int oppdragsid) throws RemoteException;
	public void leggIStokken(int tabellplass, int kormange) throws RemoteException;
	public int trekkOppdragskortInt() throws RemoteException;
	public void visSpeletErFerdigmelding(String melding) throws RemoteException;
	public void faaMelding(String melding) throws RemoteException;
	public int getAntalFullfoerteOppdrag() throws RemoteException;
	public boolean erOppdragFerdig(int oppdragsid) throws RemoteException;
}
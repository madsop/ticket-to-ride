package ttr.spelar;

import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISpelar extends Remote {
	public void registrerKlient(ISpelar s) throws RemoteException;
	public void bygg(Route rute) throws RemoteException;
	public int getGjenverandeTog() throws RemoteException;
	public String getNamn() throws RemoteException;
	public void setEittKortTrektInn(boolean b) throws RemoteException;
	public boolean getValdAllereie() throws RemoteException;
	public void settSinTur(ISpelar s) throws RemoteException;
	public int getSpelarNummer() throws RemoteException;
	public void setSpelarNummer(int nummer) throws RemoteException;
	public int getSpelarteljar() throws RemoteException;
	public void setSpelarteljar(int teljar) throws RemoteException;
	public void setTogAtt(int plass, int tog) throws RemoteException;
	public void nybygdRute(int ruteId, ISpelar byggjandeSpelar) throws RemoteException;
	public int getBygdeRuterSize() throws RemoteException;
	public int getBygdeRuterId(int j) throws RemoteException;
	public int[] getPaaBordetInt() throws RemoteException;
	public void setPaaBord(Farge[] f) throws RemoteException;
	public ArrayList<ISpelar> getSpelarar() throws RemoteException;
	public void putCardOnTable(Farge f, int plass) throws RemoteException;
	public void leggUtFem() throws RemoteException;
	public boolean areThereTooManyJokersOnTable() throws RemoteException;
	public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException;
	public void leggIStokken(int tabellplass, int kormange) throws RemoteException;
	public void visSpeletErFerdigmelding(String melding) throws RemoteException;
	public void faaMelding(String melding) throws RemoteException;


	public int getAntalFullfoerteOppdrag() throws RemoteException;
    public int getOppdragspoeng() throws RemoteException;
    public ArrayList<Mission> getOppdrag() throws RemoteException;
    public void receiveMission(Mission o) throws RemoteException;
    public int getAntalOppdrag() throws RemoteException;

    public boolean isMissionAccomplished(int oppdragsid) throws RemoteException;
    public Mission trekkOppdragskort() throws RemoteException;


    public void receiveCard(Farge farge) throws RemoteException;
    public Farge getTilfeldigKortFråBordet(int positionOnTable) throws RemoteException;
    public Farge trekkFargekort() throws RemoteException;
	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException;
	public int getNumberOfRemainingJokers() throws RemoteException;
	public void decrementCardsAt(Farge colour, int number)  throws RemoteException;
}
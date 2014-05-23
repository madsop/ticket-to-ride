package ttr.spelar;

import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

public interface PlayerAndNetworkWTF extends Remote {
	public void registrerKlient(PlayerAndNetworkWTF s) throws RemoteException;
	public void bygg(Route rute) throws RemoteException;
	public int getGjenverandeTog() throws RemoteException;
	public String getNamn() throws RemoteException;
	public void setEittKortTrektInn(boolean b) throws RemoteException;
	public boolean getValdAllereie() throws RemoteException;
	public void settSinTur(PlayerAndNetworkWTF s) throws RemoteException;
	public int getSpelarNummer() throws RemoteException;
	public void setSpelarNummer(int nummer) throws RemoteException;
	public int getSpelarteljar() throws RemoteException;
	public void setSpelarteljar(int teljar) throws RemoteException;
	public void setTogAtt(int plass, int tog) throws RemoteException;
	public void nybygdRute(int ruteId, PlayerAndNetworkWTF byggjandeSpelar) throws RemoteException;
	public int getBygdeRuterSize() throws RemoteException;
	public int getBygdeRuterId(int j) throws RemoteException;
	public int[] getPaaBordetInt() throws RemoteException;
	public void setPaaBord(Farge[] f) throws RemoteException;
	public ArrayList<PlayerAndNetworkWTF> getSpelarar() throws RemoteException;
	public void putCardOnTable(Farge f, int plass) throws RemoteException;
	public void leggUtFem() throws RemoteException;
	public boolean areThereTooManyJokersOnTable() throws RemoteException;
	public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException;
	public void leggIStokken(Farge colour, int kormange) throws RemoteException;
	public void receiveMessage(String melding) throws RemoteException;


	public int getAntalFullfoerteOppdrag() throws RemoteException;
    public int getOppdragspoeng() throws RemoteException;
    public Collection<Mission> getOppdrag() throws RemoteException;
    public void receiveMission(Mission mission) throws RemoteException;
    public int getAntalOppdrag() throws RemoteException;

    public boolean isMissionAccomplished(Mission mission) throws RemoteException;
    public Mission trekkOppdragskort() throws RemoteException;


    public void receiveCard(Farge farge) throws RemoteException;
    public Farge getRandomCardFromTheDeck(int positionOnTable) throws RemoteException;
    public Farge trekkFargekort() throws RemoteException;
	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException;
	public int getNumberOfRemainingJokers() throws RemoteException;
	public void decrementCardsAt(Farge colour, int number) throws RemoteException;
	public void showGameOverMessage(String poeng) throws RemoteException;
}
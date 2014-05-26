package ttr.spelar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import ttr.data.Colour;
import ttr.oppdrag.Mission;
import ttr.rute.Route;

public interface IPlayer extends Remote {

	/**
	 * Registers a player as this player's adversary
	 * Kokt frå distsys, øving 2.
	 * @param p	The client that is registering as the adversary
	 */
	public abstract void registrerKlient(IPlayer host) throws RemoteException;

	// FASADE
	public abstract void settSinTur(IPlayer sinTur) throws RemoteException;

	public abstract ArrayList<IPlayer> getSpelarar() throws RemoteException;

	public abstract void receiveMessage(String message) throws RemoteException;

	public abstract void showGameOverMessage(String points) throws RemoteException;

	// Oppdrag
	public abstract int getAntalFullfoerteOppdrag() throws RemoteException;

	public abstract int getOppdragspoeng() throws RemoteException;

	public abstract Collection<Mission> getOppdrag() throws RemoteException;

	public abstract void receiveMission(Mission mission) throws RemoteException;

	public abstract Mission trekkOppdragskort() throws RemoteException;

	public abstract boolean isMissionAccomplished(Mission mission) throws RemoteException;
	public abstract void removeChosenMissionFromDeck(Mission mission) throws RemoteException;

	// Kort
	public abstract void receiveCard(Colour farge) throws RemoteException;

	public abstract Colour getRandomCardFromTheDeck(int i) throws RemoteException;

	public abstract Colour trekkFargekort() throws RemoteException;

	public abstract int getNumberOfCardsLeftInColour(Colour colour) throws RemoteException;

	public abstract int getNumberOfRemainingJokers() throws RemoteException;

	public abstract void decrementCardsAt(Colour colour, int number) throws RemoteException;

	// Bord
	public abstract void leggUtFem() throws RemoteException;

	public abstract void setPaaBord(Colour[] f) throws RemoteException;

	public abstract void putCardOnTable(Colour colour, int position) throws RemoteException;

	public abstract boolean areThereTooManyJokersOnTable() throws RemoteException;

	public abstract IPlayer getThisAsISpelar() throws RemoteException;

	public abstract void nybygdRute(Route route, IPlayer byggjandeSpelar) throws RemoteException;

	public abstract Colour[] getCardsOnTable() throws RemoteException;
	
	public abstract void bygg(Route rute) throws RemoteException;

	public abstract void setEittKortTrektInn(boolean b) throws RemoteException;

	public abstract int getSpelarNummer() throws RemoteException;

	public abstract boolean hasAlreadyDrawnOneCard() throws RemoteException;

	public abstract void setRemainingTrains(int position, int numberOfTrains) throws RemoteException;

	public abstract void setPlayerNumberAndUpdatePlayerCounter(int nummer) throws RemoteException;

	public abstract int getSpelarteljar() throws RemoteException;

	public abstract Collection<Route> getBygdeRuter() throws RemoteException;

	public abstract String getNamn() throws RemoteException;

	public abstract int getGjenverandeTog() throws RemoteException;
	
	public void updateDeckOnTable(Colour colour, int kortKrevd, int krevdJokrar, int jokrar) throws RemoteException;
}
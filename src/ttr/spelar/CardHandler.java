package ttr.spelar;

import ttr.data.Farge;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CardHandler extends Remote {
    Farge getRandomCardFromTheDeck(int i) throws RemoteException;

    void receiveCard(Farge farge) throws RemoteException;

    Farge drawRandomCardFromTheDeck() throws RemoteException;

	int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException;
	
	void decrementCardsAt(Farge colour, int number) throws RemoteException;
}
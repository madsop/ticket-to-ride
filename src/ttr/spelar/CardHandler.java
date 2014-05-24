package ttr.spelar;

import ttr.data.Colour;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CardHandler extends Remote {
    Colour getRandomCardFromTheDeck(int i) throws RemoteException;

    void receiveCard(Colour farge) throws RemoteException;

    Colour drawRandomCardFromTheDeck() throws RemoteException;

	int getNumberOfCardsLeftInColour(Colour colour) throws RemoteException;
	
	void decrementCardsAt(Colour colour, int number) throws RemoteException;
}

package ttr.spelar;

import ttr.data.Farge;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IKorthandsamar extends Remote {
    Farge getRandomCardFromTheDeck(int i) throws RemoteException;

    void receiveCard(Farge farge) throws RemoteException;

    Farge trekkFargekort() throws RemoteException;

	int getNumberOfCardsLeftInColour(int i) throws RemoteException;

	int getNumberOfRemainingJokers() throws RemoteException;

	void decrementCardsAt(Farge colour, int number) throws RemoteException;
	void decrementJokers(int number) throws RemoteException;
}

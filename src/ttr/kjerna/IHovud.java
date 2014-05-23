package ttr.kjerna;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface IHovud {
    public ArrayList<Mission> getGjenverandeOppdrag();

    public Set<Route> getRuter();

    public Set<Route> getAlleBygdeRuter();

    public Table getBord();
    public boolean isNett();

    public void settIGangSpelet(boolean nett, String hostAddress) throws RemoteException;
    public void sendMessageAboutCard(boolean kort, boolean tilfeldig, Farge colour) throws RemoteException;
    public void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge newColour, int i) throws RemoteException;

    public int getAntalGjenverandeOppdrag ();
    public Mission getOppdrag();


    public ArrayList<PlayerAndNetworkWTF> getSpelarar();
    public PlayerAndNetworkWTF getMinSpelar();
    public void setMinSpelar(PlayerAndNetworkWTF spelar);
    public PlayerAndNetworkWTF getKvenSinTur();
    public void settSinTur(PlayerAndNetworkWTF spelar) throws RemoteException;
    public void nesteSpelar() throws RemoteException;

    public Set<Route> findRoutesNotYetBuilt() throws RemoteException;
    public void bygg(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException;
    public void byggTunnel(Route bygd, Farge colour, int kortKrevd, int krevdJokrar) throws RemoteException;

	public void displayGraphicallyThatThereIsNoCardHere(int positionOnTable);
	
	public void displayNumberOfRemainingTrains(int position, int numberOfTrains);
	public void showGameOverMessage(String message);
	public void receiveMessage(String message);
}
package ttr.spelar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import ttr.bord.Table;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.oppdrag.PlayerMissionHandler;
import ttr.rute.Route;

public abstract class PlayerImpl extends UnicastRemoteObject {
	private static final long serialVersionUID = -6844537139622798129L;
	protected Core hovud;
	protected Table bord;
    protected PlayerMissionHandler playerMissionHandler;

	private int playerNumber;
	private String name;
	protected ArrayList<Route> builtRoutes; // Delvis unaudsynt pga. harEgBygdMellomAogB

	protected static int playerCounter = 0;    //TODO bør flyttes vekk. Kanskje til hovud.
	private boolean einValdAllereie; //TODO denne bør vel ikkje vera her?
	
	public PlayerImpl (Core hovud, String namn, Table bord) throws RemoteException{
		super();
		this.hovud = hovud;
		this.bord = bord;
		this.name = namn;
		einValdAllereie = false;
		builtRoutes = new ArrayList<>();
        playerMissionHandler = new PlayerMissionHandler();
	}
	
	public abstract IPlayer getThisAsISpelar();

	public void bygg(Route rute)  {
		rute.setBuiltBy(getThisAsISpelar());
		// Fjern kort frå spelaren og legg dei i stokken eller ved sida av?
		builtRoutes.add(rute);
        playerMissionHandler.markRouteAsBuilt(rute);
	}

	public void setEittKortTrektInn(boolean b) {
		einValdAllereie = b;
	}
	public boolean hasAlreadyDrawnOneCard() {
		return einValdAllereie;
	}

	public int getSpelarNummer() { return playerNumber; }
	public int getSpelarteljar() { return playerCounter; }
	public void setPlayerNumberAndUpdatePlayerCounter(int number) { 
		playerNumber = number;
		playerCounter = number + 1;
	}
	public Collection<Route> getBygdeRuter() { return builtRoutes; }

	public String getNamn() { return name; }

	public int getGjenverandeTog() {
		int brukteTog = builtRoutes.stream().mapToInt(x -> x.getLength()).sum();
		return Konstantar.ANTAL_TOG - brukteTog;
	}

	public String toString() { return name; }
}
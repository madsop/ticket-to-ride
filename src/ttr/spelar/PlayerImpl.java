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
    protected PlayerMissionHandler spelarOppdragshandsamar;

	private static int spelarteljar = 0;    //TODO bør flyttes vekk. Kanskje til hovud.
	private int spelarNummer;
	private String namn;
	protected ArrayList<Route> bygdeRuter; // Delvis unaudsynt pga. harEgBygdMellomAogB

	private boolean einValdAllereie; //TODO denne bør vel ikkje vera her?
	
	public PlayerImpl (Core hovud, String namn, Table bord) throws RemoteException{
		super();
		this.hovud = hovud;
		this.bord = bord;
		this.namn = namn;
		einValdAllereie = false;
		bygdeRuter = new ArrayList<>();
        spelarOppdragshandsamar = new PlayerMissionHandler();
	}
	
	public abstract PlayerAndNetworkWTF getThisAsISpelar();

	public void bygg(Route rute) throws RemoteException  {
		rute.setBuiltBy(getThisAsISpelar());
		// Fjern kort frå spelaren og legg dei i stokken eller ved sida av?
		bygdeRuter.add(rute);
        spelarOppdragshandsamar.markRouteAsBuilt(rute);
	}

	public void setEittKortTrektInn(boolean b) {
		einValdAllereie = b;
	}
	public int getSpelarNummer() {
		return spelarNummer;
	}
	public boolean hasAlreadyDrawnOneCard() {
		return einValdAllereie;
	}

	public void setTogAtt(int position, int numberOfTrains) { hovud.displayNumberOfRemainingTrains(position, numberOfTrains); }
	public void setSpelarNummer(int nummer) { spelarNummer = nummer; }
	public int getSpelarteljar() { return spelarteljar; }
	public void setSpelarteljar(int teljar) { spelarteljar = teljar; }
	public Collection<Route> getBygdeRuter() { return bygdeRuter; }

	public String getNamn() { return namn; }

	public int getGjenverandeTog() {
		int brukteTog = bygdeRuter.stream().mapToInt(x -> x.getLength()).sum();
		return Konstantar.ANTAL_TOG - brukteTog;
	}

	public String toString() { return namn; }
}
package ttr.spelar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Optional;

import ttr.bord.Table;
import ttr.data.Konstantar;
import ttr.kjerna.Core;
import ttr.oppdrag.PlayerMissionHandler;
import ttr.oppdrag.PlayerMissionHandlerImpl;
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

	private boolean einValdAllereie;
	
	public PlayerImpl (Core hovud, String namn, Table bord) throws RemoteException{
		super();
		this.hovud = hovud;
		this.bord = bord;
		this.namn = namn;
		einValdAllereie = false;
		bygdeRuter = new ArrayList<>();
        spelarOppdragshandsamar = new PlayerMissionHandlerImpl(hovud);
	}
	
	public abstract PlayerAndNetworkWTF getThisAsISpelar();

	public void bygg(Route rute) throws RemoteException  {
		rute.setBuiltBy(getThisAsISpelar());
		// Fjern kort frå spelaren og legg dei i stokken eller ved sida av?
		bygdeRuter.add(rute);
        spelarOppdragshandsamar.bygg(rute);
	}

	public void setEittKortTrektInn(boolean b) {
		einValdAllereie = b;
	}
	public int getSpelarNummer() {
		return spelarNummer;
	}
	public boolean getValdAllereie() {
		return einValdAllereie;
	}

	public void setTogAtt(int position, int numberOfTrains) { hovud.displayNumberOfRemainingTrains(position, numberOfTrains);	}
	public void setSpelarNummer(int nummer) { spelarNummer = nummer; }
	public int getSpelarteljar() { return spelarteljar; }
	public void setSpelarteljar(int teljar) { spelarteljar = teljar; }
	public int getBygdeRuterSize() { return bygdeRuter.size(); }
	public int getBygdeRuterId(int j) { return bygdeRuter.get(j).getRouteId(); }

	public String getNamn() { return namn; }

	public int getGjenverandeTog() {
		int brukteTog = bygdeRuter.stream().mapToInt(x -> x.getLength()).sum();
		return Konstantar.ANTAL_TOG - brukteTog;
	}

	public String toString() { return namn; }

	public void nybygdRute(int ruteId, PlayerAndNetworkWTF byggjandeSpelar) {
		Route vald = getRoute(ruteId).get();
		vald.setBuiltBy(byggjandeSpelar);
		hovud.getAlleBygdeRuter().add(vald);
	}

	private Optional<Route> getRoute(int ruteId) {
		return hovud.getRuter().stream().filter(f -> f.getRouteId()==ruteId).findAny();
	}

	public int[] getPaaBordetInt() { // TODO wtf
		int[] bord = new int[Konstantar.ANTAL_KORT_PÅ_BORDET];

		for (int i = 0; i < hovud.getTable().getPaaBordet().length; i++) {
			for (int colourInt = 0; colourInt < Konstantar.FARGAR.length; colourInt++) {
				if (hovud.getTable().getCardFromTable(i) == Konstantar.FARGAR[colourInt]) {
					bord[i] = colourInt;
				}
			}
		}

		return bord;
	}

}
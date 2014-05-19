package ttr.oppdrag;

import ttr.data.Destinasjon;
import ttr.kjerna.IHovud;
import ttr.rute.IRoute;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SpelarOppdragshandsamar extends UnicastRemoteObject implements ISpelarOppdragshandsamar{
	private static final long serialVersionUID = 5194460142995578869L;
	private ArrayList<Mission> missions;
	private IHovud hovud;
	private boolean[][] harEgBygdMellomAogB;

	public SpelarOppdragshandsamar(IHovud hovud) throws RemoteException {
		super();
		missions = new ArrayList<>();
		this.hovud = hovud;
		harEgBygdMellomAogB = new boolean[Destinasjon.values().length][Destinasjon.values().length];
		initialiserMatrise();
	}

	public int getAntalOppdrag() throws RemoteException {
		return missions.size();
	}

	public void retrieveMission(Mission mission) throws RemoteException{
		if(!this.missions.contains(mission)){
			this.missions.add(mission);
		}
	}
	public ArrayList<Mission> getOppdrag() throws RemoteException  {
		return missions;
	}
	
	public int getOppdragspoeng() throws RemoteException  {
		int totalMissionValue = 0;
		for (Mission mission : missions) {
			if (haveIBuiltThisMission(mission)) {
				totalMissionValue += mission.getValue();
			} else {
				totalMissionValue -= mission.getValue();
			}
		}
		return totalMissionValue;
	}
	
	public boolean isMissionAccomplished(int oppdragsid) throws RemoteException{
		Mission mission = findMissionById(oppdragsid);
		return haveIBuiltThisMission(mission);
	}

	public int getAntalFullfoerteOppdrag() throws RemoteException{
		return (int) missions.stream().filter(x -> haveIBuiltThisMission(x)).count();
	}

	public void bygg(IRoute rute) throws RemoteException{
		// Sjekk for fullførde oppdrag?
				// Fyller matrisa med ei rute frå d1 til d2 (og motsett):
					// Må først iterere over mengda med destinasjonar for å få dei ut
		int destinasjon1 = rute.getStart().ordinal();
		int destinasjon2 = rute.getEnd().ordinal();
		harEgBygdMellomAogB[destinasjon1][destinasjon2] = true;
		harEgBygdMellomAogB[destinasjon2][destinasjon1] = true;
		transitivTillukking();
	}


	/**
	 * @return trekk eit oppdrag frå kortbunken
	 */
	public Mission trekkOppdragskort() throws RemoteException  {
		if (hovud.getAntalGjenverandeOppdrag() > 0) {
			return hovud.getOppdrag();
			//System.out.println(trekt.getDestinasjonar().toArray()[1]);
		}
		return null;
	}

	public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException {
		hovud.getGjenverandeOppdrag().removeIf(x -> (x.getMissionId() == oppdragsid));
	}

	private Mission findMissionById(int oppdragsid) {
		return missions.stream().filter(mission -> mission.getMissionId() == oppdragsid).findAny().get();
	}

	private boolean haveIBuiltThisMission(Mission mission) {
		int start = mission.getStart().ordinal();
		int end = mission.getEnd().ordinal();
		return harEgBygdMellomAogB[start][end] || harEgBygdMellomAogB[end][start];
	}

	/**
	 * Bør testes
	 */
	private void transitivTillukking() {
		// Dette er ein implementasjon av Warshallalgoritma, side 553 i Rosen [DiskMat]
		// Køyretida er på (u)behagelege 2n³, som er tilnærma likt optimalt
		int n = harEgBygdMellomAogB.length;

		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					harEgBygdMellomAogB[i][j] = harEgBygdMellomAogB[i][j] || (harEgBygdMellomAogB[i][k] && harEgBygdMellomAogB[k][j]);
				}
			}
		}
	}

	/**
	 * Oppretter ei #destinasjonar*#destinasjonar med alle verdiar false.
	 */
	private void initialiserMatrise() {
		for (int y = 0; y < Destinasjon.values().length; y++) {
			for (int x = 0; x < Destinasjon.values().length; x++) {
				harEgBygdMellomAogB[y][x] = false;
			}
		}
	}
}

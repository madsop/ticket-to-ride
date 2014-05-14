package ttr.oppdrag;

import ttr.data.Destinasjon;
import ttr.kjerna.IHovud;
import ttr.rute.IRute;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SpelarOppdragshandsamar extends UnicastRemoteObject implements ISpelarOppdragshandsamar{
	private static final long serialVersionUID = 5194460142995578869L;
	private ArrayList<IOppdrag> missions;
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

	public void faaOppdrag(IOppdrag o) throws RemoteException{
		if(!this.missions.contains(o)){
			this.missions.add(o);
		}
	}
	public ArrayList<IOppdrag> getOppdrag() throws RemoteException  {
		return missions;
	}
	/**
	 * Finn ein måte å gjera denne på - dvs sjekke om oppdrag er fullførte eller ikkje.
	 * @return antal oppdragspoeng spelaren har
	 */
	public int getOppdragspoeng() throws RemoteException  {
		int totalMissionValue = 0;
		for (IOppdrag mission : missions) {
			if (haveIBuiltThisMission(mission)) {
				totalMissionValue += mission.getVerdi();
			} else {
				totalMissionValue -= mission.getVerdi();
			}
		}

		return totalMissionValue;
	}
	public boolean erOppdragFerdig(int oppdragsid) throws RemoteException{
		IOppdrag mission = findMissionById(oppdragsid);
		if (mission==null){
			return false;
		}
		return haveIBuiltThisMission(mission);
	}

	public int getAntalFullfoerteOppdrag() throws RemoteException{
		int numberOfFulfilledMissions = 0;
		for (IOppdrag mission : missions){
			if (haveIBuiltThisMission(mission)) {
				numberOfFulfilledMissions++;
			}
		}
		return numberOfFulfilledMissions;
	}


	public void bygg(IRute rute) throws RemoteException{
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
	public IOppdrag trekkOppdragskort() throws RemoteException  {
		if (hovud.getAntalGjenverandeOppdrag() > 0) {
			return hovud.getOppdrag();
			//System.out.println(trekt.getDestinasjonar().toArray()[1]);
		}
		return null;
	}

	public void trekt(int oppdragsid) throws RemoteException {
		// Finn oppdrag
		for (int i = 0; i < hovud.getAntalGjenverandeOppdrag(); i++){
			if (hovud.getGjenverandeOppdrag().get(i).getOppdragsid() == oppdragsid){
				hovud.getGjenverandeOppdrag().remove(i);
			}
		}
	}

	private IOppdrag findMissionById(int oppdragsid) {
		for (IOppdrag mission : missions){
			if (mission.getOppdragsid() == oppdragsid){
				return mission;
			}
		}
		return null;
	}

	private boolean haveIBuiltThisMission(IOppdrag mission) {
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

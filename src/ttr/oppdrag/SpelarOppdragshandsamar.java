package ttr.oppdrag;

import ttr.data.Destinasjon;
import ttr.kjerna.IHovud;
import ttr.rute.IRute;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class SpelarOppdragshandsamar extends UnicastRemoteObject implements ISpelarOppdragshandsamar{
    private ArrayList<IOppdrag> oppdrag;
    private IHovud hovud;
    private boolean[][] harEgBygdMellomAogB;

    public SpelarOppdragshandsamar(IHovud hovud) throws RemoteException {
        super();
        oppdrag = new ArrayList<>();
        this.hovud = hovud;
        harEgBygdMellomAogB = new boolean[Destinasjon.values().length][Destinasjon.values().length];
        initialiserMatrise();
    }

    public int getAntalOppdrag()  throws RemoteException {
        return oppdrag.size();
    }

    public void faaOppdrag(IOppdrag o) throws RemoteException{
        if(!this.oppdrag.contains(o)){
            this.oppdrag.add(o);
        }
    }
    public ArrayList<IOppdrag> getOppdrag() throws RemoteException  {
        return oppdrag;
    }
    /**
     * Finn ein måte å gjera denne på - dvs sjekke om oppdrag er fullførte eller ikkje.
     * @return antal oppdragspoeng spelaren har
     */
    public int getOppdragspoeng() throws RemoteException  {
        int totalMissionValue = 0;
        for (IOppdrag mission : oppdrag) {
            int start = mission.getStart().ordinal();
            int end = mission.getEnd().ordinal();
            if (harEgBygdMellomAogB[start][end] || harEgBygdMellomAogB[end][start]) {
                totalMissionValue += mission.getVerdi();
            } else {
                totalMissionValue -= mission.getVerdi();
            }
        }

        return totalMissionValue;
    }
    public boolean erOppdragFerdig(int oppdragsid) throws RemoteException{
        IOppdrag mission = null;
        for (IOppdrag opp : oppdrag){
            if (opp.getOppdragsid() == oppdragsid){
                mission = opp;
            }
        }
        if (mission==null){
            return false;
        }

        return haveIBuiltThisMission(mission);
    }

	private boolean haveIBuiltThisMission(IOppdrag mission) {
		int start = mission.getStart().ordinal();
        int end = mission.getEnd().ordinal();
        return harEgBygdMellomAogB[start][end] || harEgBygdMellomAogB[end][start];
	}

    public int getAntalFullfoerteOppdrag() throws RemoteException{
        int numberOfFulfilledMissions = 0;
        for (IOppdrag mission : oppdrag){
            if (haveIBuiltThisMission(mission)) {
                numberOfFulfilledMissions++;
            }
        }
        return numberOfFulfilledMissions;
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
    
    public void bygg(IRute rute) throws RemoteException{

        // Sjekk for fullførde oppdrag?

        // Fyller matrisa med ei rute frå d1 til d2 (og motsett):
        // Må først iterere over mengda med destinasjonar for å få dei ut
        Iterator<Destinasjon> mengdeIterator = rute.getDestinasjonar().iterator();
        int destinasjon1 = mengdeIterator.next().ordinal();
        int destinasjon2 = mengdeIterator.next().ordinal();
        harEgBygdMellomAogB[destinasjon1][destinasjon2] = true;
        harEgBygdMellomAogB[destinasjon2][destinasjon1] = true;
        transitivTillukking();
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
}

package ttr.spelar;

import ttr.data.Destinasjon;
import ttr.kjerna.IHovud;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

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
        oppdrag = new ArrayList<IOppdrag>();
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
        int ret = 0;
        for (IOppdrag anOppdrag : oppdrag) {
            Destinasjon d1 = (Destinasjon) anOppdrag.getDestinasjonar().toArray()[0];
            int d = d1.ordinal();
            Destinasjon d2 = (Destinasjon) anOppdrag.getDestinasjonar().toArray()[1];
            int e = d2.ordinal();
            if (harEgBygdMellomAogB[d][e] || harEgBygdMellomAogB[e][d]) {
                ret += anOppdrag.getVerdi();
            } else {
                ret -= anOppdrag.getVerdi();
            }
        }

        return ret;
    }
    public boolean erOppdragFerdig(int oppdragsid) throws RemoteException{
        IOppdrag o = null;
        for (IOppdrag opp : oppdrag){
            if (opp.getOppdragsid() == oppdragsid){
                o = opp;
            }
        }
        if (o==null){
            return false;
        }

        Destinasjon d1 = (Destinasjon) o.getDestinasjonar().toArray()[0];
        int d = d1.ordinal();
        Destinasjon d2 = (Destinasjon) o.getDestinasjonar().toArray()[1];
        int e = d2.ordinal();
        return harEgBygdMellomAogB[d][e] || harEgBygdMellomAogB[e][d];
    }

    public int getAntalFullfoerteOppdrag() throws RemoteException{
        int antal = 0;
        for (IOppdrag o : oppdrag){
            Destinasjon d1 = (Destinasjon) o.getDestinasjonar().toArray()[0];
            int d = d1.ordinal();
            Destinasjon d2 = (Destinasjon) o.getDestinasjonar().toArray()[1];
            int e = d2.ordinal();
            if (harEgBygdMellomAogB[d][e] || harEgBygdMellomAogB[e][d]){
                antal++;
            }
        }
        return antal;
    }


    /**
     * Oppretter ei #destinasjonar*#destinasjonar med alle verdiar false.
     */
    private void initialiserMatrise() throws RemoteException{
        for (int y = 0; y < Destinasjon.values().length; y++) {
            for (int x = 0; x < Destinasjon.values().length; x++) {
                harEgBygdMellomAogB[y][x] = false;
            }
        }
    }
    
    public void bygg(Rute rute) throws RemoteException{

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
        IOppdrag trekt;
        if (hovud.getAntalGjenverandeOppdrag() > 0) {
            trekt = hovud.getOppdrag();
            //System.out.println(trekt.getDestinasjonar().toArray()[1]);
        }
        else {
            trekt = null;
        }
        return trekt;
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

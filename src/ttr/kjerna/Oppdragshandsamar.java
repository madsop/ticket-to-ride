package ttr.kjerna;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Oppdragshandsamar implements IOppdragshandsamar {
    private final ArrayList<IOppdrag> gjenverandeIOppdrag;

    public Oppdragshandsamar(ArrayList<IOppdrag> oppdrag){
        gjenverandeIOppdrag = oppdrag;
        stokkOppdrag();;
    }

    @Override
    public ArrayList<IOppdrag> getGjenverandeOppdrag(){
        return gjenverandeIOppdrag;
    }

    @Override
    public int getAntalGjenverandeOppdrag() {
        return gjenverandeIOppdrag.size();
    }

    @Override
    public IOppdrag getOppdrag() {
        IOppdrag IOppdrag = gjenverandeIOppdrag.get(0);
        gjenverandeIOppdrag.remove(0);
        return IOppdrag;
    }

    private void stokkOppdrag() {
        for (int i = 0; i < gjenverandeIOppdrag.size(); i++) {
            IOppdrag temp = gjenverandeIOppdrag.get(i);
            int rand = (int) (Math.random() * gjenverandeIOppdrag.size());
            gjenverandeIOppdrag.set(i, gjenverandeIOppdrag.get(rand));
            gjenverandeIOppdrag.set(rand, temp);
        }
    }

    public static void trekkOppdrag(IGUI gui, ISpelar s, boolean start) throws RemoteException {
        int talPaaOppdrag = start ? Konstantar.ANTAL_STARTOPPDRAG : Konstantar.ANTAL_VELJEOPPDRAG;

        ArrayList<IOppdrag> oppdrag = new ArrayList<IOppdrag>();

        for (int i = 0; i < talPaaOppdrag; i++) {
            IOppdrag opp = s.trekkOppdragskort();
            oppdrag.add(opp);
        }

        ArrayList<IOppdrag> k = new ArrayList<IOppdrag>();
        while (k.size() < talPaaOppdrag-2){
            k = gui.velOppdrag(oppdrag);
        }

        oppdrag = k;
        for (IOppdrag anOppdrag : oppdrag) {
            s.faaOppdrag(anOppdrag);
        }

    }
}

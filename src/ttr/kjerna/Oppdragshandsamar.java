package ttr.kjerna;

import ttr.struktur.IOppdrag;

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
}

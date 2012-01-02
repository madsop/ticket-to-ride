package ttr.rute;

import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Rutehandsamar implements IRutehandsamar {
    //TODO trengs denne klassa? og kva eksakt gjer ho? +dårleg namn
    private final Set<IRute> ruter;
    private final ArrayList<IRute> alleBygdeRuter;
    
    public Rutehandsamar(ISpelUtgaave spel){
        this.ruter = spel.getRuter();
        this.alleBygdeRuter = new ArrayList<IRute>();
    }
    @Override
    public ArrayList<IRute> getAlleBygdeRuter(){
        return alleBygdeRuter;
    }

    public Set<IRute> getRuter() {
        return ruter;
    }

    @Override
    public void nyRute(IRute rute) {
        alleBygdeRuter.add(rute);
    }


    //TODO denne metoden bør kunne gjerast mykje enklare.
    //TODO Det gjeld vel i grunn lagringa av kven som har bygd kva rute generelt.
    @Override
    public IRute[] finnFramRuter(ArrayList<ISpelar> spelarar) throws RemoteException {
        for (ISpelar spelar : spelarar) {
            for (int j = 0; j < spelar.getBygdeRuterStr(); j++) {
                int ruteId = spelar.getBygdeRuterId(j);
                for (IRute r : ruter) {
                    if (r.getRuteId() == ruteId && !(alleBygdeRuter.contains(r))) {
                        alleBygdeRuter.add(r);
                    }
                }
            }
        }

        int str = ruter.size();
        IRute[] ruterTemp = new IRute[str];
        Iterator<IRute> ruteIterator = ruter.iterator();
        for (int i = 0; i < str; i++) {
            ruterTemp[i] =  ruteIterator.next();
        }

        IRute[] ruterArray = new IRute[str-alleBygdeRuter.size()];
        int c = 0;

        for (int i = 0; i < str; i++) {
            boolean b = true;
            for (IRute eiBygdRute : alleBygdeRuter) {
                if (ruterTemp[i] == eiBygdRute) {
                    b = false;
                }
            }
            if (b) {
                IRute r = ruterTemp[i];
                if (c < ruterArray.length) {
                    ruterArray[c] = r;
                    c++;
                }

            }
        }
        return ruterArray;
    }
}
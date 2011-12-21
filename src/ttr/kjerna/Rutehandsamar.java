package ttr.kjerna;

import ttr.spelar.ISpelar;
import ttr.struktur.Rute;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Rutehandsamar implements IRutehandsamar {
    private Set<Rute> ruter;
    private ArrayList<Rute> alleBygdeRuter;
    
    public Rutehandsamar(ISpelUtgaave spel){
        this.ruter = spel.getRuter();
        this.alleBygdeRuter = new ArrayList<Rute>();
    }
    @Override
    public ArrayList<Rute> getAlleBygdeRuter(){
        return alleBygdeRuter;
    }

    public Set<Rute> getRuter() {
        return ruter;
    }

    @Override
    public void nyRute(Rute rute) {
        alleBygdeRuter.add(rute);
    }


    @Override
    public Rute[] finnFramRuter(ArrayList<ISpelar> spelarar) throws RemoteException {
        for (ISpelar aSpelarar1 : spelarar) {
            for (int j = 0; j < aSpelarar1.getBygdeRuterStr(); j++) {
                int ruteId = aSpelarar1.getBygdeRuterId(j);
                for (Rute r : ruter) {
                    if (r.getRuteId() == ruteId && !(alleBygdeRuter.contains(r))) {
                        alleBygdeRuter.add(r);
                    }
                }
            }
        }

        int str = ruter.size();
        Rute[] ruterTemp = new Rute[str];
        Iterator<Rute> it2 = ruter.iterator();
        for (int i = 0; i < str; i++) {
            ruterTemp[i] = it2.next();
        }

        /*	ArrayList<Rute>	bR = new ArrayList<Rute>();
for (ISpelar aSpelarar : spelarar) {
for (int j = 0; j < aSpelarar.getBygdeRuter().size(); j++) {
  bR.add(aSpelarar.getBygdeRuter().get(j));
}
for (int j = 0; j < aSpelarar.getBygdeRuterStr(); j++) {
  int ruteId = aSpelarar.getBygdeRuterId(j);
  for (Rute r : ruter) {
      if (r.getRuteId() == ruteId) {
          bR.add(r);
      }
  }
}
}              */

        Rute[] ruterArray = new Rute[str-alleBygdeRuter.size()];
        int c = 0;

        for (int i = 0; i < str; i++) {
            boolean b = true;
            for (Rute anAlleBygdeRuter : alleBygdeRuter) {
                if (ruterTemp[i] == anAlleBygdeRuter) {
                    b = false;
                }
            }
            if (b) {
                Rute r = ruterTemp[i];
                if (c < ruterArray.length) {
                    ruterArray[c] = r;
                    c++;
                }

            }
        }
        return ruterArray;
    }
}

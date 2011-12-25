package ttr.gui;

import ttr.kjerna.IHovud;
import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.Oppdragshandsamar;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


class Hogrepanelet extends JPanel {
    // Brukargrensesnittet - på høgresida
    private JTextField spelarnamn;
    private final IHovud hovud;
    private final IGUI gui;
    private final JFrame frame;
    private JButton kortBunke;

    private JButton trekkOppdrag, bygg, visBygde, visMineKort, visMineOppdrag;
    private JButton[] kortButtons;
    private JLabel[] togAtt;

    private gjerNokoListener gNL;
    public Hogrepanelet(IHovud hovud, GUI gui, JFrame frame){
        this.hovud = hovud;
        this.gui = gui;
        this.frame = frame;
    }

    public JButton[] getKortButtons(){
        return kortButtons;
    }

    public JTextField getSpelarnamn() {
        return spelarnamn;
    }

    public JLabel[] getTogAtt() {
        return togAtt;
    }

    public void byggHogrepanelet(){
        gNL = new gjerNokoListener();
        GridBagLayout hogregbl = new GridBagLayout();
        this.setLayout(hogregbl);
        GridBagConstraints d;
        d = new GridBagConstraints();
        d.ipadx = 20;
        d.ipady = 0;
        d.insets = Konstantar.INSETS;

        hogregbl.setConstraints(this, d);
        d.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(Konstantar.hogrebreidde, Konstantar.HOGDE));
        this.setBackground(Color.GRAY);

        spelarnamn = new JTextField("Denne spelaren sin tur");
        spelarnamn.setEditable(false);
        spelarnamn.setMinimumSize(Konstantar.SPELARNAMNDIM);

        this.setPreferredSize(new Dimension(Konstantar.hogrebreidde, Konstantar.HOGDE));


        d.gridwidth = 3;
        d.gridx = 0;
        d.gridy = 0;
        this.add(spelarnamn, d);

        d.gridwidth = 1;
        d.gridy = 1;
        trekkOppdrag = new JButton("Trekk oppdrag");
        trekkOppdrag.setMinimumSize(Konstantar.KNAPP);
        trekkOppdrag.addActionListener(gNL);
        this.add(trekkOppdrag, d);

        d.gridx = 1;
        bygg = new JButton("Bygg ei togrute");
        bygg.setMinimumSize(Konstantar.KNAPP);
        bygg.addActionListener(gNL);
        this.add(bygg, d);

        d.gridy = 2;
        d.gridx = 0;
        visBygde = new JButton("Vis bygde ruter");
        visBygde.setMinimumSize(Konstantar.KNAPP);
        visBygde.addActionListener(gNL);
        this.add(visBygde, d);

        d.gridy = 3;
        d.gridx = 0;
        visMineOppdrag = new JButton("Vis mine oppdrag");
        visMineOppdrag.setMinimumSize(Konstantar.KNAPP);
        visMineOppdrag.addActionListener(gNL);
        this.add(visMineOppdrag, d);

        d.gridx = 1;
        visMineKort = new JButton("Vis mine kort");
        visMineKort.setMinimumSize(Konstantar.KNAPP);
        visMineKort.addActionListener(gNL);
        this.add(visMineKort, d);

        togAtt = new JLabel[4];

        d.gridy = 4;
        d.gridx = 0;
        togAtt[0] = new JLabel("Tog att:");
        togAtt[0].setFont(Konstantar.TOGTALFONT);
        this.add(togAtt[0], d);

        d.gridy = 5;
        togAtt[1] = new JLabel("40");
        togAtt[1].setForeground(Color.WHITE);
        togAtt[1].setFont(Konstantar.TOGTALFONT);
        this.add(togAtt[1], d);

        d.gridx = 1;
        togAtt[2] = new JLabel("40");
        togAtt[2].setForeground(Color.BLACK);
        togAtt[2].setFont(Konstantar.TOGTALFONT);
        this.add(togAtt[2], d);

        d.gridy = 6;
        d.gridx = 0;
        togAtt[3] = new JLabel("40");
        togAtt[3].setForeground(Color.PINK);
        togAtt[3].setFont(Konstantar.TOGTALFONT);
        this.add(togAtt[3], d);

        visFargekorta(d);
    }

    /**
     * Teiknar opp eit kort på bordet
     * @param plass - kva for ein av plassane på bordet
     * @param farge - i kva farge
     */
    public void setKortPaaBordet(int plass, Farge farge) {
        kortButtons[plass].setForeground(Color.BLACK);
        kortButtons[plass].setBackground(Konstantar.fargeTilColor(farge));
        String ret;
        switch (farge) {
            case blå:
                ret = "Blå";
                break;
            case gul:
                ret =  "Gul";
                break;
            case raud:
                ret = "Raud";
                break;
            case grønn:
                ret = "Grønn";
                break;
            case kvit:
                ret = "Kvit";
                break;
            case lilla:
                ret = "Lilla";
                break;
            case oransje:
                ret = "Oransje";
                break;
            case svart:
                ret = "Svart";
                kortButtons[plass].setForeground(Color.WHITE);
                break;
            case valfri:
                ret = "Joker";
                break;
            default:
                ret = "Ops";
                break;
        }
        kortButtons[plass].setText(ret);
    }

    /**
     * Lager ein knapp for å trekkje inn kort
     * @param i plassen på bordet
     * @throws RemoteException
     */
    void kortButton(int i) throws RemoteException {
        Farge f = hovud.getBord().getPaaBordet()[i];
        if (hovud.getKvenSinTur().getValdAllereie()) {
            if (f == Farge.valfri) {
                JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
                return;
            }
            else if (f==null){return;}
            else {
                hovud.getKvenSinTur().faaKort(f);
                gui.sendKortMelding(true,false,f);
                hovud.getBord().getTilfeldigKortFråBordet(i, true);
                hovud.nesteSpelar();
            }
        }
        else {
            if (f==null){return;}
            hovud.getKvenSinTur().faaKort(f);
            gui.sendKortMelding(true,false,f);
            hovud.getBord().getTilfeldigKortFråBordet(i, true);
            if (f == Farge.valfri) {
                hovud.nesteSpelar();
            }
            hovud.getKvenSinTur().setEinVald(true);
        }
        ISpelar vert = null;
        if (hovud.isNett()){
            if (hovud.getMinSpelar().getSpelarNummer()==0) {
                vert = hovud.getMinSpelar();
            }
        }
        for (ISpelar s : hovud.getSpelarar()) {
            if (!hovud.isNett()){
                s.getTilfeldigKortFråBordet(i);
            }
            else {
                if (s.getSpelarNummer()==0) {
                    vert = s;
                }
            }
        }
        if (hovud.isNett() && vert!=null){
            Farge nyFarge = vert.getTilfeldigKortFråBordet(i);
            while (vert.sjekkJokrar()) {
                vert.leggUtFem();
                int[] paaSomInt = vert.getPaaBordetInt();

                for (int plass = 0; plass < hovud.getBord().getPaaBordet().length; plass++){
                    nyFarge = Konstantar.FARGAR[paaSomInt[plass]];
                    hovud.getMinSpelar().setPaaBordet(nyFarge,plass);
                    gui.nyPaaPlass(vert, nyFarge, plass);
                }
            }
            gui.nyPaaPlass(vert, nyFarge, i);
        }
    }

    /**
     * Blir kalla av JButton-ane på høgrepanelet. Er eigentleg koplinga mellom GUI og kjerne.
     * //TODO Bør ut frå heile gui-pakka
     * @author mads
     *
     */
    private class gjerNokoListener implements ActionListener {


        private void trekkOppragHandler() {
            try {
                gui.sendKortMelding(false, false, Konstantar.FARGAR[0]);
                if (hovud.isNett()){
                    Oppdragshandsamar.trekkOppdrag(gui,hovud.getMinSpelar(), false);
                }
                else {
                    Oppdragshandsamar.trekkOppdrag(gui, hovud.getKvenSinTur(),false);
                }
                hovud.nesteSpelar();
            }
            catch (RemoteException re) {
                re.printStackTrace();
            }
        }

        private void byggHandler() {
            Rute[] ruterArray = null;
            try {
                ruterArray = hovud.finnFramRuter();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            Rute ubygdeRuter = (Rute) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
                    JOptionPane.QUESTION_MESSAGE,null,ruterArray,ruterArray[1]);

            Rute bygd = null;
            for (Rute aRuterArray : ruterArray) {
                if (aRuterArray == ubygdeRuter) {
                    bygd = aRuterArray;
                }
            }

            if (bygd!=null) {
                int[] spelarensKort = null;
                try {
                    spelarensKort = hovud.getKvenSinTur().getKort();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                int kortKrevd = bygd.getLengde()-bygd.getAntaljokrar();
                Farge ruteFarge = bygd.getFarge();
                int krevdJokrar = bygd.getAntaljokrar();

                int plass = Konstantar.finnPosisjonForFarg(ruteFarge);

                int harjokrar = spelarensKort[spelarensKort.length-1];
                if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
                    if (bygd.isTunnel()) {
                        try {
                            hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
                        }
                        catch (RemoteException re){
                            re.printStackTrace();
                        }
                    }
                    else {
                        try {
                            hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
                        }
                        catch (RemoteException re){
                            re.printStackTrace();
                        }
                    }
                }
                else if (krevdJokrar <= harjokrar && (kortKrevd <= ( (harjokrar-krevdJokrar) + spelarensKort[plass]) ) ){
                    try {
                        if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+krevdJokrar) {
                            if (bygd.isTunnel()) {
                                hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
                            }
                            else {
                                hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
                        }
                    } catch (HeadlessException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
                }
            }
        }

        private void visMineKortHandler(){
            // vis korta mine
            JPanel korta = new JPanel();

            ISpelar visSine;
            if (hovud.isNett()) {
                visSine = hovud.getMinSpelar();
            }
            else {
                visSine = hovud.getKvenSinTur();
            }
            String[] kort = new String[Konstantar.ANTAL_FARGAR];

            for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
                try {
                    kort[i] = Konstantar.FARGAR[i] +": " +visSine.getKort()[i];
                } catch (RemoteException e) {
                    kort[i] = "";
                    e.printStackTrace();
                }
            }
            JLabel[] oppdr = new JLabel[kort.length];

            try {
                korta.add(new JLabel(visSine.getNamn()));
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            for (int i = 0; i < oppdr.length; i++) {
                oppdr[i] = new JLabel();
                oppdr[i].setText(kort[i]);
                try {
                    if (visSine.getKort()[i] != 0) {
                        oppdr[i].setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
                    }
                    else {
                        oppdr[i].setForeground(Color.LIGHT_GRAY);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                korta.add(oppdr[i]);
            }
            try {
                gui.lagRamme("Viser korta til " +visSine.getNamn(), korta);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void visMineOppdragHandler() {
            // vis oppdraga mine
            JPanel oppdraga = new JPanel();

            ISpelar visSine;
            if (hovud.isNett()) {
                visSine = hovud.getMinSpelar();
            }
            else {
                visSine = hovud.getKvenSinTur();
            }
            String oppdrg = "";
            try {
                oppdrg = visSine.getNamn() +": ";
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            try {
                for (int i = 0; i < visSine.getAntalOppdrag(); i++) {
                    IOppdrag o = visSine.getOppdrag().get(i);
                    oppdrg += o;
                    if (visSine.erOppdragFerdig(o.getOppdragsid())){
                        oppdrg += " (OK)";
                    }
                    if (i == visSine.getAntalOppdrag()-1){
                        oppdrg += ".";
                    }
                    else{
                        oppdrg += ", ";
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            JLabel oppdr = new JLabel(oppdrg);
            oppdraga.add(oppdr);
            try {
                gui.lagRamme("Viser oppdraga til " +visSine.getNamn(), oppdraga);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void visBygdeHandler() {
            JPanel bygde = new JPanel();

            if (hovud.getAlleBygdeRuter().size()>0) {
                @SuppressWarnings("unchecked") JList bygd = new JList(hovud.getAlleBygdeRuter().toArray());

                /*	if (hovud.isNett()){
                   // Finn spel-verten
                   for (int i = 0; i < bygd.getModel().getSize(); i++){
                       for (int j = 0; j < hovud.getSpelarar().size()+1; j++)
                       try {
                          if (hovud.getAlleBygdeRuter().get(i).getBygdAv().getSpelarNummer()==j){
                              bygd.set
                           }
                      } catch (RemoteException e) {
                          e.printStackTrace();
                      }
                   }
               }
                */

                bygde.add(bygd);
                gui.lagRamme("Desse rutene er bygd",bygde);
            }
            else {
                JOptionPane.showMessageDialog(frame, "Det er ikkje bygd noka rute enno. Bli den første!");
            }
        }

        private void kortBunkeHandler() {
            Farge f = null;
            try {
                f = hovud.getKvenSinTur().trekkFargekort();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            JPanel p = new JPanel();
            JLabel woho = new JLabel("Du trakk eit kort av farge " +f);
            try {
                if (f==null){return;}
                hovud.getKvenSinTur().faaKort(f);
                gui.sendKortMelding(true,true,f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            p.add(woho);
            //				 lagRamme("Du trakk inn kort", p);
            try {
                if (hovud.getKvenSinTur().getValdAllereie()) {
                    try {
                        hovud.nesteSpelar();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    hovud.getKvenSinTur().setEinVald(true);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        public void actionPerformed(ActionEvent arg0) {

            /*			try {
                   System.out.println("minSpelar: " +hovud.getMinSpelar().getNamn() +", kvenSinTur: " +hovud.getKvenSinTur().getNamn());
                   System.out.println(hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn()));
               } catch (RemoteException e2) {
                   e2.printStackTrace();
               }
                */			try {
                if (hovud.isNett() && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
                    if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
                        JOptionPane.showMessageDialog((Component) hovud.getGui(), "Det er ikkje din tur!");
                        return;
                    }
                }
            } catch (HeadlessException e2) {
                e2.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }

            if (arg0.getSource() == trekkOppdrag) {
                trekkOppragHandler();
            }
            else if (arg0.getSource() == bygg) {
                byggHandler();
            }
            else if (arg0.getSource() == visMineKort) {
                visMineKortHandler();
            }

            else if (arg0.getSource() == visMineOppdrag) {
                visMineOppdragHandler();
            }

            else if (arg0.getSource() == visBygde) {
                visBygdeHandler();
            }

            else if (arg0.getSource() == kortBunke) {
                kortBunkeHandler();
            }

            else if (arg0.getSource() == kortButtons[0]) {
                try {
                    kortButton(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else if (arg0.getSource() == kortButtons[1]) {
                try {
                    kortButton(1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else if (arg0.getSource() == kortButtons[2]) {
                try {
                    kortButton(2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else if (arg0.getSource() == kortButtons[3]) {
                try {
                    kortButton(3);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else if (arg0.getSource() == kortButtons[4]) {
                try {
                    kortButton(4);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void visFargekorta(GridBagConstraints d) {

        // Fargekorta på bordet
        d.gridy = 7;
        kortBunke = new JButton("Tilfeldig");
        kortBunke.setMinimumSize(Konstantar.KORTKNAPP);
        kortBunke.addActionListener(gNL);
        this.add(kortBunke,d);

        kortButtons = new JButton[5];

        int tel = 0;
        d.gridx = 1;
        mekkKortButton(tel,d);

        tel++;
        d.gridy++;
        d.gridx = 0;
        mekkKortButton(tel,d);

        tel++;
        d.gridx = 1;
        mekkKortButton(tel,d);

        tel++;
        d.gridy++;
        d.gridx = 0;
        mekkKortButton(tel,d);

        tel++;
        d.gridx = 1;
        mekkKortButton(tel,d);
    }

    private void mekkKortButton(int tel,GridBagConstraints d){
        kortButtons[tel] = new JButton("kort " +tel);
        kortButtons[tel].setMinimumSize(Konstantar.KORTKNAPP);
        kortButtons[tel].setBackground(Color.BLACK);
        kortButtons[tel].addActionListener(gNL);
        this.add(kortButtons[tel],d);
    }
}

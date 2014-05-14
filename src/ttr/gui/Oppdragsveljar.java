package ttr.gui;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.oppdrag.IOppdrag;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Oppdragsveljar implements IOppdragsveljar {
    private final ISpelUtgaave spel;
    private final JFrame frame;
    private JDialog oppdragsveljarBoksen;
    private JPanel vel;
    private ArrayList<IOppdrag> valde;
    private HashMap<JCheckBox,IOppdrag> oppdragÅVeljeFrå;
    private JButton ok;

    public Oppdragsveljar(ISpelUtgaave spel, JFrame frame){
        this.spel = spel;
        this.frame = frame;
    }

    private void setUpAvkrysningsboksar(ArrayList<IOppdrag> innsendteOppdragÅVeljeFrå){
        for (IOppdrag oppdrag : innsendteOppdragÅVeljeFrå){
            Object[] d = oppdrag.getDestinasjonar().toArray();

            JCheckBox oppdragsSjekkboks = new JCheckBox();
            oppdragsSjekkboks.addActionListener(new okListener());
            oppdragsSjekkboks.setSelected(false);
            vel.add(new JTextField(d[0] +" - " +d[1] + " (" +oppdrag.getVerdi() +")"));
            vel.add(oppdragsSjekkboks);
            oppdragÅVeljeFrå.put(oppdragsSjekkboks,oppdrag);
        }
    }

    @Override
    public ArrayList<IOppdrag> setUpOppdragsveljar(ArrayList<IOppdrag> innsendteOppdragÅVeljeFrå) {
        oppdragÅVeljeFrå = new HashMap<>();
        vel = new JPanel();
        GridLayout gl = new GridLayout(0, 2);
        vel.setLayout(gl);

        setUpAvkrysningsboksar(innsendteOppdragÅVeljeFrå);

        valde = new ArrayList<>();
        ok = new JButton(Infostrengar.OKLabel);
        ok.addActionListener(new okListener());
        vel.add(ok);
        ok.setEnabled(false);

        ImageIcon kartet = new ImageIcon(spel.getBakgrunnsbildet());

        JLabel kartImplementasjonen = new JLabel(kartet);
        JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(kartet.getIconWidth()+50, Konstantar.VINDUSSTORLEIK.height*14/15)); // 14/15 er hack for å få vist heile bildet
        jsp.getViewport().add(kartImplementasjonen);

        JPanel heile = new JPanel();
        heile.add(vel);
        heile.add(jsp);
        heile.setPreferredSize(Konstantar.VINDUSSTORLEIK);

        oppdragsveljarBoksen = new JDialog(frame,Infostrengar.VelOppdragLabel,true);
        oppdragsveljarBoksen.setContentPane(heile);
        oppdragsveljarBoksen.pack();
        oppdragsveljarBoksen.setVisible(true);

        return valde;
    }


    /**
     * Blir kalla når spelaren har vald oppdragEinKanVeljeNyeOppdragFrå, og (freistar å/) trykker ok.
     * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
     */
    private class okListener implements ActionListener {
        public void gjer(Object clickedItem){
            if (oppdragÅVeljeFrå.containsKey(clickedItem)){
                JCheckBox clicked = (JCheckBox)clickedItem;
                if (valde.contains(oppdragÅVeljeFrå.get(clicked))){
                    valde.remove(oppdragÅVeljeFrå.get(clicked));
                    clicked.setSelected(false);
                }
                else{
                    valde.add(oppdragÅVeljeFrå.get(clicked));
                    clicked.setSelected(true);
                    ok.setEnabled(true);
                }
            }
        }

        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == ok && (valde.size() >= oppdragÅVeljeFrå.size()-2)){
                oppdragsveljarBoksen.dispose();
                return;
            }

            for (int i = 0; i < oppdragÅVeljeFrå.size(); i++) {
                gjer(arg0.getSource());
            }
            if (valde.size() < oppdragÅVeljeFrå.size() -2){
                ok.setEnabled(false);
            }
        }
    }
}
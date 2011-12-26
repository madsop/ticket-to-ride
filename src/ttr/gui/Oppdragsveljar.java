package ttr.gui;

import ttr.struktur.IOppdrag;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

class Oppdragsveljar {
    private ArrayList<IOppdrag> valde;
    private ArrayList<IOppdrag> oppdragEinKanVeljeNyeOppdragFrå;
    private JButton ok;
    private ArrayList<JCheckBox> jcb;
    private JDialog jd;
    private final ISpelUtgaave spel;
    private final JFrame frame;

    public Oppdragsveljar(ISpelUtgaave spel, JFrame frame){
        this.spel = spel;
        this.frame = frame;
    }

    public ArrayList<IOppdrag> velOppdrag(ArrayList<IOppdrag> oppd) {
        this.oppdragEinKanVeljeNyeOppdragFrå = oppd;
        JPanel vel = new JPanel();
        GridLayout gl = new GridLayout(0, 2);

        vel.setLayout(gl);

        ArrayList<JTextField> jtf = new ArrayList<JTextField>();
        jcb = new ArrayList<JCheckBox>();

        for (int i = 0; i < oppdragEinKanVeljeNyeOppdragFrå.size(); i++) {
            IOppdrag o = oppdragEinKanVeljeNyeOppdragFrå.get(i);
            Object[] d = o.getDestinasjonar().toArray();
            jtf.add(new JTextField(d[0] +" - " +d[1] + " (" +o.getVerdi() +")"));
            jcb.add(new JCheckBox());
            jcb.get(i).addActionListener(new okListener());
            jcb.get(i).setSelected(false);
            vel.add(jtf.get(i));
            vel.add(jcb.get(i));
        }

        valde = new ArrayList<IOppdrag>();
        ok = new JButton("OK");
        ok.addActionListener(new okListener());
        vel.add(ok);
        ok.setEnabled(false);

        URL c = spel.getBakgrunnsbildet();
        ImageIcon b = new ImageIcon(c);

        JLabel a = new JLabel(b);
        JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(b.getIconWidth()+50,b.getIconHeight()-200));
        jsp.getViewport().add(a);

        JPanel heile = new JPanel();
        heile.add(vel);
        heile.add(jsp);

        jd = new JDialog(frame,"Vel oppdrag",true);
        jd.setContentPane(heile);
        jd.pack();
        jd.setVisible(true);

        return valde;
    }


    /**
     * Blir kalla når spelaren har vald oppdragEinKanVeljeNyeOppdragFrå, og (freistar å/) trykker ok.
     * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
     * @author mads
     *
     */
    private class okListener implements ActionListener {
        public void gjer(int i, ActionEvent arg0){
            if (arg0.getSource() == jcb.get(i)) {
                if (valde.contains(oppdragEinKanVeljeNyeOppdragFrå.get(i))){
                    valde.remove(oppdragEinKanVeljeNyeOppdragFrå.get(i));
                    jcb.get(i).setSelected(false);
                }
                else{
                    valde.add(oppdragEinKanVeljeNyeOppdragFrå.get(i));
                    jcb.get(i).setSelected(true);
                    ok.setEnabled(true);
                }
            }
        }

        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == ok) {
                if (valde.size() >= oppdragEinKanVeljeNyeOppdragFrå.size()-2) {
                    jd.dispose();
                    return;
                }
            }

            for (int i = 0; i < oppdragEinKanVeljeNyeOppdragFrå.size(); i++) {
                gjer(i,arg0);
            }

            int count = 0;
            for (int i = 0; i < jcb.size(); i++) {
                if (valde.contains(oppdragEinKanVeljeNyeOppdragFrå.get(i))) {
                    count++;
                }
            }
            if (count < oppdragEinKanVeljeNyeOppdragFrå.size() - 2) {
                ok.setEnabled(false);
            }
        }
    }
}

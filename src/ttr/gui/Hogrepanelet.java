package ttr.gui;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.Listeners.*;
import ttr.kjerna.IHovud;

import javax.swing.*;
import java.awt.*;


class Hogrepanelet extends JPanel {
    // Brukargrensesnittet - på høgresida
    private JTextField spelarnamn;
    private final IGUI gui;
    private final JFrame frame;

    private JButton[] kortButtons;
    private JLabel[] togAtt;
    private JButton trekkOppdrag,bygg,visBygde,visMineOppdrag,visMineKort, kortBunke;

    public Hogrepanelet(GUI gui, JFrame frame){
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
        this.add(trekkOppdrag, d);

        d.gridx = 1;
        bygg = new JButton("Bygg ei togrute");
        bygg.setMinimumSize(Konstantar.KNAPP);
        this.add(bygg, d);

        d.gridy = 2;
        d.gridx = 0;
        visBygde = new JButton("Vis bygde ruter");
        visBygde.setMinimumSize(Konstantar.KNAPP);
        this.add(visBygde, d);

        d.gridy = 3;
        d.gridx = 0;
        visMineOppdrag = new JButton("Vis mine oppdrag");
        visMineOppdrag.setMinimumSize(Konstantar.KNAPP);
        this.add(visMineOppdrag, d);

        d.gridx = 1;
        visMineKort = new JButton("Vis mine kort");
        visMineKort.setMinimumSize(Konstantar.KNAPP);
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
    
    public void addListeners(IHovud hovud){
        HandlingListener listener = new HandlingListener(gui, hovud, visBygde, visMineKort, visMineOppdrag, trekkOppdrag, bygg, frame);
        trekkOppdrag.addActionListener(listener);
        bygg.addActionListener(listener);
        visMineKort.addActionListener(listener);
        visMineOppdrag.addActionListener(listener);
        visBygde.addActionListener(listener);


        WrapperKortListener kortListener = new WrapperKortListener(kortBunke, kortButtons, hovud, gui, frame, hovud.isNett());
        kortBunke.addActionListener(kortListener);
        for (JButton button : kortButtons){
            button.addActionListener(kortListener);
        }
    }

    /**
     * Teiknar opp eit kort på bordet
     * @param plass - kva for ein av plassane på bordet
     * @param farge - i kva farge
     */
    public void teiknOppKortPåBordet(int plass, Farge farge) {
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



    private void visFargekorta(GridBagConstraints d) {

        // Fargekorta på bordet
        d.gridy = 7;
        kortBunke = new JButton("Tilfeldig");
        kortBunke.setMinimumSize(Konstantar.KORTKNAPP);
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
        this.add(kortButtons[tel],d);
    }
}

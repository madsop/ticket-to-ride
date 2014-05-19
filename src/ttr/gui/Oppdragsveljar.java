package ttr.gui;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.oppdrag.Mission;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Oppdragsveljar implements IOppdragsveljar {
    private final ISpelUtgaave gameVersion;
    private final JFrame frame;
    private JDialog missionChooserDialog;
    private JPanel choosePanel;
    private ArrayList<Mission> chosenMissions; //todo: dette er ikkje gui-stuff
    private HashMap<JCheckBox,Mission> missionsToChooooseFrom;
    private JButton ok;

    public Oppdragsveljar(ISpelUtgaave spel, JFrame frame){
        this.gameVersion = spel;
        this.frame = frame;
    }

    @Override
    public ArrayList<Mission> setUpOppdragsveljar(ArrayList<Mission> innsendteOppdragÅVeljeFrå) {
        missionsToChooooseFrom = new HashMap<>();
        choosePanel = new JPanel();
        GridLayout gl = new GridLayout(0, 2);
        choosePanel.setLayout(gl);

        setUpCheckBoxes(innsendteOppdragÅVeljeFrå);
        setUpOKButton();

        chosenMissions = new ArrayList<>();

        ImageIcon kartet = new ImageIcon(gameVersion.getBakgrunnsbildet());

        JLabel kartImplementasjonen = new JLabel(kartet);
        JScrollPane jsp = setUpScrollPane(kartet, kartImplementasjonen);
        fixMissionChooserGUIBox(jsp);

        return chosenMissions;
    }

	private void setUpOKButton() {
		ok = new JButton(Infostrengar.OKLabel);
        ok.addActionListener(new okListener());
        choosePanel.add(ok);
        ok.setEnabled(false);
	}

    private void setUpCheckBoxes(ArrayList<Mission> missionsToChooseFrom){ //TODO legg inn handtering for når oppdragsbunken er tom
        for (Mission mission : missionsToChooseFrom){
            JCheckBox missionCheckBox = new JCheckBox();
            missionCheckBox.addActionListener(new okListener());
            missionCheckBox.setSelected(false);
            choosePanel.add(new JTextField(mission.toString()));
            choosePanel.add(missionCheckBox);
            missionsToChooooseFrom.put(missionCheckBox,mission);
        }
    }

	private JScrollPane setUpScrollPane(ImageIcon kartet, JLabel kartImplementasjonen) {
		JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(kartet.getIconWidth()+50, Konstantar.VINDUSSTORLEIK.height*14/15)); // 14/15 er hack for å få vist heile bildet
        jsp.getViewport().add(kartImplementasjonen);
		return jsp;
	}

	private void fixMissionChooserGUIBox(JScrollPane jsp) {
		JPanel heile = fixJPanel(jsp);
		missionChooserDialog = new JDialog(frame,Infostrengar.VelOppdragLabel,true);
        missionChooserDialog.setContentPane(heile);
        missionChooserDialog.pack();
        missionChooserDialog.setVisible(true);
	}

	private JPanel fixJPanel(JScrollPane jsp) {
		JPanel heile = new JPanel();
        heile.add(choosePanel);
        heile.add(jsp);
        heile.setPreferredSize(Konstantar.VINDUSSTORLEIK);
		return heile;
	}


    /**
     * Blir kalla når spelaren har vald oppdragEinKanVeljeNyeOppdragFrå, og (freistar å/) trykker ok.
     * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
     */
    private class okListener implements ActionListener {
        public void perform(Object clickedItem){
            if (missionsToChooooseFrom.containsKey(clickedItem)){
                JCheckBox clicked = (JCheckBox)clickedItem;
                if (chosenMissions.contains(missionsToChooooseFrom.get(clicked))){
                    uncheck(clicked);
                }
                else{
                    check(clicked);
                }
            }
        }

		private void uncheck(JCheckBox clicked) {
			chosenMissions.remove(missionsToChooooseFrom.get(clicked));
			clicked.setSelected(false);
		}

		private void check(JCheckBox clicked) {
			chosenMissions.add(missionsToChooooseFrom.get(clicked));
			clicked.setSelected(true);
			ok.setEnabled(true);
		}

        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == ok && (chosenMissions.size() >= missionsToChooooseFrom.size()-2)){
                missionChooserDialog.dispose();
                return;
            }

            for (int i = 0; i < missionsToChooooseFrom.size(); i++) {
                perform(arg0.getSource());
            }
            if (chosenMissions.size() < missionsToChooooseFrom.size() -2){
                ok.setEnabled(false);
            }
        }
    }
}
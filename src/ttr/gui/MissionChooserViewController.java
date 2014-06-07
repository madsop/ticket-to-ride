package ttr.gui;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.oppdrag.Mission;
import ttr.utgaave.GameVersion;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MissionChooserViewController {
	private MissionChooserModel missionChooserModel;
	private URL backgroundImageURL;
	private JDialog missionChooserDialog;
	private JPanel choosePanel;
	private HashMap<JCheckBox,Mission> missionsToChooseFrom; //TODO ikkje heilt glad for denne
	private JButton okButton;

	public MissionChooserViewController(GameVersion spel, MissionChooserModel missionChooserModel) {
		this.backgroundImageURL = spel.getBakgrunnsbildet();
		this.missionChooserModel = missionChooserModel;
		missionChooserModel.addPropertyChangeListener(new MissionChooserPropertyChangeListener());
	}

	public Collection<Mission> setUpMissionChooser(ArrayList<Mission> missionsToChooseFrom) {
		this.missionsToChooseFrom = new HashMap<>();
		missionChooserModel.populate(missionsToChooseFrom);
		setUpChoosePanel();
		setUpCheckBoxes(missionsToChooseFrom);
		setUpOKButton();

		fixMissionChooserGUIBox(drawAndSetupMap());
		return missionChooserModel.getChosenMissions();
	}

	private void setUpChoosePanel() {
		choosePanel = new JPanel();
		choosePanel.setLayout(new GridLayout(0, 2));
	}

	private JScrollPane drawAndSetupMap() {
		ImageIcon kartet = new ImageIcon(backgroundImageURL);
		return setUpScrollPane(kartet, new JLabel(kartet));
	}

	private void setUpOKButton() {
		okButton = new JButton(Infostrengar.OKLabel);
		okButton.addActionListener(new OkClickedForMissionChooseListener());
		choosePanel.add(okButton);
		okButton.setEnabled(false);
	}

	private void setUpCheckBoxes(ArrayList<Mission> missionsToChooseFrom){ //TODO legg inn handtering for når oppdragsbunken er tom
		for (Mission mission : missionsToChooseFrom){
			JCheckBox missionCheckBox = createAndConfigureMissionCheckBox();
			JTextField textfield = new JTextField(mission.toString());
			textfield.setEditable(false);
			choosePanel.add(textfield);
			choosePanel.add(missionCheckBox);
			this.missionsToChooseFrom.put(missionCheckBox,mission);
		}
	}

	private JCheckBox createAndConfigureMissionCheckBox() {
		JCheckBox missionCheckBox = new JCheckBox();
		missionCheckBox.addActionListener(new OkClickedForMissionChooseListener());
		missionCheckBox.setSelected(false);
		return missionCheckBox;
	}

	private JScrollPane setUpScrollPane(ImageIcon kartet, JLabel kartImplementasjonen) {
		JScrollPane jsp = new JScrollPane();
		jsp.setPreferredSize(new Dimension(kartet.getIconWidth()+50, Konstantar.VINDUSSTORLEIK.height*14/15)); // 14/15 er hack for å få vist heile bildet
		jsp.getViewport().add(kartImplementasjonen);
		return jsp;
	}

	private void fixMissionChooserGUIBox(JScrollPane jsp) {
		missionChooserDialog = new JDialog((JFrame) null,Infostrengar.VelOppdragLabel,true);
		missionChooserDialog.setContentPane(fixJPanel(jsp));
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

	private class MissionChooserPropertyChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			JCheckBox boxClicked = missionsToChooseFrom.entrySet().stream()
					.filter(entry -> entry.getValue().toString().equals(event.getPropertyName()))
					.map(x -> x.getKey()).findAny().get();
			boxClicked.setSelected((boolean) event.getNewValue());
			if ((boolean) event.getNewValue()) {
				okButton.setEnabled(true);
			}
		}
	}

	/**
	 * Blir kalla når spelaren har vald oppdragEinKanVeljeNyeOppdragFrå, og (freistar å/) trykker ok.
	 * Nett no veldig ad-hoc-a. Bør for-løkke-styres eller noko.
	 */
	private class OkClickedForMissionChooseListener implements ActionListener {
		private void perform(Object clickedItem){
			if (missionsToChooseFrom.containsKey(clickedItem)){
				JCheckBox clicked = (JCheckBox)clickedItem;
				if (clicked.isSelected()) {
					missionChooserModel.addMission(missionsToChooseFrom.get(clicked));
				}
				else{
					missionChooserModel.removeMission(missionsToChooseFrom.get(clicked));
				}
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == okButton && missionChooserModel.isSelectionOK()) {
				missionChooserDialog.dispose();
				return;
			}

			perform(arg0.getSource());
			
			if (!missionChooserModel.isSelectionOK()) {
				okButton.setEnabled(false);
			}
		}
	}
}
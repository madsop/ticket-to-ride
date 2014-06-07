package ttr.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import ttr.oppdrag.Mission;

public class MissionChooserModel {
	private HashMap<Mission, Boolean> missionsToChooseBetween;
	private PropertyChangeSupport propertyChangeSupport;

	public MissionChooserModel() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	void addMission(Mission mission) {
		switchMissionStatus(mission, true);
	}

	void removeMission(Mission mission) {
		switchMissionStatus(mission, false);
	}

	private void switchMissionStatus(Mission mission, boolean newStatus) {
		propertyChangeSupport.firePropertyChange(mission.toString(), !newStatus, newStatus);
		missionsToChooseBetween.put(mission, newStatus);
	}

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	public void populate(Collection<Mission> missionsToChooseFrom) {
		missionsToChooseBetween = new HashMap<>();
		missionsToChooseFrom.forEach(mission -> missionsToChooseBetween.put(mission, false));
	}

	public Collection<Mission> getChosenMissions() {
		return missionsToChooseBetween.entrySet().stream()
				.filter(mission -> missionsToChooseBetween.get(mission.getKey()))
				.map(entry -> entry.getKey())
				.collect(Collectors.toSet());
	}

	public boolean isSelectionOK() {
		return getChosenMissions().size() >= missionsToChooseBetween.size() - 2;
	}
}
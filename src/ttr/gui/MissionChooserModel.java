package ttr.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ttr.oppdrag.Mission;

public class MissionChooserModel {
	private HashMap<Mission, Boolean> missionsToChooseBetween;
	private PropertyChangeSupport propertyChangeSupport;
	
	public MissionChooserModel() {
		propertyChangeSupport = new PropertyChangeSupport(this);
		missionsToChooseBetween = new HashMap<>();
	}
	
	void addMission(Mission mission) {
		propertyChangeSupport.firePropertyChange(mission.toString(), false, true);
		missionsToChooseBetween.put(mission, true);
	}
	
	void removeMission(Mission mission) {
		propertyChangeSupport.firePropertyChange(mission.toString(), true, false);
		missionsToChooseBetween.put(mission, false);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	public void populate(ArrayList<Mission> missionsToChooseFrom) {
		missionsToChooseFrom.forEach(mission -> missionsToChooseBetween.put(mission, false));
	}

	public Collection<Mission> getChosenMissions() {
		HashSet<Mission> missions = new HashSet<>();
		for (Map.Entry<Mission, Boolean> mission : missionsToChooseBetween.entrySet()) {
			if (mission.getValue()) {
				missions.add(mission.getKey());
			}
		}
		return missions;
		
//		return missionsToChooseBetween.entrySet().stream().filter(mission -> missionsToChooseBetween.get(mission))
//				.map(entry -> entry.getKey())
//				.collect(Collectors.toSet());
	}

	public boolean isSelectionOK() {
		return getChosenMissions().size() >= missionsToChooseBetween.size() - 2;
	}
}
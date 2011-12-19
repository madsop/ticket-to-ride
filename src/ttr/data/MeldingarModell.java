package ttr.data;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;


public class MeldingarModell extends AbstractListModel{
	private final PropertyChangeSupport pcs;
	private final ArrayList<String> meldingar;
	public static final String MELDINGAR_PROPERTY = "meldingar";
	
	public ArrayList<String> getMeldingar(){
		return meldingar;
	}
	
	public MeldingarModell(ArrayList<String> meldingar) {
		pcs = new PropertyChangeSupport(this);
		this.meldingar = meldingar;
	}
	public MeldingarModell() {
		pcs = new PropertyChangeSupport(this);
		meldingar = new ArrayList<String>();
	}
	
	public void nyMelding(String melding){
		meldingar.add(melding);
		pcs.firePropertyChange(MELDINGAR_PROPERTY,"",melding);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	public Object getElementAt(int index) {
		return meldingar.get(index);
	}
	
	public int getSize() {
		return meldingar.size();
	}
}

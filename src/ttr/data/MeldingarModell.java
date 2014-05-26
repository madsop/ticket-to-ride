package ttr.data;

import javax.swing.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;


public class MeldingarModell extends AbstractListModel<String> {
	private static final long serialVersionUID = -6448830036131823839L;
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
		this(new ArrayList<>());
	}
	
	public void nyMelding(String melding){
		meldingar.add(melding);
		pcs.firePropertyChange(MELDINGAR_PROPERTY,"",melding);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public String getElementAt(int index) {
		return meldingar.get(index);
	}
	
	public int getSize() {
		return meldingar.size();
	}
}
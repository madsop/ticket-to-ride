package ttr;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class BygdeListerModel implements ListModel {
	private Hovud hovud;
	
	public BygdeListerModel(Hovud hovud){
		this.hovud=hovud;
	}

	public void addListDataListener(ListDataListener arg0) {
		
	}

	public Object getElementAt(int arg0) {
		return hovud.getAlleBygdeRuter().get(arg0);
	}

	public int getSize() {
		return hovud.getAlleBygdeRuter().size();
	}

	public void removeListDataListener(ListDataListener arg0) {
	}
	
}

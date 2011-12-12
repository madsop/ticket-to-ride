package ttr.struktur;

import ttr.Hovud;

import javax.swing.*;
import javax.swing.event.ListDataListener;

class BygdeListerModel implements ListModel {
	private final Hovud hovud;
	
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

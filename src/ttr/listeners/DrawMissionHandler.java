package ttr.listeners;

import java.rmi.RemoteException;

import ttr.kjerna.Core;

public class DrawMissionHandler extends DelegationListener {
	public DrawMissionHandler(Core core) {
		super(core);
	}

	public void specific() throws RemoteException {
		core.trekkOppdrag();
	}
}
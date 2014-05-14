package ttr.rute;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.text.MessageFormat;

public class Route implements IRoute {
	private static final int[] valuesForRoute = {1,2,4,7,10,15,0,0,27};
	private final int length;
	private final Farge colour;
	private final boolean tunnel;
	private final int numberOfRequiredJokers;
	private ISpelar builtBy;
	private final int routeId;
	private final Destinasjon start;
	private final Destinasjon end;

	public Route(int routeId, Destinasjon start, Destinasjon end, int length, Farge colour, boolean tunnel, int numberOfRequiredJokers) {
		this.routeId = routeId;
		this.start = start;
		this.end = end;
		
		this.length = length;
		this.colour = colour;
		this.tunnel = tunnel;
		this.numberOfRequiredJokers = numberOfRequiredJokers;
	}

	public Destinasjon getStart() {
		return start;
	}

	public Destinasjon getEnd() {
		return end;
	}

	public int getRouteId() {
		return routeId;
	}

	public int getValue() {
		return valuesForRoute[length-1];
	}

    public int getLength() {
		return length;
	}

    public Farge getColour() {
		return colour;
	}

    public boolean isTunnel() {
		return tunnel;
	}

    public int getNumberOfRequiredJokers() {
		return numberOfRequiredJokers;
	}

    public void setBuiltBy(ISpelar spelar) {
		builtBy = spelar;
	}

    @SuppressWarnings({ "finally" })
	public String toString() {
		String builtByString = null;
		try {
			if (builtBy != null) {
				builtByString = builtBy.getNamn();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		finally {
            String string = "{0} - {1}, lengde {2}, av farge {3}, tunnel? {4}, og {5} jokrar krevs for å byggje denne. Ruta er bygd av {6}, og er verdt {7} poeng.";
			return MessageFormat.format(string,	start, end, length, colour, tunnel,	numberOfRequiredJokers, builtByString, valuesForRoute[length-1]);
		}
	}
}
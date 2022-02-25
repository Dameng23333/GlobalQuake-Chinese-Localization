package com.morce.globalquake.core;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;

import com.morce.globalquake.ui.AlertWindow;
import com.morce.globalquake.utils.GeoUtils;

public class AlertCenter {

	private GlobalQuake globalQuake;
	private HashMap<Earthquake, Warning> warnedQuakes;

	public AlertCenter(GlobalQuake globalQuake) {
		this.globalQuake = globalQuake;
		this.warnedQuakes = new HashMap<Earthquake, Warning>();
	}

	@SuppressWarnings("unchecked")
	public void tick() {
		ArrayList<Earthquake> quakes = null;
		synchronized (getGlobalQuake().getEarthquakeAnalysis().earthquakesSync) {
			quakes = (ArrayList<Earthquake>) getGlobalQuake().getEarthquakeAnalysis().getEarthquakes().clone();
		}
		for (Earthquake quake : quakes) {
			if (meetsConditions(quake) && !warnedQuakes.containsKey(quake)) {
				warnedQuakes.put(quake, new Warning());
				System.err.println("WARNING");
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							AlertWindow frame = new AlertWindow(quake);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	public static boolean meetsConditions(Earthquake quake) {
		double distGC = GeoUtils.greatCircleDistance(quake.getLat(), quake.getLon(), GeoUtils.HOME_LAT,
				GeoUtils.HOME_LON);
		double distGEO = GeoUtils.geologicalDistance(quake.getLat(), quake.getLon(), -quake.getDepth(),
				GeoUtils.HOME_LAT, GeoUtils.HOME_LON, 0.0);
		double pgaHome = GeoUtils.pgaFunctionGen1(quake.getMag(), distGEO);

		if (distGC < 400) {
			return true;
		}

		if (distGC < 2000 && quake.getMag() >= 3.5) {
			return true;
		}

		if (quake.getMag() > 5.5) {
			return true;
		}

		if (pgaHome >= 0.1) {
			return true;
		}

		return false;
	}

	public GlobalQuake getGlobalQuake() {
		return globalQuake;
	}
}

class Warning {

}

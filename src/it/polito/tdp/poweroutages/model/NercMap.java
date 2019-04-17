package it.polito.tdp.poweroutages.model;

import java.util.Map;
import java.util.TreeMap;

public class NercMap {
	
	private int id;
	private Nerc nerc;
	
	private Map<Integer,Nerc> nercMap;
	
	public NercMap() {
		nercMap = new TreeMap<Integer,Nerc>();
	}
	
	public Nerc getNerc(int id) {
		return nercMap.get(id);
	}
	
	public void put(int id, Nerc nerc) {
		nercMap.put(id, nerc);
	}
}


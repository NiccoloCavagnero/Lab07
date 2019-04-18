package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	private NercMap map;
	private List<Nerc> nercList;
	private List<PowerOutage> powerOutagesList;
	private List<PowerOutage> powerOutagesNercList;
	private List<PowerOutage> solution;
	private int maxYears;
	private int maxHours;
	private int best;
	int count = 0;
	
	public Model() {
		podao = new PowerOutageDAO();
		map = new NercMap();
		nercList = new ArrayList<Nerc>(podao.getNercList(map));
		powerOutagesList = new ArrayList<PowerOutage>(podao.getPowerOutagesList(map));
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList(map);
	}
	
	public List<String> getNercNameList() {
		List<String> list = new ArrayList<String>();
		
		for ( Nerc n : this.getNercList() )
			if ( !list.contains(n.getValue()) )
				list.add(n.getValue());
		return list;
	}
	
	public List<PowerOutage> doWorstCaseAnalysis(int maxYears,int maxHours, Nerc nerc) {
		powerOutagesNercList = new ArrayList<PowerOutage>();
		best = 0;
		for ( PowerOutage po : powerOutagesList )
			if ( po.getNerc().equals(nerc) ) {
				powerOutagesNercList.add(po);
			}
		
		this.maxYears = maxYears;
		this.maxHours = maxHours;
		solution = new ArrayList<PowerOutage>();
		
		recursion(new ArrayList<PowerOutage>());
		
		//System.out.println(sumHours(solution));
		
		return solution;
	}
	
	private void recursion(List<PowerOutage> partial) {
		
		
		
		if ( sumAffectedCustomers(partial) >= best ) {
			best = sumAffectedCustomers(partial);
			solution = new ArrayList<PowerOutage>(partial);
		}
		
		for ( PowerOutage po : powerOutagesNercList ) {
			
			if ( !partial.contains(po) ) {
				partial.add(po);
				
				if ( checkYears(partial) && checkHours(partial) ) {
					System.out.println(count);
					recursion(partial);
					count++;
				}
				
				partial.remove(po);
			}
		}
	}
	
	public int sumAffectedCustomers(List<PowerOutage> partial) {
		int sum = 0;
		
		
		  for ( PowerOutage po : partial) 
			sum += po.getCustomersAffected();
		  
		
		return sum;
	}
	
	public int sumHours(List<PowerOutage> partial) {
        int duration = 0;
		
		for ( PowerOutage po : partial ) {
			duration += po.getDuration();
		}
		return duration;
	}
	
	private boolean checkHours(List<PowerOutage> partial) {
		int duration = this.sumHours(partial);
		
		if ( duration <= maxHours ) {
			return true;
		}
		else
			return false;
	}
	
	private boolean checkYears(List<PowerOutage> partial) {
		
		int y1 = 30000;
		int y2 = -1;
		if ( partial.size() >= 2 ) {
		for ( PowerOutage po : partial ) {
			if ( po.getDateEventBegan().getYear() <= y1 )
				y1 = po.getDateEventBegan().getYear();
		}
		
		for ( PowerOutage po : partial ) {
			if ( po.getDateEventBegan().getYear() >= y2 )
				y2 = po.getDateEventBegan().getYear();
		}
		if ( (y2-y1) <= maxYears )
			return true;
		else
			return false;
		}
		return true;
	}

}
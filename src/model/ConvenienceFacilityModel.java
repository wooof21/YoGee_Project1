/**
 * 
 *@param
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 *@param
 */
public class ConvenienceFacilityModel{

	private ArrayList<HashMap<String, String>> facility;
	private HashMap<String, String> filter;
	private ArrayList<HashMap<String, String>> filterList;
	/**
	 * 
	 */
	public ConvenienceFacilityModel(){
		super();
		// TODO Auto-generated constructor stub
		facility = new ArrayList<HashMap<String,String>>();
		filter = new HashMap<String, String>();
		filter.put("name", "");
		filter.put("img", "");
		filterList = new ArrayList<HashMap<String,String>>();
	}
	public ArrayList<HashMap<String, String>> getFacility(){
		return facility;
	}
	public void setFacility(ArrayList<HashMap<String, String>> facility){
		this.facility = facility;
	}
	public HashMap<String, String> getFilter(){
		return filter;
	}
	public void setFilter(HashMap<String, String> filter){
		this.filter = filter;
	}
	public ArrayList<HashMap<String, String>> getFilterList(){
		return filterList;
	}
	public void setFilterList(ArrayList<HashMap<String, String>> filterList){
		this.filterList = filterList;
	}
	
	
}

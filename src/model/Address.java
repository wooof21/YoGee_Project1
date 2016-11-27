/**
 * 
 *@param
 */
package model;

import java.io.Serializable;

/**
 * 
 *@param
 */
public class Address implements Serializable{

	private String id;
	private String name;
	private String phone;
	private String address;
	private String sex;
	private String isSelected;
	
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex = sex;
	}
	public String getIsSelected(){
		return isSelected;
	}
	public void setIsSelected(String isSelected){
		this.isSelected = isSelected;
	}
	
	
}

/**
 * 
 *@param
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @param
 */
public class OrderRecords implements Serializable {

	private String id;
	private String title;
	private String Type;
	private String money;
	private String createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}

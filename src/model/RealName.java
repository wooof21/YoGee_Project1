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
public class RealName implements Serializable {

	private String id;
	private String type;
	private String state;
	private String img;
	private String updateDate;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

}

/**
 * 
 *@param
 */
package model;

import java.io.File;
import java.io.Serializable;

/**
 * 
 * @param
 */
public class Certification implements Serializable {

	private String title;
	private File file;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}

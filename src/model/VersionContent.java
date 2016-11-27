package model;

import java.io.Serializable;

public class VersionContent implements Serializable {

	// 版本号
	private String versionNumber;
	// 内容
	private String content;
	// 下载地址
	private String url;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

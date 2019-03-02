package com.acme.commerce.security.xss.beans;

/**
 * @author cgordon
 *
 */
public class XSSConfigBean {

	private String id = "0";
	private String storeId = "0";
	private String path = "";
	private String param = "";
	private String cleanerClass = "";
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}
	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}
	/**
	 * @return the cleanerClass
	 */
	public String getCleanerClass() {
		return cleanerClass;
	}
	/**
	 * @param cleanerClass the cleanerClass to set
	 */
	public void setCleanerClass(String cleanerClass) {
		this.cleanerClass = cleanerClass;
	}
	
	
	
}

package com.acme.commerce.security.comands;

import com.ibm.websphere.command.CacheableCommand;
import java.util.Map;

/*
 * For fetching XSS details data from DB
 */
public interface FDFetchXSSDetailsCmd extends CacheableCommand {
	
	public abstract Integer getStoreId();
	public abstract void setStoreId(Integer storeid);
	public abstract Map getConfigDetails();
	public abstract void setConfigDetails(Map map);
}

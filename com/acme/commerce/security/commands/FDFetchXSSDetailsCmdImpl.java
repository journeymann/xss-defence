package com.acme.commerce.security.comands;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.acme.commerce.catalog.helpers.FDFlowersHelperBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessageHelper;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.ibm.websphere.command.CacheableCommandImpl;
import com.ibm.websphere.command.CommandException;
import com.acme.commerce.security.xss.beans.XSSConfigBean;
import com.acme.commerce.security.xss.*;

/*
 * Class fetches XSS for specific commands/params
 */
public class FDFetchXSSDetailsCmdImpl extends CacheableCommandImpl 
 implements FDFetchXSSDetailsCmd {

	private static final String CLASSNAME = FDFetchXSSDetailsCmdImpl.class.getName();
	private static Logger logger =Logger.getLogger(CLASSNAME);
	
	private Integer storeId = null;
	private  Map<String, CleanerInterface>  configDetails = new HashMap<String, CleanerInterface>();
		
	public boolean isReadyToCallExecute(){
		return (storeId!=null);
	}
	
	/**
	 * @return the configDetails
	 */
	public Map<String, CleanerInterface> getConfigDetails() {
		return this.configDetails;
	}
	
	/**
	 * @param  configDetails 
	 */
	public void setConfigDetails(Map configDetails) {
		this.configDetails = configDetails;
	}
	
	/**
	 * @return the storeId
	 */
	public Integer getStoreId() {
		return this.storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public void performExecute() throws ECException {
		
		final String METHODNAME = "performExecute()";
		ECTrace.entry(ECTraceIdentifiers.COMPONENT_USER, CLASSNAME, METHODNAME);
		FDFlowersHelperBean flowersHelperBean = null;
		ArrayList<XSSConfigBean> configDetailsArray = new ArrayList<XSSConfigBean>();
		
		try { 
			
			flowersHelperBean = new FDFlowersHelperBean();
			configDetailsArray = flowersHelperBean.getXSSConfigDetails(this.getStoreId().toString());
			Map map = convertConfigDetailsToMap(configDetailsArray);
			setConfigDetails(map);
			
		}catch (CommandException exception) {
			logger.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Exception while running "+CLASSNAME+
					ECMessageHelper.getExceptionStackTrace(exception));
		}catch (Exception exception) {
			logger.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Exception while running "+CLASSNAME+
					ECMessageHelper.getExceptionStackTrace(exception));
		}

		ECTrace.exit(ECTraceIdentifiers.COMPONENT_USER, CLASSNAME, METHODNAME);

	}

	private Map convertConfigDetailsToMap(ArrayList<XSSConfigBean> list){
		
		Map<String, CleanerInterface> map = new HashMap<String, CleanerInterface>(); 
		
		for(XSSConfigBean item : list){
			
			String key = "/"+item.getPath()+"@@"+item.getParam();
			CleanerInterface value = null;
			Class theClass = null;
			
			try{
				theClass = Class.forName(item.getCleanerClass());
				value = (CleanerInterface)theClass.newInstance();
			}catch(ClassNotFoundException ce){
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("FDFetchXSSDetailsCmdImpl: CE Reflection failed due to: "+ce.getMessage());
				}	
				continue;
			}catch(InstantiationException ie){
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("FDFetchXSSDetailsCmdImpl: IE Reflection failed due to: "+ie.getMessage());
				}		
				continue;				
			}catch(IllegalAccessException ee){
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("FDFetchXSSDetailsCmdImpl: EE Reflection failed due to: "+ee.getMessage());
				}		
				continue;				
			}catch(Exception e){
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("FDFetchXSSDetailsCmdImpl: E Reflection failed due to: "+e.getMessage());
				}		
				continue;				
			}
			
			map.put(key,value);
		}
		return map;
	}
	
}

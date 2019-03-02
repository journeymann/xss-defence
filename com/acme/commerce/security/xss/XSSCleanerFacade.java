package com.acme.commerce.security.xss;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.net.URL;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

import com.acme.commerce.common.helpers.FDStoreAttributeHelper;
import com.acme.commerce.mbp.helpers.FDMBPConstants;
import com.acme.commerce.security.commands.FDFetchXSSDetailsCmd;
import com.acme.commerce.security.commands.FDFetchXSSDetailsCmdImpl;
import com.acme.commerce.security.xss.CUSCleanerImpl;
import com.acme.commerce.security.xss.CleanerInterface;
import com.acme.commerce.security.xss.NOCleanerImpl;
import com.acme.commerce.security.xss.URLCleanerImpl;
import com.acme.commerce.webcontroller.FDHttpServletRequest;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.websphere.command.CommandException;

public class XSSCleanerFacade {
	private static final String CLASSNAME = "com.acme.commerce.security.xss.XSSCleanerFacade";
	
    private static final Set<String> BRANDS = new HashSet<String>(Arrays.asList(new String[] {"flowers","fruitbouquets",
    		"baskets","cheryls","tpf","fanniemay","harryanddavid","wolfermans"}));	
    
    private static final String STOREID_KEY = "storeId";
    
    private static class ID {
    	static final int FLOWERS = 20051;
    	static final int BASKETS = 20054;
    	static final int CHERYLS = 10202;
    	static final int THEPOPCORNFACTORY = 10201;
    	static final int FANNIEMAY = 20052;
    	static final int HARRYANDDAVID = 21051;
    	static final int WOLFERMANS = 21551;
    }; 
	
	protected Logger logger = LoggingHelper.getLogger(XSSCleanerFacade.class);
	private static Integer defaultStoreId = 0;
	
	public XSSCleanerFacade(){
	}
	
	public Map<String,CleanerInterface> getCleanerMap(){
		return getCleanerMap(defaultStoreId);
	}
	
	public boolean isCleanXssFlag(Integer storeId){
		String secureStr = FDStoreAttributeHelper.getStringValue(FDMBPConstants.XSS_SECURE_FLAG, new Integer(storeId));
		return (secureStr!= null && !secureStr.isEmpty() && secureStr.equalsIgnoreCase("Y"))? true : false;
	}
	
	public Map<String,CleanerInterface> getCleanerMap(Integer storeId){

		Map<String,CleanerInterface> clnMap = new HashMap<String, CleanerInterface>();
		FDFetchXSSDetailsCmd detailsCmd = null;

		if(isCleanXssFlag(defaultStoreId)){
			
			try{
				detailsCmd = new FDFetchXSSDetailsCmdImpl();
				detailsCmd.setStoreId(storeId);
				detailsCmd.execute();
				clnMap = detailsCmd.getConfigDetails();
			}catch(ECException e){				
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("XSSCleanerFacade:loadCleanerMap error executing fetch config command: " + e.getMessage());
				}
				clnMap = new HashMap<String, CleanerInterface>();
			}catch(CommandException ce){
				if (LoggingHelper.isTraceEnabled(logger)) {
					logger.fine("XSSCleanerFacade:loadCleanerMap error executing fetch config command: " + ce.getMessage());
				}
				clnMap = new HashMap<String, CleanerInterface>();
			}
		}
		
		return clnMap;
	}
	
	public boolean cleanerMapContainsKey(String key){
		return getCleanerMap().containsKey(key);
	}
	
	public String getCleanerMapKey(URL url, String name, String store_id){
		
		if(!isCleanXssFlag(defaultStoreId)) return name;
		
		if(url==null) return "";
		
		String path = this.getPath(url, store_id);
	
		String key = path + "@@" + name;
		if (LoggingHelper.isTraceEnabled(logger)) {
			logger.fine("XSSCleanerFacade:getCleanerMapKey map_key="+ key);
		}		
		return key; 
	}
	
	public String clean(String key, String value){
		if(!isCleanXssFlag(defaultStoreId)) return value;
	
		CleanerInterface cleaner = getCleaner(key);
	
		String output = "";
		if(value==null || value.isEmpty()) return "";
		if (LoggingHelper.isTraceEnabled(logger)) {
			logger.fine("XSSCleanerFacade:clean() string key="+key+" value="+value);
		}		

		try{
			output = cleaner.clean(value);
		}catch(EncodingException ex){
			if (LoggingHelper.isTraceEnabled(logger)) {
				logger.fine("XSSCleanerFacade:Error EncodingException cleaning param value: value="+value+": "+ex.getMessage());
			}		
			output = "";
		}catch(IntrusionException ex){
			if (LoggingHelper.isTraceEnabled(logger)) {
				logger.fine("XSSCleanerFacade:Error IntrusionException cleaning param value: value="+value+": "+ex.getMessage());
			}		
			output = "";
		}

		if (LoggingHelper.isTraceEnabled(logger)) {
			logger.fine("XSSCleanerFacade:clean() string key="+key+" output="+output);
		}		

		return output;
	}

	public String[] clean(String key, String[] values){
	
		if(!isCleanXssFlag(defaultStoreId)) return values;
		
		if(values==null || values.length<1) return values;
		String[] output = new String[values.length];

		for(int i=0; i < values.length; i++){
			output[i] = this.clean(key, values[i].toString());
			
			if (LoggingHelper.isTraceEnabled(logger)) {
				logger.fine("XSSCleanerFacade:clean string[]: raw_value="+values[i] + " cleaned_value="+output[i]);
			}		
		}	
		
		return output;
	}
	
	private CleanerInterface getCleaner(String key){
		Map map = getCleanerMap(); 
		return (map.containsKey(key))? (CleanerInterface)map.get(key) : new NOCleanerImpl();
	}	

	private boolean isNumeric(String str) {  
	    return java.util.regex.Pattern.matches("\\d+", str);  
	}

	public Map checkRequestParamMap(FDHttpServletRequest req, Map map ){
	
		if(!isCleanXssFlag(defaultStoreId)) return map;
				
		Map output = new HashMap();

		if(map==null || map.isEmpty()) return map;
		
		Object obj = (map.containsKey(STOREID_KEY))? map.get(STOREID_KEY) : new String("0");
		String store_id = (obj instanceof String)? obj.toString() : ((Object[])obj)[0].toString(); //sometimes storeId is an array
		
		for(Object name : map.keySet()){
			Object value = map.get(name);
			String key = this.getCleanerMapKey(req.getCustomRequestURL(), (String)name, store_id); 
			
			if (LoggingHelper.isTraceEnabled(logger)) {
				logger.fine("XSSCleanerFacade:checkRequestParamMap:param="+ name +" path="+req.getPathInfo());
			}

			if(!this.cleanerMapContainsKey(key)){ 
				output.put(name, value);
			}else{
				if(value instanceof String){
					output.put(name, this.clean(key,(String)value));
				}else{
					output.put(name, this.clean(key,(String[])value));
				}
			}
		}
		
		if(map.size() != output.size()){
			if (LoggingHelper.isTraceEnabled(logger)) {
				logger.fine("XSSCleanerFacade:checkRequestParamMap: Size of cleaned map is not the same as the original.");
			}		
		}

		return output;
	}
	
	private String getPath(URL url, String store_id){

		String path = (url.getPath()!=null)? url.getPath() : "";
		
		Integer storeId = (store_id != null && !store_id.isEmpty() && isNumeric(store_id))? Integer.parseInt(store_id) : new Integer("-1");

		switch(storeId.intValue()){
			case ID.FLOWERS :
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/flowers", "") : "";				
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/fruitbouquets", "") : "";				
				break;
				
			case ID.BASKETS:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/baskets", "") : "";				
				break;
				
			case ID.FANNIEMAY:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/fanniemay", "") : "";				
				break;
				
			case ID.CHERYLS:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/cheryls", "") : "";
				break;
				
			case ID.THEPOPCORNFACTORY:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/tpf", "") : "";
				break;			

			case ID.HARRYANDDAVID:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/harryanddavid", "") : "";
				break;			

			case ID.WOLFERMANS:
				path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet/wolfermans", "") : "";
				break;			
			
			default:
				for(String brand : BRANDS){
					if(path!=null && !path.isEmpty()){
						String prefix = "/webapp/wcs/stores/servlet/".concat(brand);
						path = (path.contains(brand))? path.replace(prefix, "") : path;
					}
				}
		}
		
		path = (path!=null && !path.isEmpty())? path.replace("/webapp/wcs/stores/servlet", "") : "";
		
		return path;
	}
	
}

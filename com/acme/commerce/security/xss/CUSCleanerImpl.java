package com.acme.commerce.security.xss;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

/**
 * @author cgordon
 *
 */
public class CUSCleanerImpl implements CleanerInterface {
	private static Map<String,String> exceptions = new HashMap<String,String>();
	
	public String clean(String value) throws EncodingException, IntrusionException{
		String temp = ESAPIEncoder.getEncoder().canonicalize(value);
		String cleanStr = ESAPIEncoder.getEncoder().encodeForURL(temp);
		return customDecode(cleanStr);
	}

	private void loadExceptions(){
		if(exceptions.isEmpty()){
			exceptions.put("%3A",":");
			exceptions.put("%3a",":");	
			exceptions.put("%40","@");
			exceptions.put("%3D","=");
			exceptions.put("%3d","=");
			exceptions.put("%26","&");
			
		}
	}
	
	private String customDecode(String temp){
		
		loadExceptions();
		Iterator iter = exceptions.keySet().iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			String tmp = exceptions.get(key);
			temp = temp.replaceAll(key, tmp);
		}

		return temp;
	}
	
}

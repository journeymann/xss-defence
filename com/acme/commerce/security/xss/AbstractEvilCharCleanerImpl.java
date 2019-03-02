package com.acme.commerce.security.xss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

import com.acme.commerce.common.helpers.FDStoreAttributeHelper;

/**
 * @author cgordon
 *
 */
abstract class AbstractEvilCharCleanerImpl implements CleanerInterface {

	   protected static final String EVILATTRNAME = "EVIL_CHAR_LIST";
	   protected static final String EVILEXCEPTATTRNAME = "EVIL_CHAR_EXCEPT_LIST";	   
	   protected static final String EVILURLEXCEPTATTRNAME = "EVIL_URL_CHAR_EXCEPT_LIST";	   
	   protected static final int STORE_ID = 0;
	   protected static final String DELIMETER = ",";

	   public String clean(String value) throws EncodingException, IntrusionException{
			return this.cleanEvilChars(value);
	   }

		protected List getEvilExceptCharList(int type){
			List<String> list = new ArrayList<String>();
			
			switch(type){
			
				case CleanerInterface.EXCEPT_FORM_TYPE:
					list = getEvilExceptCharList();
					break;
				case CleanerInterface.EXCEPT_URL_TYPE:
					list = getURLExceptCharList();
					break;
				default:
					list = new ArrayList<String>();
			}
			
			return list;
		}
		
		protected List getEvilCharList(){
			return (FDStoreAttributeHelper.getStringValue(EVILATTRNAME, STORE_ID)!=null)? getStringList(FDStoreAttributeHelper.getStringValue(EVILATTRNAME, STORE_ID)): new ArrayList<String>();
		}

		private List getEvilExceptCharList(){
			return (FDStoreAttributeHelper.getStringValue(EVILEXCEPTATTRNAME, STORE_ID)!=null)? getStringList(FDStoreAttributeHelper.getStringValue(EVILEXCEPTATTRNAME, STORE_ID)): new ArrayList<String>();
		}
		
		private List getURLExceptCharList(){
			return (FDStoreAttributeHelper.getStringValue(EVILURLEXCEPTATTRNAME, STORE_ID)!=null)? getStringList(FDStoreAttributeHelper.getStringValue(EVILURLEXCEPTATTRNAME, STORE_ID)): new ArrayList<String>();			
		}
		
		private List<String> getStringList(String list){
			return new ArrayList<String>(Arrays.asList(list.split(DELIMETER)));
		}

		protected List<String> getModifiedEvilCharList(int type) {
			List<String> output = getEvilCharList();
			List<String> evilCharExceptList = getEvilExceptCharList(type);

			for(String exceptChar : evilCharExceptList){
				while(output.contains(exceptChar))	
					output.remove(exceptChar);
			}
			
			return output;
		}
		
		protected String cleanEvilChars(String value, int type) {
			String output = value;
			List<String> evilCharList = getModifiedEvilCharList(type);
		
			for(String evilChar : evilCharList){
				if(evilChar!=null){
					Pattern pattern = Pattern.compile(Pattern.quote(evilChar));
					if(pattern!=null){
						output = pattern.matcher(output).replaceAll("");
					}	
				}	
			}
			return output;
		}
		
		abstract String cleanEvilChars(String value) throws EncodingException, IntrusionException;	

}

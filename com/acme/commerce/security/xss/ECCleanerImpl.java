package com.acme.commerce.security.xss;

import java.util.regex.Pattern;
import java.util.List;
/**
 * @author cgordon
 *
 */
public class ECCleanerImpl extends AbstractEvilCharCleanerImpl {

	public String cleanEvilChars(String value) {
		String output = value;
		List<String> evilCharList = super.getEvilCharList();
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
}

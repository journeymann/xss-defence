package com.acme.commerce.security.xss;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

/**
 * @author cgordon
 *
 */
public class ESAPIEncoder {
	private static Encoder encoder = null;
	
	public ESAPIEncoder(){
	}
	
	public static Encoder getEncoder(){
		if(encoder==null){
			encoder = ESAPI.encoder();
		}
		return encoder;
	}
}

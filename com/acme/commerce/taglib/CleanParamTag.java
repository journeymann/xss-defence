package com.acme.commerce.taglib;

import com.acme.commerce.security.xss.JSCleanerImpl;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.*;

import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.IntrusionException;

import java.io.IOException;
/**
 * @author Casmon Gordon
 *
 */
public class CleanParamTag extends BodyTagSupport {
	
   public CleanParamTag(){
	   super();
   }
  
   public int doAfterBody() throws JspTagException {
      BodyContent bc = getBodyContent();
      String body = bc.getString();
      bc.clearBody();
      try {
         getPreviousOut().print(clean(body.toString()));
	  }catch(EncodingException ex){
		 throw new JspTagException("CleanParamTag: " + ex.getMessage());
	  } catch (IOException e) {
         throw new JspTagException("CleanParamTag: " + e.getMessage());
	  }catch(IntrusionException ie){
	     throw new JspTagException("CleanParamTag: " + ie.getMessage());
	  }

      return SKIP_BODY;
   }
    	
   private String clean(String value) throws EncodingException,IntrusionException{
	  JSCleanerImpl cleaner = new JSCleanerImpl();
	  return cleaner.clean(value);
   }
    
}

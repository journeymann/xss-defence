package com.acme.commerce.catalog.helpers;

/**
 * This bean is an extension of FDCatalogHelperBean.
 */
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import com.ibm.commerce.base.objects.ServerJDBCHelperBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.acme.commerce.security.xss.beans.XSSConfigBean;

public class FDAcmeHelperBean extends ServerJDBCHelperBean implements FDSQLConstants {
    private static final String CLASSNAME = FDAcmeHelperBean.class.getName();
    private static Logger logger = Logger.getLogger(CLASSNAME);
    
    public FDAcmeHelperBean(){
    	
    }
    
	/**
	 * Retrieves Addons from XORDERITEMS TABLE
	 * @param orderitem_id
	 * @return adonList
	 * throws ECSystemException
	 */

	public ArrayList<XSSConfigBean> getXSSConfigDetails(String storeId) throws ECSystemException {
		final String METHODNAME = "getXSSConfigDetails";
		ArrayList configList =  new ArrayList();
		Vector resultSet = null;
		ECTrace.entry(ECTraceIdentifiers.COMPONENT_USER, CLASSNAME, METHODNAME);
		try {
			resultSet = this.executeParameterizedQuery(FDSQLConstants.FETCH_XSS_CONFIG_DETAILS, new Object[] { storeId });
			
			for(Object vecElement : resultSet){
				XSSConfigBean config_row = new XSSConfigBean();
				if (vecElement instanceof Vector) {
					Vector row = (Vector) vecElement;
					config_row = new XSSConfigBean(); 	
					if(null != row.get(0)){
						config_row.setId(((BigDecimal)row.get(0)).toString());
					}
					if(null != row.get(1)){
						config_row.setStoreId(((BigDecimal)row.get(1)).toString());
					}
					
					if(null != row.get(2)){
						config_row.setPath((String)row.get(2));
					}
					
					if(null != row.get(3)){
						config_row.setParam((String)row.get(3));
					}
					
					if(null != row.get(4)){
						config_row.setCleanerClass((String)row.get(4));
					}
				}
				
				configList.add(config_row);
			}
		} catch (SQLException sqlException) {
			throw new ECSystemException(ECMessage._ERR_SQL_EXCEPTION, CLASSNAME, METHODNAME, new Object[] { sqlException.toString() }, sqlException);
		}
		catch (NamingException namingException) {
			throw new ECSystemException(ECMessage._ERR_NAMING_EXCEPTION, CLASSNAME, METHODNAME, new Object[] { namingException.toString() },
					namingException);
		}
		catch (Exception exception) {
			throw new ECSystemException(ECMessage._ERR_GENERIC, CLASSNAME, METHODNAME, new Object[] { exception.toString() }, exception);
		}
		ECTrace.exit(ECTraceIdentifiers.COMPONENT_USER, CLASSNAME, METHODNAME);
		
		return configList;
	  }
    
}


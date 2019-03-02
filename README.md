# commerce-xsscript-defence
logic for mitigating cross site scripting attacks from malicious actors

core feature of this code is the ability of the (websphere commerce) application to dynamically load and deploy crosssite 
scripting behaviour and functionality. Fine grained behavior can customised top address various types of attacks. If an anomaly
is detected, it can be either cleaned, i.e. the malicious character(s) can be removed or escaped for the specific scenario.

e.g. a html character can be either removed or escaped using html syntax. Similiarly a suspicious javascript character can 
also be removed or escaped using javascript syntax.

The relationship between the type of threat and the type of action to be taken is configured and maintained in the database.
Due to this configuaration information beingf persisted in the retlational store, this enables the desired dynamic behavior.

If a particular threat is detected in a high traffic scenario, then a [simple] database update can change the behavior on the fly.

#More Specifics
The class [class name] to be used to address the particular threat is retreived from the database table via a commerce 
cacheable command for speed, and then the particular cleaner class is instantiated via java reflection instance creation.

			try{
				theClass = Class.forName(item.getCleanerClass());
				value = (CleanerInterface)theClass.newInstance();
			}catch(Exception ce){
      }

# Environment

Websphere Commerce Suite 7
Java 1.6
Oracle XX

# Database 

General feature can be toggled on/off by a STORE_ATTIBUTE database table
Granular behavior configured in XSS_CONFIG table

Columns ID, STORE_ID, PATH, PARAM, CLEANER_CLASS_NAME

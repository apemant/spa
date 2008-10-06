/****license*****************************************************************
**   file: Rules.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
/*
 * Created on Oct 25, 2004
 *
 */
package hr.restart.db.replication.logging;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * @author dgradecak
 *
 * Rules by word are supported. Union between different words are not supported.
 * If the statement contains the rule specified it will be logged.
 *
 * TODO: more complex rule method
 */
class Rules {
    
    private Set wordRules = null;
    private Set complexRules = null;
    private Logger log = null;
    private int method = 3;// 3 logs all rules, 2 logs complexe rules only, 1 logs word rules only

    protected Rules(){
        
        log = Logger.getLogger(Rules.class);
        wordRules = new HashSet();
        
        log.debug("Rules created");
    }
    
    /**
     * adds a rule by word. If the word is present in the first 3 words of the SQL statement's string,
     * the rule is true, otherwise the rule is false. Does not combine different words in a rule.
     * 
     * Example:
     * A SQl statement can start with 
     * INSERT, DELETE or UPDATE
     * 
     * - INSERT INTO TABLE_NAME ...
     * - UPDATE TABLE_NAME SET ... 
     * - DELETE FROM TABLE_NAME ...
     * 
     * 1. If you want to log everything that does an INSERT, just add a rule INSERT (case insensitve).
     * 2. Rules are not combined like "rule 1: INSERT", "rule 2: TABLE_NAME", if the rule containes on of those two words,
     * 	  the condition is satisfied.
     * 
     * 
     * @param rule the rule to add
     */
    synchronized protected void addWordRule(String rule){
        
        if(rule == null)
            throw new NullPointerException("The rule cannot be null");
        
        if( rule.equals(""))
            throw new IllegalAccessError("The rule cannot be empty");
            
        wordRules.add(rule.toUpperCase());
        log.debug("Word rule added : "+rule);
    }
    
    /**
     * IN TEST! DO NOT USE THIS METHOD
     * 
     * @param rule
     */
    synchronized protected void addComplexRule(String rule){
        
        if(rule == null)
            throw new NullPointerException("The rule cannot be null");
        
        if( rule.equals(""))
            throw new IllegalAccessError("The rule cannot be empty");
            
        complexRules.add(rule.toUpperCase());
        log.debug("Complex rule added");
        
    }
    /**
	 * verify if the SQL statement has to be logged or not.
	 * @param sql
	 * @return
	 */
    synchronized protected boolean isRule(String s){        
        
        boolean ret = false;//rules.contains(s.toUpperCase());
        switch (getRuleMethod()){
        
        	case 1:
        	    	ret = verifyWordRules(s);
        	    break;
        	    
        	case 2:
        	    	ret = verifyComplexRules(null);
        	    break;        
        	    
        	case 3:
        	    	ret = verifyWordRules(s) || verifyComplexRules(null);
        	    break;
        	    
        	default:
        	    throw new IllegalArgumentException("The argument has a no-defined state");
        }
       
        
        
        log.debug("The statement "+s+" has a rule");
        return ret;
    }
    /**
     * @return Returns the method.
     */
    protected int getRuleMethod() {
        return method;
    }
    /**
	 * sets the rules method.
	 * 
	 * 3 logs all rules, 
	 * 2 logs complexe rules only, 
	 * 1 logs word rules only
	 * 
	 * By default the level 3 is used.
	 * 
	 * @param method
	 */
    protected void setRuleMethod(int method) {
        
        switch (method){
        
        	case 1:        	    
        	case 2:        	           
        	case 3:
        	    	this.method = method;
        	    	log.debug("The rule method is changed.");
        	    break;
        	    
        	default:
        	    throw new IllegalArgumentException("The argument method has no implementation for the state "+method);
        }                
    }
    
    /**
     * verify if the String sql has a word rule in the first 3 words.
     * if the 3rd word contains the character (, everything afeter is removed.
     * 
     * exemple:
     * insert into MY_TABLE(...)
     * 
     * - the words analyzed would be INSERT, INTO and MY_TABLE
     * 
     * @param sql
     * @return
     */
    private boolean verifyWordRules(String sql){

        boolean ret = false;
        
        StringTokenizer stk = new StringTokenizer(sql, " ");//first separate with the blank spaces
        
        for(int i = 1; i < 4; i++){
            String elem = stk.nextToken();
            //System.out.println("THE ELEMENT ANALYZED ----------------- > "+elem);
            
            if(i == 3){
                StringTokenizer stk2 = new StringTokenizer(elem, "("); //then separate with (
                elem = stk2.nextToken();        
            }
            if(wordRules.contains(elem.toUpperCase())){
                log.debug("A word rule has been found");
                return true;
                //ret = true;
                //break;
            }                
        }
        
        log.debug("No word rule has been found");
        return ret;
    }
    
    /*
     * TODO: IMPLEMENT THIS METHOD
     */
    private boolean verifyComplexRules(String sql){
        return false;
    }
}

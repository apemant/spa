/****license*****************************************************************
**   file: ILineConverter.java
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
 * Created on Apr 21, 2005
 */
package hr.restart.util.textconv;

import java.util.Map;

/**
 * Interface for converting parsed lines into something else, for example database record.
 * 
 * @author andrej
  */
public interface ILineConverter {

	/**
	 * Starts (initializes) converison
	 */
	public void begin();

	/**
	 * Converts given line
	 * @param line
	 */
	public void convert(ILine line);

	/**
	 * Ends conversion
	 */
	public void end();
	
	/**
	 * Map of rule definitions used to convert line to something else
	 * @param rulesForColumns Map containing Lines column name as key and rule for specified column as value  
	 */
	public void setRules(Map rulesForColumns);

	/**
	 * Aborts conversion (rollback | clean | something else
	 */
	public void abort();
	
	/**
	 * Verifier for line to convert. Implementation should check given line against this verifier
	 * @param verifier
	 */
	public void setLineVerifier(ILineVerifier verifier);

}

/****license*****************************************************************
**   file: IReport.java
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
 * Created on 29-Jul-2004
 *
 */
package hr.restart.util.reports;


/**
 * All object that can be viewable, printable or exportable should implements this interface.
 * 
 * @author gradecak
 *
 */
public interface IReport {

    /**
     * Print the object.   
     * @throws Exception
     */
	public void print() throws Exception;
	
	/**
	 * Show the object.
	 * @throws Exception
	 */
	public void preview() throws Exception;
	
	/**
	 * Exports the object for saving.
	 * @throws Exception
	 */
	public void export() throws Exception;
	
}
/****license*****************************************************************
**   file: IChartXYZ.java
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
 */
package hr.restart.util.chart;


/**
 *  * This interface proivdes some methodes that an XYZ chart should implements.
 * @author gradecak
 */

interface IChartXYZ extends IChartXY {
	
    /**
     * this method have to be implemented by the user.
     * @return the name of the axis
     */
	public String getAxisZ();	
}
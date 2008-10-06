/****license*****************************************************************
**   file: RepStatParChart.java
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
package hr.restart.robno;


import hr.restart.util.chart.ChartXY;

import java.util.ArrayList;
import java.util.Map;

import com.borland.dx.dataset.DataSet;

/*
 * Created on 29-Jul-2004
 *
 */

/**
 * @author gradecak
 *
 */
public class RepStatParChart extends ChartXY {

    ispStatPar isp = ispStatPar.getInstance();

	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getDefaultSelected()
	 */
	public String getDefaultSelected() {

		return BAR_CHART;
	}
	public RepStatParChart() throws Exception{

	    ArrayList subs = new ArrayList();
	    subs.add(isp.getPeriod());
//	    subs.add("and one more");
	    setSubtitles(subs);
	}


	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChartXY#getAxisX()
	 */
	public String getAxisX() {	
		return "NAZPAR";
	}
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChartXY#getAxisY()
	 */
	public String getAxisY() {
		//return "IPRODBP";
	    return null;
	}

	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IDataSet#getDataSet()
	 */
	public DataSet getDataSet() {
		return isp.reportSet;
	}
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getGraphTitle()
	 */
	public String getChartTitle() {
		return "Top lista kupaca";
	}

	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getNumberOfElements()
	 */
	public int getNumberOfElements() {
		return 7;
	}
    /* (non-Javadoc)
     * @see hr.restart.util.chart.ChartXY#getDefaultSelectedItem()
     */
    public String getDefaultSelectedItem() {
        // TODO Auto-generated method stub
        return "Prodajni iznos s porezom";
    }
    
    public boolean sortByValue() {
      return !isp.getSortCol().equals("NAZPAR");
    }
    
    public Map dataSetToMap() throws NullPointerException {
      return null;
    }
}

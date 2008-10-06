/****license*****************************************************************
**   file: RepStatParMonthsChart.java
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
 * Created on 17-Aug-2004
 *
 */
package hr.restart.robno;

import hr.restart.util.chart.ChartXYZ;

import java.util.ArrayList;

import com.borland.dx.dataset.DataSet;

/**
 * @author gradecak
 *
 */
public class RepStatParMonthsChart extends ChartXYZ {
  ispStatPar isp = ispStatPar/*Dva*/.getInstance();

	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getDefaultSelected()
	 */
	public String getDefaultSelected() {

		return BAR_CHART;
	}
	public RepStatParMonthsChart() throws Exception{

	    ArrayList subs = new ArrayList();
	    subs.add(isp.getPeriod());
//	    subs.add("and one more");
	    setSubtitles(subs);
	}
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChartXYZ#getAxisZ()
     */
    public String getAxisZ() {

        return "NAZPAR";
    }
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChartXY#getAxisX()
     */
    public String getAxisX() {

        return "MJESEC";
    }
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChartXY#getAxisY()
     */
    public String getAxisY() {

        return "IZNOS";
    }
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IDataSet#getDataSet()
     */
    public DataSet getDataSet() {
        return isp.getChartSet();
//        return upStatsMonths.getInstance().getChartSet();
    }
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChart#getChartTitle()
     */
    public String getChartTitle() {

        return "Top lista kupaca mjeseèno";
    }

	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getNumberOfElements()
	 */
	public int getNumberOfElements() {
		return 7;
	}
}

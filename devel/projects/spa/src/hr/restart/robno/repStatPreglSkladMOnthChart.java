/****license*****************************************************************
**   file: repStatPreglSkladMOnthChart.java
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

import hr.restart.util.chart.ChartXYZ;

import com.borland.dx.dataset.DataSet;

/*
 * Created on 2004.09.28
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repStatPreglSkladMOnthChart extends ChartXYZ{
  
  upProdajaPoDucanima uppd = upProdajaPoDucanima.getInstance();

  public String getDefaultSelected() {
    return BAR_CHART;
  }

  public repStatPreglSkladMOnthChart() throws Exception {

  }
	
  public String getAxisZ() {
    // TODO Auto-generated method stub
    return "NAZSKL";
  }
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChartXY#getAxisX()
   */
  public String getAxisX() {
    // TODO Auto-generated method stub
    return "MJESEC";
  }
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChartXY#getAxisY()
   */
  public String getAxisY() {
    // TODO Auto-generated method stub
    return "IZNOS";
  }
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IDataSet#getDataSet()
   */
  public DataSet getDataSet() {
    // TODO Auto-generated method stub
    return uppd.getChartDataset();
  }
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChart#getChartTitle()
   */
  public String getChartTitle() {
    // TODO Auto-generated method stub
    return null;
  }
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChart#getDefaultSelected()
   */
}

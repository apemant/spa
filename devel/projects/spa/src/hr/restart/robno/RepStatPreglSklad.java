/****license*****************************************************************
**   file: RepStatPreglSklad.java
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

import java.util.Map;

import com.borland.dx.dataset.DataSet;

/*
 * Created on 2004.09.27
 */

public class RepStatPreglSklad extends ChartXY{
  
 upProdajaPoDucanima uppd = upProdajaPoDucanima.getInstance();

  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChartXY#getAxisX()
   */
  
  public String getAxisX() {
    // TODO Auto-generated method stub
    return "CSNS";
  }
  
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChartXY#getAxisY()
   */
  
  public String getAxisY() {
    // TODO Auto-generated method stub
    return null;
  }
  
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChart#getChartTitle()
   */
  
  public String getChartTitle() {
    // TODO Auto-generated method stub
    return "Pregled prodaje po prodajnim mjestima";
  }
  
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IChart#getDefaultSelected()
   */
  
  public String getDefaultSelected() {
    // TODO Auto-generated method stub
    return BAR_CHART;
  }

	/*
   * (non-Javadoc)
   * 
   * @see hr.restart.util.chart.IChart#getNumberOfElements()
   */
  public int getNumberOfElements() {
    return 7;
  }
  
  /* (non-Javadoc)
   * @see hr.restart.util.chart.IDataSet#getDataSet()
   */
  
  public DataSet getDataSet() {
    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
    st.prn(uppd.getChartDataset());
    return uppd.getChartDataset();
  }

  public String getDefaultSelectedItem() {
    // TODO Auto-generated method stub
    return "Prodajni iznos s porezom";
  }
  
  public Map dataSetToMap() throws NullPointerException {
    return null;
  }
}

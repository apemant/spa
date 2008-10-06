/****license*****************************************************************
**   file: IChart.java
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
 * Created on 30-Jul-2004
 */
package hr.restart.util.chart;

import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartPanel;

/**
 * This interface should be used in the package hr.restart.util.chart only.
 * It provides a lot of methods that a graph should have in a #hr.restart.util.raFrame container.
 * The implementation is done by the provider.
 * 
 * @author gradecak
 */
abstract interface IChart {
	
	/**
	 * Repaints the graph	 
	 */
	abstract void repaintGraph();
	/**
	 * Converts a com.borland.dx.dataset.DataSet to a TreeMap.
	 * @return TreeMap. 
	 */
	abstract Map dataSetToMap() throws NullPointerException;
	
	/**
	 * 
	 * @return The graph's title.
	 */
	abstract String getChartTitle();
	
	/**
	 * @return Returns the subtitles.
	 */
	abstract public List getSubtitles();
	/**
	 * @param subtitles The subtitles to set.
	 */
	abstract public void setSubtitles(List subtitles);
	/**
	 * @return Returns the chartPanel.
	 */
	abstract ChartPanel getChartPanel();
	
	/**
	 * 
	 * @return Returns the number of elements that should be shown on the graph
	 */
	abstract int getNumberOfElements();
	

	
	
	/**
	 * Graphical initialisation for the graph, which is based on #hr.restart.util.raFrame 
	 *
	 */
	abstract void initFrame() throws Exception;
	
	/**
	 * 
	 * @return The default type of the graph.
	 */
	abstract public String getDefaultSelected();
	
	/**
	 * initialises all graph's components.
	 * @return a org.jfreechart.chart.ChartPanel
	 */
	abstract public ChartPanel initGraph();
		
}

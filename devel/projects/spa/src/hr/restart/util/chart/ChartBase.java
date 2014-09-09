/****license*****************************************************************
**   file: ChartBase.java
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

import hr.restart.util.raFrame;
import hr.restart.util.startFrame;
import hr.restart.util.reports.IReport;
import hr.restart.util.reports.dlgRunReport;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;

/**
 * This is the base class for all graphs. Every graph will have an axis X and an axis Y. 
 * A base graph will always have a BAR CHART view.
 * This class is based on a Frame from Rest Art's class raFrame.
 * 
 * @author gradecak
 */

abstract class ChartBase extends raFrame implements IChartXY, IChart, IReport {
	

    /**
     * static variable wich should be used to select a BAR CHART.
     */
    public static final String BAR_CHART = "Stupèani Grafikon";
    
	/*
	 * private variables
	 */
	final private List subtitles = new ArrayList();
	final private int defaultNumberOfElements = 5;
	
	/**
	 * Constructs an raFrame object.
	 * Sets the title for the Frame, and the same title is setted for the graph.  
	 */
	public ChartBase(){		
		//super();
		super(raFrame.DIALOG, dlgRunReport.getCurrentDlgRunReport() == null ? null : 
		        dlgRunReport.getCurrentDlgRunReport().getDlg());
		this.getJdialog().setModal(true);
		super.setTitle(getChartTitle());
	}
	
	/* (non-Javadoc)
	 * @see hr.restart.util.reports.IReport#print()
	 */
	final public void print() throws Exception{
		//System.out.println("(CHARTBASE) print");
	    try{
			ChartPanel chartPanel = initGraph();
			chartPanel.setSize(chartPanel.getPreferredSize());
			chartPanel.createChartPrintJob();
	    }catch(Exception e){
	        e.printStackTrace();
	        preview();
	    }
	}
	
	/* (non-Javadoc)
	 * @see hr.restart.util.reports.IReport#preview()
	 */
	final public void preview() throws Exception{	
		//System.out.println("(CHARTBASE) preview");		
		initFrame();		
		
		//startFrame sf = new startFrame();
		startFrame.getStartFrame().centerFrame(this, 10,"Grafikon");
		
		pack();
    	setVisible(true);
	}
		
	/* (non-Javadoc)
	 * @see hr.restart.util.reports.IReport#export()
	 */
	final public void export() throws Exception{	
		//System.out.println("(CHARTBASE) export");		
	    try{
			ChartPanel chartPanel = initGraph();
			chartPanel.setSize(chartPanel.getPreferredSize());
			chartPanel.doSaveAs();
	    }catch(Exception e){
	        e.printStackTrace();
	        preview();
		}				
	}
				
	/**
	 * @return Returns the subtitles.
	 */
	final public List getSubtitles() {
		return this.subtitles;
	}
	/**
	 * @param subtitles The subtitles to set.
	 */
	final public void setSubtitles(List subtitles) {
	    this.subtitles.clear();
		this.subtitles.addAll(subtitles);
	}
	
	final public void addSubtitles(List subtitles) {
      this.subtitles.addAll(subtitles);
  }
		
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#getNumberOfElements()
	 */
	public int getNumberOfElements() {
		// 	    
		return defaultNumberOfElements;
	}
	
	
}
/****license*****************************************************************
**   file: ChartXYZ.java
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
//TODO this class should allow to construct a graph directly from a Map

package hr.restart.util.chart;

import hr.restart.util.OKpanel;
import hr.restart.util.raImages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.BarRenderer;
import org.jfree.chart.renderer.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

import com.borland.dx.dataset.DataSet;

/**
 * All the objects that want to be able to give a graphical representation should extends this class.
 * It allows to show a graphical view of a DataSet component in a 2D X-Y-Z manner.
 * TODO : dataSetToMap() --> the static representation of the 12 months has to be modified
 * @author gradecak
 */

abstract public class ChartXYZ extends ChartBase implements IChartXYZ{
	
    /**
     * static variable wich should be used to select a LINE CHART.
     */
	public static final String LINE_CHART = "Linijski Grafikon";
		
	
	// private variables
	final private ArrayList chartTypes = new ArrayList();
	
	final private JComboBox boxChartType = new JComboBox();
	//allows to know which graph was last selected
	final private StringBuffer lastSelected = new StringBuffer();
	
	private boolean instanciated = false;
	
	// VIEW
	private CategoryDataset barDataset = null; 	
	private JFreeChart barChart = null;
	private JFreeChart lineChart = null;
	private ChartPanel chartPanel = null;
	protected JPanel buttonsPanel = null;
	private JPanel actionsPanel = null;
	private JPanel globalButtonsPanel = null;
	private JPanel mainPanel = null;
	private JComboBox comboBoxQuantity = null;
	// END
	
	
	/**
	 * This constructor calls the super constructor from #hr.restart.util.chart.ChartBase
	 *
	 */
	public ChartXYZ() throws Exception{
		super();
	}
	
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#dataSetToMap()
	 */
	final public Map dataSetToMap() throws NullPointerException{
		
	    DataSet ds = getDataSet();
		if(ds == null)
			throw new NullPointerException("The DataSet should not be null. You should have implemented the getDataSet() method.");
		
		int maxElements = 0;
		if ( comboBoxQuantity == null)
		    maxElements = getNumberOfElements();
		else
		    maxElements = new Integer(comboBoxQuantity.getSelectedItem().toString()).intValue();//getNumberOfElements();
		
		TreeMap map = new TreeMap();
												
		int i = 0;
		// TODO: static 12 has to be changed in an dynamic way
		for(ds.first(); ds.inBounds() && i < (maxElements * 12); ds.next(), i ++){

		    if( (i % maxElements) == 0 )
		        System.out.println("reste = 0 : i je "+i);
		    
			Pair pair = new Pair(ds.getString(getAxisZ()),ds.getString(getAxisX()));
			map.put(pair,new Double(ds.getBigDecimal(getAxisY()).doubleValue()));
		}
			
		return map;
	    
	    //return initMapTest();
	}	
	
	protected boolean isVariableZ() {
	  return true;
	}
	
	/**
	 * 
	 * @author gradecak
	 *
	 * Object that represents a simple 2 dimension array.
	 */
	final private class Pair implements Comparable{
		private String first = null;
		private String second = null;
		
		/**
		 * @param left
		 * @param right
		 */
		public Pair(String first, String second) {
			super();
			this.first = first;
			this.second = second;
		}		
				
		/**
		 * @return Returns the left.
		 */
		public String getFirst() {
			return first;
		}
		/**
		 * @param left The left to set.
		 */
		public void setFirst(String first) {
			this.first = first;
		}
		/**
		 * @return Returns the right.
		 */
		public String getSecond() {
			return second;
		}
		/**
		 * @param right The right to set.
		 */
		public void setSecond(String second) {
			this.second = second;
		}
		
		public int compareTo(Object o){
			
			Pair object = (Pair)o;
			
			if( this.getFirst().equals(object.getFirst()) && this.getSecond().equals(object.getSecond()))
				return 0;
			
						
			if( this.getFirst().compareTo(object.getFirst()) > 0)
				return 1;
			
			if( this.getFirst().compareTo(object.getFirst()) < 0)
				return -1;
			
			if( this.getSecond().compareTo(object.getSecond()) > 0)
				return 1;
			
			if( this.getSecond().compareTo(object.getSecond()) < 0)
				return -1;								
			
			return 0;
		}
		
	}

	final public ChartPanel initGraph(){
		
//		creates datasets		
		// to be changed with the real MAP( dataSetToMap )
	  
		barDataset = isVariableZ() ? createDataset(dataSetToMap()) : createDataset();
		//barDataset = createDataset(initMapTest());
				
		
		//creates chart panels
		barChart = createBarChart(barDataset, this.getChartTitle());
		lineChart = createLineChart(barDataset, this.getChartTitle());		
		
		//default		HAS TO BE CHANGED
		return new ChartPanel(barChart);
		
	}
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#init()
	 */
	public void initFrame() throws Exception{
	    // initializing the combo box chartTypes	
        if(isInstanciated()) {
            if(chartPanel != null)
              mainPanel.remove(chartPanel);
            chartPanel = initGraph();
            mainPanel.add(chartPanel, BorderLayout.CENTER);
            return;
        }
        setInstanciated(true);    
	    
		chartTypes.add( BAR_CHART);
		chartTypes.add( LINE_CHART);
		 
		setLastSelected(getDefaultSelected());
		
		chartPanel = initGraph();
		
		//creates buttons 
		//boxChartType = new JComboBox(chartTypes.toArray());
		
		for ( Iterator iterator = chartTypes.iterator(); iterator.hasNext();){ 
			boxChartType.addItem(iterator.next());
		}
		
		boxChartType.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent ev){
		      	
		    	if(getLastSelected() != null && getLastSelected() != boxChartType.getSelectedItem().toString()){
		    		
					try{
						setLastSelected(boxChartType.getSelectedItem().toString());
					}catch(Exception e){
					    //e.printStackTrace();
					    System.out.println("(ChartXYZ) : method --> iniFrame : "+e);
					}
						
						//removing the panel
						if(chartPanel != null)
							mainPanel.remove(chartPanel);						
							
						//switching the chart type
						if(getLastSelected().equals(BAR_CHART)){
							chartPanel = new ChartPanel(barChart);
						}else{
							if(getLastSelected().equals(LINE_CHART)){
								chartPanel = new ChartPanel(lineChart);
							}
							else{
							
							}
						}
						
						//adding the new chart panel
						mainPanel.add(chartPanel,0);
						repaintGraph();
		    	}
		    }
		});
				
		buttonsPanel = new JPanel();
		buttonsPanel.add(boxChartType);
		
		//to be removed
//		JButton btTest = new JButton("1");		
//		btTest.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent ev) {
//		      	
//		    	System.out.println("changing dataset"); // have to change the dataset content
//		    	// making a new QUERY
//		    	com.borland.dx.dataset.DataSet ds = getDataSet();
//		    	//HAS TO BE MEDIFIED
//		    	
//			  }
//		});
//		buttonsPanel.add(btTest);
		
		//adding combobox
		if (isVariableZ()) {
    		String [] quantity = { "5","10","15"};
    		comboBoxQuantity = new JComboBox(quantity);
    		comboBoxQuantity.setEditable(true);
    		comboBoxQuantity.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent ev) {
    														
    				try {
    					mainPanel.remove(chartPanel);
    				} catch (RuntimeException e1) {
    
    					//e1.printStackTrace();	
    				    System.out.println("(ChartXYZ) : method --> initGraph : "+e1);
    				}
    
    								
    				selectionChanged();
    			}
    			
    		});													
    		comboBoxQuantity.setSelectedItem(new Integer(getNumberOfElements()).toString());					
    		buttonsPanel.add(comboBoxQuantity);
		}
		
		//creates action buttons
			//adding filechooser
		actionsPanel = new JPanel();
		JButton btSave = new JButton("Snimi");
		btSave.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent ev) {
		      	
		    	try {
						chartPanel.doSaveAs();
				} catch (IOException e) {

				    System.out.println("(ChartXYZ) : method --> initFrame : "+e);
				}
			  }
			});														
		actionsPanel.add(btSave);				
		
			//adding printing button
		JButton btPrint = new JButton("Ispis");
		btPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
		
				chartPanel.createChartPrintJob();
			}
		});
		actionsPanel.add(btPrint);
		
//		 adding OKPanel
		final OKpanel okPanel = new OKpanel() {
		    public void jBOK_actionPerformed() {
		      //ok_action();
		        chartPanel.createChartPrintJob();
		    }
		    public void jPrekid_actionPerformed() {
//		      firstESC();
		        cancelPressed();		        
		    }
		};	
		okPanel.addAncestorListener(new AncestorListener() {
		    public void ancestorAdded(AncestorEvent e){
	            //		    		register the keys action
	            
	    		okPanel.registerOKPanelKeys(getJdialog());	
	        }
	        
	        public void ancestorMoved(AncestorEvent e){
	            
	        }
	        
	        public void ancestorRemoved(AncestorEvent e){
	            okPanel.unregisterOKPanelKeys(getJdialog());
	        }
		});
		okPanel.jBOK.setText("Ispis");
	    okPanel.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
	    
	    JButton btSnimi = new JButton("Snimi");
	    btSnimi.setIcon(raImages.getImageIcon(raImages.IMGSAVE));
	    btSnimi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
			    try {
					chartPanel.doSaveAs();
			    } catch (IOException e) {
			        
			        //System.out.println(e);
			        System.out.println("(ChartXYZ) : method --> initFrame : "+e);
			    }							
			}
			
		});	    
	    okPanel.add(btSnimi,BorderLayout.WEST);
		 
		//buttons to their main panel
		globalButtonsPanel = new JPanel(new BorderLayout());
		globalButtonsPanel.add(buttonsPanel, BorderLayout.CENTER);
		//globalButtonsPanel.add(actionsPanel, BorderLayout.SOUTH);
		globalButtonsPanel.add(okPanel, BorderLayout.SOUTH);
		
		//creates main panel
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(chartPanel, BorderLayout.CENTER);
		mainPanel.add(globalButtonsPanel, BorderLayout.SOUTH);
		
		//select the default one in the comboBox
		boxChartType.setSelectedItem(getDefaultSelected());
		
		this.getContentPane().add(mainPanel);
	}
	
	/**
	 * called when escape is pressed.
	 *
	 */
    final private void cancelPressed(){
        //hide();
        getJdialog().dispose();
        
    }

	/**
	 * Creates a org.jfree.data.CategoryDataset from a Map.
	 * @param map The Map
	 * @return The org.jfree.data.CategoryDataset
	 */
    final public CategoryDataset createDataset(Map map) {    	
        
		//creates the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Set set = map.keySet();    	
		Iterator iterator = set.iterator();    	
		while(iterator.hasNext()){
			
			Pair element = (Pair)iterator.next();
			Double value = (Double)map.get(element);
			    		
			dataset.addValue(value.doubleValue(), element.getFirst(), element.getSecond());
			    		
		}
		
		return dataset;        
	
	}
    
    final public CategoryDataset createDataset() {       
      DataSet ds = getDataSet();
      if(ds == null)
          throw new NullPointerException("The DataSet should not be null. You should have implemented the getDataSet() method.");
      
      //creates the dataset...
      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      
      for (ds.first(); ds.inBounds(); ds.next()) 
        dataset.addValue(ds.getBigDecimal(getAxisY()), ds.getString(getAxisZ()),ds.getString(getAxisX()));
        
      return dataset;
    }
    
	
	/**
	 * @return Returns the lastSelected.
	 */
	final protected String getLastSelected() {
		return lastSelected.toString();
	}
	/**
	 * @param lastSelected The lastSelected to set.
	 */
	final private void setLastSelected(String lastSelected) throws Exception {
		
		if(chartTypes.contains(new String(lastSelected)))
			this.lastSelected.replace(0,this.lastSelected.length(), lastSelected);
		else
			throw new Exception("This type of graph does not exist");
	}
	
	
	/**
     * Creates a LINE CHART.
     * 
     * @param dataset The org.jfree.data.CategoryDataset
     * @param title The title
     * @return org.jfree.chart.JFreeChart
     */
    final private JFreeChart createLineChart(final CategoryDataset dataset, String title) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            title,       // chart title
            "",                    // domain axis label
            "",                   // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );
        
        chart.setBackgroundPaint(Color.white);

        //print the subtitles
        java.util.List subs = getSubtitles();        
        if(subs != null)
	        for (int i = 0; i < subs.size(); i++) {
	    		chart.addSubtitle(new TextTitle(subs.get(i).toString()));
			}                
        
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        final StandardLegend legend = (StandardLegend) chart.getLegend();
        legend.setDisplaySeriesShapes(true);
        legend.setShapeScaleX(1.5);
        legend.setShapeScaleY(1.5);
        legend.setDisplaySeriesLines(true);

        

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        //plot.setBackgroundPaint(Color.lightGray);
        //plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        
        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setDrawShapes(true);
        adjustLineRenderer(renderer);

//        renderer.setSeriesStroke(
//            0, new BasicStroke(
//                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
//                1.0f, new float[] {10.0f, 6.0f}, 0.0f
//            )
//        );
//        renderer.setSeriesStroke(
//            1, new BasicStroke(
//                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
//                1.0f, new float[] {6.0f, 6.0f}, 0.0f
//            )
//        );
//        renderer.setSeriesStroke(
//            2, new BasicStroke(
//                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
//                1.0f, new float[] {2.0f, 6.0f}, 0.0f
//            )
//        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
    }
    
	/**
     * Creates a BAR CHART.
     * 
     * @param dataset The org.jfree.data.CategoryDataset
     * @param title The title
     * @return org.jfree.chart.JFreeChart
     */
    final private JFreeChart createBarChart(final CategoryDataset dataset, String title) {
    	
        final JFreeChart chart = ChartFactory.createBarChart(
            title,       // chart title
            "",               // domain axis label
            "",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // the plot orientation
            true,                    // include legend
            true,
            false
        );
        
        chart.setBackgroundPaint(Color.white);
        
        //print the subtitles
        java.util.List subs = getSubtitles();        
        if(subs != null){
	        for (int i = 0; i < subs.size(); i++) {
	    		chart.addSubtitle(new TextTitle(subs.get(i).toString()));
			}            
        }
        
//      NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        //plot.setBackgroundPaint(Color.lightGray);
        //plot.setDomainGridlinePaint(Color.white);
        //plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);      
        renderer.setItemMargin(getItemMargin());
        adjustBarRenderer(renderer);
                
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(getCategoryMargin());
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;

    }
    
    protected void adjustBarRenderer(BarRenderer renderer) {
      
    }
    
    protected void adjustLineRenderer(LineAndShapeRenderer renderer) {
      
    }
    
    protected double getItemMargin() {
      return 0.0;
    }
    
    protected double getCategoryMargin() {
      return 0.4;
    }
    
	public void repaintGraph(){
		
		this.chartPanel.repaint();
		this.chartPanel.validate();		
		
		this.globalButtonsPanel.repaint();
		this.globalButtonsPanel.validate();
		
		this.getWindow().repaint();
        this.getWindow().validate();
        
        this.chartPanel.repaint();
		this.chartPanel.validate();		
		
		this.globalButtonsPanel.repaint();
		this.globalButtonsPanel.validate();
		
		this.getWindow().repaint();
        this.getWindow().validate();
	}

    
	/**
	 * @return Returns the chartPanel.
	 */
	final public ChartPanel getChartPanel() {
		return this.chartPanel;
	}

    /**
     * @return Returns the instanciated.
     */
    final private boolean isInstanciated() {
        return instanciated;
    }
    /**
     * @param instanciated The instanciated to set.
     */
    final private void setInstanciated(boolean instanciated) {
        this.instanciated = instanciated;
    }
    /**
     * Fire some actions when an input component has changed.
     *
     */
    final protected void selectionChanged(){

        try {
            initGraph();
            
//      removing the panel
            if(chartPanel != null)
            	mainPanel.remove(chartPanel);						
            	
            //switching the chart type
            if(getLastSelected().equals(BAR_CHART)){
            	chartPanel = new ChartPanel(barChart);
            }else{
            	if(getLastSelected().equals(LINE_CHART)){
            		chartPanel = new ChartPanel(lineChart);
            	}
            	else{
            	
            	}
            }
            
            //adding the new chart panel
            mainPanel.add(chartPanel,0);
            repaintGraph();
        } catch (Exception e) {            
            //e.printStackTrace();
            System.out.println("(ChartXYZ) : method --> selectionChanged : "+e);
        }
    }
    
    /**
     * TO BE REMOVED
     * @return
     */
    final public SortedMap initMapTest(){
    	TreeMap map = new TreeMap();

        //to be removed or changed
    	Pair objectZ = new Pair("Klijent 1","01");    	    	
    	map.put((Object)objectZ,new Double(1.2));    	
    	objectZ = new Pair("Klijent 1","02");    	    	
    	map.put((Object)objectZ,new Double(0.2));    	    	       	
    	objectZ = new Pair("Klijent 1","03");    	    	
    	map.put((Object)objectZ,new Double(0.2));    	
    	objectZ = new Pair("Klijent 1","04");    	    	
    	map.put((Object)objectZ,new Double(1.2));
    	objectZ = new Pair("Klijent 1","05");    	    	
    	map.put((Object)objectZ,new Double(1.5));    	
    	objectZ = new Pair("Klijent 1","06");    	    	
    	map.put((Object)objectZ,new Double(1.2));
    	objectZ = new Pair("Klijent 1","07");    	    	
    	map.put((Object)objectZ,new Double(1.2));
    	objectZ = new Pair("Klijent 1","08");    	    	
    	map.put((Object)objectZ,new Double(1.42));
    	objectZ = new Pair("Klijent 1","09");    	    	
    	map.put((Object)objectZ,new Double(0.2));
    	objectZ = new Pair("Klijent 1","10");    	    	
    	map.put((Object)objectZ,new Double(0.7));
    	objectZ = new Pair("Klijent 1","11");    	    	
    	map.put((Object)objectZ,new Double(1.2));
    	objectZ = new Pair("Klijent 1","12");    	    	
    	map.put((Object)objectZ,new Double(1.5));
    	
//    	objectZ = new Pair("Klijent 2","01");    	    	
//    	map.put((Object)objectZ,new Double(0.2));    	
//    	objectZ = new Pair("Klijent 2","02");    	    	
//    	map.put((Object)objectZ,new Double(2.2));    	    	       	
    	objectZ = new Pair("Klijent 2","03");    	    	
    	map.put((Object)objectZ,new Double(2.2));    	
    	objectZ = new Pair("Klijent 2","04");    	    	
    	map.put((Object)objectZ,new Double(3.2));
    	objectZ = new Pair("Klijent 2","05");    	    	
    	map.put((Object)objectZ,new Double(2.5));    	
    	objectZ = new Pair("Klijent 2","06");    	    	
    	map.put((Object)objectZ,new Double(1.7));
    	objectZ = new Pair("Klijent 2","07");    	    	
    	map.put((Object)objectZ,new Double(1.9));
    	objectZ = new Pair("Klijent 2","08");    	    	
    	map.put((Object)objectZ,new Double(1.42));
    	objectZ = new Pair("Klijent 2","09");    	    	
    	map.put((Object)objectZ,new Double(0.9));
    	objectZ = new Pair("Klijent 2","10");    	    	
    	map.put((Object)objectZ,new Double(0.7));
    	objectZ = new Pair("Klijent 2","11");    	    	
    	map.put((Object)objectZ,new Double(1.2));
    	objectZ = new Pair("Klijent 2","12");    	    	
    	map.put((Object)objectZ,new Double(1.5));
    	
    	return map;
    }
}
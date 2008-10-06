/****license*****************************************************************
**   file: ChartXY.java
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
 *
 */

// TODO this class should allow to construct a graph directly from a Map

package hr.restart.util.chart;


import hr.restart.util.OKpanel;
import hr.restart.util.raImages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.BarRenderer;
import org.jfree.chart.renderer.CategoryItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DatasetUtilities;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.data.PieDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


/**
 * All the objects that want to be able to give a graphical representation should extends this class.
 * It allows to show a graphical view of a DataSet component in a 2D X-Y manner.
 * @author gradecak
 *
 */
abstract public class ChartXY extends ChartBase implements ActionListener{

    /**
     * static variable wich should be used to select a PIE CHART.
     */
	public static final String PIE_CHART = "Tortni Grafikon";	
		
	private boolean instanciated = false;
			
	//private variables
	private ArrayList charts = null;
	private JComboBox jcb = null;	
	private JComboBox comboBox = null;
	private JComboBox comboBoxQuantity = null;
	private JComboBox comboBoxOrientation = null;
	private CategoryDataset barDataset = null;
	private PieDataset pieDataset = null;
	private JFreeChart barChart = null;
	private JFreeChart pieChart = null;
	private ChartPanel chartPanel = null;
	private JPanel buttonsPanel = null;
	private JPanel mainPanel = null;	
	private String [] colNamesY = null;
	
	
	/**
	 * This constructor calls the super constructor from #hr.restart.util.chart.ChartBase
	 *
	 */
	public ChartXY(){
		super();
		
	}
	
	/* (non-Javadoc)
	 * @see hr.restart.util.chart.IChart#dataSetToMap()
	 */
    public CategoryDataset createDataSet() throws NullPointerException {
	    
      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      DataSet ds = getDataSet();
      SortDescriptor old = ds.getSort();
      String key = getAxisX();
      String value = jcb == null ? getAxisY() : colNamesY[jcb.getSelectedIndex()];
        
      ds.setSort(new SortDescriptor(new String[]{value},true, true));
      
      StorageDataSet scoped = new StorageDataSet();
      scoped.setLocale(Locale.getDefault());
      scoped.setColumns(new Column[] {
         ds.getColumn(key).cloneColumn(),
         ds.getColumn(value).cloneColumn(),
      });
      scoped.open();
	  
			BigDecimal ostali = new BigDecimal(0);
			
			int maxElements = getNumberOfElements();
			
			if(comboBoxQuantity != null)
			    maxElements = new Integer ( comboBoxQuantity.getSelectedItem().toString()).intValue();
		    
			int i = 0;
			for (ds.first(); ds.inBounds(); ds.next()) {
				i++;
				if (i>maxElements) {
					try {
					    					    					        					   
						ostali = ostali.add(ds.getBigDecimal(value));
					} catch (Exception e) {
							System.out.println("(ChartXY) : method --> dataSetToMap : "+e);
						break;
					}
				} else {
				    scoped.insertRow(false);
                    
                    scoped.setString(key, ds.getString(key));
                    scoped.setBigDecimal(value, ds.getBigDecimal(value));
				}
			}
			
			if (ostali.signum()>0) {
                scoped.insertRow(false);
                
                scoped.setString(key, "OSTALI");
                scoped.setBigDecimal(value, ostali);
            }
			scoped.post();
			
            if (sortByValue()) scoped.setSort(ds.getSort());
            else scoped.setSort(new SortDescriptor(new String[] {key}));
            
            ds.setSort(old);
            
            for (scoped.first(); scoped.inBounds(); scoped.next())
              dataset.addValue(scoped.getBigDecimal(value).doubleValue(), "", scoped.getString(key));
			return dataset;
		//return initMapTest();
	}
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChart#getChartPanel()
     */
    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }

    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChart#initFrame()
     */
    public void initFrame() throws Exception {

        if(isInstanciated())
            return;        
        
        setInstanciated(true);    
        
        verifyDialog();
        
		charts = new ArrayList();
		charts.add( BAR_CHART);
		charts.add( PIE_CHART);
				
		mainPanel = new JPanel(new BorderLayout());
							
		buttonsPanel = new JPanel(new FlowLayout());
		
		JPanel actionsPanel = new JPanel(new BorderLayout());
		JPanel savePanel = new JPanel();
				
		//adding combobox
		comboBox = new JComboBox(charts.toArray());
		comboBox.addActionListener(this);		
					
		buttonsPanel.add(comboBox);
		
		//adding filechooser
		JButton btSave = new JButton("Snimi");
		btSave.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent ev) {
		      	
		    	try {
						chartPanel.doSaveAs();
				} catch (IOException e) {
					// 
					e.printStackTrace();
					System.out.println(e);
				}
			  }
			});														
		savePanel.add(btSave);
		
		//adding printing button
		JButton btPrint = new JButton("Ispis");
		btPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
		
				chartPanel.createChartPrintJob();
			}
		});
		savePanel.add(btPrint);
		
		//creates datasets
		barDataset = createDataSet();
		pieDataset = DatasetUtilities.createPieDatasetForRow(barDataset, 0); 
		
		String value;
	    if(jcb == null)					        
	        value = getAxisY();
	    else
	        value = colNamesY[jcb.getSelectedIndex()];
		
	    PlotOrientation orientation = PlotOrientation.VERTICAL;
	    
		pieChart = createPieChart(pieDataset,value);
		barChart = createBarChart(barDataset,value, orientation);

		
		
		//adding orientation box
		String [] orientations = {"Vertikalni", "Horizontalni"}; 
		comboBoxOrientation = new JComboBox(orientations);
		comboBoxOrientation.setVisible(false);
		comboBoxOrientation.addItemListener(new ItemListener(){
		    
		    /* (non-Javadoc)
             * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
             */
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
                if( comboBoxOrientation.getSelectedItem() == "Vertikalni" )
                    selectionChanged(PlotOrientation.VERTICAL);
                else
                    selectionChanged(PlotOrientation.HORIZONTAL);
            }
		});
		
		buttonsPanel.add(comboBoxOrientation);
		
		
		
		// adding combobox
		String [] quantity = { "5","10","15"};
		comboBoxQuantity = new JComboBox(quantity);
		comboBoxQuantity.setEditable(true);
		comboBoxQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
														
				try {
					mainPanel.remove(chartPanel);
				} catch (RuntimeException e1) {
					//e1.printStackTrace();					
				}

								
				selectionChanged(PlotOrientation.VERTICAL);
			}
			
		});
													
		comboBoxQuantity.setSelectedItem(new Integer(getNumberOfElements()).toString());					
		buttonsPanel.add(comboBoxQuantity);
		
		if( jcb != null){
		    if(getDefaultSelectedItem() != null){
		        if( getDefaultSelectedItem().compareTo("") != 0)
		            jcb.setSelectedItem(getDefaultSelectedItem());
		    }
		        
		    
			buttonsPanel.add(jcb);		
		}
		
		// adding OKPanel
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
			        // 
			        e.printStackTrace();
			        System.out.println(e);
			    }							
			}
			
		});	    
	    okPanel.add(btSnimi,BorderLayout.WEST);	      
		mainPanel.add(okPanel,BorderLayout.SOUTH);
		
			 
		actionsPanel.add(buttonsPanel,BorderLayout.CENTER);
		//actionsPanel.add(savePanel,BorderLayout.SOUTH);	
		actionsPanel.add(okPanel,BorderLayout.SOUTH);
		mainPanel.add(actionsPanel,BorderLayout.SOUTH);
		  
		chartPanel = initGraph();				
		mainPanel.add(chartPanel,BorderLayout.CENTER);
		
		this.getContentPane().add(mainPanel);
    }
    private void cancelPressed(){
        //hide();
        getJdialog().dispose();
        
    }
    
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChart#initGraph()
     */
    public ChartPanel initGraph() {
                
        //creates dataset		
		barDataset = createDataSet();
		pieDataset = DatasetUtilities.createPieDatasetForRow(barDataset,0);	
						
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		//creates chart panels
		barChart = createBarChart(barDataset, this.getChartTitle(), orientation);
		pieChart = createPieChart(pieDataset, this.getChartTitle());		
		
		//default		HAS TO BE CHANGED
		return new ChartPanel(barChart);
    }
    /* (non-Javadoc)
     * @see hr.restart.util.chart.IChart#repaintGraph()
     */
    public void repaintGraph() {

		this.chartPanel.repaint();
		this.chartPanel.validate();		
		
		this.buttonsPanel.repaint();
		this.buttonsPanel.validate();
		
		this.getWindow().repaint();
        this.getWindow().validate();
        
        this.chartPanel.repaint();
		this.chartPanel.validate();		
		
		this.buttonsPanel.repaint();
		this.buttonsPanel.validate();
		
		this.getWindow().repaint();
        this.getWindow().validate();
    }
    
    /**
     * Creates a PIE CHART
     * @param dataset The org.jfree.data.PieDataset
     * @param title The title
     * @return org.jfree.chart.JFreeChart
     */
    private JFreeChart createPieChart(final PieDataset dataset,String title) { 


        final JFreeChart chart = ChartFactory.createPieChart(
            title,  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);
        
        Plot plot = chart.getPlot();                     
        
        // the subtitle from the combobox        
        if(jcb != null)
        	chart.addSubtitle(new TextTitle(jcb.getSelectedItem().toString()));
        
        //subtitles setted by the user.
        if(getSubtitles() != null)
            for (int i = 0; i < getSubtitles().size(); i++) {
        		chart.addSubtitle(new TextTitle(getSubtitles().get(i).toString()));
    		}
        
        
        final PiePlot piePlot = (PiePlot)plot; 
        piePlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        piePlot.setNoDataMessage("NO DATA!");
        piePlot.setCircular(false);
        piePlot.setLabelLinkPaint(Color.red);
        piePlot.setLabelGap(0.02);
        
        return chart;            
    }
    
    /**
     * Creates a BAR CHART
     * @param dataset The org.jfree.data.CategoryDataset
     * @param title The title
     * @return org.jfree.chart.JFreeChart
     */
    private JFreeChart createBarChart(final CategoryDataset dataset, String title, PlotOrientation orientation) {

    	
        final JFreeChart chart = ChartFactory.createBarChart(
            title,       // chart title
            "",               // domain axis label
            "",                  // range axis label
            dataset,                  // data
            orientation, // the plot orientation
            false,                    // include legend
            true,
            false
        );
        
        chart.setBackgroundPaint(Color.white);        
        	
        // the subtitle from the combobox        
        if(jcb != null)
        	chart.addSubtitle(new TextTitle(jcb.getSelectedItem().toString()));
        
        //subtitles setted by the user.
        if(getSubtitles() != null)
            for (int i = 0; i < getSubtitles().size(); i++) {
        		chart.addSubtitle(new TextTitle(getSubtitles().get(i).toString()));
    		}

        
        final Plot plot = chart.getPlot();
        
        // get a reference to the plot for further customisation...
        final CategoryPlot categoryPlot = (CategoryPlot)plot;
        
        
        categoryPlot.setNoDataMessage("NO DATA!");
        
        categoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);        
                
        final CategoryItemRenderer renderer = new CustomRenderer(
            new Paint[] {Color.red, Color.blue, Color.green,
                Color.yellow, Color.orange, Color.cyan,
                Color.magenta, Color.blue}
        );
        categoryPlot.setRenderer(renderer);
        
        renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
        renderer.setItemLabelsVisible(true);
        
        // inside
        //renderer.setBaseItemLabelPaint(Color.white);
        Font font = new Font("SansSerif", Font.PLAIN, 7);
        Font derive = font.deriveFont(Font.BOLD);
        renderer.setBaseItemLabelFont(derive);
        
        // margin
        final CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        //domainAxis.setBottomCategoryLabelPosition(new CategoryLabelPosition(RectangleAnchor.BOTTOM, TextBlockAnchor.BOTTOM_CENTER));
        domainAxis.setCategoryLabelPositions(new CategoryLabelPositions(new CategoryLabelPosition(
                RectangleAnchor.TOP, TextBlockAnchor.TOP_CENTER
        ), // TOP
        new CategoryLabelPosition(
            RectangleAnchor.BOTTOM, TextBlockAnchor.BOTTOM_CENTER
        ), // BOTTOM
        new CategoryLabelPosition(
            RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT, CategoryLabelWidthType.RANGE, 0.30f
        ), // LEFT
        new CategoryLabelPosition(
            RectangleAnchor.RIGHT, TextBlockAnchor.CENTER_RIGHT, CategoryLabelWidthType.RANGE, 0.30f
        ) // RIGHT) 
        ));

        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 0.0
        );
        renderer.setPositiveItemLabelPosition(p);
                
        if(comboBoxOrientation != null){            
            if(comboBoxOrientation.getSelectedItem() == "Vertikalni"){
	                        
	        		domainAxis.setCategoryLabelPositions(
	        		        CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0)
	        		);	        	
            }	        
        }
        
        

        return chart;

    }
    
    /**
     * A custom renderer that returns a different color for each item in a single series.
     */
    private class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    /*public CategoryDataset createDataSet(Map map) {
		//creates the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Set set = map.keySet();    	
		Iterator iterator = set.iterator();    	
		while(iterator.hasNext()){
			
		    String element = (String)iterator.next();
			BigDecimal value = (BigDecimal)map.get(element);
			System.out.println("element "+element+": "+value);
			dataset.addValue(value.doubleValue(), "", element);
						    		
		}
		
		return dataset;
    }*/
    
    private PieDataset createPieDataset() {
    	return DatasetUtilities.createPieDatasetForRow(createDataSet(),0);
        
    }
    
    /** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
        	        
    	
		try {
			mainPanel.remove(chartPanel);
		} catch (RuntimeException e1) {
			//e1.printStackTrace();
			//System.out.println(e);
		    System.out.println("(ChartXY) : method --> actionPerformed : "+e);
		}		
					
    	/*if(jcb != null)
			AXISY = colNamesY[jcb.getSelectedIndex()];
		*/

		        
        if(comboBox.getSelectedItem() == PIE_CHART){    
        	pieDataset = createPieDataset();
        	pieChart = createPieChart(pieDataset,getChartTitle());
        	chartPanel = new ChartPanel(pieChart);        	
	        mainPanel.add(chartPanel,0);
	        comboBoxOrientation.setVisible(false);
        }else{
        	if(comboBox.getSelectedItem() == BAR_CHART){
        		barDataset = createDataSet();
        		
        		PlotOrientation orientation = PlotOrientation.VERTICAL;
        		
        		if( comboBoxOrientation.getSelectedItem() == "Vertikalni" )
        		    orientation = PlotOrientation.VERTICAL;
                else
                    orientation = PlotOrientation.HORIZONTAL;
        		
        		
        		barChart = createBarChart(barDataset,getChartTitle(), orientation);
        		
        		chartPanel = new ChartPanel(barChart);
            	mainPanel.add(chartPanel,0);
            	comboBoxOrientation.setVisible(true);
        	}else{
        		//add other charts
            }        	
        }
                               
        repaintGraph();	
    }
    
    /**
     * Fire some actions when an input component has changed.
     *
     */
    private void selectionChanged(PlotOrientation orientation){
    	
    	
    	/*if(jcb != null)
			AXISY = colNamesY[jcb.getSelectedIndex()];
			*/		
    	
		if(comboBox.getSelectedItem() == PIE_CHART){
//			pieDataset = createPieDataset();
//					        
//	        pieChart = createPieChart(pieDataset,getChartTitle());	       
//	        chartPanel = new ChartPanel(pieChart);		
//		    mainPanel.add(chartPanel,0);	
		    
		    comboBox.setSelectedItem(PIE_CHART);
		    
		    //raCommonClass.getraCommonClass().setLabelLaF(comboBoxOrientation,false);
		    //comboBoxOrientation.setVisible(false);
		    
		}else{
			if(comboBox.getSelectedItem() == BAR_CHART){
//				barDataset = createDataSet(dataSetToMap());
//				
//				//PlotOrientation orientation = PlotOrientation.VERTICAL;
//		        barChart = createBarChart(barDataset,getChartTitle(), orientation);		        	
//		        chartPanel = new ChartPanel(barChart);		
//			    mainPanel.add(chartPanel,0);	
			    
			    comboBox.setSelectedItem(BAR_CHART);
			    
			    //raCommonClass.getraCommonClass().setLabelLaF(comboBoxOrientation,true);
			    //comboBoxOrientation.setVisible(true);
			}
		}
    }
    /**
     * 
     * @return String[], with all captions from the DataSet
     */
    private String[] makeCaptions(){
		
			Hashtable hcols = new Hashtable();
			Column[] ccols = getDataSet().getColumns();
			for (int i=0;i<ccols.length;i++) {
				//System.out.println("checking "+ccols[i].getColumnName());
				if (ccols[i].getDataType() == Variant.BIGDECIMAL) {
					hcols.put(ccols[i].getColumnName(),ccols[i].getCaption());
				}
			}
			
			colNamesY = (String[])hcols.keySet().toArray(new String[0]);
			String[] colCaptionsY = new String[hcols.size()];
			for (int i=0;i<colNamesY.length;i++) {
				colCaptionsY[i] = hcols.get(colNamesY[i]).toString();
			}
			
			return colCaptionsY;
    }
    
    /**
     * Verify if a JComboBox should be added to the panel or not.
     *
     */
    private void verifyDialog(){

        if (getAxisY() == null) {
            
            String[] colCaptionsY = makeCaptions();

        	jcb = new JComboBox(colCaptionsY);
			jcb.addActionListener(new ActionListener() {    			
				public void actionPerformed(ActionEvent ev) {
							    												
							selectionChanged(PlotOrientation.VERTICAL);
						}
					});
		}else{
			Column col = getDataSet().getColumn(getAxisY());
			
			ArrayList subs = new ArrayList();			
			subs.add(col.getCaption());
			
			setSubtitles(subs);
		}
		
		}
    
    
   
    /**
     * @return Returns the instanciated.
     */
    private boolean isInstanciated() {
        return instanciated;
    }
    /**
     * @param instanciated The instanciated to set.
     */
    private void setInstanciated(boolean instanciated) {
        this.instanciated = instanciated;
    }
    
    /**
     * this method should be overwrited by the programmer. You do not must to overwrite but 
     * if you want a specific element to be selected then return its name.
     * 
     * @return null. if null is returned than the first element is selected in the combobox. 
     */
    public String getDefaultSelectedItem(){
        return null;
    }
    
    public boolean sortByValue() {
      return false;
    }
    
}

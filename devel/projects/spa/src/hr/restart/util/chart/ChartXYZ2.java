/****license*****************************************************************
**   file: ChartXYZ2.java
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
package hr.restart.util.chart;

import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;

import org.jfree.chart.renderer.BarRenderer;


abstract public class ChartXYZ2 extends ChartXYZ {
  
  private JPanel optionPanel = null;
  private JPanel optionsPanel = null;
  
  int iCategoryMargin = 20;
  int iColumnMargin = -33;
  Color cFirst = Aus.halfTone(Color.blue, Color.white, 0.3f);
  Color cSecond = Aus.halfTone(Color.green, Color.black, 0.2f);
  
  JLabel colFirst = null;
  JLabel colSecond = null;
  JSlider catSlider = null;
  JSlider colSlider = null;
      

  public ChartXYZ2() throws Exception {
    super();
  }
  
  public void repaintGraph(){
    if (optionPanel != null)
      optionPanel.setVisible(getLastSelected().equals(BAR_CHART));
    super.repaintGraph();
  }

  protected boolean isVariableZ() {
    return false;
  }
  
  public void initFrame() throws Exception {
    
    super.initFrame();

    if (optionPanel != null) return;

    optionPanel = new JPanel(new BorderLayout());
    JButton btOptions = new JButton("Opcije prikaza");
    btOptions.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (optionPanel != null && optionPanel.isVisible())
          showOptions();
      }
    });
    optionPanel.add(btOptions);
    buttonsPanel.add(optionPanel);
  }
  
  void showOptions() {
    
    if (optionsPanel == null) {
      
      catSlider = new JSlider(0, 50, iCategoryMargin);
      catSlider.setPaintLabels(true);
      catSlider.setPaintTicks(true);
      catSlider.setMajorTickSpacing(10);
      catSlider.setMinorTickSpacing(5);
      catSlider.setSnapToTicks(true);
      
      colSlider = new JSlider(-80, 20, iColumnMargin);
      colSlider.setPaintLabels(true);
      colSlider.setPaintTicks(true);
      colSlider.setMajorTickSpacing(20);
      colSlider.setMinorTickSpacing(5);
      catSlider.setSnapToTicks(true);
      
      colFirst = new JLabel("               ");
      colFirst.setOpaque(true);
      colFirst.setBackground(cFirst);
      
      colSecond = new JLabel("               ");
      colSecond.setOpaque(true);
      colSecond.setBackground(cSecond);
      
      JButton selFirst = new JButton("...");
      selFirst.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Color ret = JColorChooser.showDialog(ChartXYZ2.this, "Boja prvog stupca", cFirst);
          if (ret != null) colFirst.setBackground(ret);
        }
      });
      
      JButton selSecond = new JButton("...");
      selSecond.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Color ret = JColorChooser.showDialog(ChartXYZ2.this, "Boja drugog stupca", cSecond);
          if (ret != null) colSecond.setBackground(ret);
        }
      });

      
      optionsPanel = new JPanel(new GridBagLayout());
      optionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
      GridBagConstraints c = new GridBagConstraints();
      c.weightx = 0.1;
      c.weighty = 1;
      c.gridx = 0;
      c.gridy = 0;
      c.anchor = c.LINE_START;
      c.insets = new Insets(2, 10, 2, 2);
      optionsPanel.add(new JLabel("Razmak izmeðu grupa"), c);
      
      c.gridy = 1;
      optionsPanel.add(new JLabel("Razmak izmeðu stupaca   "), c);
      
      c.gridy = 2;
      optionsPanel.add(new JLabel("Boja prvog stupca"), c);
      
      c.gridy = 3;
      optionsPanel.add(new JLabel("Boja drugog stupca"), c);
      
      c.weightx = 0.9;
      c.gridwidth = 2;
      c.gridx = 1;
      c.gridy = 0;
      c.fill = c.HORIZONTAL;
      optionsPanel.add(catSlider, c);
      
      c.gridy = 1;
      optionsPanel.add(colSlider, c);
      
      c.weightx = 0;
      c.gridwidth = 1;
      c.gridy = 2;
      c.fill = c.NONE;
      optionsPanel.add(colFirst, c);
      
      c.gridy = 3;
      optionsPanel.add(colSecond, c);
      
      c.weightx = 0.9;
      c.gridx = 2;
      c.gridy = 2;
      optionsPanel.add(selFirst, c);
      
      c.gridy = 3;
      optionsPanel.add(selSecond, c);
      
    } else {
      catSlider.setValue(iCategoryMargin);
      colSlider.setValue(iColumnMargin);
      colFirst.setBackground(cFirst);
      colSecond.setBackground(cSecond);
    }

    raInputDialog dlg = new raInputDialog();
    if (dlg.show(this, optionsPanel, "Opcije prikaza")) {
      iCategoryMargin = catSlider.getValue();
      iColumnMargin = colSlider.getValue();
      cFirst = colFirst.getBackground();
      cSecond = colSecond.getBackground();
      selectionChanged();
    }
  }
  
  protected void adjustBarRenderer(BarRenderer renderer) {
    renderer.setSeriesPaint(0, cFirst);
    renderer.setSeriesPaint(1, cSecond);
  }
    
  protected double getItemMargin() {
    return iColumnMargin / 100.0;
  }
  
  protected double getCategoryMargin() {
    return iCategoryMargin / 100.0;
  }
}

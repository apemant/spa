/****license*****************************************************************
**   file: raSwitchDialog.java
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
 * raSwitchDialog.java
 *
 * Created on 2003. listopad 24, 13:16
 */

package hr.restart.util;

import hr.restart.db.raVariant;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.raColors;
import hr.restart.swing.raGradientPainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/** .
 * @author andrej
 */
public abstract class raSwitchDialog extends hr.restart.swing.JraDialog {
  
  //parametri
  private com.borland.dx.dataset.DataSet switchSet;
  private String swColumnName;
  private String[] swPreviewCols;
  private String[] swPreviewCaptions;
  private String[] swImageDescs;
  private String swListColumn;
  private javax.swing.JComponent[] swInputFields;
  private String initialValue;
  //ui
  private boolean initialized = false;
  private JList switchList;
  private JPanel previewPanel;
  private JPanel inputPanel;
  private JLabel[] dataFields;
  private java.util.ArrayList cachedIcons;
  private raGradientPainter gradientpainter = new raGradientPainter(Color.white, Color.blue);
  private Rectangle listCellLocation;
  private Rectangle lastListCellLocation;
  private JraScrollPane switchListScroll;
  private boolean doLentPainting = false;
  private int borderSize = 5;
  private JLabel prwPanelImage;
  /** Creates a new instance of raSwitchDialog */
  public raSwitchDialog() {
    setModal(true);
  }
  
  private void jInit() {
    if (initialized) return;
    //preliminarna provjera
    if (switchSet == null) throw new IllegalArgumentException("switchSet mora biti setiran");
    if (swColumnName == null) throw new IllegalArgumentException("swColumnName mora biti setiran");
    if (switchSet.hasColumn(swColumnName) == null) throw new IllegalArgumentException("Nepoznata kolona "+swColumnName);
    getSwitchSet().open();
    switchList = new JList(
            new AbstractListModel() {
              public int getSize() {
                return getSwitchSet().getRowCount();
              }
              public Object getElementAt(int i) {
                getSwitchSet().goToRow(i);
                return raVariant.getDataSetValue(getSwitchSet(), swColumnName);
              }
            }) 
      {
        public void paint(java.awt.Graphics g) {
          super.paint(Aus.forceAntiAlias(g));
          //gradientpainter.paintGradient(g, this);
        }
      };
    
    switchList.setCellRenderer(new switchListCellRenderer());
    //switchList.setBorder(BorderFactory.createLoweredBevelBorder());
    switchList.addListSelectionListener(new ListSelectionListener() {
       //public void mouseReleased(java.awt.event.MouseEvent e) {
        public void valueChanged(ListSelectionEvent e) {
         getSwitchSet().goToRow(switchList.getSelectedIndex());
         fillPreview();
         listCellLocation = switchList.getCellBounds(switchList.getSelectedIndex(),switchList.getSelectedIndex());
         if (doLentPainting) switchList.getTopLevelAncestor().repaint();
       }
    });
    switchList.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        if (e.getClickCount() == 2) {
          startSwitch();
        }
      }
    });
    switchList.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER || e.getKeyCode() == e.VK_F10) {
          startSwitch();
        }
        if (e.getKeyCode() == e.VK_ESCAPE) {
          hide();
        }
      }
    });
    initPreview();
    switchListScroll = new JraScrollPane(switchList);
    switchListScroll.setHorizontalScrollBarPolicy(JraScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    getContentPane().add(switchListScroll, BorderLayout.WEST);
    if (previewPanel!=null) getContentPane().add(previewPanel, BorderLayout.CENTER);
    initialized = true;
    this.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        posCurrentValue();
      }
    });
  }
  
  
  private void initPreview() {
    
    if (getSwPreviewCols() == null) {
      //mozda neka lepa slikica
      previewPanel = new JPanel();
      return;
    }
    GridBagLayout gbag = new GridBagLayout();
    previewPanel = new JPanel(gbag) {
      public void paint(Graphics g) {
        super.paint(g);
        paintLent(g, this);
        gradientpainter.paintGradient(g, this);
      }
    };
    //swPreviewCols.length, 2));
    dataFields = new JLabel[getSwPreviewCols().length];
//    previewPanel.add(layoutImage(gbag, 0));
    for (int i=0; i<getSwPreviewCols().length; i++) {
      previewPanel.add(layoutLabel(gbag, i));
      previewPanel.add(layoutDataField(gbag, i));
    }
    previewPanel.setBackground(Color.white);
//    previewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, raColors.blue, raColors.blue.darker()));
    previewPanel.setPreferredSize(new Dimension(450, 0));
    BevelBorder prPanBorder = new BevelBorder(BevelBorder.RAISED,raColors.blue,raColors.blue.darker().darker());
    previewPanel.setBorder(prPanBorder);
  }

  private void paintLent(Graphics g, JComponent _this) {
    if (!doLentPainting) return;
    if (listCellLocation != null) {
      if (lastListCellLocation != null && listCellLocation.equals(lastListCellLocation)) return;
      //lastListCellLocation = listCellLocation;
      Rectangle rect = new Rectangle(0,listCellLocation.y, _this.getWidth(), listCellLocation.height);
      Color ogc = g.getColor();
      g.setColor(raColors.blue);
      ((Graphics2D)g).fill(rect);
      g.setColor(ogc);
      _this.paintComponents(g);
    }
  }
  
  private JLabel layoutImage(GridBagLayout gbag, int imgIdx) {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.fill = c.BOTH;
    prwPanelImage = new JLabel(getSwitchIcon(imgIdx));
    gbag.setConstraints(prwPanelImage, c);
    return prwPanelImage;
  }
  private JLabel layoutLabel(GridBagLayout gbag, int i) {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = i+1;
    c.fill = c.BOTH;
    JLabel jl = getLabel(i);
    gbag.setConstraints(jl, c);
    return jl;
  }
  private JLabel layoutDataField(GridBagLayout gbag, int i) {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = i+1;
    c.fill = c.BOTH;
    JLabel jl = getDataField(i);
    gbag.setConstraints(jl, c);
    return jl;
  }
  
  private JLabel getLabel(int i) {
    JLabel jl = new JLabel(getSwPreviewCaptions()[i]) {
      public void paint(java.awt.Graphics g) {
        super.paint(Aus.forceAntiAlias(g));
      }
    };
    jl.setFont(jl.getFont().deriveFont(Font.BOLD,jl.getFont().getSize()+1));
    jl.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
    jl.setHorizontalAlignment(SwingConstants.RIGHT);
//    jl.setBackground(raColors.blue);
//    jl.setOpaque(true);
    return jl;
  }
  
  private JLabel getDataField(int i) {
    JLabel jr = new JLabel() {
      public void paint(java.awt.Graphics g) {
      	super.paint(Aus.forceAntiAlias(g));
      }
    };
    jr.setFont(jr.getFont().deriveFont(Font.BOLD|Font.ITALIC,jr.getFont().getSize()+2));
//    jr.setOpaque(true);
//    jr.setBackground(raColors.blue);
    dataFields[i] = jr;
    return jr;
  }
  
  private void fillPreview() {
    if (dataFields==null) return;
    for (int i=0; i<dataFields.length; i++) {
      dataFields[i].setText(raVariant.getDataSetValue(getSwitchSet(), getSwPreviewCols()[i]).toString());
    }
    if (switchList.getSelectedIndex()>=0 && dataFields.length>0) {
      //prwPanelImage.setIcon(getSwitchIcon(switchList.getSelectedIndex()));
      try {
        ((JLabel)previewPanel.getComponent(0)).setIcon(getSwitchIcon(switchList.getSelectedIndex()));  
      } catch (Exception ex) {
        System.out.println("fillPreview ex "+ex);
      }
    }
  }
  
  public void pack() {
    jInit();
    super.pack();
    int previewHeight = previewPanel==null?0:previewPanel.getLayout().preferredLayoutSize(previewPanel).height+50;
    int switchListHeight = (switchList.getCellBounds(0,0).height*switchSet.getRowCount())+30;
    int height = switchListHeight>previewHeight?switchListHeight:previewHeight;
    System.out.println("previewHeight = "+previewHeight);
    System.out.println("switchListHeight = "+switchListHeight);
    System.out.println("height = "+height);
    setSize(getWidth(), height);
  }

  private void posCurrentValue() {
    if (initialValue == null) return;
    int selindex = 0;
    for (getSwitchSet().first(); getSwitchSet().inBounds(); getSwitchSet().next()) {
      if (raVariant.getDataSetValue(getSwitchSet(), swColumnName).equals(initialValue)) {
        switchList.setSelectedIndex(selindex);
        fillPreview();
        return;
      }
      selindex++;
    }
    getSwitchSet().first();
    fillPreview();
  }
  
 class switchListCellRenderer extends JLabel implements ListCellRenderer {
     public switchListCellRenderer() {
         setOpaque(true);
     }
     public java.awt.Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         setText(raVariant.getDataSetValue(switchSet, getSwListColumn()).toString());
         hr.restart.dlgExit.setSelectedUI(this, isSelected);
         //setBorder(null);
         setFont(list.getFont().deriveFont(Font.BOLD|Font.ITALIC,list.getFont().getSize()+1));
         setIcon(getSwitchIcon(index));
         return this;
     }
 }
 
 private Icon getSwitchIcon(int index) {
   if (swImageDescs == null) return null;
   if (index < swImageDescs.length) {
     return getCachedIcon(swImageDescs[index],index);
   }
   return getCachedIcon(swImageDescs[0],index);
 }
 
 private Icon getCachedIcon(String imageDesc, int index) {
   if (cachedIcons == null) cachedIcons = new java.util.ArrayList();
   if (cachedIcons.size() != getSwitchSet().getRowCount()) {
     cachedIcons.clear();
     for (int i=0; i<getSwitchSet().getRowCount(); i++) cachedIcons.add("null");
   }
   if (cachedIcons.get(index).toString().equals("null")) {
     cachedIcons.add(index, raImages.getImageOrModuleIcon(imageDesc));
   }
   return (Icon)cachedIcons.get(index);
 }
 
 private void startSwitch() {
   if (!validateInput()) return;
   getSwitchSet().goToRow(switchList.getSelectedIndex());
   if (doSwitch(initialValue, raVariant.getDataSetValue(getSwitchSet(), getSwColumnName()))) {
     this.hide();
   } else {
     JOptionPane.showMessageDialog(this, "Promjena nije uspjela!");
   }
 }
 
 /** Overridati da bi se provjerio unos u swInputFields
  * @return da li je validacija uspjesna
  */
 public boolean validateInput() {
   return true;
 }
 
  /** Izvrsava se na ok tipku na dialogu
   * @param oldvalue stara vrijednost swColumnName u switchSetu
   * @param newValue nova vrijednost swColumnName u switchSetu
   * @return da li je uspio promijeniti
   */  
  public abstract boolean doSwitch(Object oldvalue, Object newValue);
  
  //seteri & terijeri
  
  /** vraca prije setiranu vrijednost parametra
   * @return dataset u kojem se nalazi podatak za switchanje
   * npr. useri ili knjig
   */  
  public com.borland.dx.dataset.DataSet getSwitchSet() {
    return switchSet;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return ime kolone koja oznacava switchSet
   */  
  public String getSwColumnName() {
    return swColumnName;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return kolone iz koje se trebaju vidjeti na previewu
   */  
  public String[] getSwPreviewCols() {
    return swPreviewCols;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return naslovi od swPreviewCols
   */  
  public String[] getSwPreviewCaptions() {
    if (getSwPreviewCols() != null && swPreviewCaptions == null) {
      if (getSwitchSet()==null) return null;
      swPreviewCaptions = new String[getSwPreviewCols().length];      
      for (int i=0; i<getSwPreviewCols().length; i++) {
        swPreviewCaptions[i] = getSwitchSet().getColumn(getSwPreviewCols()[i]).getCaption();
      }
    }
    return swPreviewCaptions;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return ili iz raImages ili kao URL
   */  
  public String[] getSwImageDescs() {
    return swImageDescs;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return komponente u koje se treba upisati dodatni parametar (npr. password)
   */  
  public javax.swing.JComponent[] getSwInputFields() {
    return swInputFields;
  }
  /** vraca prije setiranu vrijednost parametra
   * @return kolonu iz dataseta ciju vrijednost prikazuje u listi
   */  
  public String getSwListColumn() {
    if (swListColumn == null) {
      swListColumn = swPreviewCols==null?swColumnName:swPreviewCols[0];
    }
    if (switchSet != null && switchSet.hasColumn(swListColumn) == null) {
      swListColumn = swColumnName;
    }
    return swListColumn;
  }

  
  
  /** setira vrijednost parametra
   * @param p dataset u kojem se nalazi podatak za switchanje
   * npr. useri ili knjig
   */ 
  public void setSwitchSet(com.borland.dx.dataset.DataSet p) {
    switchSet = p;
  }
  
  /** setira vrijednost parametra
   * @param p ime kolone koja oznacava switchSet
   */ 
  public void setSwColumnName(String p) {
    swColumnName = p;
  }
  
  /** setira vrijednost parametra
   * @param p kolone iz koje se trebaju vidjeti na previewu
   */  
  public void setSwPreviewCols(String[] p) {
    swPreviewCols = p;
  }
  
  /** setira vrijednost parametra
   * @param p naslovi od swPreviewCols
   */  
  public void setSwPreviewCaptions(String[] p) {
    swPreviewCaptions = p;
  }
  
  /** setira vrijednost parametra
   * @param p ili iz raImages ili kao URL
   */  
  public void setSwImageDescs(String[] p) {
    swImageDescs = p;
  }

  /** setira vrijednost parametra
   * @param p komponente u koje se treba upisati dodatni parametar (npr. password)
   */    
  public void setSwInputFields(javax.swing.JComponent[] p) {
    swInputFields = p;
  }
  
  /** setira vrijednost parametra
   * @param p kolona iz dataseta ciju vrijednost prikazuje u listi
   */  
  public void setSwListcolumn(String p) {
    swListColumn = p;
  }
  
  /** setira inicijalnu vrijednost swColumnName kako bi ta bila selektirana
   * ta vrijednost ce biti oldvalue u boolean doSwitch(Object oldvalue, Object newValue);
   * @param p nicijalna vrijednost swColumnName 
   */
  public void setInitialValue(String p) {
    initialValue = p;
  }
  
  ///static utilities
  public static String[] translateImageProperties(String file, com.borland.dx.dataset.DataSet ds, String keyColName) {
    Properties imgprops = FileHandler.getProperties(file);
    ds.open();
    ds.first();
    String[] imgDesc = new String[ds.getRowCount()];
    int i = 0;
    for (ds.first(); ds.inBounds(); ds.next()) {
      imgDesc[i] = imgprops.getProperty(raVariant.getDataSetValue(ds, keyColName).toString(),"gk");
      i++;
    }
    return imgDesc;
  }
}

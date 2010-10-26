/****license*****************************************************************
**   file: raAbstractShortcutContainer.java
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
package hr.restart.help;

import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.MenuTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


/**
 * @author Andrej
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class raAbstractShortcutContainer
    extends JList
    implements raShortcutContainer {

  private String file;
  private Properties props;
  private MenuTree menuTree;
//  private raShortcutItem selectedItem;
  private raShortcutContainer shortcutTarget;
  private shortcutModel model;
  
  public raAbstractShortcutContainer() {
    jInit();
  }
  public raAbstractShortcutContainer(String propsFile) {
    jInit();
    setFile(propsFile);
  }
  public raAbstractShortcutContainer(MenuTree _menutree,String propsFile) {
    jInit();
    setMenuTree(_menutree);
    setFile(propsFile);
  }
  public raAbstractShortcutContainer(MenuTree _menutree,String propsFile, raShortcutContainer _target) {
    jInit();
    setShortcutTarget(_target);
    setMenuTree(_menutree);
    setFile(propsFile);
  }
  private void jInit() {
//    setBackground(new JPanel().getBackground());
    setCellRenderer(new shortcutRenderer());
    setModel(model = new shortcutModel());
    addMouseListener(new shortcutMouseListener());
  }
  /**
   * @see hr.restart.help.raShortcutContainer#getSelectedShortcutItem()
   */
  public raShortcutItem getSelectedShortcutItem() {
    return (raShortcutItem)getSelectedValue();
  }

  /**
   * Sets the file.
   * @param file The file to set
   */
  public void setFile(String _file) {
    file = _file;
    setProps(FileHandler.getProperties(file));
  }
  public String getFile() {
    return file;
  }

  /**
   * Sets the props.
   * @param props The props to set
   */
  public void setProps(Properties _props) {
//System.out.println("setProps("+_props);
    props = _props;
//    initFromProps();
  }
  public void removeAllItems() {
    listelements.clear();
    refresh();
    if (isShortcutTarget() && props != null) {
      props.clear();
    }
  }
  public void removeAllItems_badAndUgly() {
    for (int i=0;i<getComponentCount();i++) {
      Component c = getComponent(i);
System.out.println("raAbstractShortcutContainer.removeAllItems:is instanceof raShortcutItem "+c);
      if (c instanceof raShortcutItem) {
System.out.println("raAbstractShortcutContainer.removeAllItems: true (instanceof raShortcutItem)"+c);
        raShortcutItem s = (raShortcutItem)c;
        removeItem(s);
      }
    }
  }
  void initFromProps() {
    if (props == null) return;
    if (menuTree == null) return;
    //removeAllItems();
    Object[] strings = props.keySet().toArray();
    for (int i = 0;i<strings.length;i++) {
      String indx = strings[i].toString();
      raShortcutItem item = raShortcutFileHandler.retreiveItem(menuTree,indx);
//System.out.println("item "+item+" retreived...");
      if (item != null) addItem(item);
    }
  }

  public void addItem(raShortcutItem it) {
    if (it == null) return;
    if (contains(it)) return;
    listelements.add(it);
    refresh();
    if (isShortcutTarget() && props != null) {
      props.put(it.getIndex(),it.getText());
    }
  }

  public boolean contains(raShortcutItem item) {
    //return Arrays.asList(getComponents()).contains(item);
    return listelements.contains(item);
  }
  /**
   * @see hr.restart.help.raShortcutContainer#removeItem(raShortcutItem)
   */
  public void removeItem(raShortcutItem item) {
//System.out.println("raAbstractShortcutContainer.removeItem:contains item"+item.getIndex());
    if (!contains(item)) return;
//System.out.println("raAbstractShortcutContainer.removeItem:removing item"+item.getIndex());
    listelements.remove(item);
    refresh();
    if (isShortcutTarget() && props != null) {
      props.remove(item.getIndex());
    }
  }

  /**
   * Method refresh.
   */
  private void refresh() {
    model.dataChanged();
    /*revalidate();
    repaint();*/
    //setListData(listelements.toArray());
  }

  /**
   * Returns the menuTree.
   * @return MenuTree
   */
  public MenuTree getMenuTree() {
    return menuTree;
  }


  /**
   * Sets the menuTree.
   * @param menuTree The menuTree to set
   */
  public void setMenuTree(MenuTree _menuTree) {
    menuTree =_menuTree;
//    initFromProps();
  }
  public raShortcutItem[] getItems() {
    return (raShortcutItem[])listelements.toArray(new raShortcutItem[0]);
  }
  /**
   * @see hr.restart.help.raShortcutContainer#getShortcutTarget()
   */
  public raShortcutContainer getShortcutTarget() {
    return shortcutTarget;
  }

  public void setShortcutTarget(raShortcutContainer _shortcutTarget) {
    shortcutTarget = _shortcutTarget;
  }

  public void saveSettings() {
//    System.out.println("saveSettings");
    if (props == null) {
//      System.out.println("props == null");
      return;
    }
    if (file == null) {
//      System.out.println("file == null");
      return;
    }
//    System.out.println("FileHandler.storeProperties("+file+","+props+");");
    FileHandler.storeProperties(file,props);
  }

  public int getItemCount() {
    return listelements.size();
  }
  
  public String toString() {
    return super.toString() + " elements: " + listelements;
  }
  
  public LinkedList listelements = new LinkedList();
  
  class shortcutModel extends AbstractListModel {
    public void dataChanged() {super.fireContentsChanged(this, 0, getSize());}
    public int getSize() { return listelements.size(); }
    public Object getElementAt(int i) { return listelements.get(i); }
  }
  
  //private Border lowBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
  //private Border raiseBroder = null//BorderFactory.createBevelBorder(BevelBorder.RAISED);
  public static java.awt.Color listSelectionBackground = new java.awt.Color(195,212,232);
  public static java.awt.Color listSelectionBorder = listSelectionBackground.darker().darker();
  class shortcutRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
      raShortcutItem item = (raShortcutItem) listelements.get(index);
      int bordthick = 1;
      if (isSelected && item.isEnabled()) {
        //item.setBorder(lowBorder);
        setBorder(BorderFactory.createLineBorder(listSelectionBorder,bordthick));
        setBackground(listSelectionBackground);
        setForeground(java.awt.Color.black);
        //item.setForeground(list.getForeground());
      } else {
        //item.setBorder(raiseBroder);
        setBorder(BorderFactory.createEmptyBorder(bordthick,bordthick,bordthick,bordthick));
        setBackground(list.getBackground());
        if (item.isEnabled())
          setForeground(list.getForeground());
        else setForeground(Color.gray.brighter());
      }
      setOpaque(true);
      setIcon(item.getIcon());
      setText(item.getText());
      if (getFont() != item.getFont())
        setFont(item.getFont());
      return this;
    }
    
    public void paint(java.awt.Graphics g) {
      super.paint(Aus.forceAntiAlias(g));
    }
  }

  class shortcutMouseListener extends MouseAdapter {
    /**
     * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
      if (e.getClickCount() == 2) {
        if (getSelectedShortcutItem() != null)
          getSelectedShortcutItem().actionPerformed(null);
      }
    }
  }
}

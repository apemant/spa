/****license*****************************************************************
**   file: raShortcutFileHandler.java
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

import hr.restart.util.FileHandler;
import hr.restart.util.MenuTree;
import hr.restart.util.raLLFrames;
import hr.restart.util.startFrame;

import java.awt.Container;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

public class raShortcutFileHandler {

  protected raShortcutFileHandler() {
  }

  /**
   * Upisuje u fileName informaciju o menuitemu u obriku properties filea
   */
  public static void writeOption(String fileName, JMenuItem jmitem) {
    writeOption(fileName,getOptionIndex(jmitem));
  }
  public static void writeOption(String fileName, String index) {
    Properties props = FileHandler.getProperties(fileName);
    if (props == null) props = new Properties();
    props.put(index,"raShortcutItem");
  }

  public static String getOptionIndex(JMenuItem jmitem) {
    String[] propStr = writeParentIndexes(jmitem);
    String index = propStr[0].concat(propStr[1]);
    return index;
  }
  private static TreeMap indmap = new TreeMap();
  private static String[] writeParentIndexes(JMenuItem jmitem) {
    JPopupMenu mn;
    indmap.clear();
    String mainFrClass = getParentPositions(indmap,jmitem,jmitem);
    int id = 0;
    String indexes = "";
    for (Iterator i = indmap.keySet().iterator(); i.hasNext(); ) {
      Object key = i.next();
      //ret[id] = ((Integer)indmap.get(key)).intValue();
      indexes = indexes.concat(",").concat(indmap.get(key).toString());
      id = id+1;
    }
    return new String[] {mainFrClass,indexes};
  }
  public static raShortcutItem retreiveItem(MenuTree menuTree,String index) {
    StringTokenizer toks = new StringTokenizer(index,",");
    String startfrClass = toks.nextToken();
    int[] ints = new int[toks.countTokens()];
    int ix = 0;
    do {
      int _tok = Integer.parseInt(toks.nextToken());
      ints[ix] = _tok;
      ix++;
    } while (toks.hasMoreTokens());
    JMenuItem jmitem = retreiveItem(startfrClass,ints);
    if (jmitem == null) return null;
    MenuTree.treeOption opt = menuTree.getNewTreeOption(jmitem,s_fr);
    return menuTree.getShortcutItem(opt);
  }
  private static startFrame s_fr;
  private static JMenuItem retreiveItem(String mainFrClass, int[] indexes) {
    s_fr = null;
    LinkedList sfrs = raLLFrames.getRaLLFrames().getStartFrames();
    for (Iterator i = sfrs.iterator(); i.hasNext(); ) {
      Object item = i.next();
      if (item.getClass().getName().equals(mainFrClass)) {
        s_fr = (startFrame)item;
        break;
      }
    }
    if (s_fr != null) {
      MenuElement item = s_fr.getRaJMenuBar();
      for (int i = indexes.length-1; i >= 0; i--) {
        item = item.getSubElements()[indexes[i]];
      }
      return (JMenuItem)item;
    } //else System.out.println("startframe not found");
    return null;
  }

  private static String getStartFrameClass(JMenuItem jmitem) {
    String sfcl = "";
    LinkedList ll = raLLFrames.getRaLLFrames().getStartFrames();
    for (Iterator i = ll.iterator(); i.hasNext(); ) {
      startFrame stFr = (startFrame)i.next();
      JMenuBar jmbar = stFr.getRaJMenuBar();
      if (jmbar != null) {
        if (isElementIn(jmbar,jmitem)) {
          sfcl = stFr.getClass().getName();
        }
      }
    }
    return sfcl;
  }

  private static boolean isElementIn(MenuElement menubar,MenuElement item) {
    MenuElement[] elems = menubar.getSubElements();
    for (int i = 0; i < elems.length; i++) {
      if (elems[i].equals(item)) return true;
      return isElementIn(elems[i],item);
    }
    return false;
  }

  private static String getParentPositions(TreeMap rmap, Container item, JMenuItem jmitem) {
    if (item == null) {
      return getStartFrameClass(jmitem);
    }
    Container parent;
    if (item instanceof MenuElement) {
      if (item instanceof JPopupMenu) parent = (Container)((JPopupMenu)item).getInvoker();
      else parent = item.getParent();
    } else if (item instanceof startFrame) {
      return item.getClass().getName();
    } else {
      return getParentPositions(rmap,item.getParent(),jmitem);
    }

    if (parent == null) {
      return getStartFrameClass(jmitem);
    }
    if (parent instanceof MenuElement) {
      MenuElement[] elements = ((MenuElement)parent).getSubElements();
      int idx = -1;
      for (int i = 0; i < elements.length; i++) {
        if (elements[i].equals(item)) {
          idx = i;
          break;
        }
      }
      if (idx<0) return null;
      int mapidx = rmap.size();
      rmap.put(new Integer(mapidx),new Integer(idx));
      return getParentPositions(rmap,parent,jmitem);
    } else {
      return getParentPositions(rmap,parent.getParent(),jmitem);
    }
  }

}
/****license*****************************************************************
**   file: raSearchSPA.java
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
 * raSearchSPA.java
 *
 * Created on 2004. lipanj 21, 15:00
 */

package hr.restart.help;

import java.util.HashSet;
import java.util.Iterator;
/**
 *- da trazi po menijima
 *- da trazi po helpu :)
 *- da trazi po raKeyAkcijama i raNavAkcijama 
 *- da trazi po (buducim) taskovima
 * @author  andrej
 */
public class raSearchSPA {
  
  
  protected raSearchSPA() {
  }
  private static HashSet menus;
  private static HashSet actions;
  private static HashSet helps;
  /**
   * Pretrazuje aplikaciju po zadanom stringu
   * @param criteria - zadani string
   * @param menus - po menijuma
   * @param actions - po akcijama u (instanciranim?) ekranima
   * @param help - po help html-u
   * @return array shortcutItema prigodnima za prikazivanje u shortcutContaineru
   */  
  public static raAbstractShortcutContainer search(String criteria, boolean searchmenus, boolean searchactions, boolean searchhelp) {
    clearItems();
    if (searchmenus) findMenus(criteria);
    if (searchactions) findActions(criteria);
    if (searchhelp) findHelp(criteria);
    raAbstractShortcutContainer ret = new raAbstractShortcutContainer() {
      public boolean isShortcutTarget() {return false;}
    };
    ret.setShortcutTarget(raUserDialog.getInstance().getUserPanel().getDefaultShortcutTarget());
    addSet(ret, menus);
    addSet(ret, actions);
    addSet(ret, helps);
    return ret;
  }
  private static void addSet(raAbstractShortcutContainer c, HashSet set) {
    if (set != null) {
      for (Iterator i = set.iterator(); i.hasNext(); ) {
        raShortcutItem rsci = (raShortcutItem)i.next();
        c.addItem(rsci);
      }
    }    
  }
  private static void clearItems() {
    menus = null;
    actions = null;
    helps = null;
  }
  private static void findHelp(String _c) {
    
  }
  private static void findMenus(String _c) {
    menus = new HashSet();
    HashSet allmenus = raUserDialog.getInstance().getUserPanel().getMenuTree().getAllMenuShortcutItems();
    for (Iterator i = allmenus.iterator(); i.hasNext(); ) {
      raShortcutItem item = (raShortcutItem)i.next();
      if (item.getText().toLowerCase().indexOf(_c.toLowerCase()) >=0) {
        menus.add(item);
      }
    }
  }
  private static void findActions(String _c) {
  }
}

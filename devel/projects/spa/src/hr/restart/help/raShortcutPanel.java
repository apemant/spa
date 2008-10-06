/****license*****************************************************************
**   file: raShortcutPanel.java
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

import hr.restart.util.MenuTree;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * @author Andrej
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class raShortcutPanel extends JPanel {
  private boolean shcutTarget = true;
  private raAbstractShortcutContainer shortcuts;
  private raShortcutNavBar navBar;

  public raShortcutPanel(MenuTree _menutree,String propsFile, raShortcutContainer _target,boolean isTarget) {
//    if (_target == null && !isTarget) throw new IllegalArgumentException();
    setShcutTarget(isTarget);
    jInit();
    if (_target==null) _target = shortcuts;
    setShortcutTarget(_target);
    shortcuts.setMenuTree(_menutree);
    shortcuts.setFile(propsFile);
  }
  private void jInit() {
    shortcuts = new raAbstractShortcutContainer() {
      public boolean isShortcutTarget() {
        return shcutTarget;
      }
    };
    navBar = new raShortcutNavBar(shortcuts);
    setLayout(new BorderLayout());
//    add(navBar,BorderLayout.NORTH);
    add(shortcuts,BorderLayout.CENTER);
  }
  public raShortcutNavBar getNavBar() {
    return navBar;
  }
  /**
   * Sets the shcutTarget.
   * @param shcutTarget The shcutTarget to set
   */
  public void setShcutTarget(boolean _shcutTarget) {
    shcutTarget = _shcutTarget;
  }


  /**
   * Returns the shcutTarget.
   * @return boolean
   */
  public boolean isShcutTarget() {
    return shcutTarget;
  }


  /**
   * Returns the shortcuts.
   * @return raAbstractShortcutContainer
   */
  public raAbstractShortcutContainer getShortcuts() {
    return shortcuts;
  }


  /**
   * Sets the shortcutTarget.
   * @param shortcutTarget The shortcutTarget to set
   */
  public void setShortcutTarget(raShortcutContainer _shortcutTarget) {
    shortcuts.setShortcutTarget(_shortcutTarget);
  }
}

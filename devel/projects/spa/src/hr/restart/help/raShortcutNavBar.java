/****license*****************************************************************
**   file: raShortcutNavBar.java
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

import hr.restart.util.raImages;
import hr.restart.util.raJPNavContainer;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTree;

/**
 * @author Andrej
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class raShortcutNavBar extends raJPNavContainer {
  raNavAction rnvCreateShortcut = new raNavAction("Napravi kraticu",raImages.IMGALLFORWARD,KeyEvent.VK_K,KeyEvent.CTRL_MASK) {
    public void actionPerformed(ActionEvent e) {
      createShortcut();
    }
  };
  raNavAction rnvMoveUp = new raNavAction("Premjesti iznad",raImages.IMGUP,KeyEvent.VK_UP,KeyEvent.CTRL_MASK) {
    public void actionPerformed(ActionEvent e) {
      moveUp();
    }
  };
  raNavAction rnvMoveDown = new raNavAction("Premjesti ispod",raImages.IMGDOWN,KeyEvent.VK_DOWN,KeyEvent.CTRL_MASK) {
    public void actionPerformed(ActionEvent e) {
      moveDown();
    }
  };
  raNavAction rnvOpen = new raNavAction("Otvori",raImages.IMGOPEN,KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      openShortcut();
    }
  };
  raNavAction rnvDelete = new raNavAction("Obriši kraticu",raImages.IMGUNCHECK, KeyEvent.VK_F3) {
    public void actionPerformed(ActionEvent e) {
      deleteShortcut();
    }
  };
  private raShortcutContainer shortcutContainer;

  public raShortcutNavBar(raShortcutContainer shCont) {
    shortcutContainer = shCont;
    jInit();
  }
  private void jInit() {
    boolean b = shortcutContainer.isShortcutTarget();
    boolean tree = shortcutContainer instanceof JTree;
    if (!b) addOption(rnvCreateShortcut);
//    if (!tree) addOption(rnvMoveUp);
//    if (!tree) addOption(rnvMoveDown);
    addOption(rnvOpen);
    if (b) addOption(rnvDelete);
  }
  private void createShortcut() {
    toggleItem(true);
  }
  private void moveUp() {
  }
  private void moveDown() {
  }

  private void openShortcut() {
    if (shortcutContainer == null) return;
    raShortcutItem item = shortcutContainer.getSelectedShortcutItem();
    if (item==null) return;
    item.actionPerformed(null);
  }

  private void deleteShortcut() {
    toggleItem(false);
  }

  private void toggleItem(boolean add) {
    if (shortcutContainer == null) return;
    raShortcutItem item = shortcutContainer.getSelectedShortcutItem();
    if (item==null) return;
    raShortcutContainer target = shortcutContainer.getShortcutTarget();
    if (target == null) return;
    if (add) target.addItem(item);
    else target.removeItem(item);
  }
  /**
   * Returns the shortcutContainer.
   * @return raShortcutContainer
   */
  public raShortcutContainer getShortcutContainer() {
    return shortcutContainer;
  }

  /**
   * Sets the shortcutContainer.
   * @param shortcutContainer The shortcutContainer to set
   */
  public void setShortcutContainer(raShortcutContainer shortcutContainer) {
    this.shortcutContainer = shortcutContainer;
  }


}

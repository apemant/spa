/****license*****************************************************************
**   file: AutomatedMenu.java
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
package hr.restart.sisfun;

import hr.restart.util.raLoader;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raMatPodaci;
import hr.restart.util.startFrame;

import javax.swing.JMenuItem;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomatedMenu extends AutomatedTask {
  private startFrame sf;

  public AutomatedMenu(startFrame sf) {
    this.sf = sf;
  }

  public void clickMenuItem(String menuItem) throws InterruptedException {
    JMenuItem mi = findMenuItem(sf.getJMenuBar(), menuItem);
    if (mi == null)
      throw new AutomationException(this, "Can't find menuitem: "+menuItem);
    clickMenu(sf.getJMenuBar(), mi);
  }

  public raMasterDetail findMasterDetail(String md) {
    Object o = raLoader.load(md);
    if (!(o instanceof raMasterDetail))
      throw new AutomationException(this, "Class is not raMasterDetail: "+md);
    return (raMasterDetail) o;
  }

  public raMatPodaci findMatPodaci(String mp) {
    Object o = raLoader.load(mp);
    if (!(o instanceof raMatPodaci))
      throw new AutomationException(this, "Class is not raMatPodaci: "+mp);
    return (raMatPodaci) o;
  }
}


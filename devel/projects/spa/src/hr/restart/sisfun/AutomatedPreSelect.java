/****license*****************************************************************
**   file: AutomatedPreSelect.java
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

import hr.restart.util.OKpanel;
import hr.restart.util.PreSelect;

import java.awt.Container;
import java.awt.event.KeyEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomatedPreSelect extends AutomatedTask {
  private PreSelect ps;
  private AutomatedPanel ap;

  public AutomatedPreSelect(PreSelect ps) {
    this.ps = ps;
    ap = new AutomatedPanel(ps.getSelPanel());
  }

  public void setAnything(String column, String value) throws InterruptedException {
    ap.setColumnAware(column, value, rand.nextBoolean());
  }

  public void setTextField(String column, String value) throws InterruptedException {
    ap.setTextField(column, value, rand.nextBoolean());
  }

  public void setNavField(String column, String value) throws InterruptedException {
    ap.setNavField(column, value, rand.nextBoolean());
  }

  public void setCheckBox(String column, String value) throws InterruptedException {
    ap.setCheckBox(column, value, rand.nextBoolean());
  }

  public void setRadioButton(String column, String value) throws InterruptedException {
    ap.setRadioButton(column, value);
  }

  public void setComboBox(String column, String value) throws InterruptedException {
    ap.setComboBox(column, value);
  }

  public void accept() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_F10);
    else clickComponent(findOKpanel().jBOK);
  }

  public void cancel() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_ESCAPE);
    else clickComponent(findOKpanel().jPrekid);
  }

  private OKpanel findOKpanel() throws InterruptedException {
    Container c = ps.getPreSelDialog().getContentPane();
    for (int i = 0; i < c.getComponentCount(); i++)
      if (c.getComponent(i) instanceof OKpanel)
        return (OKpanel) c.getComponent(i);
    return null;
  }
}

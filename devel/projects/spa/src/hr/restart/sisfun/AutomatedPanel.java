/****license*****************************************************************
**   file: AutomatedPanel.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raComboBox;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.JPanel;

import com.borland.dx.dataset.ColumnAware;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomatedPanel extends AutomatedTask {
  private JPanel pan;

  public AutomatedPanel(JPanel pan) {
    this.pan = pan;
  }

  public void setColumnAware(String column, String value, boolean tab) throws InterruptedException {
    ColumnAware ca = findColumnAware(pan, column);
    if (ca instanceof JlrNavField)
      lookupNavField(column, value, value);
    else if (ca instanceof JraTextField)
      setTextField(column, value, tab);
    else if (ca instanceof JraCheckBox)
      setCheckBox(column, value, tab);
    else if (ca instanceof JraRadioButton)
      setRadioButton(column, value);
    else if (ca instanceof raComboBox)
      setComboBox(column, value);
    else {
      new AutomationException(this, "Invalid column "+column).printStackTrace();
      System.err.println(ca);
    }
  }

  public void setNavField(String column, String value, boolean tab) throws InterruptedException {
    JlrNavField nav = findNavField(pan, column);
    if (nav == null)
      throw new AutomationException(this, "Can't find navfield for column: "+column);
    if (tab) tabToComponent(nav);
    else if (findFocusedComponent() != nav) clickComponent(nav, 20);
    enter(value);
  }

  public void setTextField(String column, String value, boolean tab) throws InterruptedException {
    JraTextField tf = findTextField(pan, column);
    if (tf == null)
      throw new AutomationException(this, "Can't find textfield for column: "+column);
    if (tab) tabToComponent(tf);
    else if (findFocusedComponent() != tf) clickComponent(tf, 20);
    enter(value);
  }

  public void setCheckBox(String column, String value, boolean tab) throws InterruptedException {
    JraCheckBox cb = findCheckBox(pan, column);
    if (cb == null)
      throw new AutomationException(this, "Can't find checkbox for column: "+column);
    if (cb.getSelectedDataValue().equalsIgnoreCase(value) && cb.isSelected() ||
        cb.getUnselectedDataValue().equalsIgnoreCase(value) && !cb.isSelected()) return;
    if (!tab) clickComponent(cb);
    else {
      tabToComponent(cb);
      type(KeyEvent.VK_SPACE);
    }
    if (!cb.getSelectedDataValue().equalsIgnoreCase(value) &&
        !cb.getUnselectedDataValue().equalsIgnoreCase(value))
      throw new AutomationException(this, "Invalid value ("+value+") for checkbox: "+column);
  }

  public void setRadioButton(String column, String value) throws InterruptedException {
    JraRadioButton rb = findRadioButton(pan, column);
    if (rb == null)
      throw new AutomationException(this, "Can't find radio button for column: "+column);
    for (Enumeration e = rb.getButtonGroup().getElements(); e.hasMoreElements() &&
         !rb.getSelectedValue().equalsIgnoreCase(value); rb = (JraRadioButton) e.nextElement());
    if (!rb.getSelectedValue().equalsIgnoreCase(value))
      throw new AutomationException(this, "Invalid value ("+value+" for radio button: "+column);
    clickComponent(rb);
  }

  public void setComboBox(String column, String value) throws InterruptedException {
    raComboBox cb = findComboBox(pan, column);
    if (cb == null)
      throw new AutomationException(this, "Can't find combo box for column: "+column);
    String lastVal = "";
    while (!cb.getDataValue().equalsIgnoreCase(value) && !lastVal.equals(cb.getDataValue())) {
      lastVal = cb.getDataValue();
      type(KeyEvent.VK_UP);
    }
    lastVal = "";
    while (!cb.getDataValue().equalsIgnoreCase(value) && !lastVal.equals(cb.getDataValue())) {
      lastVal = cb.getDataValue();
      type(KeyEvent.VK_DOWN);
    }
    if (!cb.getDataValue().equalsIgnoreCase(value))
      throw new AutomationException(this, "Invalid value ("+value+" for combo box: "+column);
  }

  public void lookupNavField(String column, String value) throws InterruptedException {
    lookupNavField(column, value, null);
  }

  public void lookupNavField(String column, String value, String init) throws InterruptedException {
    JlrNavField jlr = findNavField(pan, column);
    if (jlr == null)
      throw new AutomationException(this, "Can't find navfield for column: "+column);
    boolean few = jlr.getDataSet().isOpen() && jlr.getDataSet().rowCount() < 30;
    JraButton b = jlr.getNavButton() instanceof JraButton ? (JraButton) jlr.getNavButton() : null;
    if (init == null && few && b != null && b.isEnabled()) clickComponent(b);
    else {
      tabToComponent(jlr);
      if (init != null && init.length() > 0) type(init);
      type(KeyEvent.VK_F9);
    }
    if ((few = isLookupActivated()) && !navigateLookup(column, value) ||
        !few && jlr.getText().length() == 0)
      throw new AutomationException(this, "Navigation error for column: "+column);
  }

  private boolean isLookupActivated() throws InterruptedException {
    return getLookupFrame() != null;
  }

  private lookupFrame getLookupFrame() throws InterruptedException {
    for(Component p = findFocusedComponent(); p != null; p = p.getParent())
      if (p instanceof lookupFrame) return (lookupFrame) p;
    return null;
  }

  private OKpanel findLookupFrameOK() throws InterruptedException {
    Container c = getLookupFrame().getContentPane();
    for (int i = 0; i < c.getComponentCount(); i++)
      if (c.getComponent(i) instanceof OKpanel)
        return (OKpanel) c.getComponent(i);
    return null;
  }

  private boolean navigateLookup(String column, String value) throws InterruptedException {
    int lastRow = -14;
    while (lookupData.raSetView.getRow() != lastRow)
      if (lookupData.raSetView.getString(column).equalsIgnoreCase(value)) {
//        if (rand.nextBoolean()) type(KeyEvent.VK_ENTER);
        clickComponent(findLookupFrameOK().jBOK);
        delay(getActionDelay());
        checkReturnWindow();
        return true;
      } else {
        lastRow = lookupData.raSetView.getRow();
        type(KeyEvent.VK_DOWN);
      }

    if (rand.nextBoolean()) type(KeyEvent.VK_ESCAPE);
    else clickComponent(findLookupFrameOK().jPrekid);
    delay(getActionDelay());
    checkReturnWindow();
    return false;
  }


  private void checkReturnWindow() throws InterruptedException {
    Container orig = pan.getTopLevelAncestor();
    if (findFocusedWindow() == orig) return;
    new AutomationException(this, "Invalid return focus after lookup").printStackTrace();
    ((Window) orig).toFront();
  }
}

/****license*****************************************************************
**   file: AutomatedMatPodaci.java
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

import hr.restart.util.raMatPodaci;

import java.awt.Container;
import java.awt.event.KeyEvent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomatedMatPodaci extends AutomatedTask {
  protected raMatPodaci mp;
  private AutomatedPanel ap;

  protected AutomatedMatPodaci() {}
  protected void setMatPodaci(raMatPodaci mp) {
    this.mp = mp;
    ap = new AutomatedPanel(mp.getRaDetailPanel());
  }

  public AutomatedMatPodaci(raMatPodaci mp) {
    setMatPodaci(mp);
  }

  public raMatPodaci getMatPodaci() {
    return mp;
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

  public void lookUp(String column, String value, String init) throws InterruptedException {
    ap.lookupNavField(column, value, init);
  }

  public void lookUp(String column, String value) throws InterruptedException {
    ap.lookupNavField(column, value);
  }

  public boolean accept() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_F10);
    else clickComponent(mp.getOKpanel().jBOK);
    delay(getActionDelay() * 2);
    if (!isOrigWindow()) {
      type(KeyEvent.VK_ENTER);
      delay(getActionDelay());
      if (!isOrigWindow()) {
        type(KeyEvent.VK_ESCAPE);
        throw new AutomationException("Unexpected error");
      }
      type(KeyEvent.VK_ESCAPE);
      delay(getActionDelay());
      return false;
    }
    return true;
  }

  public void cancel() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_ESCAPE);
    else clickComponent(mp.getOKpanel().jPrekid);
    delay(getActionDelay() * 2);
  }

  public void start() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_F2);
    else clickComponent(mp.getNavBar().getNavContainer().getComponent(0));
    delay(getActionDelay() * 2);
  }

  public void edit() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_F4);
    else clickComponent(mp.getNavBar().getNavContainer().getComponent(1));
    delay(getActionDelay() * 2);
  }

  public void delete() throws InterruptedException {
    if (rand.nextInt(10) < 3) type(KeyEvent.VK_F3);
    else clickComponent(mp.getNavBar().getNavContainer().getComponent(2));
    delay(getActionDelay() * 2);
    Container orig = mp.getNavBar().getNavContainer().getTopLevelAncestor();
    if (!isOrigWindow()) {
      type(KeyEvent.VK_ENTER);
      if (!isOrigWindow()) {
        type(KeyEvent.VK_ESCAPE);
        throw new AutomationException("Unexpected error");
      }
    }
    delay(getActionDelay() * 2);
  }

  public static void compareDataSets(DataSet one, DataSet other) {
    String[] cols = one.getColumnNames(one.getColumnCount());
    boolean err = false;
    for (int i = 0; i < cols.length; i++) {
      if (other.hasColumn(cols[i]) == null ||
          other.getColumn(cols[i]).getDataType() != one.getColumn(cols[i]).getDataType()) {
        System.err.print(" invalid column "+cols[i]);
        err = true;
      } else {
        switch (one.getColumn(cols[i]).getDataType()) {
          case Variant.STRING:
            if (!one.getString(cols[i]).equals(other.getString(cols[i]))) {
              System.err.print(" string "+cols[i]+": "+
                one.getString(cols[i])+" != "+other.getString(cols[i]));
              err = true;
            }
            break;
          case Variant.INT:
            if (one.getInt(cols[i]) != other.getInt(cols[i])) {
              System.err.print(" int "+cols[i]+": "+
                one.getInt(cols[i])+" != "+other.getInt(cols[i]));
              err = true;
            }
            break;
          case Variant.BIGDECIMAL:
            if (one.getBigDecimal(cols[i]).compareTo(other.getBigDecimal(cols[i])) != 0) {
              System.err.print(" bigdecimal "+cols[i]+": "+
                one.getBigDecimal(cols[i])+" != "+other.getBigDecimal(cols[i]));
              err = true;
            }
            break;
          case Variant.TIMESTAMP:
            if (!one.getTimestamp(cols[i]).equals(other.getTimestamp(cols[i]))) {
              System.err.print(" timestamp "+cols[i]+": "+
                one.getTimestamp(cols[i])+" != "+other.getTimestamp(cols[i]));
              err = true;
            }
            break;
          case Variant.SHORT:
            if (one.getShort(cols[i]) != other.getShort(cols[i])) {
              System.err.print(" short "+cols[i]+": "+
                one.getShort(cols[i])+" != "+other.getShort(cols[i]));
              err = true;
            }
            break;
          case Variant.LONG:
            if (one.getLong(cols[i]) != other.getLong(cols[i])) {
              System.err.print(" long "+cols[i]+": "+
                one.getLong(cols[i])+" != "+other.getLong(cols[i]));
              err = true;
            }
            break;
          case Variant.FLOAT:
            if (one.getFloat(cols[i]) != other.getFloat(cols[i])) {
              System.err.print(" float "+cols[i]+": "+
                one.getFloat(cols[i])+" != "+other.getFloat(cols[i]));
              err = true;
            }
            break;
          case Variant.DOUBLE:
            if (one.getDouble(cols[i]) != other.getDouble(cols[i])) {
              System.err.print(" double "+cols[i]+": "+
                one.getDouble(cols[i])+" != "+other.getDouble(cols[i]));
              err = true;
            }
            break;
          default:
            System.err.print(" invalid type column "+cols[i]);
            err = true;
        }
      }
    }
    if (err) {
      System.err.println();
      System.err.println(one);
      System.err.println(other);
    } else {
      System.err.println("Comparing datasets OK");
    }
  }

  protected boolean isOrigWindow() throws InterruptedException {
    Container orig = mp.getNavBar().getNavContainer().getTopLevelAncestor();
    return (findFocusedWindow() == orig);
  }
}

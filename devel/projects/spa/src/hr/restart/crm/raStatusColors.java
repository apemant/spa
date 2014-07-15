/****license*****************************************************************
**   file: raStatusColors.java
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
package hr.restart.crm;

import hr.restart.baza.KlijentStat;
import hr.restart.baza.dM;
import hr.restart.util.Aus;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JLabel;

import com.borland.dx.dataset.DataSet;


public class raStatusColors {
  
  private static raStatusColors inst = new raStatusColors();
  
  private int serial = -1;
  
  Color defColor = new JLabel().getBackground();
  
  private HashMap cols = new HashMap();

  public raStatusColors() {
    checkChanges();
  }
  
  public static raStatusColors getInstance() {
    return inst;
  }
  
  public void checkChanges() {
    int newSerial = dM.getDataModule().getSynchronizer().getSerialNumber("KlijentStat");
    if (serial == newSerial) return;
    serial = newSerial;
    cols.clear();
    DataSet ks = dM.getDataModule().getKlijentStat();
    ks.open();
    for (ks.first(); ks.inBounds(); ks.next())
      cols.put(ks.getString("SID"), findColor(ks.getString("BOJA")));
  }
  
  public Color getColor(String sid) {
    checkChanges();
    
    return (Color) cols.get(sid);
  }
  
  private Color findColor(String col) {
    if (col.startsWith("#") || col.startsWith("0x") || col.startsWith("0X") || Aus.isDigit(col))
      try {
        return Color.decode(col);
      } catch (Exception e) {
        //
      }
    else 
      try {
        Field f = Color.class.getDeclaredField(col);
        return (Color) f.get(null);
      } catch (Exception e) {
        System.out.println(e);
      }
    return defColor;
  }
}

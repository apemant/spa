/****license*****************************************************************
**   file: Parser2Dataset.java
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
package hr.restart.util.textconv;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.Variant;

/**
 * 
 * @author andrej
 *
 */
public class Parser2Dataset {

  private static void setString(ReadWriteRow r, String cn, ILine l) {
    int w = r.getColumn(cn).getPrecision();
    String v = (String)l.getColumnValue(cn);
    if (v.length() > w) v = v.substring(0,w);
    r.setString(cn, v);
  }
  
  private static void setInt(ReadWriteRow r, String cn, ILine l) {
    try {
      String v = l.getColumnValue(cn).toString();
      int i = new Integer(v).intValue();
      r.setInt(cn,i);
    } catch (Exception e) {
//      System.out.println(e+":"+cn+"."+r);
    }
  }
  
  private static void setShort(ReadWriteRow r, String cn, ILine l) {
    try {
      String v = l.getColumnValue(cn).toString();
      short i = new Integer(v).shortValue();
      r.setShort(cn,i);
    } catch (Exception e) {
//      System.out.println(e+":"+cn+"."+r);
    }
  }
  
  private static void setTimestamp(ReadWriteRow r, String cn, ILine l) {
    try {
      Timestamp t = (Timestamp)l.getColumnValue(cn);
      r.setTimestamp(cn,t);
    } catch (Exception e) {
//      System.out.println(e+":"+cn+"."+r);
    }
  }
  
  private static void setBigdecimal(ReadWriteRow r, String cn, ILine l) {
    try {
      BigDecimal b = (BigDecimal)l.getColumnValue(cn);
      r.setBigDecimal(cn,b);
    } catch (Exception e) {
//      System.out.println(e+":"+cn+"."+r);
    }
  }
  
  private static void setValue(ReadWriteRow ds, String cn, ILine l) {
    Column dscol = ds.hasColumn(cn);
    if (dscol == null) return;
    if (dscol.getDataType() == Variant.STRING) {
      setString(ds,cn,l);
    } else if (dscol.getDataType() == Variant.INT) {
      setInt(ds,cn,l);
    } if (dscol.getDataType() == Variant.TIMESTAMP) {
      setTimestamp(ds,cn,l);
    } if (dscol.getDataType() == Variant.BIGDECIMAL) {
      setBigdecimal(ds,cn,l);
    } if (dscol.getDataType() == Variant.SHORT) {
      setShort(ds,cn,l);
    }
  }  
  /**
   * Ako je id property od kolone definirane u xml contextu isti kao naziv kolone u datasetu 
   * metoda ce napuniti kolonu u datasetu sa kolonom u lajni i tako u sirinu i daljinu
   * @param context xml file with lines and columns defined - argument for org.springframework.context.support.ClassPathXmlApplicationContext
   * @param parserbean id of fileparser defined in context
   * @param f file to parse
   * @param ds dataset to store data in
   */
  public static void fillDataSet(String context, String parserbean, File f, DataSet ds) {
    ds.open();
    Collection lines = ParserManager.getParsedLines(context,parserbean,f).values();
    for (Iterator il = lines.iterator(); il.hasNext();) {
      ILine l = (ILine) il.next();
      ds.insertRow(false);
      Collection cols = l.getColumns().values();
      for (Iterator ic = cols.iterator(); ic.hasNext();) {
        IColumn c = (IColumn) ic.next();
        setValue(ds, c.getId(), l);
      }
      ds.post();
    }
  }


}

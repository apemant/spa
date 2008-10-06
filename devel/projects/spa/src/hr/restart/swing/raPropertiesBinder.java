/****license*****************************************************************
**   file: raPropertiesBinder.java
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
 * raPropertiesBinder.java
 *
 * Created on 2003. prosinac 12, 14:56
 */

package hr.restart.swing;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JTextField;
/**
 *
 * @author  andrej
 */
public class raPropertiesBinder {
  private Hashtable bcomps = new Hashtable();
  private HashSet bcother = new HashSet();
  private Properties props;
  private String file;

  public raPropertiesBinder(String _file) {
    file = _file;
    props = FileHandler.getProperties(file);
  }
  private void putBcomps(String identifier, JTextField jt) {
    HashSet jtset = (HashSet)bcomps.get(identifier);
    if (jtset == null) jtset = new HashSet();
    jtset.add(jt);
    bcomps.put(identifier, jtset);
  }
  public JTextField[] getBcomps(String identifier) {
    HashSet jtset = (HashSet)bcomps.get(identifier);
    if (jtset == null) return null;
    return (JTextField[])jtset.toArray(new JTextField[jtset.size()]);
  }
  public void bindOther(PropertiesBindedComponent comp) {
    comp.setBinder(this);
    bcother.add(comp);
  }
  public void bind(final JTextField jt, final String identifier) {
    jt.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        postText(identifier, jt);
      }
      public void focusGained(FocusEvent e) {
        jt.selectAll();
      }
    });
    putBcomps(identifier, jt);
    if (jt.getText().equals("") || jt.getText().equals("0,00")) {
      load(identifier);
    } else {
      props.setProperty(identifier, jt.getText());
    }
  }
  public void load(String identifier) {
    JTextField[] jt = getBcomps(identifier);
    if (jt == null) return;
    String text = getText(identifier);
    for (int i=0; i<jt.length; i++) {
      jt[i].setText(text);
    }
  }
  public void post(String identifier) {
    JTextField jt = (JTextField)getBcomps(identifier)[0];
    if (jt == null) return;
    props.setProperty(identifier, jt.getText());
  }
  public void postText(String identifier, JTextField jt) {
    String text = jt.getText();
    JTextField[] fields = getBcomps(identifier);
    for (int i=0; i<fields.length; i++) {
      fields[i].setText(text);
    }
    post(identifier);
  }
  public void store() {
    for (Iterator i = bcomps.keySet().iterator(); i.hasNext(); ) {
      String identifier = i.next().toString();
      try {
        String text = ((JTextField)getBcomps(identifier)[0]).getText();
        if (text != null) props.setProperty(identifier, text);
      } catch (Exception ex) {
        System.out.println("Cannot set property "+identifier+" "+ex);
      }
    }
    FileHandler.storeProperties(file, props);
  }
  public void setText(String identifier, String text) {
    props.setProperty(identifier, text);
    load(identifier);
  }
  public String getText(String identifier) {
    return props.getProperty(identifier,"0.00");
  }
  public boolean hasIdentifier(String identifier) {
    return props.contains(identifier);
  }
  public BigDecimal getBigDecimal(String identifier) {
    try {
      return new BigDecimal(getNumericText(identifier));
    } catch (Exception ex) {
      return Aus.zero2;
    }
  }
  public void setBigDecimal(String identifier, BigDecimal value) {
    setText(identifier, formatValue(value));
  }
  public String getNumericText(String identifier) {
    VarStr vtext = new VarStr(getText(identifier));
    if (vtext.lastIndexOf('.') == -1 && vtext.lastIndexOf(',') == -1) return vtext.toString();
    if (vtext.lastIndexOf(',') == -1) return vtext.toString(); //ili nema decimalne tocke ili je .
    if (vtext.lastIndexOf('.') == -1) return vtext.replaceAll(',','.').toString(); //ili nema decimalne tocke ili je ,
    if (vtext.lastIndexOf('.') > vtext.lastIndexOf(',')) return vtext.remove(',').toString();
    return vtext.remove('.').replaceAll(',', '.').toString();
  }
  
  private String formatValue(BigDecimal bd) {
    try {
      VarStr vsbd = new VarStr(bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
      String[] asbd = vsbd.splitTrimmed('.');
      if (asbd[0].length() <= 3) return asbd[0]+","+asbd[1];
      StringBuffer dbsa0 = new StringBuffer(asbd[0]).reverse();
      StringBuffer dbsa0_formated = new StringBuffer(dbsa0.length());
      for (int i=0; i<dbsa0.length(); i++) {
        dbsa0_formated.append(dbsa0.charAt(i));
        if ((i+1)%3==0) {
          dbsa0_formated.append('.');
        }
      }
      return dbsa0_formated.reverse()+","+asbd[1];
      //return vsbd.toString();
    } catch (Exception ex) {
      return "0,00";
    }
  }
  
  public Hashtable getBindedComponents() {
    return bcomps;
  }
  public Properties getProperties() {
    return props;
  }
  public String getFileName() {
    return file;
  }
  
  public void rebindTo(raPropertiesBinder newbinder) {
    for (Iterator i = bcomps.keySet().iterator(); i.hasNext(); ) {
      Object id = i.next();
      //newbinder.bind((JTextField)oldbinder.bcomps.get(id),id.toString());
      JTextField[] comps = getBcomps(id.toString());
      if (comps!=null) {
        for (int j=0; j<comps.length; j++) {
          newbinder.bind(comps[j], id.toString());
        }
      }
    }
    for (Iterator i = bcother.iterator(); i.hasNext(); ) {
      PropertiesBindedComponent item = (PropertiesBindedComponent)i.next();
      item.setBinder(newbinder);
    }
  }
  public static raPropertiesBinder cloneBinder(raPropertiesBinder oldbinder, String _file) {
    raPropertiesBinder newbinder = new raPropertiesBinder(_file);
    for (Iterator i = oldbinder.getProperties().keySet().iterator(); i.hasNext(); ) {
      String key = i.next().toString();
      newbinder.getProperties().setProperty(key, oldbinder.getProperties().getProperty(key));
    }
    return newbinder;
  }  
  
  public interface PropertiesBindedComponent {
    public void setBinder(raPropertiesBinder binder);
  }
}

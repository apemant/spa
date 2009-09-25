/****license*****************************************************************
**   file: presPOS.java
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
package hr.restart.robno;

import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;

import hr.restart.sisfun.raUser;
import hr.restart.swing.JraCheckBox;
import hr.restart.util.VarStr;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class presPOS extends jpPreselectDoc {
  static presPOS prespos;
  
  JraCheckBox jcbDel = new JraCheckBox();
  boolean isSuper;
  
  public void defaultMatDocAllowed(){
    isMatDocAllowed = false;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = false;
  }

  public void resetDefaults() {
    super.resetDefaults();
    jcbDel.setSelected(false);
  }

  public presPOS() {
    super('N', 'F');    
//  	super('D', 'D');
    if (isSuper = raUser.getInstance().isSuper()) {
      jcbDel.setText(" Arhiva ");
      jcbDel.setHorizontalTextPosition(SwingConstants.LEADING);
      jcbDel.setHorizontalAlignment(SwingConstants.TRAILING);
      jpSelDoc.add(jcbDel, new XYConstraints(460, 20, 75, -1));
    }
    prespos=this;
  }
  public void copySelValues() {
    // TODO Auto-generated method stub
    super.copySelValues();
    if (isSuper && jcbDel.isSelected())
      getSelDataSet().setString("CSKL", 
          "#" + getSelRow().getString("CSKL")); 
      
  }
  public String refineSQLFilter(String orig) {
    return !isSuper || !jcbDel.isSelected() ? orig : new VarStr(orig).
        replace("'" + getSelRow().getString("CSKL") + "'", 
            "'#" + getSelRow().getString("CSKL") + "'").toString();
  }
  public static jpPreselectDoc getPres() {
    if (prespos==null) {
      prespos=new presPOS();
    }
    return prespos;
  }
}
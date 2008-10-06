/****license*****************************************************************
**   file: ModeDialog.java
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
package hr.restart.util;

import java.awt.Frame;
/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public class ModeDialog extends raListDialog implements loadFrame {
//  String[] modeListUI = new String[] {"Windows ekrani","Interni ekrani","Modalni ekrani","Jedan ekran*"};
  String[] modeListUI = new String[] {"Više ekrana","Jedan ekran"};
  int startIdx = 0;
  public ModeDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    setListData(modeListUI);
  }
  public void reload() {
    try {
      int propIdx = Integer.parseInt(IntParam.getTag("framemode"));
      startIdx = propIdx==0?0:3;
      setListSelectedIndex(startIdx==0?0:1);
    } catch (Exception e) {
     setListSelectedIndex(0);
    }
  }
  public void action_jBOK(){
    System.out.println("getListSelectedIndex() = "+getListSelectedIndex());
    int selIdx = getListSelectedIndex()==0?0:3;
    Integer tmpI = new Integer(selIdx);
    hr.restart.util.IntParam.setTag("framemode",tmpI.toString());
//    JOptionPane.showMessageDialog(null,"Da bi se promijenio mod rada potrebno je restartati aplikaciju!","Poruka",JOptionPane.INFORMATION_MESSAGE);
//    this.setVisible(false);
  }

  public boolean doSaving() {
    int selIdx = getListSelectedIndex()==0?0:3;
    return selIdx != startIdx;
  }

}
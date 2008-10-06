/****license*****************************************************************
**   file: frmStatusChanger.java
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
package hr.restart.os;

import hr.restart.util.raFrame;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;

import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmStatusChanger extends raFrame {
  String status;
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  StorageDataSet left = new StorageDataSet();
  StorageDataSet right = new StorageDataSet();
  raTwoTableChooser ttC = new raTwoTableChooser(this){
    public void saveChoose() {
    }

    public void actionRtoL() {
      super.actionRtoL();
    }

    public void actionLtoR() {
      super.actionLtoR();
    }

    public void actionRtoL_all() {
      super.actionRtoL_all();
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();

  public frmStatusChanger(String stat) {
    status=stat;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public frmStatusChanger() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void show() {
    System.out.println("Show mee bejbi");
    ttC.initialize();
    super.show();
  }

  private void jbInit() throws Exception {
    System.out.println("Status: "+status);
    ttC.setLeftDataSet(left);
    ttC.setRightDataSet(right);
    vl.execSQL("select * from OS_SREDSTVO where STATUS='I'");
    right=vl.RezSet;
    left=vl.RezSet;
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(ttC, BorderLayout.CENTER);
  }
}
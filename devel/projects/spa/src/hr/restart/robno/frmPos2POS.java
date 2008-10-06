/****license*****************************************************************
**   file: frmPos2POS.java
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

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class frmPos2POS extends raDocTwoTableChooser {
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);

  public frmPos2POS() {
    super("GRC");
  }

  private static frmPos2POS frmpos;

  public static boolean razdMP(StorageDataSet pres, 
          QueryDataSet mast, QueryDataSet det) {
    
    
    if (frmpos == null)
      frmpos = (frmPos2POS)_Main.getStartFrame().showFrame(
          "hr.restart.robno.frmPos2POS", 15, "Razduženje maloprodaje", false);
    det.open();
    frmpos.detail=det;
    frmpos.master=mast;
    frmpos.presel=pres;
    frmpos.show();
    return true;
  }

  public static frmPos2POS getFrmpos() {
    if (frmpos == null)
      frmpos = new frmPos2POS();
    return frmpos;
  }
}

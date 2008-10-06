/****license*****************************************************************
**   file: AutomatedMasterDetail.java
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

import hr.restart.util.raMasterDetail;

import java.awt.event.KeyEvent;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomatedMasterDetail extends AutomatedTask {
  protected raMasterDetail md;
  private AutomatedMatPodaci am, ad;
  protected AutomatedPreSelect ps;

  protected AutomatedMasterDetail() {}

  protected void setMasterDetail(raMasterDetail md) {
    this.md = md;
    createMaster();
    createDetail();
    createPreSelect();
  }

  public AutomatedMasterDetail(raMasterDetail md) {
    setMasterDetail(md);
  }

  protected void createMaster() {
    am = new AutomatedMatPodaci(md.raMaster);
  }

  protected void createDetail() {
    ad = new AutomatedMatPodaci(md.raDetail);
  }

  protected void createPreSelect() {
    ps = new AutomatedPreSelect(md.getPreSelect());
  }

  public AutomatedPreSelect getPreSelect() {
    return ps;
  }

  public AutomatedMatPodaci getMaster() {
    return am;
  }

  public AutomatedMatPodaci getDetail() {
    return ad;
  }

  public boolean isDetailShowing() {
    return md.raDetail.isShowing();
  }

  public void showDetail() throws InterruptedException {
    if (!isDetailShowing())
      type(KeyEvent.VK_F6);
  }
}

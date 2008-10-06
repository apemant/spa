/****license*****************************************************************
**   file: frmSalKonUR.java
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
package hr.restart.ur;

import hr.restart.sk.frmSalKon;

public class frmSalKonUR extends frmSalKon {
  
  public frmSalKonUR() {
    super();
  }
  protected void initPreselect() {
    pres = new presSalKonUR(getMasterSet());
  }
  public boolean addVirmani() {
    return false;
  }

  public boolean addPotvrdi() {
    return false;
  }
  
  protected int getBrojizv() {
    return -1;
  }
  
  protected void setTitleMaster() {
    this.setNaslovMaster((ira ? "Izlazni" : "Ulazni")+" dokumenti");
  }

  protected void setTitleDetail() {
    setNaslovDetail("Stavke dokumenta "+this.getMasterSet().getString("BROJDOK")+" od "+
       hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(this.getMasterSet().getTimestamp("DATDOK")));
  }
  
  

}

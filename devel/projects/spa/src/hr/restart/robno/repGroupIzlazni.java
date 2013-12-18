/****license*****************************************************************
**   file: repGroupIzlazni.java
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

import java.util.HashMap;


/**
 * @author S.G.
 *
 * Started 2005.04.07
 * 
 */

public class repGroupIzlazni extends repIzlazni {

 // HashMap arts = new HashMap();
  int rbr = 0;
  String lastgr = "";
  
  public repGroupIzlazni() {
    this(true);
  }

  public repGroupIzlazni(boolean init) {
    super(init);
  }
  
  protected void dokChanged() {
    super.dokChanged();
    rbr = 0;
    lastgr = "";
  }
  
  public int getGroup() {
    if (!lD.raLocate(dm.getArtikli(), "CART", Integer.toString(ds.getInt("CART"))) ||
        !lD.raLocate(dm.getGrupart(), "CGRART", dm.getArtikli().getString("CGRART")))
      return 0;
    
    return dm.getGrupart().getInt("SSORT");
  }
  
  public String getGROUPTEXT() {
    if (!lD.raLocate(dm.getArtikli(), "CART", Integer.toString(ds.getInt("CART"))) ||
        !lD.raLocate(dm.getGrupart(), "CGRART", dm.getArtikli().getString("CGRART")))
      return "";
    
    if (!lastgr.equals(dm.getGrupart().getString("NAZGRART"))) ++rbr;
    lastgr = dm.getGrupart().getString("NAZGRART");
    
    return rbr + ". " + lastgr;
  }
  
  public String RBRDUMMY() {
    return "";
  }
  
}

/****license*****************************************************************
**   file: allPorezi.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.math.BigDecimal;

import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class allPorezi {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private static allPorezi alp;
  private QueryDataSet trenPOREZ = new QueryDataSet();
  private QueryDataSet trenPOREZ1 = new QueryDataSet();
  String OLDtext ="";

  public allPorezi() {
  }

  public static allPorezi getallPorezi(){
    if (alp == null) {
      alp = new allPorezi();
    }
    return alp;
  }

  public QueryDataSet gettrenPOREZ() {
    return trenPOREZ;
  }
  public QueryDataSet gettrenPOREZART() {
    return trenPOREZ1;
  }
  allSelect aSS = new allSelect();
  public void findCPOR(String text){
    if (!OLDtext.equals(text)) {
      if (trenPOREZ.isOpen())
        trenPOREZ.close();
      trenPOREZ.setQuery(new QueryDescriptor(dm.getDatabase1(),
                         aSS.getQuery4aP4findCPOR(text),
                         null, true, Load.ALL));
      trenPOREZ.executeQuery();
    }
  }
  public void findCPORART(int cart){
     if (trenPOREZ1.isOpen()) trenPOREZ1.close();
      trenPOREZ1.setQuery(new QueryDescriptor(dm.getDatabase1(),
                         aSS.getQuery4aP4findCPORART(cart),
                         null, true, Load.ALL));
      trenPOREZ1.executeQuery();

  }

  public BigDecimal getNestoBD(String po_cemu,String column){
    findCPOR(po_cemu);
    return trenPOREZ.getBigDecimal(column);
  }
  public String getNestoST(String po_cemu,String column){
    findCPOR(po_cemu);
    return trenPOREZ.getString(column);
  }

  public BigDecimal getPOR1(String po_cemu){
    return getNestoBD(po_cemu,"POR1");
  }

  public BigDecimal getPOR2(String po_cemu){
    return getNestoBD(po_cemu,"POR2");
  }

  public BigDecimal getPOR3(String po_cemu){
    return getNestoBD(po_cemu,"POR3");
  }

  public BigDecimal getUNPOR1(String po_cemu){
    return getNestoBD(po_cemu,"UNPOR1");
  }

  public BigDecimal getUNPOR2(String po_cemu){
    return getNestoBD(po_cemu,"UNPOR2");
  }

  public BigDecimal getUNPOR3(String po_cemu){
    return getNestoBD(po_cemu,"UNPOR3");
  }
  public String getNAZPOR1(String po_cemu){
    return getNestoST(po_cemu,"NAZPOR1");
  }
  public String getNAZPOR2(String po_cemu){
    return getNestoST(po_cemu,"NAZPOR2");
  }
  public String getNAZPOR3(String po_cemu){
    return getNestoST(po_cemu,"NAZPOR3");
  }
}
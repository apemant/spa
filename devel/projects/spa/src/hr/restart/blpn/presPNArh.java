/****license*****************************************************************
**   file: presPNArh.java
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
package hr.restart.blpn;

public class presPNArh extends presPN{

  protected void jbInit() throws Exception {
    super.jbInit();
    this.setSelDataSet(dm.getPutnalarh());
  }
/*
  public void SetFokus(){

    rcbStatus.setRaItems(new String[][] {
      {"Svi statusi",""},
      {"Ispla\u0107en","I"},
      {"Proknjižen","K"}
    });

    rcbStatus.setSelectedIndex(0);
    short god = Short.parseShort(vl.findYear(vl.getToday()));
    getSelRow().setShort("GODINA", god);
    vl.getCommonRange(jraDatod, jraDatdo);
    jraGodina.requestFocus();
    jraGodina.selectAll();
  }*/
}

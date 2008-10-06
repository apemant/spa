/****license*****************************************************************
**   file: raKalkIzlaza.java
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


public class raKalkIzlaza {

  private static raKalkIzlaza rki;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private com.borland.dx.dataset.TableDataSet DummySet = new com.borland.dx.dataset.TableDataSet();
  private com.borland.dx.dataset.Column JIRAB = new com.borland.dx.dataset.Column("JIRAB","JIRAB",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column JIZT = new com.borland.dx.dataset.Column("JIZT","JIZT",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column POSPOR1 = new com.borland.dx.dataset.Column("POSPOR1","POSPOR1",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column NAZPOR1 = new com.borland.dx.dataset.Column("NAZPOR1","NAZPOR1",com.borland.dx.dataset.Variant.STRING);
  private com.borland.dx.dataset.Column POSPOR2 = new com.borland.dx.dataset.Column("POSPOR2","UPOSPOR2",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column NAZPOR2 = new com.borland.dx.dataset.Column("NAZPOR2","NAZPOR2",com.borland.dx.dataset.Variant.STRING);
  private com.borland.dx.dataset.Column POSPOR3 = new com.borland.dx.dataset.Column("POSPOR3","UPOSPOR3",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column NAZPOR3 = new com.borland.dx.dataset.Column("NAZPOR3","NAZPOR3",com.borland.dx.dataset.Variant.STRING);
  private com.borland.dx.dataset.Column POR1DO3 = new com.borland.dx.dataset.Column("POR1DO3","POR1DO3",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column PPOR1 = new com.borland.dx.dataset.Column("PPOR1","PPOR1",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column PPOR2 = new com.borland.dx.dataset.Column("PPOR2","PPOR2",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column PPOR3 = new com.borland.dx.dataset.Column("PPOR3","PPOR3",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column KOLZAL = new com.borland.dx.dataset.Column("KOLZAL","KOLZAL",com.borland.dx.dataset.Variant.BIGDECIMAL);

  java.math.BigDecimal Nula = new java.math.BigDecimal(0);
  java.math.BigDecimal Sto = new java.math.BigDecimal(100);

  public raKalkIzlaza() {
    InitDummyTable();
  }

  public static raKalkIzlaza getRaKalkIzlaza(){
    if (rki==null)
      rki=new raKalkIzlaza();
    return rki;
  }

  public com.borland.dx.dataset.TableDataSet getDummySet(){
    return DummySet;
  }

  public void Clean(){

    DummySet.setBigDecimal("JIRAB",Nula);
    DummySet.setBigDecimal("JIZT",Nula);
    DummySet.setBigDecimal("POSPOR1",Nula);
    DummySet.setBigDecimal("POSPOR2",Nula);
    DummySet.setBigDecimal("POSPOR3",Nula);
    DummySet.setBigDecimal("POR1DO3",Nula);
    DummySet.setBigDecimal("PPOR1",Nula);
    DummySet.setBigDecimal("PPOR2",Nula);
    DummySet.setBigDecimal("PPOR3",Nula);
    DummySet.setBigDecimal("KOLZAL",Nula);
    DummySet.setString("NAZPOR1","");
    DummySet.setString("NAZPOR2","");
    DummySet.setString("NAZPOR3","");
  }

  public void InitDummyTable(){

    if(DummySet.isOpen())
      DummySet.close();
    JIRAB.setDisplayMask("###,###,##0.00");
    JIRAB.setDefault("0");
    JIRAB.setPrecision(15);
    JIRAB.setScale(6);
    JIZT.setDisplayMask("###,###,##0.00");
    JIZT.setDefault("0");
    JIZT.setPrecision(15);
    JIZT.setScale(6);
    POSPOR1.setDisplayMask("###,###,##0.00");
    POSPOR1.setDefault("0");
    POSPOR1.setPrecision(15);
    POSPOR1.setScale(6);
    POSPOR2.setDisplayMask("###,###,##0.00");
    POSPOR2.setDefault("0");
    POSPOR2.setPrecision(15);
    POSPOR2.setScale(6);
    POSPOR3.setDisplayMask("###,###,##0.00");
    POSPOR3.setDefault("0");
    POSPOR3.setPrecision(15);
    POSPOR3.setScale(6);
    POR1DO3.setDisplayMask("###,###,##0.00");
    POR1DO3.setDefault("0");
    POR1DO3.setPrecision(15);
    POR1DO3.setScale(6);
    PPOR1.setDisplayMask("###,###,##0.00");
    PPOR1.setDefault("0");
    PPOR1.setPrecision(15);
    PPOR1.setScale(6);
    PPOR2.setDisplayMask("###,###,##0.00");
    PPOR2.setDefault("0");
    PPOR2.setPrecision(15);
    PPOR2.setScale(6);
    PPOR3.setDisplayMask("###,###,##0.00");
    PPOR3.setDefault("0");
    PPOR3.setPrecision(15);
    PPOR3.setScale(6);
    KOLZAL.setDisplayMask("###,###,##0.00");
    KOLZAL.setDefault("0");
    KOLZAL.setPrecision(15);
    KOLZAL.setScale(6);
    DummySet.setColumns(new com.borland.dx.dataset.Column[] {JIRAB,JIZT,POSPOR1,
                        POSPOR2,POSPOR3,POR1DO3,PPOR1,PPOR2,PPOR3,KOLZAL,NAZPOR1,NAZPOR2,NAZPOR3});
    DummySet.open();
    DummySet.emptyAllRows();
    DummySet.insertRow(false);
    Clean();
  }

  public void ocistiDummy(){

    if(DummySet.isOpen())
      DummySet.close();
    DummySet.open();
    DummySet.emptyAllRows();
    DummySet.insertRow(false);
    Clean();

  }
}
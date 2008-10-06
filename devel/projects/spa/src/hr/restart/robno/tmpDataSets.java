/****license*****************************************************************
**   file: tmpDataSets.java
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
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;

public class tmpDataSets {

    java.math.BigDecimal Nula = new java.math.BigDecimal(0);

    private TableDataSet MesklaTempSet = new com.borland.dx.dataset.TableDataSet();
    private Column BEFKOLUL = new Column("BEFKOLUL","BEFKOLUL",Variant.BIGDECIMAL);
    private Column BEFVRIUL = new Column("BEFVRIUL","BEFVRIUL",Variant.BIGDECIMAL);
    private Column AFTKOLUL = new Column("AFTKOLUL","AFTKOLUL",Variant.BIGDECIMAL);
    private Column AFTVRIUL = new Column("AFTVRIUL","AFTVRIUL",Variant.BIGDECIMAL);
    private Column BEFKOLIZ = new Column("BEFKOLIZ","BEFKOLIZ",Variant.BIGDECIMAL);
    private Column BEFVRIIZ = new Column("BEFVRIIZ","BEFVRIIZ",Variant.BIGDECIMAL);
    private Column AFTKOLIZ = new Column("AFTKOLIZ","AFTKOLIZ",Variant.BIGDECIMAL);
    private Column AFTVRIIZ = new Column("AFTVRIIZ","AFTVRIIZ",Variant.BIGDECIMAL);


    public com.borland.dx.dataset.TableDataSet getMesklaTempSet(){
      return MesklaTempSet;
    }

    public void MTS() {
      if(MesklaTempSet.isOpen())
          MesklaTempSet.close();
      BEFKOLUL.setDisplayMask("###,###,##0.000");
      BEFKOLUL.setDefault("0");
      BEFKOLUL.setPrecision(15);
      BEFKOLUL.setScale(6);
      BEFVRIUL.setDisplayMask("###,###,##0.00");
      BEFVRIUL.setDefault("0");
      BEFVRIUL.setPrecision(15);
      BEFVRIUL.setScale(6);
      AFTKOLUL.setDisplayMask("###,###,##0.000");
      AFTKOLUL.setDefault("0");
      AFTKOLUL.setPrecision(15);
      AFTKOLUL.setScale(6);
      AFTVRIUL.setDisplayMask("###,###,##0.00");
      AFTVRIUL.setDefault("0");
      AFTVRIUL.setPrecision(15);
      AFTVRIUL.setScale(6);

      AFTKOLIZ.setDisplayMask("###,###,##0.000");
      AFTKOLIZ.setDefault("0");
      AFTKOLIZ.setPrecision(15);
      AFTKOLIZ.setScale(6);

      AFTVRIIZ.setDisplayMask("###,###,##0.00");
      AFTVRIIZ.setDefault("0");
      AFTVRIIZ.setPrecision(15);
      AFTVRIIZ.setScale(6);

      BEFKOLIZ.setDisplayMask("###,###,##0.000");
      BEFKOLIZ.setDefault("0");
      BEFKOLIZ.setPrecision(15);
      BEFKOLIZ.setScale(6);

      BEFVRIIZ.setDisplayMask("###,###,##0.00");
      BEFVRIIZ.setDefault("0");
      BEFVRIIZ.setPrecision(15);
      BEFVRIIZ.setScale(6);
      MesklaTempSet.setColumns(new com.borland.dx.dataset.Column[] {BEFKOLUL,BEFVRIUL,AFTKOLUL,AFTVRIUL,
                                                                    BEFKOLIZ,BEFVRIIZ,AFTKOLIZ,AFTVRIIZ});
      InitMTS();
    }
  public void InitMTS(){
      MesklaTempSet.open();
      MesklaTempSet.emptyAllRows();
      MesklaTempSet.insertRow(false);
      Clean();
  }

  public void Clean() {
      MesklaTempSet.setBigDecimal("BEFKOLUL",Nula);
      MesklaTempSet.setBigDecimal("BEFVRIUL",Nula);
      MesklaTempSet.setBigDecimal("AFTKOLUL",Nula);
      MesklaTempSet.setBigDecimal("AFTVRIUL",Nula);
      MesklaTempSet.setBigDecimal("BEFKOLIZ",Nula);
      MesklaTempSet.setBigDecimal("BEFVRIIZ",Nula);
      MesklaTempSet.setBigDecimal("AFTKOLIZ",Nula);
      MesklaTempSet.setBigDecimal("AFTVRIIZ",Nula);
  }

  public void AddSubStanje(java.math.BigDecimal BD,java.math.BigDecimal IznosUl,java.math.BigDecimal IznosIz){
      MesklaTempSet.setBigDecimal("AFTKOLIZ",MesklaTempSet.getBigDecimal("BEFKOLIZ").subtract(BD));
      MesklaTempSet.setBigDecimal("AFTVRIIZ",MesklaTempSet.getBigDecimal("BEFVRIIZ").subtract(IznosIz));
      MesklaTempSet.setBigDecimal("AFTKOLUL",MesklaTempSet.getBigDecimal("BEFKOLUL").add(BD));
      MesklaTempSet.setBigDecimal("AFTVRIUL",MesklaTempSet.getBigDecimal("BEFVRIUL").add(IznosUl));
  }

  public tmpDataSets() {
    MTS();
  }
  allStanje AST =  allStanje.getallStanje();

  public void SetMesklaTempSet(DataSet tmpStanjeUlaz,DataSet tmpStanjeIzlaz) {
    Clean();
    MesklaTempSet.setBigDecimal("BEFKOLUL",tmpStanjeUlaz.getBigDecimal("KOL"));
    MesklaTempSet.setBigDecimal("AFTKOLUL",tmpStanjeUlaz.getBigDecimal("KOL"));
    MesklaTempSet.setBigDecimal("BEFVRIUL",tmpStanjeUlaz.getBigDecimal("VRI"));
    MesklaTempSet.setBigDecimal("AFTVRIUL",tmpStanjeUlaz.getBigDecimal("VRI"));
    MesklaTempSet.setBigDecimal("BEFKOLIZ",tmpStanjeIzlaz.getBigDecimal("KOL"));
    MesklaTempSet.setBigDecimal("AFTKOLIZ",tmpStanjeIzlaz.getBigDecimal("KOL"));
    MesklaTempSet.setBigDecimal("BEFVRIIZ",tmpStanjeIzlaz.getBigDecimal("VRI"));
    MesklaTempSet.setBigDecimal("AFTVRIIZ",tmpStanjeIzlaz.getBigDecimal("VRI"));
    System.out.println("values:");
    System.out.println("orig KOLUL: "+tmpStanjeUlaz.getBigDecimal("KOL"));
    System.out.println("orig KOLIZ: "+tmpStanjeIzlaz.getBigDecimal("KOL"));
  }
}


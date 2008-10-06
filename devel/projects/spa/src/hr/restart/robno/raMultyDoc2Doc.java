/****license*****************************************************************
**   file: raMultyDoc2Doc.java
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
import com.borland.dx.sql.dataset.QueryDataSet;


public class raMultyDoc2Doc {

  private raPrenosVT rPVT = new raPrenosVT();
  public raMultyDoc2Doc() {

  }






  public QueryDataSet findQDSforPossibleSennding(QueryDataSet zaglavlje,String[] field_4search,
      String[] value_4search,String typedoc,String ime_table){

    if (field_4search.length!=value_4search.length) {
      throw new java.lang.RuntimeException("field_4search.length!=value_4search.length");
    }
    String sql = "select * from "+ime_table+" where vrdok='"+typedoc+"' ";
    for (int i = 0;i<field_4search.length;i++) {
      int type = zaglavlje.getColumn(field_4search[i]).getDataType();

      if (type == com.borland.dx.dataset.Variant.STRING ||
          type == com.borland.dx.dataset.Variant.TIMESTAMP){
        sql=sql+" and "+field_4search[i]+"='"+value_4search[i]+"' ";
      } else {
        sql=sql+" and "+field_4search[i]+"="+value_4search[i];
      }
    }

    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(sql,true);
    return qds;

  }

  public void transferStavki(QueryDataSet zaglavlje,QueryDataSet _4prijenos_zaglavlje){
    QueryDataSet findaj_stavke = hr.restart.util.Util.getNewQueryDataSet(
        "select * from st"+_4prijenos_zaglavlje.getTableName()+" where "
        ,true);
  }
}
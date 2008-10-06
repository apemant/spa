/****license*****************************************************************
**   file: presPON.java
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
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class presPON extends jpPreselectDoc {
  static presPON prespon;
  public String addSQL = " and (param='' or param is null or param like 'P%')";

  public void defaultMatDocAllowed(){
    isMatDocAllowed = false;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = false;
  }

  public presPON(char cskl,char cpar) {
    super(cskl,cpar);
  }

  public presPON() {
    super('D', 'D');
    prespon=this;
  }

  public boolean applySQLFilter(){
    dontOpenWhenSQLFilterNextTime();
    boolean b =  super.applySQLFilter();
    if (!b) return false;
    QueryDataSet sqds = (QueryDataSet) getSelDataSet();
    sqds.close();
    String realQuery = sqds.getQuery().getQueryString()+addSQL;
    sqds.close();
    sqds.setQuery(new QueryDescriptor(dm.getDatabase1(),realQuery));
    b = sqds.open();
    return b;
  }


  public static jpPreselectDoc getPres() {
    if (prespon==null) {
      prespon=new presPON();

    }
    return prespon;
  }
}
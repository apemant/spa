/****license*****************************************************************
**   file: presPONkup.java
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

public class presPONkup extends presPON {

  static presPONkup prespon;

  public void defaultMatDocAllowed(){
    isMatDocAllowed = false;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = false;
  }

  public presPONkup() {
    super('D', 'F');
    addSQL = " and param like 'K%'";
    prespon=this;
  }

  public static jpPreselectDoc getPres() {
    if (prespon==null) {
      prespon=new presPONkup();
    }
    return prespon;
  }
/*
  public boolean applySQLFilter(){
    dontOpenWhenSQLFilterNextTime();
    boolean b =  super.applySQLFilter();
    if (!b) return false;
    QueryDataSet sqds = (QueryDataSet) getSelDataSet();
    sqds.close();
    String realQuery = sqds.getQuery().getQueryString()+
                       " and param like 'K%'";
    sqds.close();
    sqds.setQuery(new QueryDescriptor(dm.getDatabase1(),realQuery));
    b = sqds.open();
    return b;
  }
*/
}
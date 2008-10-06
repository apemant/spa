/****license*****************************************************************
**   file: CommonTable.java
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
package hr.restart.baza;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonTable {

  static public String SqlDefUniqueIndex="create unique index " ;
  static public String SqlDefIndex="create index " ;
  static private Statement Stmt ;
  public CommonTable() {}

  static public boolean CommonCreateTabela(Connection con,String SqlDefTabela,String NazivT){
    boolean succ = true;
    try {

      Stmt=con.createStatement() ;
      System.err.println(SqlDefTabela);
      Stmt.execute(SqlDefTabela);
      Stmt.close();
      System.err.println(NazivT+" ISKREIRAN !");

      } catch(SQLException ex )   {
         System.err.println("SQLException: "+ ex.getMessage());
         succ = false;
         }
    return succ;
  }

  public static boolean CommonCreateIdx(Connection con,String[] SqlDefIdx, String[] NazivIndex){
    boolean succ = true;
    for (int i=0; i< SqlDefIdx.length;i++){

      try {
        Stmt=con.createStatement() ;
        System.err.println(SqlDefIdx[i]);
        Stmt.execute(SqlDefIdx[i]);
        Stmt.close();
        System.err.println(NazivIndex[i]+" index iskreiran");

      } catch(SQLException ex )   {
           System.err.println("SQLException: "+ ex.getMessage());
           succ = false;
         }

    }
    return succ;
  }

  static public boolean CommonIndexDrop(Connection con,String[] ime_indexa) {
    boolean succ = true;
    for (int i=0;i<ime_indexa.length;i++)
      if (!TabeleKreiranje.DropajIndex(con,ime_indexa[i]))
        succ = false;
    return succ;
  }

/**
 *  Funkcija izvršava dropanje svih indeksa u parametru imena indexa
 */

  static public boolean CommonIndexDrop(BazaOper baza,String[] ime_indexa) {
    boolean succ = true;
    for (int i=0;i<ime_indexa.length;i++)
      if (!TabeleKreiranje.DropajIndex(baza.DajKonekciju() ,ime_indexa[i]))
        succ = false;
    return succ;
  }

}
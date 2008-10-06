/****license*****************************************************************
**   file: BazaOper.java
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
//package robno;
package hr.restart.baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



/**vbnvbn
 * Title:        Robno knjigovodstvo
 * Description:  Projekt je zamišljen kao multiuser-ska verzija robnog knjigovodstva
 * Copyright:    Copyright (c) 2001
 * Company:      Rest Art d.o.o.
 * @author Tomislav Vidakovi\u0107
 * @version 1.0
 */

public class BazaOper {

  static private String url ;
  static private String tip ;
  static private String user ;
  static private String jpass ;
  static private Connection con;
  hr.restart.util.IntParam param = new hr.restart.util.IntParam();

  public BazaOper() {

/*Ovaj dio treba napraviti tako da se definira kroz neki ekran drajver,
tip drajvera, korisnik i password kako to napraviti zasada ne znam a dotad je
 fiksno
*/

    BazaOper.url=param.URL;
    BazaOper.user=param.USER ;
    BazaOper.jpass =param.PASSWORD;
    BazaOper.tip=param.TIP;

  }

  static public void BazaInit(){


  }

  static public void BazaOpen() {
  }
  static public boolean TestCon(String user,String pass,  String url,String tip) {

    BazaOper.url=url;
    BazaOper.user=user ;
    BazaOper.jpass =pass;
    BazaOper.tip=tip;

    return BazaKonekcija();
  }

  static public void ZatvoriKonekciju() {

    try {

        con.isClosed();
        }
         catch(SQLException ex){}
    try {
        con.close();

        } catch (SQLException ex) {}

        con = null;

  }
  static public Connection DajKonekciju() {
    try {
      if (con == null || con.isClosed())
        if ( ! BazaKonekcija()) {
            return null ;
        }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return con ;
  }

  static private boolean BazaKonekcija(){

    try {
      Class.forName(BazaOper.tip);

   } catch(java.lang.ClassNotFoundException e) {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
          }

    try {
      con = DriverManager.getConnection(BazaOper.url,BazaOper.user,BazaOper.jpass);
        } catch(SQLException ex) {
          ex.printStackTrace();
                return false ;
   }

    return true ;
  }
}
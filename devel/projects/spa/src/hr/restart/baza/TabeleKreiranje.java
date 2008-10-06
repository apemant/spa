/****license*****************************************************************
**   file: TabeleKreiranje.java
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

public class TabeleKreiranje {

  static private Statement Stmt ;

  public TabeleKreiranje() {

  }

  static public void KreirajTable(BazaOper baza) {

//    Artikli.Artikli(baza);
//    Partneri.Partneri(baza);
//    Pjpar.Pjpar(baza);
//    Grupart.Grupart(baza);
//    Porezi.Porezi(baza);
//    Sklad.Sklad(baza);
//    Doku.Doku(baza);
//    Stdoku.Stdoku(baza);
//    Stanje.StanjeKreat(baza);
//    Zavtr.Zavtr(baza);
/*    Agenti.Agenti(baza);
    SEQ.SEQ(baza);
    Konta.Konta(baza);
    Orgstruktura.Orgstruktura(baza);
    Parametri.Parametri(baza);
    Korisnik.Korisnik(baza);
*/
//    Valute.Valute(baza);
//    Tecajevi.Tecajevi(baza);
//    zirorn.zirorn(baza);

//    new Shzavtr().Kreiranje(baza);
//    new doki().Kreiranje(baza);
//    new napomene().Kreiranje(baza);
//    new dob_art().Kreiranje(baza);
//    new kup_art().Kreiranje(baza);
//    new shrab().Kreiranje(baza);
//    new rabati().Kreiranje(baza);
//    new stdoki().Kreiranje(baza);
//    new serbr().Kreiranje(baza);
//    new jedmj().Kreiranje(baza);
//    new norme().Kreiranje(baza);
//    new franka().Kreiranje(baza);
//    new namjena().Kreiranje(baza);
//    new nacotp().Kreiranje(baza);
//    new nacpl().Kreiranje(baza);
//new vtzavtr().Kreiranje(baza);
//new vtrabat().Kreiranje(baza);
  }

  static public void UbijTable(BazaOper baza) {

//    Artikli.ArtikliDrop(baza);
//    Grupart.GrupartDrop(baza);*/
//    Partneri.PartneriDrop(baza);
//    Pjpar.PjparDrop(baza);*/
//  Porezi.PoreziDrop(baza);
//    Sklad.SkladDrop(baza);
//    Doku.DokuDrop(baza);
//  Stdoku.StdokuDrop(baza);
//    Stanje.StanjeDrop(baza);

//  Zavtr.ZavtrDrop(baza);
/*    Agenti.AgentiDrop(baza);
    SEQ.SEQDrop(baza);
    Konta.KontaDrop(baza);
    Orgstruktura.OrgstrukturaDrop(baza);
    Parametri.ParametriDrop(baza);
    Korisnik.KorisnikDrop(baza);
*/
//    Valute.ValuteDrop(baza);
//    Tecajevi.TecajeviDrop(baza);
//    zirorn.zirornDrop(baza);

//new Shzavtr().Dropanje(baza);
//new doki().Dropanje(baza);
//new napomene().Dropanje(baza);
//new dob_art().Dropanje(baza);
//new kup_art().Dropanje(baza);
//new shrab().Dropanje(baza);
//new rabati().Dropanje(baza);
//new stdoki().Dropanje(baza);
//new serbr().Dropanje(baza);
//new jedmj().Dropanje(baza);
//new norme().Dropanje(baza);
//new franka().Dropanje(baza);
//new namjena().Dropanje(baza);
//new nacotp().Dropanje(baza);
//new nacpl().Dropanje(baza);
//new vtzavtr().Dropanje(baza);
//new vtrabat().Dropanje(baza);

  }

 static public boolean DropajTablu(Connection con,String ime_table){

    boolean succ = true;
    String SqlDropajTablu = "drop table " + ime_table ;

    try {
      Stmt=con.createStatement() ;
      Stmt.execute(SqlDropajTablu);
      Stmt.close();
      System.err.println(ime_table+" tabela dropana");

    } catch(SQLException ex )   {
         System.err.println("SQLException: "+ ex.getMessage());
         succ = false;
    }
    return succ;
  }

 static public boolean DropajIndex(Connection con,String ime_indexa){

    boolean succ = true;
    String SqlDropajIndex = "drop index " + ime_indexa ;

    try {
      Stmt=con.createStatement() ;
      Stmt.execute(SqlDropajIndex);
      Stmt.close();
      System.err.println(ime_indexa+" index dropan");

    } catch(SQLException ex )   {
         System.err.println("SQLException: "+ ex.getMessage());
         succ = false;
                                }
    return succ;
  }
}


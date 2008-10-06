/****license*****************************************************************
**   file: Korisnik.java
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

public abstract class Korisnik extends KreirDrop {

 public void setall(){

    /*SqlDefTabela =  "create table Korisnik " +
      "(naziv CHAR(60) CHARACTER SET WIN1250 NOT NULL," +
      "memo CHAR(20) CHARACTER SET WIN1250,"+
      "adresa CHAR(60) CHARACTER SET WIN1250,"+
      "htbroj CHAR(5) CHARACTER SET WIN1250,"+
      "mjesto CHAR(30) CHARACTER SET WIN1250,"+
      "telefon1 CHAR(20) CHARACTER SET WIN1250,"+
      "telefon2 CHAR(20) CHARACTER SET WIN1250,"+
      "telefon3 CHAR(20) CHARACTER SET WIN1250,"+
      "fax      CHAR(20) CHARACTER SET WIN1250,"+
      "maticni  CHAR(13) CHARACTER SET WIN1250,"+
      "ziro CHAR(40) CHARACTER SET WIN1250,"+
      "regnaziv CHAR(60) CHARACTER SET WIN1250,"+
      "os CHAR(10) CHARACTER SET WIN1250,"+
      "serialnum CHAR(10) CHARACTER SET WIN1250,"+
      "danidosp INTEGER,"+
      "danikrpl INTEGER,"+
      "cappl CHAR(8) CHARACTER SET WIN1250,"+
      "protection CHAR(3) CHARACTER SET WIN1250,"+
      "datum date,"+
      "datumoff date,"+
      "usersnr INTEGER,"+
      "startnr INTEGER,"+
      "maxstartnr INTEGER,"+
      "locked CHAR(1) CHARACTER SET WIN1250,"+
      "actnr INTEGER,"+
      "applname CHAR(40) CHARACTER SET WIN1250,"+
      "Primary Key (naziv))" ;*/

    ddl.create("korisnik")
       .addChar("naziv", 60, true)
       .addChar("memo", 20)
       .addChar("adresa", 60)
       .addChar("htbroj", 5)
       .addChar("mjesto", 30)
       .addChar("telefon1", 20)
       .addChar("telefon2", 20)
       .addChar("telefon3", 20)
       .addChar("fax", 20)
       .addChar("maticni", 13)
       .addChar("ziro", 40)
       .addChar("regnaziv", 60)
       .addChar("os", 10)
       .addChar("serialnum", 10)
       .addInteger("danidosp", 4)
       .addInteger("danikrpl", 4)
       .addChar("cappl", 8)
       .addChar("protection", 3)
       .addDate("datum")
       .addDate("datumoff")
       .addInteger("usernr", 4)
       .addInteger("startnr", 4)
       .addInteger("maxstartnr", 4)
       .addChar("locked", 1)
       .addInteger("actnr", 4)
       .addChar("applname", 40)
       .addPrimaryKey("naziv");

    Naziv="Korisnik";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"inazivKorisnik"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on Korisnik (naziv)"} ;
*/

  }
}
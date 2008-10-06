/****license*****************************************************************
**   file: raControlDocs.java
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

import hr.restart.util.VarStr;

import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 *
 *
 *
 * treba  dodati polja u stanje
 *
 * SKAL    --- Prethodni broj kalkulacije
 * TKAL    --- Teku\u0107i broj kalkulacije
 * ITKAL   --- Broj kalkulacije po kome je napravljen zadnji izlaz
 * SITKAL  --- Broj prethodne kalkulacije po kojem je ra\u0111en izlaz
 * BSIZ    --- Broj stavki izlaza napravljenih po ITKAL -u
 * SBSIZ   --- Broj stavki izlaza napravljenih po SITKAL -u
 *
 * u stdoku i stmeskla treba dodati polje
 * SKAL
 * u stdoki i stmeskla treba dodati polje
 * ITKAL
 * SITKAL
 * SBSIZ
 *
 *
 * UNOS KALKULACIJE (ME\u0110USKLADIŠNICE)
 *
 * - postupak je slijede\u0107i ovim redoslijedom :
 *
 *    SKAL (stdoku ili stmeskla kod MES i MEU) = SKAL (stanje)
 *    SKAL (stanje) = TKAL (stanje)
 *    TKAL (stanje) = 'broj novounesene kalkulacije)
 *
 * BRISANJE KALKULACIJE (ME\u0110USKLADIŠNICE)
 *
 * - kod brisanja kalkulacije ((ili stmeskla kod MES i MEU) mora biti
 *   zadovoljeno slijede\u0107e:
 *
 *      TKAL == 'broj kalkulacije za brisanje'
 *                     i (&&)
 *     ITKAL != 'broj kalkulacije za brisanje'
 *
 * ako je to zadovoljeno postupak je slijede\u0107i
 *
 *    TKAL (stanje) = SKAL (stanje)
 *    SKAL (stanje) = SKAL (iz stdoku ili stmeskla kod MES i MEU
 *                          dokumenta koji se briše)
 *
 * ISPRAVAK KALKULACIJE
 *
 * - može se izvršiti samo ako je zadovoljeno slijede\u0107e
 *
 *      TKAL == 'broj kalkulacije za ispravak'
 *                     i (&&)
 *     ITKAL != 'broj kalkulacije za ispravak'
 *
 *
 * UNOS IZLAZNIH DOKUMENATA KOJI DIRAJU ZALIHU
 *
 * - postupak je slijede\u0107i
 *
 *  ako je ITKAL = TKAL
 *
 *     BSIZ (stanje)                            =   BSIZ (stanje) + 1
 *   SITKAL (stdoku ili stmeskla kod MES i MEU) = SITKAL (stanje)
 *    SBSIZ (stdoku ili stmeskla kod MES i MEU) =  SBSIZ (stanje)
 *
 *  ako je ITKAL !=  TKAL
 *
 *   SITKAL (stdoku ili stmeskla kod MES i MEU) = SITKAL (stanje)
 *    SBSIZ (stdoku ili stmeskla kod MES i MEU) =  SBSIZ (stanje)
 *   SITKAL (stanje)                            =  ITKAL (stanje)
 *    SBSIZ (stanje)                            =  BSIZ (stanje)
 *    ITKAL (stanje)                            =   TKAL (stanje)
 *     BSIZ (stanje)                            = 0
 *
 *
 *
 * IZMJENA IZLAZNIH DOKUMENATA KOJI DIRAJU ZALIHU
 *
 *  nepotrebne su dodatne provjere
 *
 * BRISANJE IZLAZNIH DOKUMENATA KOJI DIRAJU ZALIHU
 *
 *  - validacija
 *
 *    ITKAL (stanje) == ITKAL (na izl. dokumentu)
 *
 * ovo je jedina logi\u010Dka neugodnost ovakvog na\u010Dina
 *
 *  - postupak
 *
 *    BSIZ = BSIZ - 1
 *    ako je BSIZ == 0 tada
 *
 *   ITKAL (stanje)  = SITKAL (stanje)
 *   BSIZ (stanje)   = SBSIZ (stanje)
 *   SITKAL (stanje)   = SITKAL (stdoku ili stmeskla kod MES i MEU)
 *   SBSIZ (stanje)    = SBSIZ (stdoku ili stmeskla kod MES i MEU)
 *
 *
 *    Dodatak za prijenos jednog dokumenta u drugog
 *    e sad razmisliti treba li staviti nova polja u stdoki ili
 *    treba staviti u vtprijenos
 *
 *
 *
 */

public class raControlDocs {

  private TypeDoc TD = TypeDoc.getTypeDoc();
  private boolean isNeeded = true;

  private String SKAL   = "";
  //private Timestamp DATZK;
  //  private String TKAL   = "";
//  private String ITKAL  = "";
  private String SITKAL = "";
//  private String myKEY  ="";
//  private short BSIZ   = 0;
  private short SBSIZ  = 0;
  boolean isLastKalkulacija = true;
  boolean isIzlazExist = false;
  boolean isOutOnLastKalkulacija = true;
  boolean isDatumOutOK = true;
  /**
   *  prebacuje u lokalna polja uvijek se mora pozivati prije nekih
   * manipulacija
   * @param stavka .
   */

  public void prepareFields(DataSet stavka){

//    myKEY = getKey(stavka);

    if (TD.isDocStmeskla(stavka.getString("VRDOK"))) {

      SKAL    = stavka.getString("SKAL");
      //DATZK = stavka.getTimestamp("DATDOK");
//      ITKAL   = stavka.getString("ITKAL");
//      SITKAL  = stavka.getString("SITKAL");
//      SBSIZ   = stavka.getShort("SBSIZ");

    }
    else if (TD.isDocStdoku(stavka.getString("VRDOK"))) {
      SKAL    = stavka.getString("SKAL");
      //DATZK = stavka.getTimestamp("DATDOK");
    }
    else if (TD.isDocStdoki(stavka.getString("VRDOK"))) {
//      ITKAL   = stavka.getString("ITKAL");
      SITKAL  = stavka.getString("SITKAL");
      SBSIZ   = stavka.getShort("SBSIZ");
    }
  }

  public String getKey(String cskl,String csklul,String cskliz,
                       String vrdok,String god,int brdok,int rbsid){

    if (cskl==null || cskl.equalsIgnoreCase("")){
      if ((csklul==null || csklul.equalsIgnoreCase(""))){
        return "";
      }
      if ((cskliz==null || cskliz.equalsIgnoreCase(""))){
        return "";
      }
      return  cskliz+"-"+csklul+"-"+vrdok+"-"+god+"-"+brdok+"-"+rbsid;
    }
    return  cskl+"-"+vrdok+"-"+god+"-"+brdok+"-"+rbsid;
  }
  /**
   *
   * @param stavka stavka neovisno koja
   * @return vra\u0107a konkatinirani key
   */

  public String getKey2(DataSet stavka) {
    String returnValue = "";
    try {
      if (TD.isDocMeskla(stavka.getString("VRDOK"))) {
        returnValue = stavka.getString("CSKLIZ")+"-"+
                      stavka.getString("CSKLUL")+"-"+
                      stavka.getString("VRDOK")+"-"+
                      stavka.getString("GOD")+"-"+
                      stavka.getInt("BRDOK")+"-"+
                      stavka.getInt("rbsid");
      }
      else {
        returnValue = stavka.getString("CSKL")+"-"+
                      stavka.getString("VRDOK")+"-"+
                      stavka.getString("GOD")+"-"+
                      stavka.getInt("BRDOK")+"-"+
                      stavka.getInt("rbsid");
      }
    }
    catch (Exception ex) {
      returnValue = stavka.getTableName()+"-"+
                    stavka.getInt("CART");
    }
    return returnValue;
  }


  public static String getKey(DataSet stavka,String[] polja,String tablename) {
    String[] values = new String[polja.length];
    for (int i = 0; i < polja.length; i++) {
      Variant var = new Variant();
      stavka.getVariant(polja[i],var);
      values[i] = var.toString();
    }
    return UniversalKeyToSqlKey(tablename,polja,values);
  }
  
  public static String[] getKeyColumns(String tablename) {
  
      return hr.restart.db.raConnectionFactory.getKeyColumns(
              hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection(),
              tablename);
  }

  public static String getKey(DataSet stavka,String tablename) {
    return getKey(stavka,getKeyColumns(tablename),tablename);
  }


  public static String getKey(DataSet stavka) {

    String imetab = stavka.getTableName();
    if (imetab==null || imetab.equalsIgnoreCase("")) throw new RuntimeException("nema imena tabele u traženju kljuèa");

    // twik za stdoki i stdoku i stmeskla zbog toga sto rbrsid nije u kljuèu
    if (imetab.equalsIgnoreCase("stdoki") ||
        imetab.equalsIgnoreCase("stdoku")){
      return getKey(stavka,new String[] {"cskl","vrdok","god","brdok","rbsid"},imetab);
    } else if (imetab.equalsIgnoreCase("stmeskla")){
      return getKey(stavka,new String[] {"cskliz","csklul","vrdok","god","brdok","rbsid"},imetab);
    } else if (imetab.equalsIgnoreCase("stugovor")){
System.out.println("the kljuè = "+getKey(stavka,new String[] {"cugovor","rbsid"},imetab));    	
    	return getKey(stavka,new String[] {"cugovor","rbsid"},imetab);
    }

    return getKey(stavka,imetab);

  }
  public boolean isDataKalkulOK(java.sql.Timestamp datdok,String key){
    if (!isNeeded) return true;
    java.sql.Timestamp ts = getDateTKAL(key);
    if (ts== null) return true;  // zaobidji provjeru za one koje nema uzdamo se u sreæicu
    return raDateUtil.getraDateUtil().isLessEqual(ts,datdok);
  }
  public boolean isDataKalkulforKalkulOK(java.sql.Timestamp datdok,String key){
    java.sql.Timestamp ts = getDateTKAL(key);
    if (ts== null) return true;  // zaobidji provjeru za one koje nema uzdamo se u sreæicu kao i
                                 // u prethodnom sluèaju

    return raDateUtil.getraDateUtil().isGreaterEqual(datdok,ts);
  }



//  public boolean isDateKalkulforKalkulOK(java.sql.Timestamp datdok,String key){
//
//    if (raDateUtil.getraDateUtil().isLessEqual(hr.restart.util.Util.getNewQueryDataSet
//          ("SELECT max(datdok) as datdok FROM doki,stdoki "+
//           "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "+
//           "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok and itkal='"+key+"'",true).
//          getTimestamp("DATDOK"),datdok)) {
//      return false;
//    }
//    return true;
//  }

  public boolean isDateIzlazOK(java.sql.Timestamp datdok,String key){
    if (key.equalsIgnoreCase("") || key == null) return true;

    QueryDataSet qds1 = hr.restart.util.Util.getNewQueryDataSet
          ("SELECT max(datdok) as datdok FROM doki,stdoki "+
           "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "+
           "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok and itkal='"+key+"'",true);

//qds1.rowCount()!=0 &&
    if (qds1.rowCount()!=0 &&raDateUtil.getraDateUtil().isLess(datdok,qds1.getTimestamp("DATDOK"))) {
      return false;
    }

    QueryDataSet qds2 = hr.restart.util.Util.getNewQueryDataSet
        ("SELECT max(datdok) as datdok FROM meskla,stmeskla WHERE meskla.cskliz = stmeskla.cskliz "+
         "AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok "+
         "AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok AND itkal='"+key+"'",true);

    if (qds2.rowCount()!=0 && raDateUtil.getraDateUtil().isLess(datdok,qds2.getTimestamp("DATDOK"))) {
      return false;
    }

    return true;
  }



  private java.sql.Timestamp getDateTKAL(String key){

    HashMap key_parts = getHashMapKey(key);
    String table = "";
    if (key_parts==null) {
//System.out.println("Nema ili ne valja kljuèèèèè");
      return null;
    }
    if (TD.isDocStdoku((String) key_parts.get("VRDOK"))){
      table="doku";
    } else if (TD.isDocStdoki((String) key_parts.get("VRDOK"))){
      table="doki";
    } else if (TD.isDocStmeskla((String) key_parts.get("VRDOK"))){
      table="meskla";
    }
    return hr.restart.util.Util.getNewQueryDataSet("select datdok from "+table+" where "+getSqlPartZaglavlje(key_parts),true).getTimestamp("DATDOK");

//    return raDateUtil.getraDateUtil().isLessEqual(getDatumKalkulacije(table,
//        getSqlPartZaglavlje(key_parts)),datdok);

  }
  public String getSqlPartZaglavlje(HashMap hm){
    String forReturn ="";
    if (hm.containsKey("CSKLIZ")) {
      return "cskliz='"+(String) hm.get("CSKLIZ")+"' and csklul='"+(String) hm.get("CSKLUL")+"' and "+
             "vrdok='"+(String) hm.get("VRDOK")+"' and god='"+(String) hm.get("GOD")+"' and "+
             "brdok="+(String) hm.get("BRDOK");
    } else {
      return "cskl='"+(String) hm.get("CSKL")+"' and "+
             "vrdok='"+(String) hm.get("VRDOK")+"' and god='"+(String) hm.get("GOD")+"' and "+
             "brdok="+(String) hm.get("BRDOK");
    }
  }

//  public java.sql.Timestamp getDatumKalkulacije(String table,String uvjet){
//
//  }

  public static java.util.HashMap getHashMapKey(String key){

    java.util.HashMap hm = new java.util.HashMap();
    java.util.StringTokenizer st = new java.util.StringTokenizer(key,"-");
    String forReturn="";

     String[] string_polje = new String[st.countTokens()];

     for (int i = 0;i<string_polje.length;i++){
       string_polje[i]=st.nextToken();
     }
    try {
      if (TypeDoc.getTypeDoc().isDocMeskla(string_polje[2])){
        hm.put("CSKLIZ",string_polje[0]);
        hm.put("CSKLUL",string_polje[1]);
        hm.put("VRDOK",string_polje[2]);
        hm.put("GOD",string_polje[3]);
        hm.put("BRDOK",string_polje[4]);
        hm.put("RBRSID",string_polje[5]);
      } else {
        hm.put("CSKL",string_polje[0]);
        hm.put("VRDOK",string_polje[1]);
        hm.put("GOD",string_polje[2]);
        hm.put("BRDOK",string_polje[3]);
        hm.put("RBRSID",string_polje[4]);
      }
    }
    catch (Exception ex) {
//      ex.printStackTrace();
System.out.println("I ovdje je neispravan kljuc "+key);
      return null;
    }
     return hm;
   }



  /**
   *
   * - postupak je slijede\u0107i ovim redoslijedom :
   *
   *    SKAL (stdoku ili stmeskla kod MES i MEU) = SKAL (stanje)
   *    SKAL (stanje) = TKAL (stanje)
   *    TKAL (stanje) = 'broj novounesene kalkulacije)
   *
   * @param stavka .
   * @param stanje .
   */

  public void unosKalkulacije(DataSet stavka, DataSet stanje) {

    stavka.setString("SKAL",stanje.getString("SKAL"));
    stanje.setString("SKAL",stanje.getString("TKAL"));
    stanje.setString("TKAL",getKey(stavka));
    //stanje.setTimestamp("DATZK", stavka.getTimestamp("DATDOK"));

  }

  /**
   *   Testiranje za brisanje i izmjenu kalkulacije
   *
   *      TKAL == 'broj kalkulacije za brisanje'
   *                     i (&&)
   *     ITKAL != 'broj kalkulacije za brisanje'
   *
   * @param stavka4del .
   * @param stanje .
   * @return true ako je ok
   */

  public boolean testKalkulacije(DataSet stavka4del, DataSet stanje) {

    String key ="";
    if (TD.isDocMeskla(stavka4del.getString("VRDOK"))) {
      key = getKey(stavka4del,new String[] {"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK","rbsid"},"stmeskla");
    }
    else if (TD.isDocStdoki(stavka4del.getString("VRDOK"))) {
      key = getKey(stavka4del,new String[] {"CSKL","VRDOK","GOD","BRDOK","rbsid"},"stdoki");
    } else {
      key = getKey(stavka4del,new String[] {"CSKL","VRDOK","GOD","BRDOK","rbsid"},"stdoku");
    }
//System.out.println("getKey(stavka4del) "+getKey(stavka4del));
//System.out.println("stanje.getString(TKAL) "+stanje.getString("TKAL"));
//System.out.println("getKey(stavka4del).equals(stanje.getString(ITKAL) "+key);
    if (isNeeded) {
      isLastKalkulacija = key.equals(stanje.getString("TKAL"));
      isIzlazExist = key.equals(stanje.getString("ITKAL"));
    } else {
      return true;
    }
    return (isLastKalkulacija && !isIzlazExist);
  }

  /**
   *
   * pozvati prije obavezno     prepareFields();
   *    TKAL (stanje) = SKAL (stanje)
   *    SKAL (stanje) = SKAL (iz stdoku ili stmeskla kod MES i MEU
   *                          dokumenta koji se briše)
   *
   * @param stavka4del .
   * @param stanje .
   */

  public void brisanjeKalkulacije(DataSet stanje) {

    stanje.setString("TKAL",stanje.getString("SKAL"));
    stanje.setString("SKAL",SKAL);

  }

  /**
   * UNOS IZLAZNIH DOKUMENATA KOJI DIRAJU ZALIHU
   *
   * - postupak je slijede\u0107i
   *
   *  ako je ITKAL = TKAL
   *
   *     BSIZ (stanje)                            =   BSIZ (stanje) + 1
   *   SITKAL (stdoku ili stmeskla kod MES i MEU) = SITKAL (stanje)
   *    SBSIZ (stdoku ili stmeskla kod MES i MEU) =  SBSIZ (stanje)
   *
   *  ako je ITKAL !=  TKAL
   *
   *   SITKAL (stdoku ili stmeskla kod MES i MEU) = SITKAL (stanje)
   *    SBSIZ (stdoku ili stmeskla kod MES i MEU) =  SBSIZ (stanje)
   *   SITKAL (stanje)                            =  ITKAL (stanje)
   *    SBSIZ (stanje)                            =  BSIZ (stanje)
   *    ITKAL (stanje)                            =   TKAL (stanje)
   *     BSIZ (stanje)                            = 0
   *
   *
   * @param stavka .
   * @param stanje .
   */

  public void unosIzlaz(DataSet stavka, DataSet stanje ){

    stavka.setString("SITKAL",stanje.getString("SITKAL"));
    stavka.setShort("SBSIZ",stanje.getShort("SBSIZ"));

    if (stanje.getString("ITKAL").equals(stanje.getString("TKAL"))) {
      stanje.setShort("BSIZ",(short) (stanje.getShort("BSIZ")+1));
    }
    else {
      stanje.setString("SITKAL",stanje.getString("ITKAL"));
      stanje.setShort("SBSIZ",stanje.getShort("BSIZ"));
      stanje.setString("ITKAL",stanje.getString("TKAL"));
      stanje.setShort("BSIZ",(short) 1);
    }
    stavka.setString("ITKAL",stanje.getString("ITKAL"));
  }

  /**
   * BRISANJE IZLAZNIH DOKUMENATA KOJI DIRAJU ZALIHU
   *
   *  - validacija
   *
   *    ITKAL (stanje) == ITKAL (na izl. dokumentu)
   *
   *
   * @param stavka .
   * @param stanje .
   * @return true ako je ok
   */
  public boolean testIzlaz4Del(DataSet stavka, DataSet stanje) {

    if (isNeeded) {
      isOutOnLastKalkulacija =  (stanje.getString("TKAL").equals(stavka.getString("ITKAL")));
    }
    return isOutOnLastKalkulacija ;
  }

  /**
   * provjera datuma zadnje kalkulacije i datuma dokumenta koji ne smije biti
   * manji od datuma zadnje kalkulacije
   *
   * @param datum
   * @param stanje
   * @return
   */

//
//  public boolean testIzlaz4Unos(java.sql.Timestamp datum, DataSet stanje){
//
//
//    if (isNeeded) {
//      isDatumOutOK = datum.before(stanje.getTimestamp("DATKALK"));
//    }
//    return isDatumOutOK;
//
//
//  }

  /**
   *
   *  - postupak
   *
   * pozvati prije obavezno     prepareFields();
   *    BSIZ = BSIZ - 1
   *    ako je BSIZ == 0 tada
   *
   *   ITKAL (stanje)  = SITKAL (stanje)
   *   BSIZ (stanje)   = SBSIZ (stanje)
   *   SITKAL (stanje)   = SITKAL (stdoku ili stmeskla kod MES i MEU)
   *   SBSIZ (stanje)    = SBSIZ (stdoku ili stmeskla kod MES i MEU)
   *
   * @param stavka4del .
   * @param stanje .
   */
  public void brisanjeIzlaz(DataSet stanje) {

      stanje.setShort("BSIZ",(short) (stanje.getShort("BSIZ")-1));
      if (stanje.getShort("BSIZ")==0){
        stanje.setString("ITKAL",stanje.getString("SITKAL"));
        stanje.setShort("BSIZ",stanje.getShort("SBSIZ"));
        stanje.setString("SITKAL",SITKAL);
        stanje.setShort("SBSIZ",SBSIZ);
      }
  }

  /**
   *
   * Ako je metoda naprimjer po nabavnim cijenama onda nema
   * ograni\u010Denja kod brisanja i izmjene dokumenata
   * @param isNeeded boolean
   */

  public void setisNeeded(boolean isNeeded) {
    this.isNeeded = isNeeded;
  }

  public String errorMessage() {
    String returnMassage="";
    if (!isLastKalkulacija) returnMassage="Ovo nije zadnja kalkulacija. Nemogu\u0107a akcija !";
    else if (isIzlazExist) returnMassage="Postoje izlazni dokumenti po ovoj kalkulaciji. Nemogu\u0107a akcija !";
    else if (!isOutOnLastKalkulacija) returnMassage="Ovaj dokument nije izašao po zadnjoj kalkulaciji. Nemogu\u0107a akcija !";
    else if (!isDatumOutOK) returnMassage="Datum ovog dokumenta je manji od zadnje kalkulacije za ovaj artikl. Nemogu\u0107a akcija !";
    return returnMassage;
  }

  // CACHING (ab.f)
  
  private static HashMap colSizes = new HashMap();

  public static String UniversalKeyToSqlKey(String tableName,String[] keys,String[] values){
    if (keys.length != values.length) throw new RuntimeException("keys.length != values.length");

    VarStr var = new VarStr();
    for (int i=0;i<keys.length;i++){
      try {
        // provjeri cache
        String qcol = tableName + "." + keys[i];
        Integer csize = (Integer) colSizes.get(qcol);
        if (csize == null) {  // nema
          java.sql.ResultSet comkys = hr.restart.baza.dM.getDatabaseConnection()
                    .getMetaData().getColumns(null,null,tableName, keys[i]);          
          if (comkys.next())  
            if (comkys.getShort("DATA_TYPE")==java.sql.Types.CHAR ||
                comkys.getShort("DATA_TYPE")==java.sql.Types.LONGVARCHAR ||
                comkys.getShort("DATA_TYPE")==java.sql.Types.VARCHAR)
              csize = new Integer(comkys.getInt("COLUMN_SIZE"));
          if (csize == null) csize = new Integer(-1);
          colSizes.put(qcol, csize);  // zapamti za kasnije
        }
        if (csize.intValue() < 0) var = var.append(values[i]).append("-");
        else var = var.append(hr.restart.gk.prepare2Zim.prepareString
            (values[i],csize.intValue(),true,' ')).append("-");
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return "baga";
      }
    }
    return var.toString();
  }

  public static String getUniversalKey(HashMap hm,String[] keys,String table){

    String[] values = new String[keys.length];
    for (int i=0;i<keys.length;i++) {
      if (hm.containsKey(keys[i])) {
        values[i] = (String) hm.get(keys[i]);
      } else {
        throw new RuntimeException("Ne valja jer zadani kljuè se ne sadržava u tabeli koji ste poslali ");
      }
    }
    return UniversalKeyToSqlKey(table,keys,values);
  }

  /*
  - Napravili smo kalkulaciju
  - Napravili smo izlazni dokument
  - Dosli su nam dodatni papiri i moramo popraviti kalkulaciju ali kako kad je vec nastalo
  razduzenje
  - E tu je nevolja jer trebamo popraviti
    a) Sve izlaze koji su nastali po toj kalkulaciji (cijene razduženja, iznose .....)
    b) ažurirati stanje

  Ogranièenja ::
  - Mislim da trebam (nisam najsigurniji ) omoguæiti ispravku samo zadnje kalkulacije i njenih izlaza
  - Paziti na knjiženja, prenešenosti, statuse etc..
  Izmjena dokumenata koji su nastali nakon kalkulacije
  Problem su meðuskladišnice koje su negdje drugdje ulazni dokument i tamo se onda mora napraviti isto !!!
  Ili ako ima takvo sto ne moze se promjeniti

  */

  /*
  provjeriti statuse da nije knjižen, prenešen
  */

  public boolean tesatChangeIzlazPossible(){
    return true;
  }


  public void chAllIzlaz(QueryDataSet origigi){

    QueryDataSet qds =
        hr.restart.util.Util.getNewQueryDataSet("SELECT * from stdoki where itkal=",true);

    QueryDataSet qdsStanje = hr.restart.util.Util.getNewQueryDataSet
        ("SELECT * from stdoki where itkal=",true);

    java.math.BigDecimal oldNCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal newNCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal oldVCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal newVCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal oldMCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal newMCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal oldZCprice = new java.math.BigDecimal("0.00");
    java.math.BigDecimal newZCprice = new java.math.BigDecimal("0.00");

    for(qds.first();qds.inBounds();qds.next()){
      qds.setBigDecimal("NC",newNCprice);
      qds.setBigDecimal("VC",newVCprice);
      qds.setBigDecimal("MC",newMCprice);
      qds.setBigDecimal("ZC",newZCprice);
    }

  }
}
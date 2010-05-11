/****license*****************************************************************
**   file: OrgStr.java
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
package hr.restart.zapod;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.zirorn;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class OrgStr {
  static OrgStr orgstr;
  com.borland.dx.sql.dataset.QueryDataSet knjigovodstva;
  com.borland.dx.sql.dataset.QueryDataSet knjigziro;
  com.borland.dx.sql.dataset.QueryDataSet orgstr2;
  com.borland.dx.dataset.StorageDataSet porgs = null;
  //com.borland.dx.dataset.DataSetView dsw;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.lookupData LD = hr.restart.util.lookupData.getlookupData();
  hr.restart.util.Util Ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();
  private String lastSearchedKnjig = "";
  private java.beans.PropertyChangeSupport chSupp;
  protected OrgStr() {
    try {
      dm.getOrgstruktura().open();  
    } catch (Exception ex) {
    }
  }
  public static OrgStr getOrgStr() {
    if (orgstr==null) orgstr = new OrgStr();
    return orgstr;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getKnjigovodstva() {
    if (knjigovodstva==null) {
      knjigovodstva = new com.borland.dx.sql.dataset.QueryDataSet();
      try {
        String knjigsql = "SELECT * FROM ORGSTRUKTURA WHERE NALOG='1'";
      	Aus.setFilter(knjigovodstva, knjigsql);        
        knjigovodstva.setColumns(dm.getOrgstruktura().cloneColumns());
        knjigovodstva.open();
        VarStr andNotIn = new VarStr(" AND CORG NOT IN (");
        boolean rightDefined = false;
        for (knjigovodstva.first(); knjigovodstva.inBounds(); knjigovodstva.next()) {
          if (!raUser.getInstance().canAccessTable(
              knjigovodstva.getTableName(),knjigovodstva.getString("CORG"),"P")) {
            andNotIn.append("'"+knjigovodstva.getString("CORG")+"',");
            rightDefined = true;
          }
        }
        andNotIn = new VarStr(andNotIn).chop().append(')');
System.out.println("not in = "+andNotIn+", rightDefined = "+rightDefined);        
        if (rightDefined) {
System.out.println("setting filter "+knjigsql+andNotIn);          
          Aus.setFilter(knjigovodstva, knjigsql + andNotIn);
          knjigovodstva.open();
        }
      } catch (Exception e) {}
    } else {
//      knjigovodstva.refresh();
    }
    return knjigovodstva;
  }
  /**
   * Vraca sve ziro racune od zadanog knjigovodstva u obliku querydataseta
   */
  public com.borland.dx.sql.dataset.QueryDataSet getKnjigziro(String cKnjig) {
    knjigziro = zirorn.getDataModule().getFilteredDataSet(
        Condition.equal("CORG", cKnjig));
    knjigziro.open();
    knjigovodstva.open();
    return knjigziro;
  }
  /**
   * Vraca QueryDataSet kreiran prethodnim getKnjigziro(String cKnjig).
   * Pazi!! Ako prije nije pozvana metoda getKnjigziro(String cKnjig) vraca null ili neko cudo od prije
   * (zbog statickog gettera)
   */
  public com.borland.dx.sql.dataset.QueryDataSet getCurrentKnjigziro() {
    return knjigziro;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOrgstr2() {
    if (orgstr2==null) {
      orgstr2 = new com.borland.dx.sql.dataset.QueryDataSet();
      try {
        orgstr2.setQuery(dm.getOrgstruktura().getQuery());
        orgstr2.setColumns(dm.getOrgstruktura().cloneColumns());
        orgstr2.open();
      } catch (Exception e) {e.printStackTrace();}
    } else {
//      orgstr2.refresh();
    }
    return orgstr2;
  }

  public com.borland.dx.dataset.StorageDataSet getOrgstrFromCurrKnjig() {
    String strKNJIG = dlgGetKnjig.getKNJCORG();
    return getOrgstrFromKnjig(strKNJIG);
  }
  public com.borland.dx.dataset.StorageDataSet getOrgstrAndCurrKnjig() {
    String strKNJIG = dlgGetKnjig.getKNJCORG();
    return getOrgstrAndKnjig(strKNJIG);
  }
  public com.borland.dx.dataset.StorageDataSet getOrgstrFromKnjig(String knjig) {
    return getOrgstrFromKnjig(knjig,false);
  }
  public com.borland.dx.dataset.StorageDataSet getOrgstrAndKnjig(String knjig) {
    return getOrgstrFromKnjig(knjig,true);
  }
  private com.borland.dx.dataset.StorageDataSet getOrgstrFromKnjig(String knjig,boolean andknjig) {
    if (!knjig.equals(lastSearchedKnjig)) {
      porgs = new com.borland.dx.dataset.StorageDataSet();
      lastSearchedKnjig = knjig;
      com.borland.dx.dataset.Column[] cols = Ut.cloneCols(dm.getOrgstruktura().getColumns());
      porgs.close();
      porgs.setColumns(cols);
      porgs.setTableName("ORGSTRUKTURA");
      porgs.open();
      addPripOrgs(porgs,knjig,Ut.cloneCols(cols));
    }
    if (andknjig) {
      addOrgToPorgs(knjig);
    } else {
      rmvOrgFromPorgs(knjig);
    }
    return porgs;
  }
  private void addOrgToPorgs(String corg) {
    if (LD.raLocate(porgs,new String[] {"CORG"},new String[] {corg})) return;
    if (LD.raLocate(dm.getOrgstruktura(),new String[] {"CORG"},new String[] {corg})) {
      porgs.insertRow(false);
      dm.getOrgstruktura().copyTo(porgs);
      porgs.post();
    }
  }
  private void rmvOrgFromPorgs(String corg) {
    if (LD.raLocate(porgs,new String[] {"CORG"},new String[] {corg})) {
      porgs.deleteRow();
    }
  }
  
  public String getInQuery(com.borland.dx.dataset.DataSet _orgs) {
    return getInQuery(_orgs, "corg");
  }

  public String getInQuery(com.borland.dx.dataset.DataSet _orgs, String col) {
      /*
      SQL error code = -901
      Implementation limit exceeded
      too many values (more than 1500) in member list to match against
      */
      int cnt = 1;
      String in = "(";
      _orgs.first();
      do {
        if (cnt >= Condition.MAXINQUERY) {
          in = in.substring(0,in.length()-1)+") OR "+col+" in (";
          cnt=0;
        }        
        in = in+"'"+_orgs.getString("CORG")+"',";
        cnt++;
      } while (_orgs.next());
      in = in.substring(0,in.length()-1)+")";
//      System.err.println("*** I N *** "+in);
      return in;
  }

  private void addPripOrgs(com.borland.dx.dataset.StorageDataSet prgs,String corg,com.borland.dx.dataset.Column[] cols) {
    com.borland.dx.sql.dataset.QueryDataSet pomset = new com.borland.dx.sql.dataset.QueryDataSet();
    Aus.setFilter(pomset, "SELECT * FROM orgstruktura where PRIPADNOST = '"+corg+"' order by corg");    
    pomset.setColumns(cols);
    pomset.open();
    if (pomset.getRowCount()==0) return;
    pomset.first();
    do {
      if (pomset.getString("CORG").equals(corg))
        if (!pomset.next()) break;
      prgs.insertRow(false);
      pomset.copyTo(prgs);
      addPripOrgs(prgs,pomset.getString("CORG"),Ut.cloneCols(cols));
    } while (pomset.next());
    pomset.close();
    pomset = null;
  }

  private boolean addZiroQuestion(String strCKnjig, String strZiro) {
    int answ = javax.swing.JOptionPane.showOptionDialog(
      null,
      "Želite li dodati žiro ra\u010Dun "+strZiro+" za knjigovodstvo "+strCKnjig+"?",
      "Organizacijske jedinice",
      javax.swing.JOptionPane.YES_NO_OPTION,
      javax.swing.JOptionPane.QUESTION_MESSAGE,
      null,new String[] {"Da","Ne"},"Ne");
    return (answ==javax.swing.JOptionPane.YES_OPTION);
  }
  private boolean checkZiro(String strCKnjig, String strZiro) {
    return hr.restart.util.Valid.getValid().chkExistsSQL(getCurrentKnjigziro(),"ZIRO",strZiro);
  }
/**
 * Dodaje ziro racun iz parametara uz pitanje useru i sve ostale pocasti
 */
  public boolean addZiro(String strCKnjig, String strZiro) {
    if (!isKnjigovodstvo()) {
System.out.println("addZiro!isKnjigovodstvo() return true");
      return true;
    }
    if (checkZiro(strCKnjig, strZiro)) {
System.out.println("checkZiro(strCKnjig, strZiro) return true");
      return true;
    }
    if (!addZiroQuestion(strCKnjig,strZiro)) {
System.out.println("!addZiroQuestion(strCKnjig,strZiro) return false");
      return true;
    }
    try {
      dm.getZirorn().open();
      dm.getZirorn().insertRow(true);
      dm.getZirorn().setString("CORG",strCKnjig);
      dm.getZirorn().setString("ZIRO",strZiro);
      dm.getZirorn().post();
      dm.getZirorn().saveChanges();
System.out.println("try-addZiro( dodano return true" );
      return true;
    } catch (Exception e) {
System.out.println("try-addZiro( exception return false" );
e.printStackTrace();
      return false;
    }
  }
/**
 * da li je pripadnost = org jedinici na tekucem slogu orgstruktura
 */
  public boolean pripEqualsCorg() {
    return dm.getOrgstruktura().getString("CORG").equals(dm.getOrgstruktura().getString("PRIPADNOST"));
  }
/**
 * da li je tekuca jedinica knjigovodstvo, odnosno da li je nalog = 0
 */
  public boolean isKnjigovodstvo() {
    return dm.getOrgstruktura().getString("NALOG").equals("1");
  }
/**
 * Vra\u0107a knjigovodstvo od pripadajuce org jedinice
 */
  public String getPripKnjig(String strCorg) {
    dm.getOrgstruktura().open();
    DataSet dsw = dm.getOrgstruktura().cloneDataSetView();
    dsw.open();
    String ret = getUpperOrg(dsw, strCorg);
    dsw.removeRowFilterListener(null);
    dsw.close();
    return ret;
  }
  private String getUpperOrg(DataSet dsw, String strCorg) {
    if (LD.raLocate(dsw,new String[] {"CORG"},new String[]{strCorg})) {
      if (dsw.getString("NALOG").equals("1")) return strCorg;
      return getUpperOrg(dsw, dsw.getString("PRIPADNOST"));
    }

    return strCorg;
  }
  
  public static String getZiroForCorg(String corg) {
    DataSet corgs = Orgstruktura.getDataModule().getTempSet();
    corgs.open();
    while (lookupData.getlookupData().raLocate(corgs, "CORG", corg)) {
      String prip = corgs.getString("PRIPADNOST");
      if (corgs.getString("ZIRO").length() > 0 || corg.equals(prip))
        return corgs.getString("ZIRO");
      corg = prip;
    }
    return null;
  }
  
  public static String[] getBranchCorgs(String strCorg) {
    /**@todo: pronaci sve org jedinice koje su u toj grani znaci tu i sve vise */
    java.util.HashSet corgs = new java.util.HashSet();
    hr.restart.baza.dM.getDataModule().getOrgstruktura().open();
    String _corg = strCorg;
    corgs.add(_corg);
    while (true) {
      if (!hr.restart.util.lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getOrgstruktura(), "CORG", _corg)) break;
      _corg = hr.restart.baza.dM.getDataModule().getOrgstruktura().getString("PRIPADNOST");
      if (_corg.equals(getKNJCORG())) {
        corgs.add(_corg);
        break;
      }
      corgs.add(_corg);
    }
    return (String[])corgs.toArray(new String[0]);
  }
  
  public static String getKNJCORG() {
    return dlgGetKnjig.getKNJCORG();
  }
  public static String getKNJCORG(boolean upit) {
    return dlgGetKnjig.getKNJCORG(upit);
  }

  private java.beans.PropertyChangeSupport getChangeSupport() {
    if (chSupp == null) chSupp = new java.beans.PropertyChangeSupport(this);
    return chSupp;
  }

  /**
   * dodati raKnjigChangeListener ako treba refreshati koji query kad se promijeni knjigovodstvo
   * evo primjera iz hr.restart.util.startFrame gdje se promjenom knjigovodstva mijenja title startFramea:
   * <pre>
   * hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
   *     new hr.restart.zapod.raKnjigChangeListener() {
   *       public void knjigChanged(String oldKnjig,String newKnjig) {
   *         startFrame.this.setStartFrameTitle();
   *       }
   *     }
   * );
   * </pre>
   * @param lis
   */
  public void addKnjigChangeListener(raKnjigChangeListener lis) {
    getChangeSupport().addPropertyChangeListener("knjig",lis);
  }

  public void removeKnjigChangeListener(raKnjigChangeListener lis) {
    if (chSupp == null) return;
    getChangeSupport().removePropertyChangeListener("knjig",lis);
    if (!getChangeSupport().hasListeners("knjig")) chSupp = null;
  }

  public void fireKnjigChanged(String oldKnjig, String newKnjig) {
    if (chSupp == null) return;
    getChangeSupport().firePropertyChange("knjig",oldKnjig,newKnjig);
  }
  //test
  public static void main(String[] args) {
    String[] ing = OrgStr.getBranchCorgs("1");
    hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
    ST.prn(ing);
  }
}
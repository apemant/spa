/****license*****************************************************************
**   file: frmArhiviranje.java
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
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulorg;
import hr.restart.baza.Kumulrad;
import hr.restart.baza.Odbici;
import hr.restart.baza.Odbiciobr;
import hr.restart.baza.Primanjaobr;
import hr.restart.baza.Prisutobr;
import hr.restart.baza.RSPeriodobr;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.Vrsteodb;
import hr.restart.baza.Vrsteprim;
import hr.restart.db.raPreparedStatement;
import hr.restart.db.raVariant;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import javax.swing.JOptionPane;

import hr.restart.util.VarStr;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmArhiviranje extends frmObradaPL {
  boolean suc;
  lookupData ld = lookupData.getlookupData();
  raPreparedStatement addKUMULORGARH = new raPreparedStatement("kumulorgarh", raPreparedStatement.INSERT);
  raPreparedStatement addKUMULRADARH = new raPreparedStatement("kumulradarh", raPreparedStatement.INSERT);
  raPreparedStatement addPRIMANJAARH = new raPreparedStatement("primanjaarh", raPreparedStatement.INSERT);
  raPreparedStatement addODBICIARH = new raPreparedStatement("odbiciarh", raPreparedStatement.INSERT);
  raPreparedStatement addPRISUTARH = new raPreparedStatement("prisutarh", raPreparedStatement.INSERT);
  raPreparedStatement addRSPERIODARH = new raPreparedStatement("rsperiodarh", raPreparedStatement.INSERT);
  public frmArhiviranje() {
    
  }
/*  public void componentShow() {
    tds.open();
    if (tds.getRowCount()==0) {
      tds.insertRow(true);
    }
    jlrCorg.setText("");
    jlrCorg.emptyTextFields();
    setEnable(false);
    jlrCorg.requestFocus();
  }*/
  public void okPress() {
    suc=makeTransaction();
    if (!suc) return;
    raObracunPL obracun = raObracunPL.getInstance();
    raIniciranje rin = raIniciranje.getInstance();

    obracun.initObracun(tds.getShort("GODINA"),tds.getShort("MJESEC"),tds.getShort("RBR"),tds.getString("CORG"),tds.getTimestamp("DATUM"));
    boolean ponsucc = obracun.ponobracun();
    rin.ponistavanje(tds.getString("CORG"));
    dm.getOrgpl().refresh();
  }
  private boolean makeTransaction() {
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        //polja za arhiviranjee iz drugih tabela
        String[] orgsplCols = new String[] {"copcine", "nacobrs", "nacobrb", 
          "satimj", "osnkoef", "satnorma", "datumispl", "brojdana", "stopak", "parametri"};
        String[] parametriplCols = new String[] {"minpl", "minosdop", "osnpor1",
          "osnpor2", "osnpor3", "osnpor4", "osnpor5"};
        String[] radniciplCols = new String[] {"cradmj", "css", "cvro", "cisplmj",
          "copcine", "rsinv", "rsoo", "brutosn", "brutdod", "brutmr", "brutuk", 
          "godstaz", "stopastaz", "datstaz", "podstaz", "datpodstaz", "nacobrb", 
          "koef", "koefzar", "oluk", "olos", "clanomf", "corg", "parametri"};
        String[] vrsteprimCols = new String[] {"cobr", "cosn", "rsoo", "rnalog", 
          "regres", "cgrprim", "cvrparh", "stavka", "parametri"};
        String[] odbiciKey = new String[] {"cvrodb", "ckey", "ckey2", "rbrodb"};
        String[] odbiciCols = new String[] {"pnb1", "pnb2", "iznos", "stopa", 
          "datpoc", "datzav", "glavnica", "rata", "saldo", "stavka"};
        String[] vrsteodbCols = new String[] {"nivoodb", "tipodb", "vrstaosn", 
          "osnovica", "cpov", "parametri"};
        //tabele
        QueryDataSet vrsteprim = Vrsteprim.getDataModule().getTempSet();
        vrsteprim.open();
        QueryDataSet vrsteodb = Vrsteodb.getDataModule().getTempSet();
        vrsteodb.open();
        
        try {
          QueryDataSet kumulorg = Kumulorg.getDataModule()
                    .getTempSet("CORG in "+OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig()));
          kumulorg.open();
          for (kumulorg.first(); kumulorg.inBounds(); kumulorg.next()) {//loop kumulorg
            String currcorg = kumulorg.getString("CORG");
            raIniciranje.getInstance().posOrgsPl(kumulorg.getString("CORG"));//pos orgpl
            dm.getParametripl().open();
            dm.getParametripl().first(); //pos parametripl
            //kumulorgArh
            addKUMULORGARH.setValues(kumulorg);
            setValuesObrada(addKUMULORGARH);
            setValues(addKUMULORGARH, orgsplCols, dm.getOrgpl());
            setValues(addKUMULORGARH, parametriplCols, dm.getParametripl());
            //datum isplate
            addKUMULORGARH.setTimestamp("DATUMISPL", tds.getTimestamp("DATUM"), false);
            setOjFor(addKUMULORGARH,null,1);
            addKUMULORGARH.execute();
            
            //kumulradarh
            QueryDataSet radnicipl = Radnicipl.getDataModule().getTempSet(Condition.equal("CORG", currcorg)+" AND cvro = '"+kumulorg.getString("CVRO")+"'");
            radnicipl.open();
            for (radnicipl.first(); radnicipl.inBounds(); radnicipl.next()) {
              Condition condCradnik = Condition.equal("CRADNIK",radnicipl);
              System.out.println("condcradnik :" + condCradnik);
              QueryDataSet ds = Kumulrad.getDataModule().getTempSet(condCradnik);
              ds.open();
              if (ds.getRowCount() == 0) continue;
              addKUMULRADARH.setValues(ds);
              setValuesObrada(addKUMULRADARH);
              setValues(addKUMULRADARH, radniciplCols, radnicipl);
              setOjFor(addKUMULRADARH, radnicipl.getString("CRADNIK"), 2);
              addKUMULRADARH.execute();
              addKUMULRADARH.clearParameters();//RJESAVA GADAN BUG AKO NEMA ISPLATE NEGO SAMO EVIDENCIJA SATI (PORODILJSKI)
              //primanjaarh
              QueryDataSet primanjaobr = Primanjaobr.getDataModule().getTempSet(condCradnik);
              primanjaobr.open();
              for (primanjaobr.first(); primanjaobr.inBounds(); primanjaobr.next()) {
                addPRIMANJAARH.clearParameters();
                addPRIMANJAARH.setValues(primanjaobr);
                setValuesObrada(addPRIMANJAARH);
                if (!ld.raLocate(vrsteprim, "CVRP", Short.toString(primanjaobr.getShort("CVRP")))) 
                  throw new Exception("Nepoznata vrsta primanja "+primanjaobr.getString("CVRP"));
                //setValues(addPRIMANJAARH, vrsteprimCols, primanjaobr);//UFF!
                setValues(addPRIMANJAARH, vrsteprimCols, vrsteprim);
                setOjFor(addPRIMANJAARH, radnicipl.getString("CRADNIK"), 3);
                addPRIMANJAARH.execute();
              }
              
              //odbiciarh
              QueryDataSet odbiciobr = Odbiciobr.getDataModule().getTempSet(condCradnik);
              odbiciobr.open();
              for (odbiciobr.first(); odbiciobr.inBounds(); odbiciobr.next()) {
                addODBICIARH.setValues(odbiciobr);
                setValuesObrada(addODBICIARH);
                if (!ld.raLocate(vrsteodb, "CVRODB", Short.toString(odbiciobr.getShort("CVRODB")))) 
                  throw new Exception("Nepoznata vrsta odbitka "+odbiciobr.getString("CVRODB"));
                setValues(addODBICIARH, vrsteodbCols, vrsteodb);
                QueryDataSet odbici1 = Odbici.getDataModule().getTempSet(Condition.whereAllEqual(odbiciKey, odbiciobr));
                odbici1.open();
                odbici1.first();
                if (odbici1.getBigDecimal("SALDO").signum()!=0) {//potrebno je azurirati saldo (valjda)
                	odbici1.setBigDecimal("SALDO",odbiciobr.getBigDecimal("SALDO"));
                	raTransaction.saveChanges(odbici1);
                }
                setValues(addODBICIARH, odbiciCols, odbici1);
                setOjFor(addODBICIARH, radnicipl.getString("CRADNIK"), 4);
                setOjForOdbici(addODBICIARH, radnicipl.getString("CRADNIK"), vrsteodb.getString("NIVOODB"));
                addODBICIARH.execute();
              }
              
              //PRISUTARH
              QueryDataSet prisutobr = Prisutobr.getDataModule().getTempSet(condCradnik);
              prisutobr.open();
              for (prisutobr.first(); prisutobr.inBounds(); prisutobr.next()) {
                addPRISUTARH.setValues(prisutobr);
                setValuesObrada(addPRISUTARH);
                setOjFor(addPRISUTARH, radnicipl.getString("CRADNIK"), 5);
                addPRISUTARH.execute();
              }

              //RSPERIODARH
              QueryDataSet rsperiodobr = RSPeriodobr.getDataModule().getTempSet(condCradnik);
              rsperiodobr.open();
              for (rsperiodobr.first(); rsperiodobr.inBounds(); rsperiodobr.next()) {
                addRSPERIODARH.setValues(rsperiodobr);
                setValuesObrada(addRSPERIODARH);
                setOjFor(addRSPERIODARH, radnicipl.getString("CRADNIK"), 6);
                addRSPERIODARH.execute();
              }
            }//radnicipl for
          }//kumulorg for
          
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw ex;
        }
      }
    };
    boolean b=trans.execTransaction();
    return b;
  }
  
  protected void setOjForOdbici(raPreparedStatement stmt,
      String cradnik, String nivoodb) {
    String ojFor = frmIniciranje.getOJFor();
    if ("".equals(ojFor)) return;
    String cradFor = new VarStr(cradnik).replaceLast("@"+ojFor,"").toString().trim();
    
    if (nivoodb.toUpperCase().startsWith("RA")) {
      stmt.setValue("CKEY", cradFor, false);
    }
    if (nivoodb.toUpperCase().startsWith("PO")) {
      stmt.setValue("CKEY", ojFor, false);
    }
    
    
  }
  protected void setOjFor(raPreparedStatement stmt, String cradnik, int i) {
    String ojFor = frmIniciranje.getOJFor();
    if ("".equals(ojFor)) return;
    String cradFor="";
    if (cradnik!=null) cradFor = new VarStr(cradnik).replaceLast("@"+ojFor,"").toString().trim();
    
    switch (i) {
    case 1: //kumulorgarh
      stmt.setString("CORG", ojFor, false);
      stmt.setString("KNJIG", ojFor, false);
      break;

    case 2: //kumulradarh
      stmt.setString("CORG", ojFor, false);
      stmt.setString("CRADNIK", cradFor, false);
      
      break;
      
    case 3: //primanjaarh
      stmt.setString("CORG", ojFor, false);
      stmt.setString("CRADNIK", cradFor, false);
      
      break;

    case 4: //odbiciarh
      stmt.setString("CRADNIK", cradFor, false);
      
      break;

    case 5: //prisutarh
      stmt.setString("CRADNIK", cradFor, false);
      
      break;
      
    case 6: //rsperiodarh
      stmt.setString("CRADNIK", cradFor, false);
      
      break;

    default:
      break;
    }
  }
  private void setValuesObrada(raPreparedStatement ps) {
    System.out.println("setValuesObrada "+ dm.getOrgpl());
    setValues(ps, new String[] {"godobr", "mjobr", "rbrobr"}, dm.getOrgpl());
  }
  
  private void setValues(raPreparedStatement ps, String[] cols, ReadRow row) {
    for (int i=0; i<cols.length; i++) {
      System.out.println("setValues: "+cols[i].toUpperCase()+" = "+raVariant.getDataSetValue(row, cols[i].toUpperCase()));
      ps.setValue(cols[i].toUpperCase(), raVariant.getDataSetValue(row, cols[i].toUpperCase()),false);
    }
  }
          
  public void showMessage() {
    if (suc) JOptionPane.showConfirmDialog(jp,"Obra\u010Dun je uspješno arhiviran !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
    else JOptionPane.showConfirmDialog(jp,"Greška kod arhiviranja !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    this.hide();
  }
  
  /*private void makeKumulOrg() {

  }*/
  /*  public void doAfterLookup() {
    rcc.setLabelLaF(jp, false);
  }*/
  /*
  private boolean makeTransaction() {
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          System.out.println("tds: "+tds.getTimestamp("DATUM"));
          raTransaction.runSQL(sjQuerys.getKUMULORGARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg"), tds.getTimestamp("datum")));
          raTransaction.runSQL(sjQuerys.getKUMULRADARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg")));
          raTransaction.runSQL(sjQuerys.getPRIMANJAARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg")));
          raTransaction.runSQL(sjQuerys.getODBICIARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg")));
          raTransaction.runSQL(sjQuerys.getPRISUTARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg")));
          raTransaction.runSQL(sjQuerys.getRSPERIODARH(tds.getShort("godina"), tds.getShort("mjesec"), tds.getShort("rbr"), tds.getString("corg")));
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw ex;
        }

      }
    };
    boolean b=trans.execTransaction();
    return b;

  }
   */

}
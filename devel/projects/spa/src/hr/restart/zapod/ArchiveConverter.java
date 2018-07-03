package hr.restart.zapod;

import hr.restart.baza.*;
import hr.restart.sisfun.SepaException;
import hr.restart.sisfun.TextFile;
import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


public class ArchiveConverter {
  
  
  lookupData ld = lookupData.getlookupData();
  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public ArchiveConverter() {
    
  }
  
  
  public void fillVirmans(File sepa, int mjobr, int rbrobr) {
    if (!ld.raLocate(dM.getDataModule().getLogotipovi(), "CORG", OrgStr.getKNJCORG(false)))
      throw new SepaException("Nedefiniran OIB tvrtke na logotipovima!");
      
    String oib = dM.getDataModule().getLogotipovi().getString("OIB");
    if (oib.length() == 0)
      throw new SepaException("Nedefiniran OIB tvrtke na logotipovima!");

    int rbr = 0;
    DataSet vir = Virmani.getDataModule().openEmptySet();

    try {
      Document doc = new SAXBuilder().build(sepa);
      Element root = doc.getRootElement();
      if (!root.getName().equals("Document") || !root.getNamespaceURI().equals("urn:iso:std:iso:20022:tech:xsd:scthr:pain.001.001.03"))
        throw new SepaException("Pogrešan SEPA dokument!");
      
      Element main = root.getChild("CstmrCdtTrfInitn");
      Timestamp datum = (Timestamp) df.parse(main.getChild("GrpHdr").getChildText("CreDtTm"));
      String key = OrgStr.getKNJCORG(false) + "-" + Util.getUtil().getYear(datum) + "-" + mjobr + "-" + rbrobr;
      for (Iterator i = main.getChildren("PmtInf").iterator(); i.hasNext(); ) {
        Element row = (Element) i.next();
        Timestamp datinv = (Timestamp) df.parse(row.getChildText("ReqdExctnDt"));
        String nateret = row.getChild("Dbtr").getChildText("Nm");
        for (Iterator a = row.getChild("Dbtr").getChild("PstlAdr").getChildren("AdrLine").iterator(); i.hasNext(); )
          nateret = nateret + "\n" + ((Element) a).getText();
        String nateretrac = row.getChild("DbtrAcct").getChild("Id").getChildText("IBAN");
        String uoib = row.getChild("UltmtDbtr").getChild("Id").getChild("OrgId").getChild("Othr").getChildText("Id");
        
        for (Iterator p = row.getChildren("CdtTrfTxInf").iterator(); i.hasNext(); ) {
          Element pay = (Element) i.next();
          String pnbz = pay.getChild("PmtId").getChildText("EndToEndId");
          BigDecimal iznos = Aus.getDecNumber(pay.getChild("Amt").getChildText("InstdAmt"));
          String ukorist = pay.getChild("Cdtr").getChildText("Nm");
          String ukoristrac = pay.getChild("CdtrAcct").getChild("Id").getChildText("IBAN");
          String pnbo = pay.getChild("RmtInf").getChild("Strd").getChild("CdtrRefInf").getChildText("Ref");
          String svrha = pay.getChild("RmtInf").getChild("Strd").getChildText("AddtlRmtInf");
          
          vir.insertRow(false);
          vir.setString("APP", "pl");
          vir.setString("KNJIG", OrgStr.getKNJCORG(false));
          vir.setString("CKEY", key);
          vir.setShort("RBR", (short) ++rbr);
          vir.setString("JEDZAV", "NNDNN");
          vir.setString("NATERET", nateret);
          vir.setString("SVRHA", svrha);
          vir.setString("UKORIST", ukorist);
          vir.setString("BRRACNT", nateretrac);
          if (pnbz != null && pnbz.length() > 4) {
            vir.setString("PNBZ1", pnbz.substring(2, 4));
            vir.setString("PNBZ2", pnbz.substring(4));
          }
          vir.setString("BRRACUK", ukoristrac);
          if (pnbo != null && pnbo.length() > 4) {
            vir.setString("PNBO1", pnbo.substring(2, 4));
            vir.setString("PNBO2", pnbo.substring(4));
          }
          vir.setTimestamp("DATUMIZV", new Timestamp(datinv.getTime()));
          vir.setTimestamp("DATUMPR", new Timestamp(datum.getTime()));
          if (uoib != null && !uoib.equals(oib))
            vir.setString("RID", uoib);
          vir.post();
        }
        
      }
      vir.saveChanges();
    } catch (SepaException e) {
      throw e;      
    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    throw new SepaException("Greška unutar SEPA dokumenta!");
  }
  
  public DataSet convert(String knjig) {
    
    StorageDataSet outkum = Aus.createSet("Kumulorgarh.GODOBR .MJOBR .RBROBR REDOVNI:1 CORG:12 KNJIG:12 SATI.2 " +
        "BRUTO.2 MIO1.2 MIO2.2 DOPZO.2 DOPZAP.2 DOPOZ.2 DOHODAK.2 NEOP.2 ISKNEOP.2 POROSN.2 " +
        "POR1.2 POR2.2 POR3.2 POR4.2 PORUK.2 PRIR.2 PORIPRIR.2 NETO.2 NAKNADE.2 OBUSTAVE.2 KREDITI.2 NARUKE.2 " +
        "SATIMJ.2 @DATUMOBR @DATUMISPL BROJDANA MINPL.2 MINOSDOP.2 MAXOSDOP.2 " +
        "OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 STOPAPOR1.2 STOPAPOR2.2 STOPAPOR3.2 STOPAPOR4.2");
    
    StorageDataSet outnak = Aus.createSet("CNAK NAZIV:50 IZNOS.2");
    
    StorageDataSet outbank = Aus.createSet("CBANKE NAZIV:50 IBAN:20 BIC:11 OIB:11");
    
    StorageDataSet outkred = Aus.createSet("CBANKE CKREDIT CRADNIK:6 GLAVNICA.2 RATA.2 SALDO.2");
    
    StorageDataSet outob = Aus.createSet("COBUS NAZIV:50 IZNOS.2");
    StorageDataSet outobrad = Aus.createSet("COBUS CRADNIK:6 NAZIV:50 IZNOS.2");
    
    StorageDataSet outprim = Aus.createSet("CVRP NAZIV:50 VRSTA:1 KOEF.2 NEOD:1 VRODN:1");
    
    StorageDataSet outprimarh = Aus.createSet("CRADNIK:6 CVRP GODOBR MJOBR RBROBR REDOVNI:1 BRUTO.2 KOEF.2 NETO.2 SATI.2 " +
    		"ODDANA DODANA BRUTO2.2 DOPZO.2 DOPOZ.2 DOPZAP.2 MIO1.2 MIO2.2 DOHODAK.2 OLAKSICA.2 OSNOVICA.2 " +
    		"POR1.2 POR2.2 POR3.2 POR4.2 OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 OSNPOR4.2 POR.2 PRIREZ.2");
    
    StorageDataSet outnakarh = Aus.createSet("GODOBR MJOBR RBROBR CNAK CRADNIK:6 IZNOS.2");
    
    StorageDataSet outkredarh = Aus.createSet("GODOBR MJOBR RBROBR CBANKE CKREDIT CRADNIK:6 IZNOS.2");
    
    StorageDataSet outobarh = Aus.createSet("GODOBR MJOBR RBROBR COBUS CRADNIK:6 IZNOS.2");
    
    StorageDataSet outrad = Aus.createSet("CRADNIK:6 CSS:5 CVRO:1 CISPLMJ COPCINE:3 RSINV:5 RSOO:5 BRUTOSN.2 BRUTDOD.2 " +
        "BRUTMR.2 BRUTUK.2 GODSTAZ STOPASTAZ.5 @DATSTAZ PODSTAZ @DATPODSTAZ NACOBR:1 KOEF.5 KOEFZAR.5 @DATDOL @DATODL " +
        "@DATREGRES OLUK.5 OLOS.5 JMBG:13 BRRADKNJ:15 REGBRRK:15 REGBRMIO:15 BROSIGZO:15 ZIJMBGZO:1 BROBVEZE:15 CLANOMF:1 " +
        "RSB:5 RSZ:5 CORG:12 ADRESA:64 BROJTEK:32 OIB:24 PARAMETRI:20 DRUGITEK:30 IME:64 PREZIME:64 IMEOCA:64 TITULA:16 " +
        "AKTIV:1 CBANKE CBANKEDR MINULIRAD:1 BRADSTAZ:2 HRVI.2 BRDJECE BRUZD VRINV:6");
    
    StorageDataSet outorg = Aus.createSet("CORG:12 COPCINE:3 NACOBRS:1 NACOBRB:1 SATIMJ.2 OSNKOEF.6 SATNORMA.2 OOZO:6 " +
    		"PODRUREDZO:3 REGBRMIO:15 REGBRZO:15 PODMATBR:4 CGRORG:5 GODOBR MJOBR RBROBR @DATUMISPL BROJDANA STOPAK.2 " +
    		"RSZ:5 RSIND:4 PARAMETRI:20 STATUS:1 @DATUM1 @DATUM2 @DATUMPOR @DATUMODB @DATUMISP @DATUMDOSP VRIBODA.2 " +
    		"NAZIV:128 CMJESTA MJESTO:32 ADRESA:64 PBR PRIPADNOST:12 NALOG:1 FISK:1 FPP:32 FPATH:64 FKEY:32 FPOJED:1 " +
    		"FPDV:1 CCERT:12 RDVA:1 UIGODMJ:6 OIB:11 IBAN:64 REDOVNI:1 SWIFT:16");
        
    StorageDataSet outfond = Aus.createSet("GODINA MJESEC SATIRAD.2 SATIPRAZ.2 SATIUK.2");
    
    DataSet fond = FondSati.getDataModule().openTempSet(Condition.equal("KNJIG", knjig));
    String[] fondcc = {"GODINA", "MJESEC", "SATIRAD", "SATIPRAZ", "SATIUK"};
    for (fond.first(); fond.inBounds(); fond.next()) {
      outfond.insertRow(false);
      dM.copyColumns(fond, outfond, fondcc);
    }
        
    HashSet nakset = new HashSet();
    HashSet primset = new HashSet();
    
    DataSet vprim = Vrsteprim.getDataModule().openTempSet();
    for (vprim.first(); vprim.inBounds(); vprim.next()) {
      if (vprim.getString("PARAMETRI").startsWith("DDD")) {
        primset.add(new Integer(vprim.getShort("CVRP")));
        outprim.insertRow(false);
        outprim.setInt("CVRP", vprim.getShort("CVRP"));
        outprim.setString("NAZIV", vprim.getString("NAZIV"));
        outprim.setString("VRSTA", "n");
        outprim.setBigDecimal("KOEF", vprim.getBigDecimal("KOEF"));
        outprim.setString("NEOD", vprim.getString("NEOD").equalsIgnoreCase("D") ? "1" : "0");
        outprim.setString("VRODN", "r");
      } else if (vprim.getString("PARAMETRI").startsWith("NNN")) {
        nakset.add(new Integer(vprim.getShort("CVRP")));
        outnak.insertRow(false);
        outnak.setInt("CNAK", vprim.getShort("CVRP"));
        outnak.setString("NAZIV", vprim.getString("NAZIV"));
        outnak.setBigDecimal("IZNOS", Aus.zero2);
      }
    }
    
    DataSet pov = Povjerioci.getDataModule().openTempSet();
    DataSet vodb = Vrsteodb.getDataModule().openTempSet();
    DataSet rodb = Odbici.getDataModule().openTempSet();
    DataSet banke = Bankepl.getDataModule().openTempSet();
    
    HashSet kredset = new HashSet();
    HashMap kredmap = new HashMap();
    HashSet obset = new HashSet();
    HashSet povbanks = new HashSet();
    for (banke.first(); banke.inBounds(); banke.next()) {
      outbank.insertRow(false);
      outbank.setInt("CBANKE", banke.getInt("CBANKE"));
      outbank.setString("NAZIV", banke.getString("NAZBANKE"));
      if (sw.containsKey(banke.getString("PREFIKS")))
        outbank.setString("BIC", (String) sw.get(banke.getString("PREFIKS")));
      povbanks.add(new Integer(banke.getInt("CPOV")));
    }
    
    int zo, oz, zap, osob, por, prir, mio1, mio2;
    zo = oz = zap = osob = por = prir = mio1 = mio2 = -1;
    for (vodb.first(); vodb.inBounds(); vodb.next()) {
      if (isOdb(vodb, "POVR", "S", "1", "1")) {
        if (ld.raLocate(rodb, new String[] {"CVRODB", "CKEY"}, new String[] {""+vodb.getShort("CVRODB"), knjig})) {
          if (rodb.getBigDecimal("STOPA").intValue() == 15 && zo < 0) zo = vodb.getShort("CVRODB");
          else if (rodb.getBigDecimal("STOPA").movePointRight(1).intValue() == 17 && zap < 0) zap = vodb.getShort("CVRODB");
          else if (rodb.getBigDecimal("STOPA").movePointRight(1).intValue() == 5 && oz < 0) oz = vodb.getShort("CVRODB");
        }
      } else if (isOdb(vodb, "RA", "S", "3", "0") && !povbanks.contains(new Integer(vodb.getInt("CPOV")))) {
        if (ld.raLocate(pov, "CPOV", vodb)) {
          String zr = pov.getString("ZIRO");
          for (Iterator i = sw.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry me = (Map.Entry) i.next();
            if (zr.startsWith((String) me.getKey()) ||
                (zr.startsWith("HR") && zr.length() > 12 && zr.substring(4, 11).equals(me.getKey()))) {
              System.out.println("here bank");
              if (!ld.raLocate(outbank, "BIC", (String) me.getValue())) {
                System.out.println("Nepoznata banka za kredit " + vodb);
                break;
              }
              if (!ld.raLocate(rodb, "CVRODB", vodb)) {
                System.out.println("Nepoznat kredit " + vodb);
                break;
              }
              kredset.add(new Integer(vodb.getShort("CVRODB")));
              kredmap.put(new Integer(vodb.getShort("CVRODB")), new Integer(outbank.getInt("CBANKE")));
              outkred.insertRow(false);
              outkred.setInt("CBANKE", outbank.getInt("CBANKE"));
              outkred.setInt("CKREDIT", vodb.getShort("CVRODB"));
              outkred.setString("CRADNIK", rodb.getString("CKEY"));
              outkred.setBigDecimal("GLAVNICA", rodb.getBigDecimal("GLAVNICA"));
              outkred.setBigDecimal("RATA", rodb.getBigDecimal("RATA"));
              outkred.setBigDecimal("SALDO", rodb.getBigDecimal("SALDO"));
              System.out.println("insert kred " + outkred);
              break;
            }
          }          
        }
        if (ld.raLocate(rodb, "CVRODB", vodb)) {
          obset.add(new Integer(vodb.getShort("CVRODB")));
          outob.insertRow(false);
          outob.setInt("COBUS", vodb.getShort("CVRODB"));
          outob.setString("NAZIV", vodb.getString("OPISVRODB"));
          outob.setBigDecimal("IZNOS", rodb.getBigDecimal("RATA"));
          
          outobrad.insertRow(false);
          outobrad.setInt("COBUS", vodb.getShort("CVRODB"));
          outobrad.setString("CRADNIK", rodb.getString("CKEY"));
          outobrad.setString("NAZIV", vodb.getString("OPISVRODB"));
          outobrad.setBigDecimal("IZNOS", rodb.getBigDecimal("RATA"));
        
        } else {
          System.out.println("Nepoznat odbitak " + vodb);
          break;
        }
      } else if (isOdb(vodb, "RA", "K", "1", "4") && osob < 0) osob = vodb.getShort("CVRODB");
      else if (isOdb(vodb, "OPVR", "S", "2", "4") && por < 0) por = vodb.getShort("CVRODB");
      else if (isOdb(vodb, "OP", "S", "2", "5") && prir < 0) prir = vodb.getShort("CVRODB");
      else if (isOdb(vodb, "RA", "S", "1", "1")) {
        if (mio1 < 0) mio1 = vodb.getShort("CVRODB");
        else if (mio2 < 0) mio2 = vodb.getShort("CVRODB");
      }
    }
    
    if (mio1 < 0) {
      System.out.println("*** Nepoznat MIO1 odbitak ***");
      return null;
    }
    
    if (mio2 < 0) {
      System.out.println("*** Nepoznat MIO2 odbitak ***");
      return null;
    }
    
    if (osob < 0) {
      System.out.println("*** Nepoznat osobni odbitak ***");
      return null;
    }
    
    if (por < 0) {
      System.out.println("*** Nepoznat odbitak poreza ***");
      return null;
    }
    
    if (prir < 0) {
      System.out.println("*** Nepoznat odbitak prireza ***");
      return null;
    }
    
    if (zo < 0) {
      System.out.println("*** Nepoznat odbitak zdravstvo ***");
      return null;
    }
    
    if (zap < 0) {
      System.out.println("*** Nepoznat odbitak zapošljavanje ***");
      return null;
    }
    
    if (oz < 0) {
      System.out.println("*** Nepoznat odbitak ozljede ***");
      return null;
    }

    String[] radkey = {"GODOBR", "MJOBR", "RBROBR", "CRADNIK"};
    String[] corgkey = {"GODOBR", "MJOBR", "RBROBR", "CORG"};
    
    Condition corgs = OrgStr.getCorgsCond(knjig);
        
    DataSet kums = Kumulorgarh.getDataModule().openTempSet(corgs);
    
    DataSet kumrad = Kumulradarh.getDataModule().openTempSet(corgs);
    HashDataSet hashr = new HashDataSet(kumrad, radkey);
        
    DataSet zrad = Radnici.getDataModule().openTempSet(corgs);
    DataSet plrad = Radnicipl.getDataModule().openTempSet(corgs);
    
    Condition crads = Condition.in("CRADNIK", zrad);
    
    DataSet orgpl = Orgpl.getDataModule().openTempSet(corgs);
    DataSet zorg = Orgstruktura.getDataModule().openTempSet(corgs);
    String[] ccz = {"CORG", "NAZIV", "MJESTO", "ADRESA", "PRIPADNOST"};
    String[] ccpl = {"COPCINE", "NACOBRS", "NACOBRB", "SATIMJ", "OSNKOEF", "SATNORMA", "OOZO", 
        "PODRUREDZO", "REGBRMIO", "REGBRZO", "PODMATBR", "CGRORG", "GODOBR", "MJOBR", "RBROBR",
        "DATUMISPL", "BROJDANA", "STOPAK", "RSZ", "RSIND", "PARAMETRI", "DATUM1", "DATUM2",
        "DATUMPOR", "DATUMODB", "DATUMISP", "DATUMDOSP"};
    for (orgpl.first(); orgpl.inBounds(); orgpl.next()) {
      ld.raLocate(zorg, "CORG", orgpl);
      outorg.insertRow(false);
      dM.copyColumns(zorg, outorg, ccz);
      dM.copyColumns(orgpl, outorg, ccpl);
      outorg.setString("STATUS", "");
      outorg.setString("REDOVNI", "1");
    }
        
    String[] ccrz = {"CRADNIK", "IME", "PREZIME", "IMEOCA", "TITULA", "CORG"};
    String[] ccrpl = {"CSS", "CVRO", "CISPLMJ", "COPCINE", "RSINV", "RSOO", "BRUTOSN", "BRUTDOD", "BRUTMR", "BRUTUK",
        "GODSTAZ", "STOPASTAZ", "DATSTAZ", "PODSTAZ", "DATPODSTAZ", "KOEF", "KOEFZAR", "DATDOL", "DATODL", "DATREGRES",
        "OLUK", "OLOS", "JMBG", "BRRADKNJ", "REGBRRK", "REGBRMIO", "BROSIGZO", "ZIJMBGZO", "BROBVEZE", "RSB", "RSZ",
        "ADRESA", "BROJTEK", "PARAMETRI", "OIB", "DRUGITEK"};
    for (plrad.first(); plrad.inBounds(); plrad.next()) {
      ld.raLocate(zrad, "CRADNIK", plrad);
      outrad.insertRow(false);
      dM.copyColumns(zrad, outrad, ccrz);
      dM.copyColumns(plrad, outrad, ccrpl);
      outrad.setString("CVRO", "r");
      
      outrad.setString("NACOBR", plrad.getString("NACOBRB"));
      if (outrad.getString("NACOBR").equals("6"))
        outrad.setString("NACOBR", "2");
      outrad.setString("AKTIV", plrad.getString("AKTIV").equalsIgnoreCase("D") ? "1" : "0");
      outrad.setString("MINULIRAD", "0");
      outrad.setString("BRADSTAZ", "0");
      outrad.setString("CLANOMF", "1");
      outrad.setBigDecimal("HRVI", Aus.zero2);
      outrad.setInt("BRDJECE", 0);
      outrad.setInt("BRUZD", 0);
      outrad.setString("VRINV", "n");
    }
    
    
    DataSet prirad = Odbiciarh.getDataModule().openTempSet(crads.and(Condition.equal("CVRODB", prir)));
    HashDataSet prirh = new HashDataSet(prirad, radkey);
    
    DataSet prim = Primanjaarh.getDataModule().openTempSet(corgs);
    
    DataSet odb = Odbiciarh.getDataModule().openTempSet(crads);   
    
    String[] cckum = {"GODOBR", "MJOBR", "RBROBR", "CORG", "KNJIG", 
        "MINPL", "MINOSDOP", "OSNPOR1", "OSNPOR2", "OSNPOR3", "SATIMJ", "DATUMISPL"};
    kums.setSort(new SortDescriptor(corgkey));
    for (kums.first(); kums.inBounds(); kums.next()) {
      if (outkum.isEmpty() || dM.compareColumns(kums, outkum, corgkey) != null) {
        outkum.insertRow(false);
        dM.copyColumns(kums, outkum, cckum);
        outkum.setTimestamp("DATUMOBR", outkum.getTimestamp("DATUMISPL"));
        outkum.setString("REDOVNI", "1");
      }
    }
    
    HashDataSet hashk = new HashDataSet(outkum, corgkey);
    hashk.dump();
    /*StorageDataSet outprimarh = Aus.createSet("CRADNIK:6 CVRP GODOBR MJOBR RBROBR REDOVNI:1 BRUTO.2 KOEF.2 NETO.2 SATI.2 " +
        "ODDANA DODANA BRUTO2.2 DOPZO.2 DOPOZ.2 DOPZAP.2 MIO1.2 MIO2.2 DOHODAK.2 OLAKSICA.2 OSNOVICA.2 " +
        "POR1.2 POR2.2 POR3.2 POR4.2 OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 OSNPOR4.2 POR.2 PRIREZ.2");
    StorageDataSet outkum = Aus.createSet("GODOBR MJOBR RBROBR REDOVNI:1 CORG:12 KNJIG:12 SATI.2 " +
        "BRUTO.2 MIO1.2 MIO2.2 DOPZO.2 DOPZAP.2 DOPOZ.2 DOHODAK.2 NEOP.2 ISKNEOP.2 POROSN.2 " +
        "POR1.2 POR2.2 POR3.2 POR4.2 PORUK.2 PRIR.2 PORIPRIR.2 NETO.2 NAKNADE.2 OBUSTAVE.2 KREDITI.2 NARUKE.2 " +
        "SATIMJ.2 @DATUMOBR @DATUMISPL BROJDANA MINPL.2 MINOSDOP.2 MAXOSDOP.2 " +
        "OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 STOPAPOR1.2 STOPAPOR2.2 STOPAPOR3.2 STOPAPOR4.2");*/
    
    

    String[] ccprim = {"GODOBR", "MJOBR", "RBROBR", "CRADNIK", "CVRP", "SATI", "KOEF", "BRUTO", "NETO"};
    prim.setSort(new SortDescriptor(new String[] {"GODOBR", "MJOBR", "RBROBR", "CRADNIK", "RBR"}));
    String crad = "";
    
    BigDecimal prirs = Aus.zero0;
    for (prim.first(); prim.inBounds(); prim.next()) {
      if (hashk.loc(prim)) {
        outprimarh.insertRow(false);
        dM.copyColumns(prim, outprimarh, ccprim);
        outprimarh.setString("REDOVNI", "1");
        Aus.add(hashk.get(), "SATI", prim);
        Aus.add(hashk.get(), "BRUTO", prim);

        if (!crad.equals(prim.getString("CRADNIK"))) {
          if (prirh.loc(prim)) 
            prirs = prirh.get().getBigDecimal("OBRSTOPA");
          else prirs = Aus.zero0;
          if (hashr.loc(prim)) {
            osn =  new BigDecimal[] {Aus.zero0, Aus.zero0, Aus.zero0, Aus.zero0};
            olak = hashr.get().getBigDecimal("NEOP");
            crad = prim.getString("CRADNIK");
            if (primset.contains(new Integer(prim.getShort("CVRP")))) {
              obrPrim(hashk.get(), outprimarh, prirs);
            } else if (nakset.contains(new Integer(prim.getShort("CVRP")))) {
              if (prim.getBigDecimal("BRUTO").signum() > 0) {
                outnakarh.insertRow(false);
                dM.copyColumns(prim, outnakarh, radkey);
                outnakarh.setInt("CNAK", prim.getShort("CVRP"));
                Aus.set(outnakarh, "IZNOS", prim, "BRUTO");
                Aus.add(hashk.get(), "NARUKE", outnakarh, "IZNOS");
              }
            } else {
              System.out.println("cant find prim type " + prim);
            }
          } else {
            System.out.println("cant find rad kum " + prim);
          }
        } else {
          if (primset.contains(new Integer(prim.getShort("CVRP")))) {
            obrPrim(hashk.get(), outprimarh, prirs);
          } else if (nakset.contains(new Integer(prim.getShort("CVRP")))) {
            outnakarh.insertRow(false);
            dM.copyColumns(prim, outnakarh, radkey);
            outnakarh.setInt("CNAK", prim.getShort("CVRP"));
            Aus.set(outnakarh, "IZNOS", prim, "BRUTO");
            Aus.add(hashk.get(), "NARUKE", outnakarh, "IZNOS");
          } else {
            System.out.println("cant find prim type " + prim);
          }
        }
        hashk.get().post();
      } else {
        System.out.println("cant find kum " + prim);
      }
    }
    
    String[] cobr = {"GODOBR", "MJOBR", "RBROBR"};
    for (odb.first(); odb.inBounds(); odb.next()) {
      if (odb.getBigDecimal("OBRIZNOS").signum() <= 0) continue;
      Integer cvrodb = new Integer(odb.getShort("CVRODB"));
      if (obset.contains(cvrodb)) {
        outobarh.insertRow(false);
        dM.copyColumns(odb, outobarh, cobr);
        outobarh.setInt("COBUS", cvrodb.intValue());
        outobarh.setString("CRADNIK", odb.getString("CKEY"));
        outobarh.setBigDecimal("IZNOS", odb.getBigDecimal("OBRIZNOS"));
      } else if (kredset.contains(cvrodb)) {
        outkredarh.insertRow(false);
        dM.copyColumns(odb, outkredarh, cobr);
        outobarh.setInt("CKREDIT", cvrodb.intValue());
        outobarh.setString("CRADNIK", odb.getString("CKEY"));
        outobarh.setBigDecimal("IZNOS", odb.getBigDecimal("OBRIZNOS"));
        outobarh.setInt("CBANKE", ((Integer) kredmap.get(cvrodb)).intValue());
      }
    }
    
    /*frmTableDataView frm = new frmTableDataView(false, true, false);
    frm.setDataSet(outkum);
    frm.show();*/
    
    /*HashDataSet hashp = new HashDataSet(outprimarh, new String[] {"GODOBR", "MJOBR", "RBROBR", "CRADNIK"});
    
    for (odb.first(); odb.inBounds(); odb.next()) {
      if (hashp.loc(odb)) {
        hashk.loc(hashp.get());
        short cvro = odb.getShort("CVRDOB");
        if (cvro == mio1) {
          Aus.set(hashp.get(), "MIO1", odb, "IZNOS");
          Aus.add(hashk.get(), "MIO1", odb, "IZNOS");
        } else if (cvro == mio2) {
          Aus.set(hashp.get(), "MIO2", odb, "IZNOS");
          Aus.add(hashk.get(), "MIO2", odb, "IZNOS");
        } else if (cvro == osob) {
          // ništa
        } else if (cvro == por) {
          if (odb.getShort("RBRODB") == 1) {
            
          }
        }
      } else {
        System.out.println("cant find kum " + prim);
      }
    }*/
    
    /*TextFile out = TextFile.write("place.txt");
    
    generateInserts(outfond, "fondsati", out);
    generateInserts(outnak, "naknade", out);
    generateInserts(outbank, "banke", out);
    generateInserts(outprim, "primanja", out);
    generateInserts(outob, "obustave", out);
    generateInserts(outorg, "orgpl", out);
    generateInserts(outrad, "radnicipl", out);
    generateInserts(outkred, "kreditirad", out);
    generateInserts(outkum, "kumulorgarh", out);
    generateInserts(outprimarh, "primanjaarh", out);
    generateInserts(outnakarh, "naknadearh", out);
    generateInserts(outobarh, "obustavearh", out);
    generateInserts(outkredarh, "kreditiarh", out);
    
    out.close();*/
    
    StorageDataSet outcountry = Aus.createSet("CODE:20 CNAME:128 ABBR:10 TRADE_ABBR:10");
    DataSet zem = ZpZemlje.getDataModule().openTempSet();
    for (zem.first(); zem.inBounds(); zem.next()) {
      outcountry.insertRow(false);
      outcountry.setString("CODE", zem.getString("CZEM"));
      outcountry.setString("CNAME", zem.getString("NAZIVZEM"));
      outcountry.setString("ABBR", zem.getString("OZNZEM"));
      outcountry.setString("TRADE_ABBR", zem.getString("TRGZEM"));
    }
    
    StorageDataSet outcounty = Aus.createSet("CODE:20 CNAME:128 COUNTRY_CODE:20");
    DataSet zup = Zupanije.getDataModule().openTempSet();
    for (zup.first(); zup.inBounds(); zup.next()) {
      outcounty.insertRow(false);
      outcounty.setString("CODE", ""+zup.getShort("CZUP"));
      outcounty.setString("CNAME", zup.getString("NAZIVZUP"));
      outcounty.setString("COUNTRY_CODE", "385");
    }
    
    StorageDataSet outcity = Aus.createSet("ZIP:20 CNAME:128 COUNTY_CODE:20 COUNTRY_CODE:20");
    DataSet mj = Mjesta.getDataModule().openTempSet();
    for (mj.first(); mj.inBounds(); mj.next()) {
      outcity.insertRow(false);
      outcity.setString("ZIP", "" + mj.getInt("PBR"));
      outcity.setString("CNAME", mj.getString("NAZMJESTA"));
      if (!mj.isNull("CZUP"))
        outcity.setString("COUNTY_CODE", ""+mj.getShort("CZUP"));
      if (mj.getString("CZEM").length() > 0)
        outcity.setString("COUNTRY_CODE", mj.getString("CZEM"));
    }
    
    
    TextFile out = TextFile.write("city.txt");
    
    generateInserts(outcountry, "countries", out);
    generateInserts(outcounty, "counties", out);
    generateInserts(outcity, "cities", out);
    
    out.close();
    
    return prim;
  }
  
  void generateTable(DataSet ds, String tname, TextFile tf) {
    
    tf.out("CREATE TABLE " + tname + " (");
    String next = ",";
    for (int i = 0; i < ds.getColumnCount(); i++) {
      if (i == ds.getColumnCount() - 1) next = "";
      Column c = ds.getColumn(i);
      switch (c.getDataType()) {
        case Variant.INT:
          tf.out("  " + c.getColumnName() + " integer" + next);
          break;
        case Variant.STRING:
          tf.out("  " + c.getColumnName() + " character varying(" + c.getPrecision() + ")" + next);
          break;
        case Variant.BIGDECIMAL:
          tf.out("  " + c.getColumnName() + " numeric(" + c.getPrecision() + "," + c.getScale() + ")" + next);
          break;
        case Variant.TIMESTAMP:
          tf.out("  " + c.getColumnName() + " timestamp without time zone" + next);
          break;
        case Variant.LONG:
          tf.out("  " + c.getColumnName() + " bigint" + next);
          break;
        case Variant.FLOAT:
          tf.out("  " + c.getColumnName() + " real" + next);
          break;
        case Variant.DOUBLE:
          tf.out("  " + c.getColumnName() + " double" + next);
          break;
      }
    }
    tf.out(") WITH (OIDS=FALSE);");
    tf.out("ALTER TABLE " + tname + " OWNER TO postgres;");
  }
  
  void generateInserts(DataSet ds, String tname, TextFile tf) {
        
    tf.out("DELETE FROM "+tname+";");
    String comm = "INSERT INTO " + tname + "(" +
        VarStr.join(ds.getColumnNames(ds.getColumnCount()),',') + ") VALUES (";
    
    VarStr line = new VarStr();
    VarStr part = new VarStr();
    Variant v = new Variant();
    for (ds.first(); ds.inBounds(); ds.next()) {
      line.clear().append(comm);
      for (int i = 0; i < ds.columnCount(); i++) {
        if (ds.getColumn(i).getSqlType() != java.sql.Types.NULL && ds.getColumn(i).getVisible() != 0) {
          ds.getVariant(ds.getColumn(i).getColumnName(), v);
          if (v.getType() != Variant.INPUTSTREAM) {
            if (!v.isNull()) part.clear().append(v);
            else part.clear().append("NULL");
            if (v.getType() == Variant.STRING || v.getType() == Variant.TIMESTAMP)
              part.replace("'", "''").insert(0, '\'').append('\'');
          }
          line.append(part).append(',');
        }
      }
      line.chop().append(");");
      tf.out(line.toString());
    }
  }
  
  public void generateGEO(File input, File output) {
    TextFile tf = TextFile.read(input);
    String line = tf.in();
    
    String[] cols = new VarStr(line).splitTrimmed(',');
    for (int i = 0; i < cols.length; i++) {
      if (cols[i].startsWith("\"") && cols[i].endsWith("\""))
        cols[i] = cols[i].substring(1, cols[i].length() - 1);
    }
    
    JSONObject obj = new JSONObject();
    JSONArray feats = new JSONArray();
    obj.put("type", "FeatureCollection");
    
    
    while ((line = tf.in()) != null) {
      cols = new VarStr(line).splitTrimmed("\",\"");
      for (int i = 0; i < cols.length; i++) {
        if (cols[i].startsWith("\""))
          cols[i] = cols[i].substring(1, cols[i].length());
        if (cols[i].endsWith("\""))
          cols[i] = cols[i].substring(0, cols[i].length() - 1);
      }
      System.out.println(cols[3]);
      if (cols.length < 4 || !cols[3].startsWith("POLYGON")) continue;
      
      int b = cols[3].indexOf("((");
      int e = cols[3].indexOf("))");
      if (b < 0 || e < 0 || e < b) continue;
      
      
      
      JSONObject f = new JSONObject();
      f.put("type", "Feature");
      JSONObject props = new JSONObject();
      props.put("IDBrick", cols[0]);
      props.put("Name", cols[1]);
      props.put("NameEN", cols[2]);
      f.put("properties", props);
      
      JSONObject geom = new JSONObject();
      geom.put("type", "Polygon");
      ArrayList coords = new ArrayList();
      ArrayList chunk = new ArrayList();
      
      String[] rawc = new VarStr(cols[3].substring(b + 2, e)).splitTrimmed(',');
      for (int c = 0; c < rawc.length; c++) {
        String[] xy = new VarStr(rawc[c]).split();
        ArrayList pair = new ArrayList();
        pair.add(new BigDecimal(xy[0]));
        pair.add(new BigDecimal(xy[1]));
        chunk.add(pair);
      }
      coords.add(chunk);
      geom.put("coordinates", coords);
      f.put("geometry", geom);
      feats.add(f);      
    }
    obj.put("features", feats);
    
    TextFile out = TextFile.write(output);
    
    obj.write(out.getWriter());
    
    out.close();
    
    System.out.println(obj.toString());
  }
  
  public static void generate(DataSet ds, String tname, String fname) {
    ArchiveConverter ac = new ArchiveConverter();
    TextFile out = TextFile.write(fname);
    
    ac.generateTable(ds, tname, out);
    
    ac.generateInserts(ds, tname, out);
    
    out.close();
  }
  
  boolean isOdb(DataSet vrodb, String nivo, String tip, String vrsta, String osn) {
    return vrodb.getString("NIVOODB").equals(nivo) &&
        vrodb.getString("TIPODB").equals(tip) &&
        vrodb.getString("VRSTAOSN").equals(vrsta) &&
        vrodb.getString("OSNOVICA").equals(osn);
  }
  
  BigDecimal olak = Aus.zero0;
  BigDecimal[] osn = {Aus.zero0, Aus.zero0, Aus.zero0, Aus.zero0};
  
  void obrPrim(DataSet kum, DataSet prim, BigDecimal prirs) {
    Timestamp dat = kum.getTimestamp("DATUMISPL");
    BigDecimal zos = new BigDecimal("15.00");
    BigDecimal zap = new BigDecimal("1.70");
    BigDecimal oz = new BigDecimal("0.50");
    BigDecimal mio1 = new BigDecimal(15);
    BigDecimal mio2 = new BigDecimal(5);
    BigDecimal[] stope = null;
    BigDecimal[] osnpor = null;
    if (!dat.before(Aus.createTimestamp(2017, 1, 1))) {
      osnpor = new BigDecimal[] {new BigDecimal(17500), null, null};
      stope = new BigDecimal[] {new BigDecimal(24), new BigDecimal(36), Aus.zero0, Aus.zero0};
    } else if (!dat.before(Aus.createTimestamp(2015, 1, 1))) {
      osnpor = new BigDecimal[] {new BigDecimal(2200), new BigDecimal(11000), null};
      stope = new BigDecimal[] {new BigDecimal(12), new BigDecimal(25), new BigDecimal(40), Aus.zero0};
    } else if (!dat.before(Aus.createTimestamp(2012, 3, 1))) {
      osnpor = new BigDecimal[] {new BigDecimal(2200), new BigDecimal(6600), null};
      stope = new BigDecimal[] {new BigDecimal(12), new BigDecimal(25), new BigDecimal(40), Aus.zero0};
    } else if (!dat.before(Aus.createTimestamp(2010, 7, 1))) {
      osnpor = new BigDecimal[] {new BigDecimal(3600), new BigDecimal(7200), null};
      stope = new BigDecimal[] {new BigDecimal(12), new BigDecimal(25), new BigDecimal(40), Aus.zero0};
    } else if (!dat.before(Aus.createTimestamp(2008, 7, 1))) {
      osnpor = new BigDecimal[] {new BigDecimal(3600), new BigDecimal(5400), new BigDecimal(16200)};
      stope = new BigDecimal[] {new BigDecimal(15), new BigDecimal(25), new BigDecimal(35), new BigDecimal(45)};
    } else {
      osnpor = new BigDecimal[] {new BigDecimal(3200), new BigDecimal(4800), new BigDecimal(14400)};
      stope = new BigDecimal[] {new BigDecimal(15), new BigDecimal(25), new BigDecimal(35), new BigDecimal(45)};
    }
    
    Aus.percentage(prim, "DOPZO", "BRUTO", zos);
    Aus.percentage(prim, "DOPZAP", "BRUTO", zap);
    Aus.percentage(prim, "DOPOZ", "BRUTO", oz);
    Aus.set(prim, "BRUTO2", "BRUTO");
    Aus.addTo(prim, "BRUTO2", new String[] {"DOPZO", "DOPZAP", "DOPOZ"});
    Aus.percentage(prim, "MIO1", "BRUTO", mio1);
    Aus.percentage(prim, "MIO2", "BRUTO", mio2);
    Aus.sub(prim, "DOHODAK", "BRUTO", "MIO1");
    Aus.sub(prim, "DOHODAK", "MIO2");
    
    if (olak.compareTo(prim.getBigDecimal("DOHODAK")) > 0) {
      Aus.set(prim, "OLAKSICA", "DOHODAK");
      olak = olak.subtract(prim.getBigDecimal("OLAKSICA"));
    } else {
      Aus.set(prim, "OLAKSICA", olak);
      olak = Aus.zero0;
    }
    Aus.sub(prim, "OSNOVICA", "DOHODAK", "OLAKSICA");
    
    BigDecimal op = prim.getBigDecimal("OSNOVICA");
    
    op = calcPorez(0, prim, op, osnpor, stope);
    op = calcPorez(1, prim, op, osnpor, stope);
    op = calcPorez(2, prim, op, osnpor, stope);
    calcPorez(3, prim, op, osnpor, stope);
    
    Aus.clear(prim, "POR");
    Aus.addTo(prim, "POR", new String[] {"POR1", "POR2", "POR3", "POR4"});
    Aus.percentage(prim, "PRIREZ", "POR", prirs);
    
    Aus.set(prim, "NETO", "DOHODAK");
    Aus.sub(prim, "NETO", "POR");
    Aus.sub(prim, "NETO", "PRIREZ");
    
    Aus.add(kum, "MIO1", prim);
    Aus.add(kum, "MIO2", prim);
    Aus.add(kum, "DOPZO", prim);
    Aus.add(kum, "DOPZAP", prim);
    Aus.add(kum, "DOPOZ", prim);
    Aus.add(kum, "DOHODAK", prim);
    Aus.add(kum, "NEOP", prim, "OLAKSICA");
    Aus.add(kum, "ISKNEOP", prim, "OLAKSICA");
    Aus.add(kum, "POROSN", prim, "OSNOVICA");
    Aus.add(kum, "POR1", prim);
    Aus.add(kum, "POR2", prim);
    Aus.add(kum, "POR3", prim);
    Aus.add(kum, "POR4", prim);
    Aus.add(kum, "PORUK", prim, "POR");
    Aus.add(kum, "PRIR", prim, "PRIREZ");
    Aus.add(kum, "PORIPRIR", prim, "POR");
    Aus.add(kum, "PORIPRIR", prim, "PRIREZ");
    Aus.add(kum, "NETO", prim);
    Aus.add(kum, "NARUKE", prim, "NETO");
  }
  
  BigDecimal calcPorez(int n, DataSet prim, BigDecimal op, BigDecimal[] osnpor, BigDecimal[] stope) {
    // dosegnut prag osnovice sljedeæeg poreza
    BigDecimal to = op;
    if (n < 3 && osnpor[n] != null && osnpor[n].signum() > 0 && osnpor[n].subtract(osn[n]).compareTo(op) < 0)
        to = osnpor[n].subtract(osn[n]);
    osn[n] = osn[n].add(to);
    Aus.set(prim, "OSNPOR" + (n + 1), to);
    Aus.set(prim, "POR" + (n + 1), to.multiply(stope[n]).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));    
    return op.subtract(to);
  }
    
  // 318240    3115279.1.1
  String[][] swl = {
      {"4133006", "SKOVHR22"},
      {"4109006", "DALMHR22"},
      {"2488001", "BFKKHR22"},
      {"2485003", "CROAHR2X"},
      {"2402006", "ESBCHR22"},
      {"2493003", "HKBOHR2X"},
      {"1001005", "NBHRHR2D"},
      {"2390001", "HPBZHR2X"},
      {"2500009", "HAABHR22"},
      {"2492008", "IMXXHR22"},
      {"2380006", "ISKBHR2X"},
      {"2411006", "JADRHR2X"},
      {"2400008", "KALCHR2X"},
      {"4124003", "KENBHR22"},
      {"2481000", "KREZHR2X"},
      {"2407000", "OTPVHR2X"},
      {"2408002", "PAZGHR2X"},
      {"2386002", "PDKCHR2X"},
      {"4132003", "SPRMHR22"},
      {"2340009", "PBZGHR2X"},
      {"2484008", "RZBHHR2X"},
      {"2403009", "SMBRHR22"},
      {"2503007", "VBCRHR22"},
      {"2412009", "SBSLHR2X"},
      {"2330003", "SOGEHR22"},
      {"2483005", "STEDHR22"},
      {"6717002", "ASBZHR22"},
      {"2489004", "VBVZHR22"},
      {"2381009", "CCBZHR2X"},
      {"2360000", "ZABAHR2X"}
    };
    HashMap sw = new HashMap();
    {
      for (int i = 0; i < swl.length; i++)
        sw.put(swl[i][0], swl[i][1]);
    }
  
}

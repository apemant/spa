package hr.restart.zapod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.*;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;


public class ArchiveConverter {
  
  lookupData ld = lookupData.getlookupData();

  public ArchiveConverter() {
    
  }
  
  public void convert(String knjig) {
    
    StorageDataSet outkum = Aus.createSet("GODOBR MJOBR RBROBR REDOVNI:1 CORG:12 KNJIG:12 SATI.2 " +
        "BRUTO.2 MIO1.2 MIO2.2 DOPZO.2 DOPZAP.2 DOPOZ.2 DOHODAK.2 NEOP.2 ISKNEOP.2 POROSN.2 " +
        "POR1.2 POR2.2 POR3.2 POR4.2 PORUK.2 PRIR.2 PORIPRIR.2 NETO.2 NAKNADE.2 OBUSTAVE.2 KREDITI.2 NARUKE.2 " +
        "SATIMJ.2 @DATUMOBR @DATUMISPL BROJDANA MINPL.2 MINOSDOP.2 MAXOSDOP.2 " +
        "OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 STOPAPOR1.2 STOPAPOR2.2 STOPAPOR3.2 STOPAPOR4.2");
    
    StorageDataSet outnak = Aus.createSet("CNAK NAZIV:50 IZNOS.2");
    
    StorageDataSet outbank = Aus.createSet("CBANKE NAZIV:50 IBAN:20 BIC:11 OIB:11");
    
    StorageDataSet outkred = Aus.createSet("CBANKE CKREDIT CRADNIK:6 GLAVNICA.2 RATA.2 SALDO.2");
    
    StorageDataSet outob = Aus.createSet("COBUS NAZIV:50 IZNOS.2");
    
    StorageDataSet outprim = Aus.createSet("CVRP NAZIV:50 VRSTA:1 KOEF.2 NEOD:1 VRODN:1");
    
    StorageDataSet outprimarh = Aus.createSet("CRADNIK:6 CVRP GODOBR MJOBR RBROBR REDOVNI:1 BRUTO.2 KOEF.2 NETO.2 SATI.2 " +
    		"ODDANA DODANA BRUTO2.2 DOPZO.2 DOPOZ.2 DOPZAP.2 MIO1.2 MIO2.2 DOHODAK.2 OLAKSICA.2 OSNOVICA.2 " +
    		"POR1.2 POR2.2 POR3.2 POR4.2 OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 OSNPOR4.2 POR.2 PRIREZ.2");
    
    StorageDataSet outnakarh = Aus.createSet("GODOBR MJOBR RBROBR CNAK CRADNIK:6 IZNOS.2");
    
    StorageDataSet outkredarh = Aus.createSet("GODOBR MJOBR RBROBR CBANKE CKREDIT CRADNIK:6 IZNOS.2");
    
    StorageDataSet outobarh = Aus.createSet("GODOBR MJOBR RBROBR COBUS CRADNIK:6 IZNOS.2");
   
    
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
    HashSet povbanks = new HashSet();
    for (banke.first(); banke.inBounds(); banke.next()) {
      outbank.insertRow(false);
      outbank.setInt("CBANKE", banke.getInt("CBANKE"));
      outbank.setString("NAZIV", banke.getString("NAZBANKE"));
      if (sw.containsKey(banke.getString("PREFIX")))
        outbank.setString("BIC", (String) sw.get(banke.getString("PREFIX")));
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
              if (!ld.raLocate(outbank, "BIC", (String) me.getValue())) {
                System.out.println("Nepoznata banka za kredit " + vodb);
                break;
              }
              if (!ld.raLocate(rodb, "CVRODB", vodb)) {
                System.out.println("Nepoznat kredit " + vodb);
                break;
              }
              kredset.add(new Integer(vodb.getShort("CVRODB")));
              outkred.insertRow(false);
              outkred.setInt("CBANKE", outbank.getInt("CBANK"));
              outkred.setInt("CKREDIT", vodb.getShort("CVRODB"));
              outkred.setString("CRADNIK", rodb.getString("CKEY"));
              outkred.setBigDecimal("GLAVNICA", rodb.getBigDecimal("GLAVNICA"));
              outkred.setBigDecimal("RATA", rodb.getBigDecimal("RATA"));
              outkred.setBigDecimal("SALDO", rodb.getBigDecimal("SALDO"));
              break;
            }
          }
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
      return;
    }
    
    if (mio2 < 0) {
      System.out.println("*** Nepoznat MIO2 odbitak ***");
      return;
    }
    
    if (osob < 0) {
      System.out.println("*** Nepoznat osobni odbitak ***");
      return;
    }
    
    if (por < 0) {
      System.out.println("*** Nepoznat odbitak poreza ***");
      return;
    }
    
    if (prir < 0) {
      System.out.println("*** Nepoznat odbitak prireza ***");
      return;
    }
    
    if (zo < 0) {
      System.out.println("*** Nepoznat odbitak zdravstvo ***");
      return;
    }
    
    if (zap < 0) {
      System.out.println("*** Nepoznat odbitak zapošljavanje ***");
      return;
    }
    
    if (oz < 0) {
      System.out.println("*** Nepoznat odbitak ozljede ***");
      return;
    }

    Condition corgs = OrgStr.getCorgsCond(knjig);
    
    DataSet kums = Kumulorgarh.getDataModule().openTempSet(corgs);
    
    DataSet zrad = Radnici.getDataModule().openTempSet(corgs);
    
    Condition crads = Condition.in("CRADNIK", zrad);
    
    DataSet prim = Primanjaarh.getDataModule().openTempSet(corgs);
    
    DataSet odb = Odbiciarh.getDataModule().openTempSet(crads);   
    
    for (kums.first(); kums.inBounds(); kums.next()) {
      
    }
  }
  
  boolean isOdb(DataSet vrodb, String nivo, String tip, String vrsta, String osn) {
    return vrodb.getString("NIVOODB").equals(nivo) &&
        vrodb.getString("TIPODB").equals(tip) &&
        vrodb.getString("VRSTAOSN").equals(vrsta) &&
        vrodb.getString("OSNOVICA").equals(osn);
  }
  
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

package hr.restart.gk;

import hr.restart.db.raVariant;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import java.io.File;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.sun.mail.imap.Utility.Condition;

public class GKDataTransfer {

  /**
   * 
   * @param nalog zaglavlje tekuæeg naloga
   * @param mode za sada 'horex'
   * @return File u koji su zdampani podaci
   */
  public static File serializeData(QueryDataSet nalog, String mode) {
    try {
      
      if ("horex".equalsIgnoreCase(mode)) {
        if (!nalog.getString("STATUS").equals("K")) {
          JOptionPane.showMessageDialog(null, "Nalog mora biti proknjižen!","Greška", JOptionPane.ERROR_MESSAGE);
          return null;
        }
        String iraosn = "17,19,21";
        String irapdv = "18,20,22";
        String uraosn = "6,7,8";
        String urapdv = "11,13,15";
        String cnaloga = nalog.getString("CNALOGA");
        String fn = "prijenos"+cnaloga+"_"+new java.sql.Date(nalog.getTimestamp("DATUMKNJ").getTime());
        String qry = "SELECT skstavke.VRDOK as VRSTA, skstavke.cpar as KUPAC, partneri.oib as OIB, " +
            "skstavke.brojdok as BROJ, " +
            "skstavke.datdok as DATUM," +
            "skstavke.ssaldo as IZNOS," +
            "( select sum(IP+ID) FROM UIstavke" +
            "   WHERE skstavke.knjig = uistavke.knjig" +
            "   AND skstavke.cpar = uistavke.cpar" +
            "   AND skstavke.vrdok = uistavke.vrdok" +
            "   AND skstavke.brojdok = uistavke.brojdok" +
            "   AND ((uistavke.ckolone in ("+iraosn+") and skstavke.vrdok in ('IRN','OKK')) "
                    + "OR (uistavke.ckolone in ("+uraosn+") and skstavke.vrdok in ('URN','OKD')))"
            + ") as OSNOVICA," +
            "( select sum(IP+ID) FROM UIstavke" +
            "   WHERE skstavke.knjig = uistavke.knjig" +
            "   AND skstavke.cpar = uistavke.cpar" +
            "   AND skstavke.vrdok = uistavke.vrdok" +
            "   AND skstavke.brojdok = uistavke.brojdok" +
            "   AND ((uistavke.ckolone in ("+irapdv+") and skstavke.vrdok in ('IRN','OKK')) "
                    + "OR (uistavke.ckolone in ("+urapdv+") and skstavke.vrdok in ('URN','OKD')))"
            + ") as PDV, " +
            "  25.00 AS STOPA, " +
            "skstavke.DATDOSP as DATDOSP " +
            " FROM skstavke, partneri where " +
            "partneri.cpar = skstavke.cpar " +
            "AND skstavke.vrdok in ('IRN','OKK', 'URN', 'OKD')" +
            "AND skstavke.cgkstavke like '"+cnaloga+"-%'";
        System.out.println(qry);
        StorageDataSet racuni = Aus.q(qry);
        int poscpar;
        try {
          poscpar = Integer.parseInt(frmParam.getParam("gk", "poscpar", "2472", "Koji partner je partner za POS"));
        } catch (Exception e) {
          e.printStackTrace();
          poscpar = 0;
        }
        for (racuni.first(); racuni.inBounds(); racuni.next()) {
          if (racuni.getString("VRSTA").equals("IRN") || racuni.getString("VRSTA").equals("OKK")) {
            if (racuni.getInt("KUPAC") == poscpar) {
              racuni.setString("VRSTA", "MP");
              racuni.post();
            } else {
              racuni.setString("VRSTA", "RN");
            }
          } else {
            racuni.setString("VRSTA", "UR");
          }
          //micanje godine iz broja
          StringTokenizer brtok = new StringTokenizer(racuni.getString("BROJ"),"-");
          if (brtok.countTokens() == 4) {
            String broj = brtok.nextToken()+"-"+brtok.nextToken()+"-"+brtok.nextToken();
            racuni.setString("BROJ", broj);
          }
        }
        String[] cols = new String[] {"VRSTA","KUPAC","OIB","BROJ","DATUM","IZNOS","OSNOVICA","PDV","STOPA","DATDOSP"};
        File frn = new File(fn+".rn");
        TextFile tw = TextFile.write(frn);  
        dumpTable(tw, racuni, cols);
        tw.close();
        
        //partneri
        String kry = "SELECT 'PP' AS VRSTA, cpar, oib, nazpar, mj, adr, mb, zr from Partneri WHERE " +
            "EXISTS (SELECT cpar from skstavke where skstavke.cpar = partneri.cpar " +
            "AND skstavke.cgkstavke like '"+cnaloga+"-%')";
        StorageDataSet partneri = Aus.q(kry);
        String[] kols = new String[] {"VRSTA", "CPAR", "OIB", "NAZPAR", "MJ", "ADR","MB", "ZR"};
        File fpp = new File(fn+".pp");
        TextFile tp = TextFile.write(fpp);  
        dumpTable(tp, partneri, kols);
        tp.close();
        
        // cat fpp frn > ret :-|
        String line;
        File ret = new File(fn+".txt");
        TextFile tfret = TextFile.write(ret);
        TextFile rpp = TextFile.read(fpp);
        while ((line = rpp.in()) != null) tfret.out(line);
        tfret.close();
        tfret = TextFile.append(ret.getAbsolutePath());
        TextFile rrn = TextFile.read(frn);
        while ((line = rrn.in()) != null) tfret.out(line);        
        tfret.close();
        return ret;
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    return null;
  }

  public static void dumpTable(TextFile f, StorageDataSet table, String[] cols) {
    String sep = sep = Aus.getDumpSeparator();
    VarStr line = new VarStr();
    table.open();
    for (table.first(); table.inBounds(); table.next()) {
      line.clear();
      for (int i = 0; i < cols.length; i++) {
        line.append(raVariant.getDataSetValue(table, cols[i])).append(sep);
      }
      System.out.println(line);
      f.out(line.toString());
    }
  }

}

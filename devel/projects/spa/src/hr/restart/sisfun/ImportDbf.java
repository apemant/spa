package hr.restart.sisfun;

import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.robno.Util;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;
import org.xBaseJ.indexes.NDX;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class ImportDbf {

  static Map artmap;
  
  public static void start(String root) {
    
    try {
      
      DataSet arts = Aus.q("SELECT * FROM artikli");
      artmap = new HashMap();
      for (arts.first(); arts.inBounds(); arts.next()) {
        artmap.put(arts.getString("CART1"), 
            new Integer(arts.getInt("CART")));
      }
      
      File rootf = new File(root);
      File vesna = new File(rootf, "vesna");
      File tena = new File(rootf, "tena");
      File pierre = new File(rootf, "pierre");
      
      Valid.getValid().runSQL("DELETE FROM stanje");
      
      DBF dbf = new DBF(new File(vesna, "MPSTANJE.DBF").toString());      
      dbf.useIndex(new File(vesna, "MPSTANJE.NDX").toString());
      process(dbf, "1");
      dbf.close();
      
      dbf = new DBF(new File(tena, "MPSTANJE.DBF").toString());      
      dbf.useIndex(new File(tena, "MPSTANJE.NDX").toString());
      process(dbf, "2");
      dbf.close();
      
      dbf = new DBF(new File(pierre, "MPSTANJE.DBF").toString());      
      //dbf.useIndex(new File(pierre, "MPSTANJE.NDX").toString());
      process(dbf, "3");
      dbf.close();
      
/*      System.out.println("Rows:");
      System.out.println(dbf.getRecordCount());
      for (int j = 1; j <= dbf.getFieldCount(); j++) {
        System.out.print(dbf.getField(j).getName());
        System.out.print(",");
      }
      System.out.println();*/
//   PM,SIFRA,NC,MPC,RABAT_DOBA,ZTN_DOBA,DATUM_A,DATUM_U,DATUM_P,
//    ULAZ_KOL,ULAZ_VRI,IZLAZ_KOL,IZLAZ_VRI,NOMEN,DOKUMENT,SNIZENO,AKCIJA,INVENTURA,U_VELEP,U_ODJAVA,U_PREDISP,U_PLUS,U_MINUS,Z_GORE,Z_DOLE,I_POPUST,REZERVA,NCJINVEST,
      
      
      /*for (int i = 1; i <= dbf.getRecordCount(); i++) {
        dbf.gotoRecord(i, false);
        for (int j = 1; j <= dbf.getFieldCount(); j++) {
          System.out.print(dbf.getField(j).get().trim());
          System.out.print(",");
        }
        System.out.println();
      }
      dbf.close();*/
      
    } catch (xBaseJException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  static void process(DBF dbf, String prefix) throws Exception {
//  PM,SIFRA,NC,MPC,RABAT_DOBA,ZTN_DOBA,DATUM_A,DATUM_U,DATUM_P,
//  ULAZ_KOL,ULAZ_VRI,IZLAZ_KOL,IZLAZ_VRI,NOMEN,DOKUMENT,SNIZENO,AKCIJA,INVENTURA,U_VELEP,U_ODJAVA,U_PREDISP,U_PLUS,U_MINUS,Z_GORE,Z_DOLE,I_POPUST,REZERVA,NCJINVEST,
    
    QueryDataSet st = Stanje.getDataModule().getTempSet(Condition.nil);
    st.open();
    String god = "2011"; 
    Timestamp beg = hr.restart.util.Util.getUtil().getYearBegin(god);
    BigDecimal pf = new BigDecimal("1.23");
    
    String[] clr = new VarStr("KOLPS KOLREZ NABPS MARPS PORPS VPS KOLSKLADPS").split();
    
    for (int i = 1; i <= dbf.getRecordCount(); i++) {
      dbf.gotoRecord(i, false);
      if (dbf.deleted()) continue;
      
      String pm = dbf.getField("PM").get().trim();
      String pfx = pm;
      if (pm.equals("051")) pfx = "001";
      String sif = pfx + "-" + dbf.getField("SIFRA").get().trim();
      Integer ci = (Integer) artmap.get(sif);
      if (ci == null) {
        System.out.println("Nema šifre " + sif);
        continue;
      }
      
      BigDecimal mpc = num(dbf, "MPC");
      if (mpc.signum() == 0) continue;
      
      st.insertRow(false);
      st.setString("GOD", god);
      st.setString("CSKL", prefix+pm);
      st.setInt("CART", ci.intValue());
      st.setBigDecimal("KOLUL", num(dbf, "ULAZ_KOL"));
      st.setBigDecimal("KOLIZ", num(dbf, "IZLAZ_KOL"));
      st.setBigDecimal("VUL", num(dbf, "ULAZ_VRI"));
      st.setBigDecimal("VIZ", num(dbf, "IZLAZ_VRI"));
      st.setBigDecimal("NC", num(dbf, "NC"));
      st.setBigDecimal("MC", mpc);
      
      
      
      for (int ic = 0; ic < clr.length; ic++)
        Aus.clear(st, clr[ic]);
      
      st.setTimestamp("DATZK", beg);
      st.setBigDecimal("VC", st.getBigDecimal("MC").
          divide(pf, 2, BigDecimal.ROUND_HALF_UP));
      
      Aus.set(st, "ZC", "MC");
      Aus.sub(st, "KOL", "KOLUL", "KOLIZ");
      Aus.sub(st, "VRI", "VUL", "VIZ");
      Aus.mul(st, "NABUL", "NC", "KOLUL");
      Aus.mul(st, "NABIZ", "NC", "KOLIZ");
      
      Aus.set(st, "PORUL", "VUL");
      Aus.div(st, "PORUL", pf);
      Aus.sub(st, "MARUL", "PORUL", "NABUL");
      Aus.sub(st, "PORUL", "VUL", "PORUL");
      
      Aus.set(st, "PORIZ", "VIZ");
      Aus.div(st, "PORIZ", pf);
      Aus.sub(st, "MARIZ", "PORIZ", "NABIZ");
      Aus.sub(st, "PORIZ", "VIZ", "PORIZ");
      
      Aus.set(st, "KOLSKLAD", "KOL");
      Aus.set(st, "KOLSKLADUL", "KOLUL");
      Aus.set(st, "KOLSKLADIZ", "KOLIZ");
      st.post();
    }
    
    frmTableDataView view = new frmTableDataView(true, true, false);
    view.setDataSet(st);
    view.show();
  }
  
  static BigDecimal num(DBF dbf, String col) throws Exception {
    return Aus.getDecNumber(dbf.getField(col).get().trim());
  }
}


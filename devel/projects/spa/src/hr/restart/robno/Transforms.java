package hr.restart.robno;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.*;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;


public class Transforms {

  QueryDataSet art;
  StorageDataSet ret;
  lookupData ld = lookupData.getlookupData();
  public Transforms() {
    art = Artikli.getDataModule().openTempSet();
    ret = Aus.createSet("NOTES:200");
  }
  
  
  
  public DataSet moveArt(int cfrom, int cto, String god, boolean save) {
    ret.empty();
    if (!ld.raLocate(art, "CART", cfrom+"")) return addNote("ERROR: cfrom not found: " + cfrom);

    art.setString("AKTIV", "N");
    art.post();
    String[] acc = {"CART", "CART1", "BC", "NAZART"};
    addNote("cfrom aktiv changed: " + cfrom);
    
    if (!ld.raLocate(art, "CART", cto+"")) return addNote("ERROR: cto not found: " + cto);
    
    Condition cg = Condition.equal("GOD", god);
    Condition cf = Condition.equal("CART", cfrom);
    Condition ct = Condition.equal("CART", cto);
    
    QueryDataSet dsstdoki = stdoki.getDataModule().openTempSet(cg.and(cf));
    if (dsstdoki.rowCount() > 0) {
      for (dsstdoki.first(); dsstdoki.inBounds(); dsstdoki.next()) {
        dM.copyColumns(art, dsstdoki, acc);
        dsstdoki.post();
      }
      
      DataSet dsinto = stdoki.getDataModule().openTempSet(cg.and(ct));
      
      addNote("stdoki rows changed: " + dsstdoki.rowCount() + 
          "  avg NC = " + avg(dsstdoki, "NC") + " (old " + avg(dsinto, "NC") + ")  " +
          "avg ZC = " + avg(dsstdoki, "ZC") + " (old " + avg(dsinto, "ZC") + ")  ");
    }
    
    QueryDataSet dsstdoku = Stdoku.getDataModule().openTempSet(cg.and(cf));
    if (dsstdoku.rowCount() > 0) {
      for (dsstdoku.first(); dsstdoku.inBounds(); dsstdoku.next()) {
        dM.copyColumns(art, dsstdoku, acc);
        dsstdoku.post();
      }
      
      DataSet dsinto = Stdoku.getDataModule().openTempSet(cg.and(ct));
      
      addNote("stdoku rows changed: " + dsstdoku.rowCount() + 
          "  avg NC = " + avg(dsstdoku, "NC") + " (old " + avg(dsinto, "NC") + ")  " +
          "avg DC = " + avg(dsstdoku, "DC") + " (old " + avg(dsinto, "DC") + ")  "+
          "avg ZC = " + avg(dsstdoku, "ZC") + " (old " + avg(dsinto, "ZC") + ")  ");
    }
    
    QueryDataSet dsstpos = Stpos.getDataModule().openTempSet(cg.and(cf));
    if (dsstpos.rowCount() > 0) {
      for (dsstpos.first(); dsstpos.inBounds(); dsstpos.next()) {
        dM.copyColumns(art, dsstpos, acc);
        dsstpos.post();
      }
      
      DataSet dsinto = Stpos.getDataModule().openTempSet(cg.and(ct));
      
      addNote("stpos rows changed: " + dsstpos.rowCount() + 
          "  avg MC = " + avg(dsstpos, "MC") + " (old " + avg(dsinto, "MC") + ")  ");
    }
    
    QueryDataSet dsstmeskla = Stmeskla.getDataModule().openTempSet(cg.and(cf));
    if (dsstmeskla.rowCount() > 0) {
      for (dsstmeskla.first(); dsstmeskla.inBounds(); dsstmeskla.next()) {
        dM.copyColumns(art, dsstmeskla, acc);
        dsstmeskla.post();
      }
      
      DataSet dsinto = Stmeskla.getDataModule().openTempSet(cg.and(ct));
      
      addNote("stdoku rows changed: " + dsstmeskla.rowCount() + 
          "  avg NC = " + avg(dsstmeskla, "NC") + " (old " + avg(dsinto, "NC") + ")  " +
          "avg ZC = " + avg(dsstmeskla, "ZC") + " (old " + avg(dsinto, "ZC") + ")  "+
          "avg ZCUL = " + avg(dsstmeskla, "ZCUL") + " (old " + avg(dsinto, "ZCUL") + ")  ");
    }
    
    QueryDataSet dsstug = stugovor.getDataModule().openTempSet(cf);
    if (dsstug.rowCount() > 0) {
      for (dsstug.first(); dsstug.inBounds(); dsstug.next()) {
        dM.copyColumns(art, dsstug, acc);
        dsstug.post();
      }
      addNote("stugovor rows changed: " + dsstmeskla.rowCount());
    }
    
    QueryDataSet dsda = dob_art.getDataModule().openTempSet(cf);
    if (dsda.rowCount() > 0) {
      DataSet dsdao = dob_art.getDataModule().openTempSet(ct);
      for (dsda.first(); dsda.inBounds(); dsda.next()) {
        if (ld.raLocate(dsdao, "CPAR", dsda)) {
          addNote("WARN: removed dob_art duplicate, CPAR = " + dsda.getInt("CPAR") 
              + "  DC = " + dsda.getBigDecimal("DC") + " (old " + dsdao.getBigDecimal("DC"));
          dsda.deleteRow();
        } else {
          dM.copyColumns(art, dsda, acc);
          dsda.post();
          addNote("dob_art changed for CPAR " + dsda.getInt("CPAR"));
        }
      }
    }
    
    QueryDataSet dska = kup_art.getDataModule().openTempSet(cf);
    if (dska.rowCount() > 0) {
      DataSet dskao = kup_art.getDataModule().openTempSet(ct);
      for (dska.first(); dska.inBounds(); dska.next()) {
        if (ld.raLocate(dskao, "CPAR", dsda)) {
          addNote("WARN: removed kup_art duplicate, CPAR = " + dska.getInt("CPAR") 
              + "  DC = " + dska.getBigDecimal("DC") + " (old " + dskao.getBigDecimal("DC") + ")");
          dska.deleteRow();
        } else {
          dM.copyColumns(art, dska, acc);
          dska.post();
          addNote("kup_art changed for CPAR " + dska.getInt("CPAR"));
        }
      }
    }
    
    String[] ck = {"CORG","CSKL","CPAR"};
    QueryDataSet dsc = Cjenik.getDataModule().openTempSet(cf);
    if (dsc.rowCount() > 0) {
      DataSet dsco = Cjenik.getDataModule().openTempSet(ct);
      for (dsc.first(); dsc.inBounds(); dsc.next()) {
        if (ld.raLocate(dsco, ck, dsc)) {
          addNote("WARN: removed cjenik duplicate, CPAR = " + dsc.getInt("CPAR") 
              + "  VC = " + dsc.getBigDecimal("VC") + " (old " + dsco.getBigDecimal("DC") + ")");
          dsc.deleteRow();
        } else {
          dM.copyColumns(art, dsc, acc);
          dsc.post();
          addNote("cjenik changed for CPAR " + dsc.getInt("CPAR"));
        }
      }
    }
    
    QueryDataSet dsnor = norme.getDataModule().openTempSet(cf);
    if (dsnor.rowCount() > 0) {
      DataSet dsnoro = norme.getDataModule().openTempSet(ct);
      for (dsnor.first(); dsnor.inBounds(); dsnor.next()) {
        if (ld.raLocate(dsnoro, "CARTNOR", dsnor)) {
          addNote("WARN: removed norme duplicate, CARTNOR = " + dsnor.getInt("CARTNOR"));
          dsnor.deleteRow();
        } else {
          dM.copyColumns(art, dsnor, acc);
          dsnor.post();
          addNote("norme changed for CARTNOR " + dsnor.getInt("CARTNOR"));
        }
      }
    }
    
    QueryDataSet dsnorm = norme.getDataModule().openTempSet(Condition.equal("CARTNOR", cfrom));
    if (dsnorm.rowCount() > 0) {
      DataSet dsnormo = norme.getDataModule().openTempSet(Condition.equal("CARTNOR", cto));
      if (dsnormo.rowCount() > 0) {
        addNote("WARN: removed norme duplicate for CARTNOR");
        dsnorm.deleteAllRows();
      } else {
        for (dsnorm.first(); dsnorm.inBounds(); dsnorm.next()) {
          dsnorm.setInt("CARTNOR", cto);
          dsnorm.post();
        }
        addNote("norme changed for CARTNOR");
      }
    }
    
    if (!save) {
      addNote("saving skipped, no changes");
    } else {
      if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] 
          {dsstdoki, dsstdoku, dsstmeskla, dsstpos, dsstug, dsda, dska, dsc, dsnor, dsnorm, art}))
        addNote("ERROR saving data");
      else addNote("Everything saved OK.");
    }    
    return ret;
  }
  
  private BigDecimal avg(DataSet ds, String col) {
    BigDecimal avg = Aus.zero0;
    if (ds.rowCount() == 0) return avg;
    
    for (ds.first(); ds.inBounds(); ds.next()) avg = avg.add(ds.getBigDecimal(col));
    return avg.divide(new BigDecimal(ds.rowCount()), avg.scale(), BigDecimal.ROUND_HALF_UP);
  }
  
  private DataSet addNote(String line) {
    ret.insertRow(false);
    ret.setString("NOTES", line);
    ret.post();
    return ret;
  }

}

package hr.restart.sisfun;

import hr.restart.baza.Artikli;
import hr.restart.baza.Grupart;
import hr.restart.baza.Pjpar;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;


public class ArtikliImport {
  
  static String getString(HSSFRow row, int col) {
    HSSFCell cell = row.getCell((short) col);
    if (cell == null) return "";
    return cell.getStringCellValue().trim();
  }
  
  static BigDecimal getNum(HSSFRow row, int col) {
    HSSFCell cell = row.getCell((short) col);
    if (cell == null) return Aus.zero0;
    return BigDecimal.valueOf(cell.getNumericCellValue());
  }
  
  public static void main(String[] args) throws Exception {
    JFrame main = new JFrame();
    JFileChooser fc = new JFileChooser(new File("."));
    if (fc.showOpenDialog(main) != fc.APPROVE_OPTION) return;
    
    dM.setMinimalMode();
    
    FileInputStream fis = new FileInputStream(fc.getSelectedFile());    
    POIFSFileSystem fs = new POIFSFileSystem(fis);
    HSSFWorkbook wb = new HSSFWorkbook(fs);
    HSSFSheet sheet = wb.getSheetAt(0);
    
    /*StorageDataSet art = Artikli.getDataModule().getReadonlySet();
    art.open();
    StorageDataSet ga = Grupart.getDataModule().getReadonlySet();
    ga.open();
    StorageDataSet pga = Grupart.getDataModule().getReadonlySet();
    pga.open();
    
    lookupData ld = lookupData.getlookupData();
    int num = 10000;
    
    for (int i = 3; i< 191; i++) {
      HSSFRow row = sheet.getRow(i);
      
      String gr1 = getString(row, 7);
      String gr2 = getString(row, 4);
      if (!ld.raLocate(ga, "NAZGRART", gr1)) {
        ga.insertRow(false);
        ga.setString("CGRART", gr1.toUpperCase().substring(0, 1));
        ga.setString("NAZGRART", gr1);
        ga.setString("CGRARTPRIP", "D");
        ga.setInt("SSORT", 0);
      }
      if (!ld.raLocate(pga, "NAZGRART", gr2)) {
        ga.setInt("SSORT", ga.getInt("SSORT") + 1);
        pga.insertRow(false);
        pga.setString("CGRART", ga.getString("CGRART") + ga.getInt("SSORT")); 
        pga.setString("NAZGRART", gr2);
        pga.setString("CGRARTPRIP", ga.getString("CGRART"));
      }
      
      art.insertRow(false);
      art.setInt("CART", num++);
      art.setString("CART1", getNum(row, 1).toPlainString());
      try {
        art.setString("BC", getNum(row, 0).toPlainString());
      } catch (RuntimeException e) {
        art.setString("BC", getString(row, 0).trim());
      }
      String naz = getString(row, 2);
      if (naz.length() > 50) naz = naz.substring(0, 50);
      art.setString("NAZART", naz);
      art.setString("JM", getString(row, 3));
      art.setBigDecimal("VC", getNum(row, 5));
      art.setString("CPOR", "1");
      art.setString("CGRART", pga.getString("CGRART"));
      art.setString("TIPART", "R");
      art.setString("VRART", "R");
      art.setString("NAZPRI", art.getString("NAZART"));
    }
    for (pga.first(); pga.inBounds(); pga.next()) {
      ga.insertRow(false);
      pga.copyTo(ga);
    }
    
    Artikli.getDataModule().dumpTable(art, new File("."), "artikli");
    Artikli.getDataModule().dumpTable(ga, new File("."), "grupart");*/
    
    StorageDataSet pj = Pjpar.getDataModule().getReadonlySet();
    pj.open();
    
    lookupData ld = lookupData.getlookupData();
    for (int i = 4; i < 136; i++) {
      HSSFRow row = sheet.getRow(i);
      
      pj.insertRow(false);
      pj.setInt("CPAR", 99999);
      pj.setInt("PJ", getNum(row, 0).intValue());
      pj.setString("NAZPJ", getString(row, 1));
      pj.setString("MJPJ", getString(row, 1));
      pj.setString("ADRPJ", getString(row, 2));
      pj.setString("TELPJ", getString(row, 3));
      pj.setString("KOPJ", getString(row, 4));
    }
    
    Pjpar.getDataModule().dumpTable(pj, new File("."), "pjpar");
    
    fis.close();
  }
}

package hr.restart.util.misc;


import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.KreirDrop;
import hr.restart.baza.Tablice;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;

public class KreToRoo {
  public KreToRoo() {
    // TODO Auto-generated constructor stub
  }
  public static void main(String[] args) {
    KreToRoo ktr = new KreToRoo();
    VarStr ret = new VarStr(300);
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        ret.append(ktr.createRoo(args[i])).append("\n");
      }
    } else {
      QueryDataSet tab = Tablice.getDataModule().getFilteredDataSet("");
      tab.open();
      for (tab.first(); tab.inBounds(); tab.next()) {
        ret.append(ktr.createRoo(tab.getString("KLASATAB"))).append("\n");
      }
    }
    
    FileHandler.writeConverted(ret.toString(), "erp.roo", null);
    System.out.println(ret.toString());
  }
  private VarStr createRoo(String tab) {
    VarStr s = new VarStr(300);
    KreirDrop t = KreirDrop.getModule(tab);
    if (t == null) {
      try {
        t = (KreirDrop) Class.forName(tab).newInstance();
      } catch (Exception e) {
        // silent
      }
    }
    if (t == null) {
      System.err.println("KreirDrop.getModule("+tab+") == null !!!!");
      return new VarStr();
    } else {
      VarStr className = new VarStr(t.Naziv.toLowerCase());
      className.setCharAt(0, t.Naziv.toUpperCase().charAt(0));
      className.insert(0, "Tbl");
      
      s.append("entity --class ~.domain.").append(className)
        .append(" --table ").append(t.Naziv.toUpperCase())
        .append(" --identifierField JPAID")
        .append(" --versionField JPAVER")
        .append(" --testAutomatically")
        .append("\n");
        
      Column[] cols = t.getColumns();
      for (int i = 0; i < cols.length; i++) {
        Column col = cols[i];
        s.append("field");
        String type;
        //test
//          s.append(col.getColumnName()).append(" t:").append(col.getDataType()).append(" "+col.toString()).append("\n");
        //etest
        String digitsInt = col.getPrecision()>0?" --digitsInteger "+col.getPrecision():"";
        String digitsFract = col.getScale()>=0?" --digitsFraction "+col.getScale():"";
        digitsInt = "".equals(digitsFract)?"":digitsInt;
        digitsFract = "".equals(digitsInt)?"":digitsFract;
        
        switch (col.getDataType()) {
        
        case Variant.TIMESTAMP:
          s.append(" date --fieldName ").append(col.getColumnName()).append(" --type java.util.Date ");//.append(" --dateTimeFormatPattern dd-MM-yyyy hh:mm:ss");
          break;
        
        case Variant.STRING:
          s.append(" string --fieldName ").append(col.getColumnName()).append(" --sizeMax ").append(col.getPrecision());
          break;
          
        case Variant.BIGDECIMAL:
          s.append(" number --type java.math.BigDecimal --fieldName ").append(col.getColumnName()).append(digitsInt).append(digitsFract);
          break;
          
        case Variant.INT:
          s.append(" number --type java.lang.Integer --fieldName ").append(col.getColumnName());
          break;
          
        case Variant.SHORT:
          s.append(" number --type java.lang.Short --fieldName ").append(col.getColumnName());
          break;
          
        case Variant.DOUBLE:
          s.append(" number --type java.lang.Double --fieldName ").append(col.getColumnName()).append(digitsInt).append(digitsFract);
          break;
  
        default:
          s.append(" string --fieldName ").append(col.getColumnName());
          break;
        }
        s.append(" --permitReservedWords\n");
      }
      return s;
    }
  }
}

package hr.restart.util.misc;

import hr.restart.baza.Condition;
import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Tablice;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

public class KreToGrails {
  public KreToGrails() {
    // TODO Auto-generated constructor stub
  }
  
  public static void main(String[] args) {
    if (args != null && args.length > 0) {
      if ("perApp".equals(args[0])) {
        createPluginPerApp();
      } else {
        createPlugin(args, null);
      }
    } else {
      createPlugin(ConsoleCreator.getModuleClasses(), null);
    }
  }

  private static void createPluginPerApp() {
    StorageDataSet tstset = Tablice.getDataModule().getTempSet(Condition.equal("APP", "!undef!"));
    tstset.open();
    if (tstset.getRowCount() > 0) {
      if (JOptionPane.showConfirmDialog(null, "Ima "+tstset.getRowCount()+" !undef!-a koji ce biti ignorirani. Nastaviti enivej?", "Kvescn", JOptionPane.YES_NO_OPTION) 
          == JOptionPane.NO_OPTION) {
        System.exit(0);
      }
    }
    StorageDataSet appset = Tablice.getDataModule().getTempSet("APP != '!undef!'");
    appset.open();
//    HashMap<String, ArrayList<String>> apptbles = new HashMap<String, ArrayList<String>>();
    HashMap apptbles = new HashMap();
    for (appset.first(); appset.inBounds(); appset.next()) {
      String app = appset.getString("APP");
      ArrayList tlist = (ArrayList) apptbles.get(app);
      if (tlist == null) tlist = new ArrayList();
      tlist.add(appset.getString("KLASATAB"));
      apptbles.put(app, tlist);
    }
    for (Iterator iterator = apptbles.keySet().iterator(); iterator.hasNext();) {
      String app = (String) iterator.next();
      String[] ents = (String[])((ArrayList) apptbles.get(app)).toArray(new String[] {});
      createPlugin(ents, app);
    }
  }

  public static void createPlugin(String[] ents, String name) {
    if (name == null) name = "grails_domain";
    File dir = new File("grails_domain"+File.separatorChar+name);
    if (dir.exists() && dir.isDirectory()) {
      File[] todel = dir.listFiles();
      for (int j = 0; j < todel.length; j++) {
        todel[j].delete();
      }
    } else dir.mkdirs();
    for (int i = 0; i < ents.length; i++) {
      String[] fna = new VarStr(ents[i]).split('.');
      String fn = new VarStr(fna[fna.length-1].toLowerCase()).capitalize()+".groovy";
      fn = dir.getAbsolutePath()+File.separator+fn;
      System.out.println("Writing "+fn);
      FileHandler.writeConverted(createGrails(ents[i], name).toString(), fn, null);
    }
  }
  public static VarStr createGrails(String tab, String name) {
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
      s.append("package hr.restart.erp."+name+".domain\n\n");
      s.append("class ").append(new VarStr(t.Naziv.toLowerCase()).capitalize()).append(" {\n");

      VarStr columns = new VarStr();
      
      VarStr mapping = new VarStr();
      mapping.append("\t\tstatic mapping = {\n");
      mapping.append("\t\t\ttable \"").append(t.Naziv.toUpperCase()).append("\"\n");
      mapping.append("\t\t\tversion true\n");
      mapping.append("\t\t\tid column: \"JPA_ID\"\n");
      
      VarStr constraints = new VarStr();
      constraints.append("\t\tstatic constraints = {\n");

      Column[] cols = t.getColumns();
      for (int i = 0; i < cols.length; i++) {
        Column col = cols[i];
        String cn = "cl"+col.getColumnName().substring(0,1).toUpperCase()+col.getColumnName().toLowerCase().substring(1).trim();
        String dbcn = col.getColumnName().toUpperCase();
        
        constraints.append("\t\t\t").append(cn).append("(nullable: true)\n");

        switch (col.getDataType()) {
        
        case Variant.TIMESTAMP:
          columns.append("\tDate ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\"\n");
          break;
        
        case Variant.STRING:
          columns.append("\tString ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\", length: ").append(col.getPrecision()).append("\n");
          break;
          
        case Variant.BIGDECIMAL:
          columns.append("\tBigDecimal ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase())
            .append("\", scale: ").append(col.getScale()).append(", precision: ").append(col.getPrecision()).append("\n");
          break;
          
        case Variant.INT:
          columns.append("\tInteger ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\"\n");
          break;
          
        case Variant.SHORT:
          columns.append("\tShort ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\"\n");
          break;
          
        case Variant.DOUBLE:
          columns.append("\tDouble ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\"\n");
          break;
  
        default:
          columns.append("\tString ").append(cn).append("\n");
          mapping.append("\t\t\t").append(cn).append("\tcolumn: \"").append(dbcn.toUpperCase()).append("\"\n");
          break;
        }
      }
      mapping.append("\t\t}\n");
      constraints.append("\t\t}\n");
      s.append(columns).append("\n")
      .append(mapping).append("\n")
      .append(constraints).append("}\n");
        
      return s;
    }
  }
}

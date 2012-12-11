package hr.restart.util.misc;

import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.dM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hr.restart.util.VarStr;

public class IDTriggerCreator {
  private static VarStr createtrigger = new VarStr(
 //      " SET TERM ^ ; \n"
      " CREATE TRIGGER idhandler%TBL%\n"
      +" before insert or update on %TBL%\n"
      +" AS \n"
      +" BEGIN\n" 
      +"    if (new.JPA_ID is null) then\n"
      +"     begin\n"
      +"         new.JPA_ID = gen_id(HIBERNATE_SEQUENCE,1);\n"
      +"         new.\"VERSION\" = '1';\n"
      +"     end    \n"
      +" END\n"
   //   +" SET TERM ; ^\n"
      );
  public static void main(String[] args) throws SQLException {
//    dM.getDataModule().loadModules();
    dM.setMinimalMode();
    Statement stmt = dM.getDataModule().getDatabaseConnection().createStatement();
    ResultSet rs = stmt.executeQuery("SELECT CAST(count(*) AS NUMERIC) AS XCNT FROM RDB$TRIGGERS where RDB$TRIGGER_NAME like 'IDHANDLER%'");
    rs.next();
    if (rs.getInt(1) == 0) {
      for (int i = 0; i < ConsoleCreator.getModuleClasses().length; i++) {
        String mc = ConsoleCreator.getModuleClasses()[i];
        String tbl = mc.substring(mc.lastIndexOf(".")+1).trim().toUpperCase();
        System.out.println(tbl);
        String crtr = new VarStr(createtrigger).replaceAll("%TBL%", tbl).toString();
        System.out.println(crtr);
        stmt.execute(crtr);
      }
    }
  }
}

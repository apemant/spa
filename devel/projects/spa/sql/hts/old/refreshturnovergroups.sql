<#>
import hr.restart.db.raVariant;
orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.1:1521/ABS1","abssolute","abssolute");
tog=orac.getData("turnovergroupsperinvoiceline.sql","");
for (tog.first();tog.inBounds();tog.next()) {
q="UPDATE stdoki set brpri='"+tog.getString("tog_code")+"' WHERE veza ='"
+tog.getString("invoiceinumberB")+
"' AND cart1='"+tog.getString("cart")+"' AND vrdok='RAC' AND CSKL='01'";
hr.restart.util.Valid.getValid().runSQL(q); 
}

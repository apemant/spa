<#>
setAccessibility(true);
import hr.restart.db.raVariant;
orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.1:1521/ABS1","abssolute","abssolute");
veza = orac.getData("select cast (c.CUSTOMERNUMBER as char(20)) as cpar, cast (e.employeenumber as char(10)) as CAG from contractspercustomer cp, customer c, employee e where cp.customer_id = c.customer_id and cp.accountmanager_id = e.employee_id");
pars =  Partneri.getDataModule().copyDataSet();
pars.open();
for (veza.first();veza.inBounds();veza.next()) {
   if (lookupData.getlookupData().raLocate(pars,"CPAR",veza.getString("CPAR"))) {
      pars.setInt("CAGENT",Integer.parseInt(veza.getString("CAG")));
     pars.post();
   } 
}
pars;

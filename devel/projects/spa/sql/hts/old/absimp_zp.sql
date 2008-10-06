<#>
import hr.restart.db.raVariant;
orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.1:1521/ABS1","abssolute","abssolute");
//raVariant.setDebug(true);

//Partneri
ABScust = orac.getData("customers.sql", "");
pars =  Partneri.getDataModule().copyDataSet();
newp = Partneri.getDataModule().getTempSet("0=1");
newp.open();
pars.open();
for (ABScust.first(); ABScust.inBounds(); ABScust.next()) {
  if (!Valid.getValid().chkExistsSQL(pars, "CPAR", ABScust.getDouble("CUSTOMERNUMBER")+"")) {
    newp.insertRow(false);
    raVariant.copyTo(ABScust, newp, "-,-,cpar,adr,mj,mb,nazpar,tel,telfax,emadr,zr");
    if ("".equals(newp.getString("mb"))) {
      newp.setString("MB",("KRIVI:"+newp.getInt("CPAR")));
    }
    newp.post();
  }
}
//newp.saveChanges();

//Artikli
ABSprod = orac.getData("productwithsizes.sql","");
arts = Artikli.getDataModule().copyDataSet();
newa = Artikli.getDataModule().getTempSet("0=1");
newa.open();
arts.open();
arts.setSort(new SortDescriptor(new String[] {"CART"}));
arts.last();
int lca = arts.getInt("CART");
dupli = new HashSet();
for (ABSprod.first(); ABSprod.inBounds(); ABSprod.next()) {
  prodcode = ABSprod.getString("PRODUCTCODE").trim();
  if (!Valid.getValid().chkExistsSQL(arts, "CART1", prodcode) && !dupli.contains(prodcode)) {
    dupli.add(prodcode);
    newa.insertRow(false);
    raVariant.copyTo(ABSprod, newa, "cart1,nazart,cgrart");
    lca++;
    newa.setInt("CART",lca);
    newa.setString("BC", newa.getString("CART1"));
    newa.setString("JM","kom");
    newa.setString("CPOR","1");
    newa.post();
  }
}


try {
	raTransaction.saveChangesInTransaction(
	  new com.borland.dx.sql.dataset.QueryDataSet[] {
		newp,
		newa
	  });                    
	javax.swing.JOptionPane.showMessageDialog(null, 
		"Preneseno je "+newp.getRowCount()+" novih partnera, "+newa.getRowCount()+" novih artikala i NEMA novih raèuna");
}  catch (Exception e) {
	javax.swing.JOptionPane.showMessageDialog(null, 
		"Prijenos dokumenata nije uspio!! Probajte ponovo!",
		"!!!ERROR!!!",JOptionPane.ERROR_MESSAGE);
}


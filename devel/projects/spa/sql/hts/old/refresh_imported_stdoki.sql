<#>
arts = Artikli.getDataModule().copyDataSet();
  stanje = hr.restart.baza.Stanje.getDataModule().getTempSet(Condition.equal("GOD","2008"));
  stanje.open();

newstd = Util.getNewQueryDataSet("SELECT * from stdoki where cart is null");
for (newstd.first();newstd.inBounds();newstd.next()) {
    if (lookupData.getlookupData().raLocate(arts,"CART1",newstd.getString("CART1"))) {
      newstd.setInt("CART",arts.getInt("CART"));
      newstd.setString("BC",arts.getString("BC"));
      newstd.setString("NAZART",arts.getString("NAZART"));
      newstd.setString("JM",arts.getString("JM"));

  if (lookupData.getlookupData().raLocate(stanje,new String[] {"CART","CSKL"},
               new String[] {arts.getInt("CART")+"","1000"})) {
    stanjeFound = true;
  } else {
    if (newstd.getString("CART1").trim().endsWith("P")) {
      if (!lookupData.getlookupData().raLocate(arts,"CART1",
	new VarStr(newstd.getString("CART1")).chop().toString().trim())) {
        stanjeFound = false;
      } else {
        if (lookupData.getlookupData().raLocate(stanje,new String[] {"CART","CSKL"},
               new String[] {arts.getInt("CART")+"","1000"})) {
          stanjeFound = true;
        } else {stanjeFound = false;}
      }
    } else { stanjeFound = false; }
  }
 if (stanjeFound) {
      newstd.setBigDecimal("NC",stanje.getBigDecimal("NC"));
      newstd.setBigDecimal("INAB",newstd.getBigDecimal("NC").multiply(newstd.getBigDecimal("KOL")));
      newstd.setBigDecimal("VC",stanje.getBigDecimal("VC"));
      newstd.setBigDecimal("IBP",newstd.getBigDecimal("VC").multiply(newstd.getBigDecimal("KOL")));
      newstd.setBigDecimal("MC",stanje.getBigDecimal("MC"));
      newstd.setBigDecimal("ISP",newstd.getBigDecimal("MC").multiply(newstd.getBigDecimal("KOL")));
}
    }
} //endfor
newstd

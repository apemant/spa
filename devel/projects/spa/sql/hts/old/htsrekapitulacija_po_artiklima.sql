<#>
<#title>REKAPITULACIJA PO ARTIKLIMA za period $adod - $bddo
<#bddo>DO;T;;;;;0
<#visible>0,1,2,3,4
<#sums>FV,NV,RC
<#adod>OD;F;;;;;0
source(Aus.findFileAnywhere("HTSpreprepset.bsh").toURL());
    repset.setSort(new SortDescriptor(new String[] {"CART1"}));
    StorageDataSet rez2 = new StorageDataSet();
    rez2.addColumn(dM.createStringColumn("CART1", "Oznaka", 20));
    rez2.addColumn(dM.createStringColumn("NAZART", "Naziv artikla", 50));
    rez2.addColumn(dM.createBigDecimalColumn("FV", "Prodajna vrijednost"));
    rez2.addColumn(dM.createBigDecimalColumn("NV", "Nabavna vrijednost"));
    rez2.addColumn(dM.createBigDecimalColumn("RC", "Razlika u cijeni"));
    rez2.open();
    String __cart1 = "$#";
    for (repset.first();repset.inBounds();repset.next()) {
      if (!repset.getString("CNADGR").equals("100")) continue;
      String t_cart1 = repset.getString("CART1");
      if (t_cart1.equals(__cart1)) {
        rez2.setBigDecimal("FV", rez2.getBigDecimal("FV").add(repset.getBigDecimal("IPRODBP")));
        rez2.setBigDecimal("NV", rez2.getBigDecimal("NV").add(repset.getBigDecimal("NV")));
        rez2.setBigDecimal("RC", rez2.getBigDecimal("RC").add(repset.getBigDecimal("RC")));
      } else {
        rez2.insertRow(false);
        rez2.setString("CART1", repset.getString("CART1"));
        rez2.setString("NAZART", repset.getString("NAZART"));
        rez2.setBigDecimal("RC", repset.getBigDecimal("RC"));
        rez2.setBigDecimal("NV", repset.getBigDecimal("NV"));
        rez2.setBigDecimal("FV",repset.getBigDecimal("IPRODBP"));
        __cart1 = t_cart1;
      }
      rez2.post();
    }
rez2
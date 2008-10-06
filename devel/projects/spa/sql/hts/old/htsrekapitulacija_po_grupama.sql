<#>
<#groups>NAZNADGR
<#title>REKAPITULACIJA PO GRUPAMA za period $adod - $bddo
<#bddo>DO;T;;;;;0
<#visible>0,1,4,5,6
<#sums>FV,NV,RC
<#adod>OD;F;;;;;0
source(Aus.findFileAnywhere("HTSpreprepset.bsh").toURL());
    StorageDataSet rez = new StorageDataSet();
    rez.addColumn(dM.createStringColumn("BRPRI", "Oznaka grupe", 10));
    rez.addColumn(dM.createStringColumn("GRNAZ", "Grupa", 50));
    rez.addColumn(dM.createStringColumn("CNADGR", "Oznaka nadgrupe", 10));
    rez.addColumn(dM.createStringColumn("NAZNADGR", "Nadgrupa", 50));
    rez.addColumn(dM.createBigDecimalColumn("FV", "Prodajna vrijednost"));
    rez.addColumn(dM.createBigDecimalColumn("NV", "Nabavna vrijednost"));
    rez.addColumn(dM.createBigDecimalColumn("RC", "Razlika u cijeni"));
    rez.open();
    String __brpri = "$#";
    for (repset.first();repset.inBounds();repset.next()) {
      String t_brpri = repset.getString("BRPRI");
      if (t_brpri.equals(__brpri)) {
        rez.setBigDecimal("FV", rez.getBigDecimal("FV").add(repset.getBigDecimal("IPRODBP")));
        rez.setBigDecimal("NV", rez.getBigDecimal("NV").add(repset.getBigDecimal("NV")));
        rez.setBigDecimal("RC", rez.getBigDecimal("RC").add(repset.getBigDecimal("RC")));
      } else {
        rez.insertRow(false);
        rez.setString("BRPRI", repset.getString("BRPRI"));
        rez.setString("GRNAZ", repset.getString("GRNAZ"));
        rez.setString("CNADGR", repset.getString("CNADGR"));
        rez.setString("NAZNADGR", repset.getString("NAZNADGR"));
        rez.setBigDecimal("RC", repset.getBigDecimal("RC"));
        rez.setBigDecimal("NV", repset.getBigDecimal("NV"));
        rez.setBigDecimal("FV",repset.getBigDecimal("IPRODBP"));
        __brpri = t_brpri;
      }
      rez.post();
    }
rez
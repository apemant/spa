<#>
<#fromValue>Datum racuna OD;F;;;;;0
<#title>Kupci za racune za knjigovodstvo
<#toValue>DO;T;;;;;0
kupci = Util.getNewQueryDataSet(kry = "SELECT cpar, nazpar, mj, adr, mb, zr from Partneri WHERE " +
    "EXISTS (SELECT cpar from skstavke where skstavke.cpar = partneri.cpar " +
    "AND skstavke.knjig='01' " +
    "AND skstavke.vrdok in ('IRN','OKK') AND skstavke.ID!=0 " +
    "AND "+Condition.between("skstavke.datdok", fromValue, toValue)
    +")");
System.out.println(kry);
for (kupci.first();kupci.inBounds();kupci.next()) {
    String s = kupci.getString("MB");
    char[] arr = s.toCharArray();
    String nar = "";
    for (int i = 0; i < arr.length; i++) {
      if (Character.isDigit(arr[i])) nar = nar + arr[i];
    }
    kupci.setString("MB",nar);
    if (kupci.getString("NAZPAR").trim().equals("")) {
      kupci.setString("NAZPAR","0");
    } else {
      kupci.setString("NAZPAR",kupci.getString("NAZPAR").trim());
    }
    if (kupci.getString("MJ").trim().equals("")) {
      kupci.setString("MJ","0");
    } else {
      kupci.setString("MJ",kupci.getString("MJ").trim());
    }    
    if (kupci.getString("ADR").trim().equals("")) {
      kupci.setString("ADR","0");
    } else {
      kupci.setString("ADR",kupci.getString("ADR").trim());
    }    
    if (kupci.getString("MB").trim().equals("")) {
      kupci.setString("MB","0");
    } else {
      kupci.setString("MB",kupci.getString("MB").trim());
    }
    if (kupci.getString("ZR").trim().equals("")) {
      kupci.setString("ZR","0");
    } else {
      kupci.setString("ZR",kupci.getString("ZR").trim());
    }    
    kupci.post();
}
kupci;

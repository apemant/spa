<#>
<#fromValue>Datum racuna OD;F;;;;;0
<#title>Racuni kupaca za knjigovodstvo
<#toValue>DO;T;;;;;0
<#visible>4,5,6,7,8,9
ds = Util.getNewQueryDataSet(qry = "SELECT Skstavke.cpar as KUPAC, " +
    "skstavke.brojdok as BROJ, " +
    "skstavke.datdok as DATUM," +
    "skstavke.ssaldo as IZNOS," +
    "( select sum(IP) FROM UIstavke" +
    "   WHERE skstavke.knjig = uistavke.knjig" +
    "   AND skstavke.cpar = uistavke.cpar" +
    "   AND skstavke.vrdok = uistavke.vrdok" +
    "   AND skstavke.brojdok = uistavke.brojdok" +
    "   AND uistavke.ckolone in (12,14)) as OSNOVICA," +
    "( select sum(IP) FROM UIstavke" +
    "   WHERE skstavke.knjig = uistavke.knjig" +
    "   AND skstavke.cpar = uistavke.cpar" +
    "   AND skstavke.vrdok = uistavke.vrdok" +
    "   AND skstavke.brojdok = uistavke.brojdok" +
    "   AND uistavke.ckolone in (13,15)) as PDV " +
    " FROM skstavke where " +
    "skstavke.knjig='01' " +
    "AND skstavke.vrdok in ('IRN','OKK') AND skstavke.ID!=0 " +
    "AND "+Condition.between("skstavke.datdok", fromValue, toValue), false);
System.out.println(qry);
/*ds.setColumns(new Column[] {
    dM.createIntColumn("KUPAC"),
    dM.createStringColumn("BROJ", 50),
    dM.createTimestampColumn("DATUM"),
    dM.createBigDecimalColumn("IZNOS"),
    dM.createBigDecimalColumn("OSNOVICA"),
    dM.createBigDecimalColumn("PDV")
});*/
ds;

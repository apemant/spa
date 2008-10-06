<#>
import hr.restart.db.raVariant;
orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.1:1521/ABS1","abssolute","abssolute");

//Otpremnice - prodaja
         arts = Artikli.getDataModule().copyDataSet(); 
         arts.open();                                  

		now = new java.sql.Timestamp(System.currentTimeMillis());
		seqopis2 = "1000"+"OTP"+Util.getUtil().getYear(now);
		intseq = Valid.getValid().findSeqInteger(seqopis2, false, false).intValue();
	        otp_num = "8"+Valid.getValid().maskZeroInteger(new Integer(intseq-1),9);
//otp_num > 31282(seq) i delivery_date >= 01-10-2007
ABSotp = orac.getData("otprsa6.sql",otp_num);
  godset = Util.getNewQueryDataSet("select max(god) as god from Knjigod WHERE app='robno' and corg='01'");
  stanje = hr.restart.baza.Stanje.getDataModule().getTempSet(Condition.equal("GOD",godset.getString("GOD")));
  stanje.open();
newotp = doki.getDataModule().getTempSet("0=1");
newotp.open();
newstotp = stdoki.getDataModule().getTempSet("0=1");
newstotp.open();
int bo = -1;
int rbs = 0;
for (ABSotp.first(); ABSotp.inBounds(); ABSotp.next()) {
if (ABSotp.getDate("DELIVERYDATE").before(java.sql.Date.valueOf("2007-12-31"))) {
	continue;
}
  newstotp.insertRow(false);
  raVariant.copyTo(ABSotp, newstotp, "cradnal,cpar,datdok,cart1,kol,veza");
  newstotp.setInt("BRDOK",Integer.valueOf(newstotp.getString("CRADNAL").trim().substring(1)).intValue());
  newstotp.setString("CSKL","1000");
  newstotp.setString("VRDOK","OTP");

  boolean stanjeFound;
  if (!lookupData.getlookupData().raLocate(arts,"CART1",newstotp.getString("CART1"))) {
    lookupData.getlookupData().raLocate(arts,"CART","1");
  }
  if (lookupData.getlookupData().raLocate(stanje,new String[] {"CART","CSKL"},
               new String[] {arts.getInt("CART")+"","1000"})) {
    stanjeFound = true;
  } else {
    if (newstotp.getString("CART1").trim().endsWith("P")) {
      if (!lookupData.getlookupData().raLocate(arts,"CART1",
	new VarStr(newstotp.getString("CART1")).chop().toString().trim())) {
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
    newstotp.setInt("CART",arts.getInt("CART"));
    newstotp.setString("BC",arts.getString("BC"));
    newstotp.setString("NAZART",arts.getString("NAZART"));
    newstotp.setString("JM",arts.getString("JM"));
    newstotp.setBigDecimal("NC",stanje.getBigDecimal("NC"));
    newstotp.setBigDecimal("INAB",newstotp.getBigDecimal("NC").multiply(newstotp.getBigDecimal("KOL")));
    newstotp.setBigDecimal("VC",stanje.getBigDecimal("VC"));
    newstotp.setBigDecimal("IBP",newstotp.getBigDecimal("VC").multiply(newstotp.getBigDecimal("KOL")));
    newstotp.setBigDecimal("MC",stanje.getBigDecimal("MC"));
    newstotp.setBigDecimal("ISP",newstotp.getBigDecimal("MC").multiply(newstotp.getBigDecimal("KOL")));
    newstotp.setBigDecimal("ZC",newstotp.getBigDecimal("NC"));
    newstotp.setBigDecimal("IRAZ",newstotp.getBigDecimal("ZC").multiply(newstotp.getBigDecimal("KOL")));
    newstotp.setString("CSKLART",newstotp.getString("CSKL"));
  } else {
    System.out.println("Ne mogu pronaci stanje za artikl "+newstotp.getString("CART1"));
  }

  _bo = newstotp.getInt("BRDOK");
  if (bo != _bo) {
    newotp.insertRow(false);
    raVariant.copyTo(ABSotp, newotp, "njesra,cpar,datdok,cart1,kol,pnbz2,sysdat");
    newotp.setInt("BRDOK",newstotp.getInt("BRDOK"));
    newotp.setString("CSKL",newstotp.getString("CSKL"));
    newotp.setString("VRDOK",newstotp.getString("VRDOK"));
    newotp.setString("GOD",Util.getUtil().getYear(newotp.getTimestamp("datdok")));
    newotp.post();
    bo=_bo;
    rbs=1;
  } else {
    rbs++;
  }
  newstotp.setString("GOD",newotp.getString("GOD"));
  newstotp.setShort("RBR",(short)rbs);

  newstotp.post();
}
//seq otpremnice
newotp;

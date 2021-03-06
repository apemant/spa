<#>
import hr.restart.db.raVariant;
//orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.1:1521/ABS1","abssolute","abssolute");
orac = new hr.restart.db.OraCustomConnection("jdbc:oracle:thin:@//192.168.1.160:1521/XE","abssolute","abssolute");
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
    newa.post();
  }
}
//newa.saveChanges();

//* PROMETNI PODACI *//
HashMap seqMap = new HashMap();
//racuni - zaglavlja
         //arts = Artikli.getDataModule().copyDataSet(); //ima gore
         //arts.open();                                  //ima gore
         arts.refresh();
         pdvkoef = new BigDecimal(1.22);
		
		now = new java.sql.Timestamp(System.currentTimeMillis());
		seqopis = "01"+"RAC"+Util.getUtil().getYear(now);
		intseq = Valid.getValid().findSeqInteger(seqopis, false, false).intValue();
		inv_num = "6"+Valid.getValid().maskZeroInteger(new Integer(intseq-1),9);

ABSrac = orac.getData("invoice_header.sql",inv_num);
newd = doki.getDataModule().getTempSet("0=1");
newd.open();
newstd = stdoki.getDataModule().getTempSet("0=1");
newstd.open();
int b=0;
int _rbr=0;
for (ABSrac.first(); ABSrac.inBounds(); ABSrac.next()) {
  try {
    _b = Integer.valueOf(raVariant.getDataSetValue(ABSrac,"INVOICEINUMBERB").toString().substring(1));
  } catch (StringIndexOutOfBoundsException _bex) {
System.out.println("_bex for "+newd.getString("OPIS").trim());
    _b = -1;
  }
  if (_b == b) {
    newd.setBigDecimal("uirac", ABSrac.getBigDecimal("PAYTHISAMOUNT")
                .add(newd.getBigDecimal("uirac")));
    newd.setBigDecimal("provisp", ABSrac.getBigDecimal("VAT")
                .add(newd.getBigDecimal("provisp")));//cijena bez poreza
  } else {
    newd.insertRow(false);
    _rbr=0;
    raVariant.copyTo(ABSrac, newd, "cpar,opis,datdok,ddosp,uirac,provisp");
    b = Integer.valueOf(newd.getString("OPIS").trim().substring(1));
    newd.setString("CSKL","01");
    newd.setString("VRDOK","RAC");
    newd.setString("PNBZ2",newd.getString("OPIS"));
    newd.setString("GOD",Util.getUtil().getYear(newd.getTimestamp("datdok")));
    newd.setInt("BRDOK",b);
   }
   newd.post();

//racuni - stavke - za svako zaglavlje
//_ts = System.currentTimeMillis();
    ABSstav = orac.getData("invoice_line.sql",new Double(ABSrac.getDouble("HID")).intValue()+"");
//System.out.println("Query executed in "+(System.currentTimeMillis()-_ts));
    for (ABSstav.first(); ABSstav.inBounds(); ABSstav.next()) {
      newstd.insertRow(false);
      _rbr++;
//System.out.println("RN: "+b+" rbr = "+_rbr);
      raVariant.copyTo(ABSstav, newstd, "veza,rbsrn,cart1,cradnal,kol,vc");
//System.out.println("   main data copied ...");
      newstd.setString("CSKL",newd.getString("CSKL"));
      newstd.setString("VRDOK",newd.getString("VRDOK"));
      newstd.setString("GOD",newd.getString("GOD"));
      newstd.setInt("BRDOK",b);
      newstd.setShort("RBR",(short)_rbr);
//System.out.println("   keys set ...");
	if (!lookupData.getlookupData().raLocate(arts,"CART1",newstd.getString("CART1"))) {
	  lookupData.getlookupData().raLocate(arts,"CART","1");
      }
      newstd.setInt("CART",arts.getInt("CART"));
      newstd.setString("BC",arts.getString("BC"));
      newstd.setString("NAZART",arts.getString("NAZART"));
      newstd.setString("JM",arts.getString("JM"));
//System.out.println("   artikl data copied ...");
	newstd.setBigDecimal("FC",newstd.getBigDecimal("VC"));
      newstd.setBigDecimal("IPRODBP",
        newstd.getBigDecimal("KOL").multiply(newstd.getBigDecimal("VC")));
      newstd.setBigDecimal("IPRODSP",
        newstd.getBigDecimal("IPRODBP").multiply(pdvkoef));
	newstd.setBigDecimal("POR1",
        newstd.getBigDecimal("IPRODSP").add(newstd.getBigDecimal("IPRODBP").negate()));
//System.out.println("   values calculated ...");
      newstd.post();
    }
}
//seq racuni
seqMap.put(seqopis, new Integer(b));

//Otpremnice - prodaja
		now = new java.sql.Timestamp(System.currentTimeMillis());
		seqopis2 = "1000"+"OTP"+Util.getUtil().getYear(now);
		intseq = Valid.getValid().findSeqInteger(seqopis, false, false).intValue();
		otp_num = "6"+Valid.getValid().maskZeroInteger(new Integer(intseq-1),9);

ABSotp = orac.getData("DeliveryNotePerCustomerPerTypeOfMerchandise.sql",otp_num);
//NEDOVRSENO!!!


//sejvanje
raLocalTransaction saver = new raLocalTransaction() {
  public boolean transaction() {
    for (Iterator iter = seqMap.keySet().iterator(); iter.hasNext();) {
      String sqop = (String) iter.next();
      Valid.getValid().setSeqFilter(sqop);
      dM.getDataModule().getSeq().setDouble("BROJ",((Integer)seqMap.get(sqop)).doubleValue());
      Valid.getValid().unlockCurrentSeq(false);
      raTransaction.saveChanges(dM.getDataModule().getSeq());
    }
    raTransaction.saveChanges(newp);
    raTransaction.saveChanges(newa);
    raTransaction.saveChanges(newd);
    raTransaction.saveChanges(newstd);
    return true;
  }
};

if (saver.execTransaction()) {
  javax.swing.JOptionPane.showMessageDialog(null, 
      "Preneseno je "
        +newp.getRowCount()+" novih partnera, "
        +newa.getRowCount()+" novih artikala, "
        +ABSrac.getRowCount()+" novih ra�una, "
		+ABSotp.getRowCount()+" novih otpremnica, "
  );
} else {
  javax.swing.JOptionPane.showMessageDialog(null, 
      "Prijenos dokumenata nije uspio!! Probajte ponovo!",
      "!!!ERROR!!!",JOptionPane.ERROR_MESSAGE);
}

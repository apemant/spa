<#>
hr.restart.util.mail.DataMailer d = new hr.restart.util.mail.DataMailer();
d.addQuery("doki", "SELECT * FROM doki where status not in ('P') and datdok>'31.12.2006'");
d.addQuery("stdoki", "SELECT * FROM stdoki where exists (select * from doki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok and doki.status <>'P' and datdok>'31.12.2006')");
d.addQuery("doku", "SELECT * FROM doku where status not in ('P') and datdok>'31.12.2006'");
d.addQuery("stdoku", "SELECT * FROM stdoku where exists (select * from doku WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok and doku.status <>'P' and datdok>'31.12.2006')");
d.addQuery("meskla", "SELECT * FROM meskla where status not in ('P') and datdok>'31.12.2006'");
d.addQuery("stmeskla", "SELECT * FROM stmeskla where exists (select * from meskla WHERE meskla.csklul = stmeskla.csklul AND meskla.cskliz = stmeskla.cskliz AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok and meskla.status <>'P' and datdok>'31.12.2006')");
d.addQuery("rate", "SELECT * FROM rate where exists (select * from doki WHERE doki.vrdok='GOT' and doki.cskl = rate.cskl AND doki.vrdok = rate.vrdok AND doki.god = rate.god AND doki.brdok = rate.brdok and doki.status <>'P' and datdok>'31.12.2006')");
String vrati="";
if (d.sendMailUI("Podaci <umetni naziv prodavaonice> ...")) {
vrati="Slanje uspješno";
Valid.getValid().runSQL("update doki set status='P' where status not in ('P')");
Valid.getValid().runSQL("update doku set status='P' where status not in ('P')");
Valid.getValid().runSQL("update meskla set status='P' where status not in ('P')");
}
else {
vrati="Slanje neuspještno, pošaljite ponovo kroz: Alati-MailBox";
}
vrati

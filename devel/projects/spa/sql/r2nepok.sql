<#>
<#groups>CKNJIGE+NAZKNJIGE+getKnjigeU,CPAR+NAZPAR+getPartneriDob
<#datdok>Do datuma;T;;;;;9
<#title>Pregled nepokrivenih R2 raèuna  do $datdok
<#visible>6,7,8,9,10,11
sk = Skstavke.getDataModule().getTempSet("knjig vrdok brojizv brojkonta cknjige cpar brojdok extbrdok datdok datumknj cgkstavke ip","datpri>='9000-01-01' and vrdok in ('URN','OKD') and datdok<='"+datdok+"'");
sk.open();
sk.getColumn("EXTBRDOK").setWidth(5);
sk.getColumn("EXTBRDOK").setCaption("UFA");
sk.getColumn("DATUMKNJ").setCaption("Primitak");
sk.getColumn("IP").setCaption("Iznos");
sk.getColumn("BROJDOK").setWidth(15);
sk.getColumn("CGKSTAVKE").setWidth(25);
sk.getColumn("CGKSTAVKE").setCaption("Nalog za knjiženje");
sk

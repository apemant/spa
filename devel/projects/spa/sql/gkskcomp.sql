<#>
gk = Util.getNewQueryDataSet("SELECT *  from Gkstavke WHERE brojkonta='1400' and knjig='3' and EXTRACT(YEAR FROM datumknj)=2006");

sk = Util.getNewQueryDataSet("SELECT uistavke.* FROM skstavke,uistavke WHERE skstavke.knjig=uistavke.knjig AND skstavke.cpar=uistavke.cpar AND skstavke.vrdok=uistavke.vrdok AND skstavke.brojdok=uistavke.brojdok AND skstavke.cknjige=uistavke.cknjige AND skstavke.knjig='3' AND skstavke.vrdok IN ('URN', 'OKD') AND (DATPRI BETWEEN '2006-01-01 00:00:00' AND '2006-12-31 23:59:59') AND (cnacpl is null or cnacpl != 'PK') and uistavke.ckolone=13");

Aus.compareValues(gk, sk, "ID");

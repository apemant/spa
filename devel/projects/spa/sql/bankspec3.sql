<#>
<#groups>CISPLMJ+NAZIV+getIsplMJ
<#sums>NARUKE
<#title>STROJOGRAD  SPECIFIKACIJA ZA BANKU 
<#visible>1,2,3,4,5
SELECT radnicipl.cisplmj,radnici.prezime, radnici.ime, radnicipl.BROJTEK, radnicipl.jmbg, kumulrad.naruke from radnici, radnicipl, kumulrad
WHERE radnici.cradnik = radnicipl.cradnik
AND radnici.cradnik = kumulrad.cradnik
AND radnicipl.cradnik = kumulrad.cradnik
and radnicipl.cisplmj=3

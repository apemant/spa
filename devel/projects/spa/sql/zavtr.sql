<#>
<#groups>
<#01cskl>Skladište;S;getSklad;CSKL,NAZSKL;0,1
<#sums>
<#orders>
<#title>Pregled ukupnih izdvajanja po vrstama zavisnih troškova
<#visible>$default
select max(czt) as czt,max(ztr.nzt) as nzt, sum(izt) as izt FROM vtztr,ztr  
WHERE vtztr.czt = ztr.czt and cskl=$01cskl group by czt


<#>
<#groups>CSKL+NAZSKL+getSklad
<#sums>
<#orders>CART1
<#cskl>Skladište;S;getSklad;CSKL,NAZSKL;0,1,2,3
<#visible>1,2,3,4
<#title>Kolièinsko stanje s prodajnom cijenom
SELECT stanje.cskl,artikli.cart1,artikli.nazart ,stanje.kol,stanje.vc from artikli, stanje WHERE artikli.cart = stanje.cart and stanje.cskl = $cskl and stanje.kol > 0


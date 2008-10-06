<#>
<#groups>CSKL+NAZSKL+getSklad,opis
<#zopis>Opis grupe;S;;;
<#sums>kol
<#orders>CART1
<#zart>Artikl/grupa;S;;;
<#cskl>Skladište;S;getSklad;CSKL,NAZSKL;0,1,2,3
<#visible>1,2,3,4
<#title>Kolièinsko stanje s prodajnom cijenom
SELECT stanje.cskl,artikli.cart1,artikli.nazart ,stanje.kol,stanje.vc,$zopis as Opis from artikli, stanje WHERE artikli.cart = stanje.cart and stanje.cskl = $cskl and artikli.cart1 like $zart||'%' and kol > 0 and $zopis=$zopis


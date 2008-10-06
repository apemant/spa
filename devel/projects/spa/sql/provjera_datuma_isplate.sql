<#>
<#drbr>RBR;I;;;;;5
<#aknjig>Knjigovodstvo;S;getOrgstruktura;CORG, NAZIV;0,1;;12
<#bgodobr>Godina;I;;;;;5
<#cmj>Mjesec;I;;;;;5
select corg, datumispl from kumulorgarh WHERE knjig='$aknjig' and godobr=$bgodobr and mjobr=$cmj and rbrobr=$drbr

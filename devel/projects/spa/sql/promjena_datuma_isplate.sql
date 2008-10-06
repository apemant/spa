<#>
<#drbr>RBR;I;;;;;5
<#aknjig>Knjigovodstvo;S;getOrgstruktura;CORG, NAZIV;0,1;;12
<#zdatispl>Novi datum isplate;F;;;;;0
<#bgodobr>Godina;I;;;;;5
<#cmj>Mjesec;I;;;;;5
update kumulorgarh set DATUMISPL=$zdatispl WHERE knjig='$aknjig' and godobr=$bgodobr and mjobr=$cmj and rbrobr=$drbr

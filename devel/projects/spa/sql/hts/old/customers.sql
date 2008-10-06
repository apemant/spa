select c.customernumber, 
a.street, 
a.city ,
c.vatnumber, 
c.name, 
c.phoneno, 
c.faxno, 
c.email,
'KRIVI:'||c.customernumber as zr
from customer c, address a   
where c.invoiceaddress_id=a.address_id;
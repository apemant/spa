SELEct cast ((nvl((select x.invoicenumber
              from invoiceheader x
             where x.invoiceheader_id = ih.parentinvoiceheader_id),
            ih.invoicenumber)) as char(20)) as invoiceinumberB,
            il.code as cart,
cast (tog.code as char(10)) as tog_code from invoiceline il, invoiceheader ih, turnovergroup tog 
where ih.INVOICEHEADER_ID = il.INVOICEHEADER_ID 
and il.turnovergroup_id=tog.turnovergroup_id 
and il.TURNOVERGROUP_ID is not null
AND (nvl((select x.invoicenumber
              from invoiceheader x
             where x.invoiceheader_id = ih.parentinvoiceheader_id),
            ih.invoicenumber)) > 8000000000
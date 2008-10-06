select (nvl((select x.invoicenumber
              from invoiceheader x
             where x.invoiceheader_id = ih.parentinvoiceheader_id),
            ih.invoicenumber)) as invoiceinumberB,
       il.linenumber,
       il.code as productcode,
       case
         when il.invoicelinetype_id = 5 then
          'SALES'
         else
          'SERVICE'
       end as linetype,
       il.billedqty,
       il.price as price,
       turnovergroup_id as tog_id
  from invoiceheader ih, invoiceline il
 where il.invoiceheader_id = ih.invoiceheader_id
 and il.invoiceheader_id = &1
-- and il.invoicelinetype_id <> 18 
-- order by 1, 2;
select ih.billtocustomernumber,
       (nvl((select x.invoicenumber
              from invoiceheader x
             where x.invoiceheader_id = ih.parentinvoiceheader_id),
            ih.invoicenumber)) as invoiceinumberB,
       ih.invoicedate,
       ih.netpaymentdays,
       ih.paythisamount,
       ih.totalamountexclvat as vat,
       ih.invoiceheader_id as hid
  from invoiceheader ih
 where ih.invoicenumber > 0
   and ((ih.invoicenumber > &1 and ih.parentinvoiceheader_id is null) or
       ih.parentinvoiceheader_id in
       (select x.invoiceheader_id
           from invoiceheader x
          where x.invoicenumber > &1
            and x.invoicenumber > 0))
 order by 2;

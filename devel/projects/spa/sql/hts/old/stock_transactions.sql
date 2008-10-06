select t.factorydescription,
       st.transactiondate,
       st.quantity,
       r.code as reason_code,
       (select rd.description
          from reasoncode_desc rd
         where rd.reasoncode_id = r.reasoncode_id
           and rd.language_id =
               (select l.language_id from languages l where l.code = 'DE_DE')) as reason_description,
       sto.code as stockroom_code,
       p.code product_code,
       sd.code size_code,
       (select c.customernumber
          from workorderstocktransaction wst,
               workorderline             wl,
               workorder                 wo,
               customer                  c
         where wst.stocktransaction_id = st.stocktransaction_id
           and wst.workorderline_id = wl.workorderline_id
           and wl.workorder_id = wo.workorder_id
           and wo.customer_link_id = c.customer_id) as customernumber,
       (select sup.suppliernumber
          from purchaseorderstocktransaction pos,
               purchaseorderline             pol,
               purchaseorderheader           ph,
               supplier                      sup
         where pos.stocktransaction_id = st.stocktransaction_id
           and pos.purchaseorderline_id = pol.purchaseorderline_id
           and pol.purchaseorderheader_id = ph.purchaseorderheader_id
           and ph.supplier_id = sup.supplier_id) as supplierrnumber
  from stocktransaction         st,
       transactiontype          t,
       stockreservationpergrade srpg,
       stockdefinitionperitem   sdpi,
       stockroom                sto,
       product                  p,
       sizedefinition           sd,
       reasoncode               r
 where st.transactiontype_id = t.transactiontype_id
   and st.stockreservationpergrade_id = srpg.stockreservationpergrade_id
   and srpg.stockdefinitionperitem_id = sdpi.stockdefinitionperitem_id
   and sdpi.stockroom_id = sto.stockroom_id
   and sdpi.product_id = p.product_id
   and sdpi.sizedefinition_id = sd.sizedefinition_id
   and st.reasoncode_id = r.reasoncode_id
   and st.transactiondate > sysdate - 30

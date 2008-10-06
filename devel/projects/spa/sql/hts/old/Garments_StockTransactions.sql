select c.customernumber,
       trunc(s.scantimestamp) as transactiondate,
       p.code as prod_code,
       sd.code as size_code,
       s.transactiontype_id,
       qu.code as neu_gebraucht, - - new or used
       1 as quantity,
       tt.factorydescription,
       case
         when s.transactiontype_id >= 10 and s.transactiontype_id < 20 then
          'TO STOCK'
         when s.transactiontype_id >= 20 and s.transactiontype_id < 30 then
          'TO CUSTOMER' --can be from stock or direct purchase 
         when s.transactiontype_id >= 40 and s.transactiontype_id <= 45 then
          'RAGGING' -- to trash
         else
          'OTHER'
       end direction
  from scan                s,
       customer_link       c,
       product_link        p,
       sizedefinition_link sd,
       qualitygrade        qu,
       transactiontype     tt
 where (s.transactiontype_id between 10 and 29 or
       s.transactiontype_id between 40 and 45)
   and s.customer_link_id = c.customer_link_id
   and s.product_link_id = p.product_link_id
   and s.qualitygrade_id = qu.qualitygrade_id
   and s.transactiontype_id = tt.transactiontype_id
   and s.sizedefinition_link_id = sd.sizedefinition_link_id
  group by c.customernumber,trunc(s.scantimestamp),p.code,sd.code,s.transactiontype_id,qu.code,tt.factorydescription
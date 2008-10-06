select case
         when sd.sizedefinition_id in (1,2) then
           p.code
         else
           p.code||'#'||sd.code 
       end
               as productcode,
       (select pd.description
          from product_desc pd
         where pd.product_id = p.product_id
           and pd.language_id =
               (select l.language_id from languages l where l.code = 'HR_HR')) as name,
       pg.code as groupcode
       
  from product p, productgroup pg, sizespersizetype spt, sizedefinition sd
 where p.productgroup_id = pg.productgroup_id
 and p.sizetype_id = spt.sizetype_id
 and spt.sizedefinition_id = sd.sizedefinition_id 
order by p.code, sd.code
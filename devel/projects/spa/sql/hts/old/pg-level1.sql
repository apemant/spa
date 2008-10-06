select pg.code as groupcode, 
       (select pgd.description 
          from productgroup_desc pgd 
         where pgd.productgroup_id = p.productgroup_id 
           and pgd.language_id = 
               (select l.language_id from languages l where l.code = 'HR_HR')) as groupname, 
       act.code as activitycode, 
       (select acd.description 
          from activity_desc acd 
         where acd.activity_id = pg.activity_id 
           and acd.language_id = 
               (select l.language_id from languages l where l.code = 'HR_HR')) as activityname 
  from product p, productgroup pg, activity act 
 where p.productgroup_id = pg.productgroup_id 
   and pg.activity_id = act.activity_id 
 order by act.code,pg.code;

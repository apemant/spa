select tg.CODE, td.DESCRIPTION from turnovergroup tg, turnovergroup_desc td 
where tg.TURNOVERGROUP_ID = td.TURNOVERGROUP_ID and td.LANGUAGE_ID=1 order by tg.code
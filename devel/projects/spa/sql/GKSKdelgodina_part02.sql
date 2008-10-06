delete FROM Pokriveni WHERE not exists (select * from skstavke where pokriveni.cracuna=skstavke.cskstavke);
delete FROM PokriveniRadni;

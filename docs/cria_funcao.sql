CREATE FUNCTION ativa_orcamento(id_orcamento integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare flg_ativo boolean; flg_inativo boolean; res record; 
begin
	-- raise notice 'Recebendo %', id_orcamento;
	for res in select ativo from financ.orcamento where id = id_orcamento loop
		flg_ativo := res.ativo;
	--	raise notice 'flag ativo %', flg_ativo;
	end loop;	
	
	-- atualizando orcamento recebido
	flg_inativo := not flg_ativo;
	update financ.orcamento set ativo = flg_inativo where id = id_orcamento;
	
	if flg_inativo= true then
		update financ.orcamento set ativo = flg_ativo where id <> id_orcamento;
	end if;

	
	--update financ.orcamento set ativo = flg_ativo where id = id_orcamento;
end;
$$;
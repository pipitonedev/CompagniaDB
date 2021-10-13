package it.prova.dao.impiegato;

import java.util.Date;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato> {
	
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput)throws Exception;
	public int countByDataFondazioneCompagniaMaggioreDi(Date dataInput) throws Exception;
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoS) throws Exception;
	public List<Impiegato> findAllErroriAssunzione() throws Exception;

}

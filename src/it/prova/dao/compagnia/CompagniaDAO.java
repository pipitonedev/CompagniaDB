package it.prova.dao.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {

	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date dataInput) throws Exception;

	public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSocialeInput) throws Exception;

}

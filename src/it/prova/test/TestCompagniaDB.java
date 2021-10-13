package it.prova.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.dao.Constants;
import it.prova.dao.compagnia.CompagniaDAO;
import it.prova.dao.compagnia.CompagniaDAOImpl;
import it.prova.dao.impiegato.ImpiegatoDAO;
import it.prova.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public class TestCompagniaDB {

	public static void main(String[] args) {

		Compagnia nuovaCompagniaInstance = new Compagnia("Microsoft", 800000000);
		nuovaCompagniaInstance.setDataFondazione(new Date(1975 - 10 - 11));

		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			System.out.println("Nella tabella sono presenti:  " + compagniaDAOInstance.list().size() + " elementi.");
			compagniaDAOInstance.insert(nuovaCompagniaInstance);

			Long idDaUsarePerRicerca = 3L;
			Compagnia cercami = compagniaDAOInstance.get(idDaUsarePerRicerca);
			if (cercami != null)
				System.out.println("\nCompagnia con id:  " + idDaUsarePerRicerca + ": " + cercami);
			else
				System.out.println("\nCompagnia con id: " + idDaUsarePerRicerca + " non trovato");

			System.out.println("\n-----TEST COMPAGNIA CON RAGIONE SOCIALE----\n");
			List<Compagnia> compagnie = new ArrayList<>();
			compagnie = compagniaDAOInstance.findAllByRagioneSocialeContiene("eur");
			System.out.println(compagnie);

			System.out.println("\n-----TEST COMPAGNIA CON FATTURATO MAGGIORE DI-------");
			List<Impiegato> listaDiImpiegati = impiegatoDAOInstance.findAllByCompagniaConFatturatoMaggioreDi(9000000);
			for (Impiegato impiegato : listaDiImpiegati) {
				System.out.println("\n" + impiegato);
			}

			System.out.println("\n-----TEST CONTA----------");
			System.out.println(impiegatoDAOInstance.countByDataFondazioneCompagniaMaggioreDi(new Date(834976000)));
			
			System.out.println("\n--------ERRORI D'ASSUNZIONE--------");
			List<Impiegato> lista = impiegatoDAOInstance.findAllErroriAssunzione();
				for (Impiegato impiegato : lista) {
					System.out.println(impiegato);
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

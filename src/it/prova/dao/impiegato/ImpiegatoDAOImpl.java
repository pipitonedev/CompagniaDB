package it.prova.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;
		try (Statement ps = connection.createStatement();
				ResultSet rs = ps
						.executeQuery("select * from impiegato i inner join compagnia c on c.id=i.id_compagnia")) {

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
				impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
				impiegatoTemp.setId(rs.getLong("i.ID"));

				Compagnia compagniaTemp = new Compagnia();
				compagniaTemp.setId(rs.getLong("c.id"));
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));

				impiegatoTemp.setCompagnia(compagniaTemp);
				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, idInput);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("NOME"));
					result.setCognome(rs.getString("COGNOME"));
					result.setCodiceFiscale(rs.getString("codfiscale"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codfiscale=?, datanascita=?, dataassunzione=?, id_compagnia=? where id=?;")) {

			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setDate(3, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(3, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(4, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codfiscale, datanascita, dataassunzione, id_compagnia) VALUES (?, ?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getCompagnia().getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		String query = "select * from compagnia where 1=1 ";
		if (input.getNome() != null && !input.getNome().isEmpty()) {
			query += " and nome like '" + input.getNome() + "%' ";
		}

		if (input.getCognome() != null && !input.getCognome().isEmpty()) {
			query += " and cognome like '" + input.getCognome() + "%' ";
		}

		if (input.getCodiceFiscale() != null && !input.getCodiceFiscale().isEmpty()) {
			query += " and codfiscale like '" + input.getCodiceFiscale() + "%' ";
		}

		if (input.getDataNascita() != null) {
			query += " and datanascita ='" + new java.sql.Date(input.getDataNascita().getTime()) + "' ";
		}

		if (input.getDataAssunzione() != null) {
			query += " and datanaAssunzione ='" + new java.sql.Date(input.getDataAssunzione().getTime()) + "' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("nome"));
				impiegatoTemp.setCognome(rs.getString("cognome"));
				impiegatoTemp.setCodiceFiscale(rs.getString("codfiscale"));
				impiegatoTemp.setDataNascita(rs.getDate("datanascita"));
				impiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				impiegatoTemp.setId(rs.getLong("ID"));
				result.add(impiegatoTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on c.id=i.id_compagnia where c.id= ?")) {

			ps.setLong(1, compagniaInput.getId());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODFISCALE"));
					impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
					impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					impiegatoTemp.setId(rs.getLong("i.ID"));
					result.add(impiegatoTemp);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return result;
		}
	}

	@Override
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturato) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT i.* FROM compagnia c inner join impiegato i on c.id=i.id_compagnia where c.fatturatoannuo > ?")) {

			ps.setLong(1, fatturato);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODFISCALE"));
					impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
					impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					impiegatoTemp.setId(rs.getLong("i.ID"));
					result.add(impiegatoTemp);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return result;
		}
	}

	@Override
	public int countByDataFondazioneCompagniaMaggioreDi(Date dataInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		int conta = 0;

		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT count(*) as totale  FROM compagnia c inner join impiegato i on c.id=i.id_compagnia where c.datafondazione >= ?");) {
			ps.setDate(1, new java.sql.Date(dataInput.getTime()));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					conta = rs.getInt("totale");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return conta;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT * FROM compagnia c inner join impiegato i on c.id=i.id_compagnia where c.datafondazione > i.dataassunzione");) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
					impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					impiegatoTemp.setId(rs.getLong("ID"));
					result.add(impiegatoTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}

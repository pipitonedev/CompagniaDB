package it.prova.dao.compagnia;

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

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Compagnia> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;
		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
				compagniaTemp.setId(rs.getLong("ID"));
				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Compagnia get(Long idInput) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id=?")) {

			ps.setLong(1, idInput);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					result.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					result.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
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
	public int update(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			ps.setLong(4, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia (ragionesociale, fatturatoannuo, datafondazione) VALUES (?, ?, ?);")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM compagnia WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;

		String query = "select * from compagnia where 1=1 ";
		if (input.getRagioneSociale() != null && !input.getRagioneSociale().isEmpty()) {
			query += " and ragionesociale like '" + input.getRagioneSociale() + "%' ";
		}

		if (input.getDataFondazione() != null) {
			query += " and datafondazione ='" + new java.sql.Date(input.getDataFondazione().getTime()) + "' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
				compagniaTemp.setId(rs.getLong("ID"));
				result.add(compagniaTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date dataInput) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection
				.prepareStatement("SELECT * FROM impiegato i where i.dataassunzione > ?");) {

			ps.setDate(1, new java.sql.Date(dataInput.getTime()));
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

	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSocialeInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<>();

		try (PreparedStatement ps = connection
				.prepareStatement("SELECT * FROM compagnia c where c.ragionesociale like ?");) {
			ps.setString(1, "%" + ragioneSocialeInput + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					compagniaTemp.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
					compagniaTemp.setId(rs.getLong("ID"));
					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}

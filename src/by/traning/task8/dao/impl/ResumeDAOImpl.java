package by.traning.task8.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import by.traning.task8.dao.ResumeDAO;
import by.traning.task8.dao.pull.ConnectionPool;
import by.traning.task8.dao.pull.exception.ConnectionPoolException;
import by.traning.task8.domains.Resume;
import by.traning.task8.exception.DAOException;
import by.traning.task8.exception.DataDoesNotExistException;

public class ResumeDAOImpl implements ResumeDAO {
	private static final Logger LOG = Logger.getLogger(ApplicantDAOImpl.class);
	private static final String SQL_CREATE_RESUME = "INSERT INTO resume (name, email) VALUES (?, ?);";
	private static final String SQL_UPDATE_RESUME = "UPDATE resume SET name=?, email=? WHERE id_resume=?";
	private static final String SQL_DELETE_RESUME = "DELETE resume WHERE id_resume=?";
	private static final String SQL_FIND_RESUME_BY_APPLICANT = "SELECT r.name "
			+ "FROM resume as r JOIN applicant as a ON a.email = r.email " + "WHERE a.email=?";
	private static final String SQL_APPLY_FOR_JOB = "INSERT INTO verify (id_vacancy, id_resume) VALUES (?, ?)";

	@Override
	public boolean create(Resume entity) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_CREATE_RESUME);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getEmail());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			LOG.error("Faild create Resume: ", e);
			throw new DAOException("Faild create Resume: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}
	}

	@Override
	public void update(Resume entity) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_UPDATE_RESUME);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getEmail());
			ps.setInt(3, entity.getIdResume());
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Faild update Resume: ", e);
			throw new DAOException("Faild update Resume: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}
	}

	@Override
	public void delete(int idResume) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_DELETE_RESUME);
			ps.setInt(1, idResume);
			ps.executeQuery();
		} catch (SQLException e) {
			LOG.error("Faild delete Resume: ", e);
			throw new DAOException("Faild delete Resume: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}

	}

	@Override
	public Resume findResumeByApplicant(String email) throws DAOException, DataDoesNotExistException {
		Resume resume = new Resume();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_FIND_RESUME_BY_APPLICANT);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs.next()) {
				resume = CreateEntity(rs);
			} else {
				throw new DataDoesNotExistException("Resume not found!");
			}
		} catch (SQLException e) {
			LOG.error("Faild to find Applicant: ", e);
			throw new DAOException("Faild ifind Applicant: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps, rs);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}

		return resume;
	}

	@Override
	public List<Resume> findListAplicantResume(String email) throws DAOException, DataDoesNotExistException {
		List<Resume> resume = new ArrayList<Resume>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_FIND_RESUME_BY_APPLICANT);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs.next()) {
				resume.add(CreateEntity(rs));
			} else {
				throw new DataDoesNotExistException("Company not found!");
			}
		} catch (SQLException e) {
			LOG.error("Faild to find Languages: ", e);
			throw new DAOException("Faild to find Languages: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps, rs);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}
		return resume;
	}

	@Override
	public void ApplayforJob(int idResume, int idVacancy) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ConnectionPool pool = null;
		try {
			pool = ConnectionPool.getInstance();
			conn = pool.takeConnection();
			ps = conn.prepareStatement(SQL_APPLY_FOR_JOB);
			ps.setInt(1, idVacancy);
			ps.setInt(2, idResume);
			ps.executeUpdate();

		} catch (SQLException e) {
			LOG.error("Faild Aplay for a job: ", e);
			throw new DAOException("Faild aplay for a job: ", e);
		} catch (ConnectionPoolException e) {
			LOG.error("Connection pool problems!", e);
			throw new DAOException("Connection pool problems!", e);
		} finally {
			try {
				ConnectionPool.getInstance().closeConnection(conn, ps);
			} catch (ConnectionPoolException e) {
				LOG.error("Faild to close connection", e);
				throw new DAOException("Faild to close connection", e);
			}
		}
	}

	private Resume CreateEntity(ResultSet set) throws SQLException {
		Resume resume = new Resume();
		resume.setIdResume(set.getInt(1));
		resume.setName(set.getString(2));
		resume.setEmail(set.getString(3));
		return resume;

	}

}

package com.yash.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.yash.entities.Jobs;
import com.yash.integrate.ConnectionManager;
public class JDBCJobsDAOImpl implements JobsDAO {
	@Override
	public List<Jobs> getAllJobs() throws ClassNotFoundException, SQLException {
		Connection connection=ConnectionManager.openConnection();
		Statement statement=connection.createStatement();
		ResultSet resultSet=
				statement.executeQuery("select * from jobs");
		List<Jobs> jobsList=new ArrayList<>();
		while(resultSet.next()){
			Jobs jobs=new Jobs();
			jobs.setJobId(resultSet.getString("job_id"));
			jobs.setJobTitle(resultSet.getString("job_title"));
			jobsList.add(jobs);
		}
		ConnectionManager.closeConnection();
		return jobsList;
	  }
	}
package com.yash.dao;
import java.sql.SQLException;
import java.util.List;
import com.yash.entities.Jobs;
public interface JobsDAO {
	public List<Jobs> getAllJobs() throws ClassNotFoundException,SQLException;
}

package com.yash.dao;
import java.sql.SQLException;
import java.util.List;
import com.yash.entities.Departments;
public interface DepartmentsDAO {
	public List<Departments> getAllDepartments() throws ClassNotFoundException, SQLException;
}

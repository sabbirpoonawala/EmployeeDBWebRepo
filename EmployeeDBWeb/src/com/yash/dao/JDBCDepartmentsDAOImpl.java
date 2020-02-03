package com.yash.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.yash.entities.Departments;
import com.yash.integrate.ConnectionManager;
import com.yash.service.DepartmentsServiceImpl;

public class JDBCDepartmentsDAOImpl implements DepartmentsDAO {

    static Logger log = Logger.getLogger(JDBCDepartmentsDAOImpl.class.getName());
    static {
        BasicConfigurator.configure();
    }

	@Override
	public List<Departments> getAllDepartments() throws ClassNotFoundException, SQLException {
		Connection connection=ConnectionManager.openConnection();
		Statement statement=connection.createStatement();
		ResultSet resultSet=
				statement.executeQuery("select * from departments");
		
		List<Departments> departmentList=new ArrayList<>();
		while(resultSet.next()) {
			Departments departments=new Departments();
			departments.setDepartmentId(resultSet.getInt("department_id"));
			departments.setDepartmentName(resultSet.getString("department_name"));
			departmentList.add(departments);
		}
		ConnectionManager.closeConnection();
		log.info("Department List returned:"+departmentList);
		return departmentList;
	}
}

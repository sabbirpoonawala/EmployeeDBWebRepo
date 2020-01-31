package com.yash.helper;
import com.yash.dao.DepartmentsDAO;
import com.yash.dao.EmployeesDAO;
import com.yash.dao.JDBCDepartmentsDAOImpl;
import com.yash.dao.JDBCEmployeesDAOImpl;
import com.yash.dao.JDBCJobsDAOImpl;
import com.yash.dao.JobsDAO;
import com.yash.service.DepartmentsService;
import com.yash.service.DepartmentsServiceImpl;
import com.yash.service.EmployeesService;
import com.yash.service.EmployeesServiceImpl;
import com.yash.service.JobsService;
import com.yash.service.JobsServiceImpl;
public class FactoryEmployeeDB{
	private FactoryEmployeeDB() {}
	public static EmployeesDAO createEmployeesDAO(){
		return new JDBCEmployeesDAOImpl();		
	}
	public static EmployeesService createEmployeesService(){
		return new EmployeesServiceImpl();
	}
	public static JobsDAO createJobsDAO(){
		return new JDBCJobsDAOImpl();		
	}
	public static JobsService createJobsService(){
		return new JobsServiceImpl();
	}
	public static DepartmentsDAO createDepartmentsDAO(){
		return new JDBCDepartmentsDAOImpl();		
	}
	public static DepartmentsService createDepartmentsService(){
		return new DepartmentsServiceImpl();
	}
}

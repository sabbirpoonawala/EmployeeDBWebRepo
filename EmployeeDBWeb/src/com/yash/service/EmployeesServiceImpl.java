package com.yash.service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.yash.controller.EmployeeController;
import com.yash.dao.EmployeesDAO;
import com.yash.entities.Departments;
import com.yash.entities.Employees;
import com.yash.exception.NoEmployeeFoundException;
import com.yash.exception.SalaryNotValidException;
import com.yash.helper.FactoryEmployeeDB;
import com.yash.model.AllEmployeesModel;
import com.yash.model.DepartmentsModel;
import com.yash.model.EmployeesModel;
import com.yash.model.JobsModel;
import com.yash.model.ManagersModel;
public class EmployeesServiceImpl implements EmployeesService {
	private final String RESULTFORSUCCESS="success";
	private final String RESULTFORFAILURE="fail";
	
    static Logger log = Logger.getLogger(EmployeesServiceImpl.class.getName());
	private EmployeesDAO employeesDAO;
	public EmployeesServiceImpl() {
		this.employeesDAO=FactoryEmployeeDB.createEmployeesDAO();
        BasicConfigurator.configure();
	}
	public List<ManagersModel> getManagers(){
		List<ManagersModel> managerModelList=new ArrayList<>();
		try {
			List<Employees> employees=employeesDAO.getManagers();
			for(Employees employee:employees) {
				ManagersModel managerModel=new ManagersModel();
				managerModel.setFirstName(employee.getFirstName());
				managerModel.setLastName(employee.getLastName());
				managerModel.setEmployeeId(employee.getEmployeeId());
				managerModelList.add(managerModel);
			}
		} catch (ClassNotFoundException | SQLException e) {
          log.error(e);		
      }
        return managerModelList;
	}
	
	@Override
	public List<AllEmployeesModel> retrieveAllEmployees() {
		List<AllEmployeesModel> allemployeesModelList=new ArrayList<>();
		try {
			List<Employees> employeesList=employeesDAO.getAllEmployees();
			for(Employees employees:employeesList) {			
				AllEmployeesModel employeesAllModel=new AllEmployeesModel();
				employeesAllModel.setEmployeeId(employees.getEmployeeId());
				employeesAllModel.setFirstName(employees.getFirstName());
				employeesAllModel.setLastName(employees.getLastName());
				employeesAllModel.setSalary(employees.getSalary());
				employeesAllModel.setCommissionPCT(employees.getCommissionPCT());				
				employeesAllModel.setPhoneNumber(employees.getPhoneNumber());
				employeesAllModel.setEmail(employees.getEmail());
				employeesAllModel.setHireDate(employees.getHireDate());
				employeesAllModel.setJobId(employees.getJobId());
				employeesAllModel.setManagerId(employees.getManagerId());
				employeesAllModel.setDepartmentId(employees.getDepartmentId());
				allemployeesModelList.add(employeesAllModel);
			}
		} catch (ClassNotFoundException | SQLException e) {
	          log.error(e);		
		}
		return allemployeesModelList;
	}
	
	@Override
	public List<EmployeesModel> retrieveEmployees() {
		List<EmployeesModel> employeesModelList=new ArrayList<>();
		try {
			List<Employees> employeesList=employeesDAO.getAllEmployees();
			for(Employees employees:employeesList) {		
				EmployeesModel employeesModel=new EmployeesModel();
				employeesModel.setEmployeeId(employees.getEmployeeId());
				employeesModel.setFullName(employees.getFirstName()+" "+employees.getLastName());
				employeesModel.setTotalSalary(employees.getSalary()+employees.getSalary()*employees.getCommissionPCT());				
				employeesModel.setContactDetails("Ph No:"+employees.getPhoneNumber()+","+"Email:"+employees.getEmail());
				employeesModel.setEmail(employees.getEmail());
				employeesModelList.add(employeesModel);
			}	
		} catch (ClassNotFoundException | SQLException e) {
	          log.error(e);		
		}
		return employeesModelList;
	}
	@Override
	public EmployeesModel retrieveDepartmentName(int employeeId) {
		Employees employees=null;
		EmployeesModel employeesModel=new EmployeesModel();
		try {
			employees=employeesDAO.getDeparmentName(employeeId);
			employeesModel.setEmployeeId(employees.getEmployeeId());
			Departments departments=employees.getDepartments();
			DepartmentsModel departmentModel=new DepartmentsModel();
			departmentModel.setDepartmentName(departments.getDepartmentName());
			employeesModel.setDepartmentsModel(departmentModel);
		} catch (ClassNotFoundException | SQLException e) {
			log.error(e);
		}
		return employeesModel;
	}
	@Override
	public String registerEmployee(AllEmployeesModel model) {
		Employees employees=new Employees();
		employees.setEmployeeId(model.getEmployeeId());
		employees.setFirstName(model.getFirstName());
		employees.setLastName(model.getLastName());
		employees.setEmail(model.getEmail());
		employees.setPhoneNumber(model.getPhoneNumber());
		employees.setHireDate(model.getHireDate());
		employees.setJobId(model.getJobId());
		if(model.getSalary()>25000) {
			employees.setSalary(model.getSalary());
		}else {
			try {
			throw new SalaryNotValidException("Salary not valid");
			}catch(SalaryNotValidException e) {
				log.error("!ERROR[Salary must be greater than 25000]",e);
			}
		}
		employees.setCommissionPCT(model.getCommissionPCT());
		employees.setManagerId(model.getManagerId());
		employees.setDepartmentId(model.getDepartmentId());
		String result=RESULTFORFAILURE;
		try {
			boolean stored=employeesDAO.storeEmployeeDetails(employees);
			if(stored)
				result=RESULTFORSUCCESS;
			
		} catch (ClassNotFoundException | SQLException e) {
			log.error("!ERROR[Registration failed because of internal issues...]",e);
		}
		return result;
	}
	@Override
	public String updateEmployee(AllEmployeesModel model) {
		Employees employees=new Employees();
		employees.setEmployeeId(model.getEmployeeId());
		employees.setEmail(model.getEmail());
		employees.setPhoneNumber(model.getPhoneNumber());
		employees.setJobId(model.getJobId());
		employees.setDepartmentId(model.getDepartmentId());
		employees.setManagerId(model.getManagerId());
		employees.setCommissionPCT(model.getCommissionPCT());
		if(model.getSalary()>25000) {
			employees.setSalary(model.getSalary());
		}else {
			try {
			throw new SalaryNotValidException("Salary not valid");
			}catch(SalaryNotValidException e) {
		          log.error("!ERROR[Salary must be greater than 25000]",e);
			}
		}
		String result=RESULTFORFAILURE;
		try {
			boolean updated=employeesDAO.updateEmployee(employees);
			if(updated)
				result="success";
		} catch (ClassNotFoundException | SQLException e) {
			 log.error("!ERROR[Salary updation failed!!]",e);
		}
		return result;
	}
	@Override
	public String deleteEmployee(AllEmployeesModel model) {
		List<EmployeesModel> employeesList= retrieveEmployees();
		String result=RESULTFORFAILURE;
		boolean employeeFound=false;
		Employees employees=new Employees();
		for(EmployeesModel employeesModel: employeesList) {
			if(employeesModel.getEmployeeId()==model.getEmployeeId()) {
				employees.setEmployeeId(model.getEmployeeId());
				employeeFound=true;
				break;
			}
		}
		if(employeeFound) {
		try {
			boolean deleted=employeesDAO.deleteEmployeeDetails(employees);
			if(deleted)
				result=RESULTFORSUCCESS;
		} catch (ClassNotFoundException | SQLException e) {
			 log.error("!ERROR[Employee record deletion failed!!]",e);
		}	
   	}
		else {
			try {
				throw new NoEmployeeFoundException("Employee not found");
			} catch (NoEmployeeFoundException e) {
				log.error("!ERROR[Employee with specified id does not exist!!]",e);
			}
		}
		return result;
}
	@Override
	public EmployeesModel retrieveJobTitle(int employeeId) {
		EmployeesModel employeesModel=new EmployeesModel();
		try {
			String jobTitle=employeesDAO.getJobTitle(employeeId);
			JobsModel jobsModel=new JobsModel();
			jobsModel.setJobTitle(jobTitle);
			employeesModel.setEmployeeId(employeeId);
			employeesModel.setJobsModel(jobsModel);
		} catch (ClassNotFoundException | SQLException e) {
			log.error("![Error Job Title could not be retrieved!!]",e);
		}
		return employeesModel;
	}
	@Override
	public EmployeesModel retrieveEmployeeTax(int employeeId) {
		EmployeesModel employeesModel=new EmployeesModel();
		try {
			Employees employees=employeesDAO.getEmployeeTaxOnSalary(employeeId);
			employeesModel.setEmployeeId(employees.getEmployeeId());
			employeesModel.setTotalSalary(employees.getSalary()+(employees.getSalary()*employees.getCommissionPCT()));
			employeesModel.setTax(employees.getTax());
		} catch (ClassNotFoundException | SQLException e) {
			log.error("![Error Employee Tax details could not be retrieved!!]",e);
		}
		return employeesModel;
	}
}

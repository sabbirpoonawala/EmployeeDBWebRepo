package com.yash.service;
import java.util.List;
import com.yash.model.AllEmployeesModel;
import com.yash.model.EmployeesModel;
import com.yash.model.ManagersModel;
public interface EmployeesService {
	public List<EmployeesModel> retrieveEmployees();
	public List<AllEmployeesModel> retrieveAllEmployees();
	public EmployeesModel retrieveDepartmentName(int employeeId);
	public String registerEmployee(AllEmployeesModel model);
	public String deleteEmployee(AllEmployeesModel employees);
	public EmployeesModel retrieveJobTitle(int employeeId);
	public EmployeesModel retrieveEmployeeTax(int employeeId);
	public List<ManagersModel> getManagers();
	public String updateEmployee(AllEmployeesModel employeesModel);
}

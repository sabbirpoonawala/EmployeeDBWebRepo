package com.yash.controller;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.yash.converter.DateConverter;
import com.yash.helper.FactoryEmployeeDB;
import com.yash.model.AllEmployeesModel;
import com.yash.model.DepartmentsModel;
import com.yash.model.EmployeesModel;
import com.yash.model.JobsModel;
import com.yash.model.ManagersModel;
import com.yash.service.DepartmentsService;
import com.yash.service.EmployeesService;
import com.yash.service.JobsService;
import com.yash.validation.EmployeesModelValidator;

/**
 * Servlet implementation class EmployeeController
 */
@WebServlet("/employee")
public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static EmployeesService employeeService=null;
    private static DepartmentsService departmentService=null;
    private static JobsService jobsService=null;
    
    private static final String DEPARTMENT_LIST_NAME="departmentsList";
    private static final String JOB_LIST_NAME="jobsList";
    private static final String MANAGER_LIST_NAME="managersList";
    private static final String SUCCESS="success";
    private static final String EMPLOYEES_MODEL="employeesModel";
    private static final String OPERATION="operation";
    
    private static final String EMPLOYEE_SUCCESS_PAGE="employeesuccess.jsp";
    private static final String EMPLOYEE_FAIL_PAGE="employeefail.jsp";
    private static final String ERROR_PAGE="error.jsp";
    
    private static final String EMPLOYEE_ID_PARAM="employeeId";
    
    static Logger log = Logger.getLogger(EmployeeController.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeController() {
        super();
        BasicConfigurator.configure();
    }
  static{
	employeeService=FactoryEmployeeDB.createEmployeesService();
	departmentService=FactoryEmployeeDB.createDepartmentsService();
	jobsService=FactoryEmployeeDB.createJobsService();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	log.info("HTTP GET REQUEST");
    	String action=request.getParameter("action");
		if(action.contentEquals("view")) {
		List<EmployeesModel> employeesModelList=employeeService.retrieveEmployees();
		request.setAttribute("employeesModelList", employeesModelList);
		if(!employeesModelList.isEmpty()) {	
			RequestDispatcher dispatcher=
					request.getRequestDispatcher("employeedetails.jsp");
			try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error(e);

			}
		}else {
			RequestDispatcher dispatcher=
					request.getRequestDispatcher("noemployeedetails.jsp");
			try {
				dispatcher.forward(request,response);
				}catch(NullPointerException e) {
					response.sendRedirect(ERROR_PAGE);
					log.error(e);

				}		}
		}
		if(action.contentEquals("loadform")) {
		     loadForm(request,response);
			}
           if(action.contentEquals("viewEmployee")) {
        	   viewEmployee(request,response);
			}		
          }
	
    protected void loadForm(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
    	List<DepartmentsModel> departmentsList=
				departmentService.retrieveDepartments();
		List<JobsModel> jobsList=jobsService.retrieveJobs();
		List<ManagersModel> managersList=employeeService.getManagers();
		request.setAttribute(DEPARTMENT_LIST_NAME, departmentsList);
		request.setAttribute(JOB_LIST_NAME, jobsList);
		request.setAttribute(MANAGER_LIST_NAME, managersList);
		RequestDispatcher dispatcher=
				request.getRequestDispatcher("employeeform.jsp");
		try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error("Failed to delegate");
			}
    }
    
    protected void viewEmployee(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    	List<AllEmployeesModel> allemployeesList=employeeService.retrieveAllEmployees();
		request.setAttribute("allemployeesList", allemployeesList);	
		RequestDispatcher dispatcher=
				request.getRequestDispatcher("allemployees.jsp");
		try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error("Failed to delegate");

			}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("HTTP POST REQUEST");

    	String action=request.getParameter("action");
        if(action.contentEquals("newEmployee")) {
        newEmployee(request,response);
        }
        if(action.contentEquals("updateEmployeeForm")) {
        	updateEmployeeForm(request,response);
        }
        if(action.contentEquals("updateEmployee")) {
        	updateEmployee(request,response);	
        }
        if(action.contentEquals("deleteEmployee")) {
        	deleteEmployee(request,response);
        }
	}
    protected void newEmployee(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    	int employeeId=0;
    	try {
        employeeId=Integer.parseInt(request.getParameter(EMPLOYEE_ID_PARAM));
    	}catch(NumberFormatException e) {
    		response.sendRedirect(ERROR_PAGE);
    	}
    	String firstName=request.getParameter("firstName");
    	String lastName=request.getParameter("lastName");
    	String email=request.getParameter("email");
    	String phoneNumber=request.getParameter("phoneNumber");
    	String hireDateReq=request.getParameter("hireDate");
    	LocalDate hireDate=DateConverter.convertLocaleDate(hireDateReq, "-");
    	String jobId=request.getParameter("jobId");
    	double salary=0.0;
    	double commissionPCT=0.0;
    	int managerId=0;
    	int departmentId=0;
    	try {
        salary=Double.parseDouble(request.getParameter("salary"));
        commissionPCT=Double.parseDouble(request.getParameter("commissionPCT"));
        managerId=Integer.parseInt(request.getParameter("managerId"));
    	departmentId=Integer.parseInt(request.getParameter("departmentId"));
    	}catch(NumberFormatException e) {
    		response.sendRedirect(ERROR_PAGE);
			log.error(e);
    	}
    	EmployeesModelValidator validator=new EmployeesModelValidator();
    	boolean employeeIdExist=validator.employeeIdExists(employeeId);
    	boolean validFirstName=validator.validString(firstName);
    	boolean validLastName=validator.validString(lastName);
    	boolean validEmail=validator.validEmail(email);
    	boolean validSalary=validator.validSalary(salary);
    	boolean emailExist=validator.emailExist(email);
    	if(employeeIdExist || !validFirstName || !validLastName || !validEmail || !validSalary || emailExist) {
    		  if(employeeIdExist) {
              	request.setAttribute("employeeIderror","Employee Id already exist");
              }
              if(!validFirstName) {
          		request.setAttribute("firstnameerror","First Name not valid");
              }
              if(!validLastName) {
          		request.setAttribute("lastnameerror","Last Name not valid");
              }
              if(!validEmail) {
          		request.setAttribute("emailerror","Email not valid");
              }
              if(!validSalary) {
          		request.setAttribute("salaryerror","Salary not valid");
              }
              if(emailExist) {
            		request.setAttribute("emailexisterror","Email already exist");
              }
        RequestDispatcher dispatcher=
				request.getRequestDispatcher("employeeform.jsp");
        List<DepartmentsModel> departmentsList=
				departmentService.retrieveDepartments();
		List<JobsModel> jobsList=jobsService.retrieveJobs();
		List<ManagersModel> managersList=employeeService.getManagers();
		request.setAttribute(DEPARTMENT_LIST_NAME, departmentsList);
		request.setAttribute(JOB_LIST_NAME, jobsList);
		request.setAttribute(MANAGER_LIST_NAME, managersList);
		try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error(e);
			}
    	}else {
    		AllEmployeesModel employeesModel=new AllEmployeesModel();
    		employeesModel.setEmployeeId(employeeId);
    		employeesModel.setFirstName(firstName);
    		employeesModel.setLastName(lastName);
    		employeesModel.setEmail(email);
    		employeesModel.setPhoneNumber(phoneNumber);
    		employeesModel.setJobId(jobId);
    		employeesModel.setHireDate(hireDate);
    		employeesModel.setSalary(salary);
    		employeesModel.setCommissionPCT(commissionPCT);
    		employeesModel.setDepartmentId(departmentId);
    		employeesModel.setManagerId(managerId);
    		String outcome=employeeService.registerEmployee(employeesModel);
    		
    		if(outcome.contentEquals(SUCCESS)) {
    			 RequestDispatcher dispatcher=
    	    				request.getRequestDispatcher(EMPLOYEE_SUCCESS_PAGE);
    			 request.setAttribute(EMPLOYEES_MODEL,employeesModel);
       			 request.setAttribute(OPERATION, "Below Employee record Registration was Successfully");
       			try {
    				dispatcher.forward(request,response);
    				}catch(NullPointerException e) {
    					response.sendRedirect(ERROR_PAGE);
    					log.error(e);
    				}        	
       			}else {
    			 RequestDispatcher dispatcher=
    	    				request.getRequestDispatcher(EMPLOYEE_FAIL_PAGE);
       			 request.setAttribute(OPERATION, "Employee Registration Failed");
       			try {
    				dispatcher.forward(request,response);
    				}catch(NullPointerException e) {
    					response.sendRedirect(ERROR_PAGE);
    					log.error(e);

    			    }        		
       		}	
    	}
    }
    
    protected void updateEmployeeForm(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    	int employeeId=0;
    	try {
        employeeId=Integer.parseInt(request.getParameter(EMPLOYEE_ID_PARAM));
    	}catch(NumberFormatException e) {
			response.sendRedirect(ERROR_PAGE);
			log.error(e);
    	}
    	RequestDispatcher dispatcher=
				request.getRequestDispatcher("employeeupdateform.jsp");
    	List<AllEmployeesModel> allEmployees=employeeService.retrieveAllEmployees();
    	AllEmployeesModel allemployeesModel=new AllEmployeesModel();
    	for(AllEmployeesModel employeesModel:allEmployees) {
    		if(employeesModel.getEmployeeId()==employeeId) {
    			allemployeesModel=employeesModel;
    		}
    	}
        List<DepartmentsModel> departmentsList=
				departmentService.retrieveDepartments();
		List<JobsModel> jobsList=jobsService.retrieveJobs();
		List<ManagersModel> managersList=employeeService.getManagers();
		request.setAttribute("allemployeesModel", allemployeesModel);
		request.setAttribute(DEPARTMENT_LIST_NAME, departmentsList);
		request.setAttribute(JOB_LIST_NAME, jobsList);
		request.setAttribute(MANAGER_LIST_NAME, managersList);
		try {
		dispatcher.forward(request,response);
		}catch(NullPointerException e) {
			log.error(e);
		}
    
    }
    protected void updateEmployee(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    	int employeeId=0;
    	try {
        employeeId=Integer.parseInt(request.getParameter(EMPLOYEE_ID_PARAM));
    	}catch(NumberFormatException e) {
    		response.sendRedirect(ERROR_PAGE);
			log.error(e);
    	}
    	String firstName=request.getParameter("firstName");
    	String lastName=request.getParameter("lastName");
    	String email=request.getParameter("email");
    	String phoneNumber=request.getParameter("phoneNumber");
    	String jobId=request.getParameter("jobId");
     	double salary=0.0;
    	double commissionPCT=0.0;
    	int managerId=0;
    	int departmentId=0;
    	try {
        salary=Double.parseDouble(request.getParameter("salary"));
        commissionPCT=Double.parseDouble(request.getParameter("commissionPCT"));
        managerId=Integer.parseInt(request.getParameter("managerId"));
    	departmentId=Integer.parseInt(request.getParameter("departmentId"));
    	}catch(NumberFormatException e) {
    		response.sendRedirect(ERROR_PAGE);
			log.error(e);

    	}
        String hireDateReq=request.getParameter("hireDate");
    	
    	LocalDate hireDate=DateConverter.convertLocaleDate(hireDateReq, "-");
    	AllEmployeesModel employeesModel=new AllEmployeesModel();
		employeesModel.setEmployeeId(employeeId);
		employeesModel.setFirstName(firstName);
		employeesModel.setLastName(lastName);
		employeesModel.setEmail(email);
		employeesModel.setPhoneNumber(phoneNumber);
		employeesModel.setHireDate(hireDate);
		employeesModel.setJobId(jobId);
		employeesModel.setSalary(salary);
		employeesModel.setCommissionPCT(commissionPCT);
		employeesModel.setDepartmentId(departmentId);
		employeesModel.setManagerId(managerId);	
		String outcome=employeeService.updateEmployee(employeesModel);
		if(outcome.contentEquals(SUCCESS)) {
			 RequestDispatcher dispatcher=
	    				request.getRequestDispatcher(EMPLOYEE_SUCCESS_PAGE);
			 request.setAttribute(EMPLOYEES_MODEL,employeesModel);
			 request.setAttribute(OPERATION, "Below Employee record Updated Successfully");
			try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error(e);
			}   		}else {
			 RequestDispatcher dispatcher=
	    				request.getRequestDispatcher(EMPLOYEE_FAIL_PAGE);
			 request.setAttribute(OPERATION, "Employee Update Failed");
			try {
			dispatcher.forward(request,response);
			}catch(NullPointerException e) {
				response.sendRedirect(ERROR_PAGE);
				log.error(e);
			}   
			}

    }
    protected void deleteEmployee(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    	int employeeId=0;
    	try {
        employeeId=Integer.parseInt(request.getParameter(EMPLOYEE_ID_PARAM));
    	}catch(NumberFormatException e) {
    		response.sendRedirect(ERROR_PAGE);
			log.error(e);
    	}
    	AllEmployeesModel employeesModel=new AllEmployeesModel();
    	employeesModel.setEmployeeId(employeeId);
    	String outcome=employeeService.deleteEmployee(employeesModel);
    	List<AllEmployeesModel> allEmployeesList=employeeService.retrieveAllEmployees();
    	for(AllEmployeesModel employees:allEmployeesList) {
    		if(employeesModel.getEmployeeId()==employeeId) {
    			employeesModel=employees;
    		}
    	}
    	if(outcome.contentEquals(SUCCESS)) {
  			 RequestDispatcher dispatcher=
  	    				request.getRequestDispatcher(EMPLOYEE_SUCCESS_PAGE);
  			 request.setAttribute(EMPLOYEES_MODEL,employeesModel);
  			 request.setAttribute(OPERATION, "Below Employee record deleted Successfully");
  			try {
				dispatcher.forward(request,response);
				}catch(NullPointerException e) {
					response.sendRedirect(ERROR_PAGE);
					log.error(e);

				}      		}else {
  			 RequestDispatcher dispatcher=
  	    				request.getRequestDispatcher(EMPLOYEE_FAIL_PAGE);
  			 request.setAttribute(OPERATION, "Employee Delete Failed");
  			try {
				dispatcher.forward(request,response);
				}catch(NullPointerException e) {
					response.sendRedirect(ERROR_PAGE);
					log.error(e);
				}      		
  		}

    }
}

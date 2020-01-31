package com.yash.test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.yash.dao.EmployeesDAO;
import com.yash.dao.JDBCEmployeesDAOImpl;
import com.yash.entities.Employees;
public class TestJDBCEmployeesDAOImpl {
	@Test
	public void testGetAllEmployeesPositive() {
		EmployeesDAO employeesDAO=
				new JDBCEmployeesDAOImpl();
		try {
			List<Employees> employeesList=
					employeesDAO.getAllEmployees();
			assertEquals(true,employeesList.isEmpty());
		} catch (ClassNotFoundException | SQLException e) {
			assertTrue(false);
		}
	}
}

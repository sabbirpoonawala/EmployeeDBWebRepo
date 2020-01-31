package com.yash.test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.yash.validation.EmployeesModelValidator;
public class TestRegisterEmployeesModelValidator {
	private EmployeesModelValidator validator=null;
	@BeforeEach
	public void setUp(){
		validator=new EmployeesModelValidator();
	}
	@AfterEach
	public void tearDown(){
		validator=null;
	}
	@Test
	public void testValidStringPositive() {
		boolean actual=validator.validString("sabbir");
		assertEquals(true,actual);
    	}
	@Test
	public void testValidStringNegative() {
		boolean actual=validator.validString("sabbir111");
		assertEquals(false,actual);
    	}
	@Test
	public void testValidNumberPositive() {
		boolean actual=validator.validNumber(12000);
		assertEquals(true,actual);
	}
	@Test
	public void testValidSalaryPositive() {
		boolean actual=validator.validSalary(34000);
		assertEquals(true,actual);
	}
	@Test
	public void testValidSalaryNegative() {
		boolean actual=validator.validSalary(340000);
		assertEquals(false,actual);
	}
	@Test
	public void testValidSalaryZero() {
		boolean actual=validator.validSalary(0);
		assertEquals(false,actual);
	}
}

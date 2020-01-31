package com.yash.test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import com.yash.integrate.ConnectionManager;
public class TestConnectionManager {
	@Test
	public void testOpenConnectionPositive() {
		try(Connection connection=ConnectionManager.
				openConnection()){
			assertEquals(true,connection!=null);
		} catch (ClassNotFoundException | SQLException e) {
			assertTrue(false);
		}
	}
}

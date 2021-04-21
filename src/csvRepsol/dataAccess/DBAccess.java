package csvRepsol.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.log4j.Logger;

import csvRepsol.constants.EmployeeConstants;
import csvRepsol.entities.Employee;

public class DBAccess {

	private static Connection conn;
	private static Logger log = Logger.getLogger(DBAccess.class);

	public static boolean tryConnection() {
		boolean conectado = true;
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			conn.close();
		} catch (SQLException e) {
			log.error("no conectado");
			conectado = false;
		}
		return conectado;
	}

	public static HashMap<String, Employee> getEmployeesFromServer() {
		HashMap<String, Employee> employeeList = new HashMap<String, Employee>();
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			String query = "SELECT * FROM employees;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Employee emp = new Employee(rset.getString(EmployeeConstants.ID),
						rset.getString(EmployeeConstants.NAME), rset.getString(EmployeeConstants.SURNAME1),
						rset.getString(EmployeeConstants.SURNAME2), rset.getString(EmployeeConstants.PHONE),
						rset.getString(EmployeeConstants.EMAIL), rset.getString(EmployeeConstants.JOB),
						rset.getDate(EmployeeConstants.HIRING_DATE), rset.getInt(EmployeeConstants.YEAR_SALARY),
						rset.getBoolean(EmployeeConstants.SICK_LEAVE));
				employeeList.put(rset.getString(EmployeeConstants.ID), emp);
			}

		} catch (SQLException e) {
			log.error("ni idea nano");
		}

		return employeeList;

	}

	public static void updateEmployee(Employee emp) {
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createEmployee(Employee emp) {

	}

	public static void deleteEmployee(Employee emp) {

	}

}

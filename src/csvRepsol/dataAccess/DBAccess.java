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

import csvRepsol.entities.Employee;

public class DBAccess {

	private static Connection conn;
	private static Logger log = Logger.getLogger(DBAccess.class);
	private final static int ID = 0;
	private final static int NAME = 1;
	private final static int SURNAME1 = 2;
	private final static int SURNAME2 = 3;
	private final static int PHONE = 4;
	private final static int EMAIL = 5;
	private final static int JOB = 6;
	private final static int HIRING_DATE = 7;
	private final static int YEAR_SALARY = 8;
	private final static int SICK_LEAVE = 9;

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

	public static List<Employee> getEmployeesFromServer() {
		List<Employee> employeeList = new ArrayList<Employee>();
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			String qwuery = "SELECT * FROM employees;";
			PreparedStatement stmt = conn.prepareStatement(qwuery);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Employee emp = new Employee(rset.getString(ID), rset.getString(NAME), rset.getString(SURNAME1),
						rset.getString(SURNAME2), rset.getString(PHONE), rset.getString(EMAIL), rset.getString(JOB),
						rset.getDate(HIRING_DATE), rset.getInt(YEAR_SALARY), rset.getBoolean(SICK_LEAVE));
				employeeList.add(emp);
			}

		} catch (SQLException e) {
			log.error("ni idea nano");
		}

		return employeeList;

	}

	public static void updateEmployee(Employee emp) {

	}

	public static void createEmployee(Employee emp) {

	}

	public static void deleteEmployee(Employee emp) {

	}

}

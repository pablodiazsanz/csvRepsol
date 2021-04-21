package csvRepsol.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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

	public static void updateEmployee(Employee emp, List<String> extraData) {
	
	}

	public static void createEmployee(Employee emp) {
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			String query = "INSERT INTO employee VALUES ('" + emp.getId() + "','" + emp.getName() + "','"
					+ emp.getSurname1() + "','" + emp.getSurname2() + "','" + emp.getTlf() + "','" + emp.getMail()
					+ "','" + emp.getJob() + "','" + emp.getHiringDate() + "','" + emp.getYearSalary() + "','"
					+ emp.isSickLeave() + "',)";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			log.trace("empleado "+emp.getId()+" creado con exito");
		} catch (SQLException e) {
			log.error("no ha podido crear al empleado:"+ emp.getId());
		}
	}

	public static void deleteEmployee(Employee emp) {
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
			String query = "DELETE FROM employees WHERE ID = '" + emp.getId() + "';";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			log.trace("empleado "+emp.getId()+" borrado con exito");
		} catch (SQLException e) {
			log.error("no ha podido borrar al empleado:"+ emp.getId());
		}
	}

}

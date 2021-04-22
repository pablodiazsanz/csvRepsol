package csvRepsol.dataAccess;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			conn.close();
		} catch (SQLException e) {
			log.error("no conectado", e);
			conectado = false;
		}
		return conectado;
	}

	public static HashMap<String, Employee> getEmployeesFromServer() {
		HashMap<String, Employee> employeeList = new HashMap<String, Employee>();
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			String query = "SELECT * FROM employee;";
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
			log.error("ni idea nano", e);
		}

		return employeeList;

	}

	public static void updateEmployee(Employee updatedEmployee, List<String> extraData) {
		// Creamos esta variable para enviarle al fichero CSV el contenido.
		String query = "UPDATE employee SET";

		/*
		 * Estas variables son las que vamos a darle a la cadena updatedData para pasar
		 * los datos. Estas cadenas se van a modificar si la lista extraData no contiene
		 * el dato
		 */

		// Estas variables son las vamos a utilizar para comprobar si el dato ha
		// cambiado o no.
		boolean name = true;
		boolean surname1 = true;
		boolean surname2 = true;
		boolean phone = true;
		boolean email = true;
		boolean job = true;
		boolean hiringDate = true;
		boolean yearSalary = true;
		boolean sickLeave = true;

		// Comprobamos que la lista no está vacia.
		if (!extraData.isEmpty()) {

			// Recorremos la lista para saber que datos no se han cambiado.
			for (int i = 0; i < extraData.size(); i++) {

				if (extraData.get(i).equals("name")) {
					name = false;

				} else if (extraData.get(i).equals("surname1")) {
					surname1 = false;

				} else if (extraData.get(i).equals("surname2")) {
					surname2 = false;

				} else if (extraData.get(i).equals("phone")) {
					phone = false;

				} else if (extraData.get(i).equals("email")) {
					email = false;

				} else if (extraData.get(i).equals("job")) {
					job = false;

				} else if (extraData.get(i).equals("hiringDate")) {
					hiringDate = false;

				} else if (extraData.get(i).equals("yearSalary")) {
					yearSalary = false;

				} else if (extraData.get(i).equals("sickLeave")) {
					sickLeave = false;
				}

			}
		}

		// Si los datos han sido cambiados, establecemos el dato para pasarselo al
		// fichero CSV.
		if (name) {
			query += " name = '" + updatedEmployee.getName() + "',";
		}
		if (surname1) {
			query += " first_surname = '" + updatedEmployee.getSurname1() + "',";
		}
		if (surname2) {
			query += " second_surname = '" + updatedEmployee.getSurname2() + "',";
		}
		if (phone) {
			query += " phone = '" + updatedEmployee.getTlf() + "',";
		}
		if (email) {
			query += " email = '" + updatedEmployee.getMail() + "',";
		}
		if (job) {
			query += " job = '" + updatedEmployee.getJob() + "',";
		}
		if (hiringDate) {
			query += " hiring_date = parsedatetime('"
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(updatedEmployee.getHiringDate())
					+ "', 'dd/MM/yyyy HH:mm:ss'),";
		}
		if (yearSalary) {
			query += " year_salary = '" + updatedEmployee.getYearSalary() + "',";
		}
		if (sickLeave) {
			query += " sick_leave = '" + updatedEmployee.isSickLeave() + "',";
		}

		// Metemos los datos en la cadena que vamos a darle al fichero CSV con los datos
		// correctos.
		query = query.substring(0, query.length() - 1);
		query += " WHERE ID = '" + updatedEmployee.getId() + "';";
		// Añadimos la linea de datos al fichero CSV.

		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
			log.trace("empleado " + updatedEmployee.getId() + " actualizado con exito");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createEmployee(Employee emp) {
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			String query = "INSERT INTO employee(id,name,first_surname,second_surname,phone,email,job,hiring_date,year_salary,sick_leave) VALUES ('"
					+ emp.getId() + "','" + emp.getName() + "','" + emp.getSurname1() + "','" + emp.getSurname2() + "',"
					+ emp.getTlf() + ",'" + emp.getMail() + "','" + emp.getJob() + "',parsedatetime('"
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(emp.getHiringDate())
					+ "', 'dd/MM/yyyy HH:mm:ss')," + emp.getYearSalary() + "," + emp.isSickLeave() + ");";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.execute();
			log.trace("empleado " + emp.getId() + " creado con exito");
		} catch (SQLException e) {
			log.error("no ha podido crear al empleado:" + emp.getId(), e);
		}
	}

	public static void deleteEmployee(Employee emp) {
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			String query = "DELETE FROM employee WHERE ID = '" + emp.getId() + "';";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.execute();
			log.trace("empleado " + emp.getId() + " borrado con exito");
		} catch (SQLException e) {
			log.error("no ha podido borrar al empleado:" + emp.getId(), e);
		}
	}

}

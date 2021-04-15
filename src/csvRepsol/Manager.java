package csvRepsol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class Manager {

	private static Logger log = Logger.getLogger(Manager.class);

	private static CsvAccess dao;

	/**
	 * Este m�todo compara las listas que se obtienen desde los csv del cliente y
	 * del servidor y analiza si hay que modificar alg�n usuario, dar de alta o dar
	 * de baja.
	 *
	 * @param clientData Esta lista contiene los empleados que tiene el csv del
	 *                   cliente
	 * @param serverData Esta lista contiene los empleados que tiene el csv del
	 *                   servidor
	 */
	public static void compare(HashMap<String, Employee> clientData, HashMap<String, Employee> serverData) {
		dao = new CsvAccess();
		log.info("Empezamos la comparacion de usuarios");
		// En este bucle vamos a recorrer todos los empleados de cliente para
		// compararlos con los del servidor
		for (String i : clientData.keySet()) {

			/*
			 * Aqu� vamos a modificar el empleado si tiene alg�n dato modificado y lo
			 * pasamos al tercer CSV como un empleado que se ha modificado, solo con los
			 * datos cambiados. Se actualize o no, lo eliminaremos de la lista del servidor
			 * para saber cuales han sido eliminados de la lista de cliente, y asi saber los
			 * que hay que borrar.
			 */
			if (serverData.containsKey(i)) {
				if (!clientData.get(i).getName().equalsIgnoreCase(serverData.get(i).getName().toLowerCase())
						|| !clientData.get(i).getSurname1().equalsIgnoreCase(serverData.get(i).getSurname1())
						|| !clientData.get(i).getSurname2().equalsIgnoreCase(serverData.get(i).getSurname2())
						|| !clientData.get(i).getTlf().equalsIgnoreCase(serverData.get(i).getTlf())
						|| !clientData.get(i).getMail().equalsIgnoreCase(serverData.get(i).getMail())
						|| !clientData.get(i).getJob().equalsIgnoreCase(serverData.get(i).getJob())
						|| clientData.get(i).getHiringDate().compareTo(serverData.get(i).getHiringDate()) != 0
						|| clientData.get(i).getYearSalary() != serverData.get(i).getYearSalary()
						|| clientData.get(i).isSickLeave() != serverData.get(i).isSickLeave()) {

					updateEmployee(clientData.get(i), serverData.get(i));
					log.info("[" + clientData.get(i).getId() + "] - Modificado");

				} else {
					log.info("[" + clientData.get(i).getId() + "] - No modificado");

				}

				serverData.remove(i);
			} else {
				// Aqu�, si no se ha modificado, como el empleado no est� en la lista del
				// servidor
				// lo pasamos al tercer CSV como un nuevo empleado que se ha creado.
				dao.writeCSV(clientData.get(i), "CREATE");
				log.info("[" + clientData.get(i).getId() + "] - Creado");
			}
		}
		// Aqu� pasamos al tercer CSV los empleados que se encuentran en la lista del
		// servidor pero
		// que han sido eliminados de la lista del cliente, por lo tanto los que se van
		// a eliminar.
		log.info("Empezamos el borrado de usuarios");
		for (String i : serverData.keySet()) {
			dao.writeCSV(serverData.get(i), "DELETE");
			log.info("[" + serverData.get(i).getId() + "] - Eliminado");
		}
	}

	/**
	 *
	 * En este m�todo se comparan los empleados que tienen el mismo identificador de
	 * las listas del servidor y del cliente y se devuelve un empleado nuevo,
	 * �nicamente con las modificaciones realizadas.
	 *
	 * @param clientEmployee Este empleado se obtiene de la lista de clentes.
	 * @param serverEmployee Este empleado se obtiene de la lista de clentes.
	 * @return Devuelve el empleado con el identificador y con las modificaciones
	 *         que tiene.
	 */
	private static void updateEmployee(Employee clientEmployee, Employee serverEmployee) {
		// Creamos un empleado vac�o con el id de los que vamos a comparar
		Employee updatedEmployee = new Employee(clientEmployee.getId(), "", "", "", "", "", "", null, -1, false);
		List<String> extraData = new ArrayList<String>();

		// En los if, comparamos dato a dato para saber cuales han sido modificados, y
		// si
		// se han modificado, metemos el dato del cliente en el empleado que devolvemos.
		if (!clientEmployee.getName().equalsIgnoreCase(serverEmployee.getName())) {
			updatedEmployee.setName(clientEmployee.getName());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Nombre - {" + updatedEmployee.getName() + "}");
		}
		if (!clientEmployee.getSurname1().equalsIgnoreCase(serverEmployee.getSurname1())) {
			updatedEmployee.setSurname1(clientEmployee.getSurname1());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: 1� apellido - {" + updatedEmployee.getSurname1()
					+ "}");
		}
		if (!clientEmployee.getSurname2().equalsIgnoreCase(serverEmployee.getSurname2())) {
			updatedEmployee.setSurname2(clientEmployee.getSurname2());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: 2� apellido - {" + updatedEmployee.getSurname2()
					+ "}");
		}
		if (!clientEmployee.getTlf().equalsIgnoreCase(serverEmployee.getTlf())) {
			updatedEmployee.setTlf(clientEmployee.getTlf());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Telefono - {" + updatedEmployee.getTlf() + "}");
		}
		if (!clientEmployee.getMail().equalsIgnoreCase(serverEmployee.getMail())) {
			updatedEmployee.setMail(clientEmployee.getMail());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Email - {" + updatedEmployee.getMail() + "}");
		}
		if (!clientEmployee.getJob().equalsIgnoreCase(serverEmployee.getJob())) {
			updatedEmployee.setJob(clientEmployee.getJob());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Puesto de trabajo - {" + updatedEmployee.getJob()
					+ "}");
		}
		if (clientEmployee.getHiringDate().compareTo(serverEmployee.getHiringDate()) != 0) {
			updatedEmployee.setHiringDate(clientEmployee.getHiringDate());
			String hiringDateFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.format(clientEmployee.getHiringDate());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Fecha de contratacion - {" + hiringDateFormatted
					+ "}");
		} else {
			extraData.add("hiringDate");
		}
		if (clientEmployee.getYearSalary() != serverEmployee.getYearSalary()) {
			updatedEmployee.setYearSalary(clientEmployee.getYearSalary());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Salario anual - {" + updatedEmployee.getYearSalary()
					+ "}");
		} else {
			extraData.add("yearSalary");
		}
		if (clientEmployee.isSickLeave() != serverEmployee.isSickLeave()) {
			updatedEmployee.setSickLeave(clientEmployee.isSickLeave());
			log.info("[" + updatedEmployee.getId() + "] - Modifica: Baja - {" + updatedEmployee.isSickLeave() + "}");
		} else {
			extraData.add("sickLeave");
		}

		dao.writeUpdatedEmployeeCSV(updatedEmployee, extraData, "UPDATE");

	}
}

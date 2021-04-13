package csvRepsol;

import java.util.HashMap;

public class MainClass {

	public static void main(String[] args) {
		CsvAccess csvAccess = new CsvAccess();
		csvAccess.createCSV();
		HashMap<String, Employee> clientData = csvAccess.readCSV("client");
		HashMap<String, Employee> serverData = csvAccess.readCSV("server");

		Manager.compare(clientData, serverData);

	}
}

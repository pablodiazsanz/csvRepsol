package csvRepsol;

import java.util.Date;

public class Employee {
	private String id, name, surname1, surname2, tlf, mail, job;
	private Date fecha_contratacion;
	private int salario_bruto_anual;
	private boolean baja;

	public Employee() {
	}

	public Employee(String id, String name, String surname1, String surname2, String tlf, String mail, String job,
			Date fecha_contratacion, int salario_bruto_anual, boolean baja) {
		super();
		this.id = id;
		this.name = name;
		this.surname1 = surname1;
		this.surname2 = surname2;
		this.tlf = tlf;
		this.mail = mail;
		this.job = job;
		this.fecha_contratacion = fecha_contratacion;
		this.salario_bruto_anual = salario_bruto_anual;
		this.baja = baja;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname1() {
		return surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}

	public String getSurname2() {
		return surname2;
	}

	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}

	public String getTlf() {
		return tlf;
	}

	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Date getFecha_contratacion() {
		return fecha_contratacion;
	}

	public void setFecha_contratacion(Date fecha_contratacion) {
		this.fecha_contratacion = fecha_contratacion;
	}

	public int getSalario_bruto_anual() {
		return salario_bruto_anual;
	}

	public void setSalario_bruto_anual(int salario_bruto_anual) {
		this.salario_bruto_anual = salario_bruto_anual;
	}

	public boolean isBaja() {
		return baja;
	}

	public void setBaja(boolean baja) {
		this.baja = baja;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", surname1=" + surname1 + ", surname2=" + surname2 + ", tlf="
				+ tlf + ", mail=" + mail + ", job=" + job + ", fecha_contratacion=" + fecha_contratacion
				+ ", salario_bruto_anual=" + salario_bruto_anual + ", baja=" + baja + "]";
	}

	/**
	 *
	 * Escribe la linea del empleado en formato CSV.
	 *
	 * @return String con la linea en csv
	 */
	public String toCSV() {

		return id + ";" + name + ";" + surname1 + ";" + surname2 + ";" + tlf + ";" + mail + ";" + job +
				";" + fecha_contratacion + ";" + salario_bruto_anual + ";" + baja;
	}
}

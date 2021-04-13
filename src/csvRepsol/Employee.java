package csvRepsol;

public class Employee {
	private String id, name, surname1, surname2, tlf, mail, job;

    public Employee(){}

    public Employee(String id, String name, String surname1, String surname2, String tlf, String mail, String job) {
        this.id = id;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.tlf = tlf;
        this.mail = mail;
        this.job = job;
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

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname1='" + surname1 + '\'' +
                ", surname2='" + surname2 + '\'' +
                ", tlf='" + tlf + '\'' +
                ", mail='" + mail + '\'' +
                ", job='" + job + '\'' +
                '}';
    }

    /**
     *
     * Escribe la linea del empleado en formato CSV.
     *
     * @return String con la linea en csv
     */
    public String toCSV(){

        return  id + ";" +
                name + ";" +
                surname1 + ";" +
                surname2 + ";" +
                tlf + ";" +
                mail + ";" +
                job  ;
    }
}

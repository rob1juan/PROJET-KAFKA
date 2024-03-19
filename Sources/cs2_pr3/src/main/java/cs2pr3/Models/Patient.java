package cs2pr3.Models;

import java.util.Date;
import java.util.List;

public class Patient {
    private String idPatient;
    private Date birthday;
    private String sex;
    private String nationality;
    private List<Name> names;
    private List<Address> addresses;

    public Patient(String idPatient, Date birthday, String sex, String nationality, List<Name> names, List<Address> addresses) {
        this.idPatient = idPatient;
        this.birthday = birthday;
        this.sex = sex;
        this.nationality = nationality;
        this.names = names;
        this.addresses = addresses;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}

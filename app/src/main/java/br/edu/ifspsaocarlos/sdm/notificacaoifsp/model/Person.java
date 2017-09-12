package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 8/15/2017.
 */

/*
* Não tem herança nos Objetos Realm, então tem que fazer uma composição para suprir o problema.
* Lembrando que não é permitido um objeto como primary key do realm
*
* Error:(12, 8) error: Valid model classes must either extend RealmObject or implement RealmModel.
* */
@RealmClass
public class Person extends RealmObject {

    @PrimaryKey
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean gender;
    private Date birthday;
    private int idInstituto;


    public Person(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getIdInstituto() {
        return idInstituto;
    }

    public void setIdInstituto(int idInstituto) {
        this.idInstituto = idInstituto;
    }
}

package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

/**
 * Created by rapha on 8/15/2017.
 */
//@RealmClass
//public class Professor extends RealmObject {
public class Professor {
    //@PrimaryKey
    private String id; /*It happens because Realm doesn't accept object as primary key*/
    private Person person;
    private String graduation;
    private String graduationType;


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        this.id = person.getUsername(); /*It happens because Realm doesn't accept object as primary key*/
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getGraduationType() {
        return graduationType;
    }

    public void setGraduationType(String graduationType) {
        this.graduationType = graduationType;
    }
}

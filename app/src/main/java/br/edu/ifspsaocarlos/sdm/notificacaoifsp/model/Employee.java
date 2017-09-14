package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

/**
 * Created by rapha on 8/15/2017.
 */
//@RealmClass
//public class Employee extends RealmObject{
public class Employee{
    //@PrimaryKey
    private String id; /*It happens because Realm doesn't accept object as primary key*/
    private Person person;
    private String function;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        this.id = person.getUsername(); /*It happens because Realm doesn't accept object as primary key*/
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}

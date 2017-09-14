package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

/**
 * Created by rapha on 8/15/2017.
 */
//@RealmClass
//public class Student extends RealmObject {
public class Student{
    //@PrimaryKey
    private String id; /*It happens because Realm doesn't accept object as primary key*/
    private Person person;
    private String team;

    public Student(){

    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        this.id = person.getUsername(); /*It happens because Realm doesn't accept object as primary key*/
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

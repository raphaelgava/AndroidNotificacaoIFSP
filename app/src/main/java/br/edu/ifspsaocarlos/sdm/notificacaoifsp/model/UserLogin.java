package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

/**
 * Created by rapha on 8/14/2017.
 */

import java.util.Date;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.EnumUserType;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class UserLogin extends RealmObject {

    @PrimaryKey
    private int id;
    private String token;
    private String group;
    private boolean flagProf;
    private Date lastUpdate;

    public UserLogin() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean getFlag() {
        return flagProf;
    }

    public void setFlag(boolean flagProf) {
        this.flagProf = flagProf;
    }

    public EnumUserType getPersonType(){
        if (group.equals("Student")){
            return EnumUserType.ENUM_STUDENT;
        }else if (group.equals("Employee")){
            return EnumUserType.ENUM_EMPLOYEE;
        }else if (group.equals("Professor")){
            return EnumUserType.ENUM_PROFESSOR;
        }else if (group.equals("Admin")){
            if (flagProf == true){
                return EnumUserType.ENUM_PROFESSOR;
            }else{
                return EnumUserType.ENUM_EMPLOYEE;
            }
        }
        return EnumUserType.ENUM_NOTHING;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

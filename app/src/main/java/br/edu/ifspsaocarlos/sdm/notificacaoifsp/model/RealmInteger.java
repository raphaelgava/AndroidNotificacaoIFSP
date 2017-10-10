package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 10/9/2017.
 */
@RealmClass
public class RealmInteger extends RealmObject implements Serializable {

    private static final long  serialVersionUID = 100L;
    @PrimaryKey
    private int pk;

    public RealmInteger(){
        pk = 0;
    }

    public RealmInteger(int val) {
        this.pk = val;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }
}

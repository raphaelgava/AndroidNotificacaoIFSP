package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 3/15/2017.
 */
@RealmClass
public class Remetente  extends RealmObject implements Serializable {
    //public class Offering  extends RealmObject{
    private static final long  serialVersionUID = 100L;
    @PrimaryKey
    private int pk;
    private String descricao;
    private boolean is_active;
    private boolean checked;
    private String tipo;

    public Remetente() {
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

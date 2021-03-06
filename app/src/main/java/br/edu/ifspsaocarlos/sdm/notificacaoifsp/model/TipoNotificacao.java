package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 10/12/2017.
 */

@RealmClass
public class TipoNotificacao  extends RealmObject implements Serializable {
    private static final long  serialVersionUID = 102L;
    @PrimaryKey
    private int pk;
    private String cor;
    private String descricao;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String toString(){
        return getPk() + "-" + getDescricao();
    }
}

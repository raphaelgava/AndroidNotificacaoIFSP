package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 10/12/2017.
 */

@RealmClass
public class MyClass extends RealmObject implements Serializable {
    private static final long  serialVersionUID = 101L;
    @PrimaryKey
    private int pk;
    private int id_instituto;
    private String descricao;
    private String sigla;
    private int qtd_modulos;
    private int carga_horaria;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId_instituto() {
        return id_instituto;
    }

    public void setId_instituto(int id_instituto) {
        this.id_instituto = id_instituto;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getQtd_modulos() {
        return qtd_modulos;
    }

    public void setQtd_modulos(int qtd_modulos) {
        this.qtd_modulos = qtd_modulos;
    }

    public int getCarga_horaria() {
        return carga_horaria;
    }

    public void setCarga_horaria(int carga_horaria) {
        this.carga_horaria = carga_horaria;
    }
}

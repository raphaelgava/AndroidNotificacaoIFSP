package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.util.Date;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.EnumUserType;
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
    // TODO: 9/14/2017 remover as classes filhas que não estao sendo mais utilizadas!!!! 
    @PrimaryKey
    private int pk;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String sexo;
    private Date datanascimento;
    private int id_instituto;

    private int type;

    //Employee
    private String funcao;

    //Professor
    private String formacao;
    private String tipo_formacao;

    //Student
    private String turma;


    public Person(){

    }

    public int getType() {
        return type;
    }

    public void setType(EnumUserType type) {
        this.setType(type.ordinal());
    }


    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(Date datanascimento) {
        this.datanascimento = datanascimento;
    }

    public int getId_instituto() {
        return id_instituto;
    }

    public void setId_instituto(int id_instituto) {
        this.id_instituto = id_instituto;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getTipo_formacao() {
        return tipo_formacao;
    }

    public void setTipo_formacao(String tipo_formacao) {
        this.tipo_formacao = tipo_formacao;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}

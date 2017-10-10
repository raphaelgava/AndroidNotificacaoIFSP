package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.MyGsonBuilder;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
/**
 * Created by rapha on 9/28/2016.
 */

//@org.parceler.Parcel(implementations = { OfferingRealmProxy.class },
//        value = org.parceler.Parcel.Serialization.BEAN,
//        analyze = { Offering.class })
//OUTRA OPÇÃO PARA ENVIAR OBJETO PELO INTENT É O Parcelable!!!
@RealmClass
public class Offering  extends RealmObject implements Serializable {
//public class Offering  extends RealmObject{
    private static final long  serialVersionUID = 100L;
    @PrimaryKey
    private int pk;
    private int semestre;
    private int week;
    private int time;
    private int period;
    private int ano;
    private int qtd;
    private int id_professor;
    private int id_disciplina;
    private boolean is_active;
    private String dataInicio;
    private String descricao;
    private boolean checked;
    private String professor;
    private int id_curso;
    private int id_user;
    private String sigla;
    @JsonAdapter(MyGsonBuilder.JsonAdapter.class)
    private RealmList<RealmInteger> alunos;

    public Offering(){
        pk = 0;
        semestre = 0;
        week = 0;
        time  = 0;
        period  = 0;
        ano  = 0;
        qtd = 0;
        id_professor = 0;
        id_disciplina = 0;
        is_active = false;
        dataInicio = "";
        descricao = "";
        checked = false;
        professor = "";
        id_curso = 0;
        id_user = 0;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getId_professor() {
        return id_professor;
    }

    public void setId_professor(int id_professor) {
        this.id_professor = id_professor;
    }

    public int getId_disciplina() {
        return id_disciplina;
    }

    public void setId_disciplina(int id_disciplina) {
        this.id_disciplina = id_disciplina;
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

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getId_curso() {
        return id_curso;
    }

    public void setId_curso(int id_curso) {
        this.id_curso = id_curso;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public RealmList<RealmInteger> getAlunos() {
        return alunos;
    }

    public void setAlunos(RealmList<RealmInteger> alunos) {
        this.alunos = alunos;
    }

    public void addAluno(int valor){
        if (alunos == null){
            this.alunos = new RealmList<RealmInteger>();
        }
        RealmInteger val = new RealmInteger(valor);
        this.alunos.add(val);
    }
}

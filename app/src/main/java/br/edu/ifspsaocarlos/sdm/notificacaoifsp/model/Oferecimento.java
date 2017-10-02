package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

/**
 * Created by rapha on 9/28/2016.
 */
//OUTRA OPÇÃO PARA ENVIAR OBJETO PELO INTENT É O Parcelable!!!
public class Oferecimento implements Serializable {
    private static final long  serialVersionUID = 100L;
    private String sigla;

    private int semestre;
    private int week;
    private int time;
    private int period;
    private int ano;
    private int qtd;
    private int id_professor;
    private int id_disciplina;
    private String dataInicio;

    public Oferecimento(){
        semestre = 0;
        week = 0;
        time  = 0;
        period  = 0;
        ano  = 0;
        qtd = 0;
        id_professor = 0;
        id_disciplina = 0;
        dataInicio = "";

        sigla = "";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
}

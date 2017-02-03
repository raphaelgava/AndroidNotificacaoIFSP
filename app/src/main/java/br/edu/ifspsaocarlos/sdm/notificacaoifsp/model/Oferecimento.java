package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.util.Date;

/**
 * Created by rapha on 9/28/2016.
 */
public class Oferecimento {
    private Date data;
    private String sigla;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}

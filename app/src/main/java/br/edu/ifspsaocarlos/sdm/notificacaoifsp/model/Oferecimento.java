package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rapha on 9/28/2016.
 */
//OUTRA OPÇÃO PARA ENVIAR OBJETO PELO INTENT É O Parcelable!!!
public class Oferecimento implements Serializable {
    public static final long  serialVersionUID = 100L;
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

    public String getDataString(){
        //java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yy");
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(this.data);
    }
}

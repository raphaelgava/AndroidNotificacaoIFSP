package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import java.io.Serializable;

/**
 * Created by rapha on 3/15/2017.
 */

public class Remetente implements Serializable {
    public static final long  serialVersionUID = 100L;
    private int code;
    private String description;
    private boolean checked;

    public Remetente() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrption) {
        this.description = descrption;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

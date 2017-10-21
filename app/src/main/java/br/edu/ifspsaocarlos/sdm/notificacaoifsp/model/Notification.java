package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import android.util.Log;

import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.MyGsonBuilder;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 10/11/2017.
 */
@RealmClass
public class Notification extends RealmObject implements Serializable {
    //public class Offering  extends RealmObject{
    private static final long  serialVersionUID = 100L;
    @PrimaryKey
    private int pk;
    private String datahora;
    private int id_tipo;
    private Integer id_local;
    private String descricao;
    private String titulo;
    private int servidor;
    private int id_user;
    @JsonAdapter(MyGsonBuilder.JsonAdapter.class)
    private RealmList<RealmInteger> remetente;

    public Notification(){
        pk = 0;
        id_tipo = 0;
        descricao = "";
        titulo = "";
        servidor = 0;
        id_local = null;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public Date getDatahora() {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try{
            d = formatDate.parse(datahora);
        }catch (Exception e){
            Log.d("TCC", "Error to parse date: " + e.toString());
        }
        return d;
        //return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public Integer getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getServidor() {
        return servidor;
    }

    public void setServidor(int servidor) {
        this.servidor = servidor;
    }

    public RealmList<RealmInteger> getRemetente() {
        return remetente;
    }

    public void setRemetente(RealmList<RealmInteger> remetente) {
        this.remetente = remetente;
    }

    public void addRemetent(int valor){
        if (remetente == null){
            this.remetente = new RealmList<RealmInteger>();
        }
        RealmInteger val = new RealmInteger(valor);
        this.remetente.add(val);
    }

    public void clearRemetent(){
        RealmList<RealmInteger> list = getRemetente();
        if (list != null){
            list.clear();
        }
    }

    public void removeRemetent(int valor){
        RealmInteger val = new RealmInteger(valor);
        RealmList<RealmInteger> list = getRemetente();
        for (int i = 0; i < list.size(); i++){
            RealmInteger a = list.get(i);
            if (a.getPk() == val.getPk()){
                list.remove(i);
                if (i > 0)
                    i--;
            }
        }
    }

    public int getSize(){
        if (remetente == null){
            this.remetente = new RealmList<RealmInteger>();
        }
        return getRemetente().size();
    }

    public void clear(){
        if (remetente != null){
            remetente.clear();
        }
    }
}

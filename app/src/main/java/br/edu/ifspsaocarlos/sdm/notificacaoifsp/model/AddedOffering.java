package br.edu.ifspsaocarlos.sdm.notificacaoifsp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by rapha on 10/5/2017.
 */
@RealmClass
public class AddedOffering extends RealmObject{
    @PrimaryKey
    private String myPk;
    private int pk;
    private Offering offer;
    private int username;
    public AddedOffering(){


    }

    public AddedOffering(Offering offer) {
        if (offer != null) {
            this.offer = offer;
            this.myPk = offer.getMyPk();
            this.pk = offer.getPk();
            this.username = offer.getId_user();
        }
    }

    public String getMyPk() {
        return myPk;
    }

    public int getPk() {
        return pk;
    }

    public Offering getOffer() {
        return offer;
    }

    public void setOffer(Offering offer) {
        if (offer != null) {
            this.offer = offer;
            this.pk = offer.getPk();
            this.myPk = offer.getMyPk();
            this.username = offer.getId_user();
        }
    }
}

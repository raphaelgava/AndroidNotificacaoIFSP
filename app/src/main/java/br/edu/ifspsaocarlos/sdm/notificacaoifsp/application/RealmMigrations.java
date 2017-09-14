package br.edu.ifspsaocarlos.sdm.notificacaoifsp.application;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by rapha on 8/15/2017.
 */

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            // changes from version 1 to version 2
            final RealmObjectSchema userSchema = schema.get("UserLogin");
            if (!userSchema.hasField("id")) {
                userSchema.addField("id", int.class);
            }
            userSchema.addPrimaryKey("id");

            //userSchema.addField("age", int.class);

            oldVersion++;
        }

        if (oldVersion == 2) { // TODO: 9/14/2017 esta com problema ao acessar esse migration
            /*
            final RealmObjectSchema userSchema = schema.get("UserLogin");

            if (!userSchema.hasField("lastUpdate")){
                userSchema.addField("lastUpdate", Date.class);
            }
*/
            oldVersion++;

        }
    }
}

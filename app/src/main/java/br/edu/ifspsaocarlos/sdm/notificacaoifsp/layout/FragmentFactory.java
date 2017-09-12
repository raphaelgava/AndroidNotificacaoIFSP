package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;

/**
 * Created by rapha on 7/2/2017.
 */

public class FragmentFactory {

    public FragmentFactory(){

    }

    public static TemplateFragment CreateFragment(Context context, Bundle args, int fragType){
        if (fragType == R.id.nav_class_schedule) {
            return GridNotificationsFragment.newInstance(context, args);
        } else if (fragType == R.id.nav_user_data) {
            return ChangeUserDataFragment.newInstance(context, args);
        } else if (fragType == R.id.nav_notification) {
            return CreateNotificationFragment.newInstance(context, args);
        }
        Log.d("TCC", "Não foi encontrado o fragmento ao ser criado");
        return null;
    }
}

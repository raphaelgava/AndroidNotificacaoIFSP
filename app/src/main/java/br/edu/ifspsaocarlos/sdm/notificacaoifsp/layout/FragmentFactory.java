package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.ServiceState;

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
        } else if (fragType == R.id.nav_create_notification) {
            ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_TIPO_NOTIFICACAO);
            ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_LOCAL);
            return CreateNotificationFragment.newInstance(context, args);
        }
        Log.d("TCC", "Não foi encontrado o fragmento ao ser criado");
        return null;
    }
}

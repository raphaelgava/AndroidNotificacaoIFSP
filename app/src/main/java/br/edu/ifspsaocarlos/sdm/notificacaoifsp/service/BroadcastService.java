package br.edu.ifspsaocarlos.sdm.notificacaoifsp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rapha on 11/22/2017.
 */

public class BroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TCC", "Broadcast starting Service!");
        context.startService(new Intent(context.getApplicationContext(), FetchJSONService.class));
    }
}

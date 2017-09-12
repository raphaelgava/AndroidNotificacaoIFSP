package br.edu.ifspsaocarlos.sdm.notificacaoifsp.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by rapha on 7/4/2016.
 */
public class Connection {
    //check before any change or send notification if there is connection

    /*EXAMPLE
    private boolean sendMessageIsAble(String message) {
        if (Connection.connectionVerify(this)) {
            if (message.length() > 0) {
                return true;
            }
        } else {
            Toast.makeText(this, "No momento não há conexão.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
     */
    public static boolean connectionVerify(Context context) {
        boolean conectado = false;
        if (context != null) {
            ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conectivtyManager.getActiveNetworkInfo() != null
                    && conectivtyManager.getActiveNetworkInfo().isAvailable()
                    && conectivtyManager.getActiveNetworkInfo().isConnected()) {
                conectado = true;
            }
        }
        return conectado;
    }
}

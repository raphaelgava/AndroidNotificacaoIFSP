package br.edu.ifspsaocarlos.sdm.notificacaoifsp.util;

import java.util.Stack;

/**
 * Created by rapha on 7/11/2017.
 */

public class ServiceState {

    private Stack<EnumServiceState> states;
    private boolean flagStateEmpty; //@todo: verificar se a assincronia não vai gerar problema. se der problema então gerar uma flag working (só chama a proxima atividade se finalizou a última)
    private static ServiceState instance;


    public static ServiceState getInstance(){
        if (instance == null){
            instance = new ServiceState();
        }
        return instance;
    }

    private ServiceState(){
        states = new Stack<>();
        flagStateEmpty = true;
    }

    public void pushState(EnumServiceState state){
        states.push(state);
    }

    public EnumServiceState popState(){
        //flagStateEmpty = false;
        return states.pop();
    }

    public void finishLastPop(){
        flagStateEmpty = true;
    }

    public boolean hasItemEnabled(){
        //return (states.size() > 0) && (flagStateEmpty == true);
        return (states.size() > 0);
    }

    public enum EnumServiceState {
        ENUM_USER, //User data
        ENUM_NOTIFICATION,
        ENUM_OFERECIMENTO
    }
}

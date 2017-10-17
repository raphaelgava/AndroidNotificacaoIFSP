package br.edu.ifspsaocarlos.sdm.notificacaoifsp.util;

import java.util.Stack;

/**
 * Created by rapha on 7/11/2017.
 */

public class ServiceState {

    private Stack<EnumServiceState> states;
    private static boolean flagStateEmpty; //@todo: verificar se a assincronia não vai gerar problema. se der problema então gerar uma flag working (só chama a proxima atividade se finalizou a última)
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
        flagStateEmpty = false;
        return states.pop();
    }

    public static void finishLastPop(){
        flagStateEmpty = true;
    }

    public boolean hasItemEnabled(){
        return (states.size() > 0) && (flagStateEmpty == true);
        //return (states.size() > 0);
    }

    public enum EnumServiceState {
        ENUM_USER, //User data
        ENUM_NOTIFICATION,
        ENUM_OFERECIMENTO,
        ENUM_REMETENTE,
        ENUM_INSERT_STUDENT_OFFERING,
        ENUM_INSERT_NOTIFICATION,
        ENUM_REMOVE_STUDENT_OFFERING,
        ENUM_TIPO_NOTIFICACAO,
        ENUM_LOCAL
    }
}

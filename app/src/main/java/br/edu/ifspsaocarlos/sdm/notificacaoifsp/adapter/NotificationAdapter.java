package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MapsActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import io.realm.Realm;

/**
 * Created by rapha on 3/15/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder> {

    private List<Notification> mListaNotifications;
    private List<Notification> allItems;


    public NotificationAdapter(List<Notification> listaNotifiations) {
        this.mListaNotifications = listaNotifiations;
        this.allItems = listaNotifiations;
    }

    private Filter filter;
    boolean running;
    public Filter getFilter() {
        final Handler handler = new Handler();
        running = true;

        filter = new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = allItems.size();
                    results.values = allItems;
                } else {
                    final CharSequence finalFiltro = filtro.toString().toLowerCase();
                    new Thread(){
                        public void run() {
                            while (running) {
                                try {
                                    Thread.sleep(10);
                                    handler.post(new Runnable() {
                                        public void run() {
                                            mListaNotifications = new ArrayList<Notification>();
                                            for (int i = 0; i < allItems.size(); i++) {
                                                Notification data = allItems.get(i);

                                                String condicaoD = data.getDescricao().toLowerCase();
                                                String condicaoT = data.getTitulo().toLowerCase();

                                                if ((condicaoD.contains(finalFiltro)) || (condicaoT.contains(finalFiltro))) {
                                                    //se conter adiciona na lista de itens filtrados.
                                                    mListaNotifications.add(data);
                                                }
                                            }
                                            running = false;
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }.start();

                    while (running){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Define o resultado do filtro na variavel FilterResults
                    results.count = mListaNotifications.size();
                    results.values = mListaNotifications;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                mListaNotifications = (List<Notification>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }

//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @Override
//            protected FilterResults performFiltering(CharSequence filtro) {
//                FilterResults results = new FilterResults();
//                //se não foi realizado nenhum filtro insere todos os itens.
//                if (filtro == null || filtro.length() == 0) {
//                    results.count = allItems.size();
//                    results.values = allItems;
//                } else {
//                    mListaNotifications = new ArrayList<Notification>();
//                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
//                    for (int i = 0; i < allItems.size(); i++) {
//                        Notification data = allItems.get(i);
//
//                        filtro = filtro.toString().toLowerCase();
//                        String condicao = data.getDescricao().toLowerCase();
//
//                        if (condicao.contains(filtro)) {
//                            //se conter adiciona na lista de itens filtrados.
//                            mListaNotifications.add(data);
//                        }
//                    }
//                    // Define o resultado do filtro na variavel FilterResults
//                    results.count = mListaNotifications.size();
//                    results.values = mListaNotifications;
//                }
//                return results;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                mListaNotifications = (List<Notification>) results.values; // Valores filtrados.
//                notifyDataSetChanged();  // Notifica a lista de alteração
//            }
//
//        };
//        return filter;
//    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new ItemViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //Recuperar o objeto da lista
        Notification notification = mListaNotifications.get(position);

        if (notification != null) {
            //Setar os valores conforme a grid faz scroll
            holder.txtType.setText(notification.getTitulo());
            holder.txtDescription.setText(notification.getDescricao());
            //set position to identify which object was selected
            holder.mLayoutPrincipal.setTag(position);

            if (notification.isChecked()){
                holder.imgStatus.setImageResource(R.drawable.opened2);
            }else{
                holder.imgStatus.setImageResource(R.drawable.closed2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListaNotifications.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutPrincipal;
        private TextView txtType;
        private TextView txtDescription;
        private ImageView imgStatus;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtType = (TextView) itemView.findViewById(R.id.txtNotificationType);
            txtDescription = (TextView) itemView.findViewById(R.id.txtNotificaitonDesc);

            mLayoutPrincipal = (RelativeLayout) itemView.findViewById(R.id.llNotificationList);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(v);
                }
            });

            imgStatus = (ImageView) itemView.findViewById(R.id.img_status);
        }

        private void selectItem(View v) {
            Notification object = mListaNotifications.get((Integer) v.getTag());
            if(object != null)
            {
                Intent notificationIntent = new Intent(v.getContext(), MapsActivity.class);

                Gson gson = new Gson();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
//                object.setChecked(true);
                Notification newObject = realm.copyToRealmOrUpdate(object);
                String json = gson.toJson(realm.copyFromRealm(newObject));
                notificationIntent.putExtra("notificacao", json);
//                realm.commitTransaction();
                realm.cancelTransaction();

                //Não pode inserir essa opção aqui pois se cancelar o pedido de GPS vai fechar a app!!!
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                v.getContext().startActivity(notificationIntent);
            }
        }
    }
}

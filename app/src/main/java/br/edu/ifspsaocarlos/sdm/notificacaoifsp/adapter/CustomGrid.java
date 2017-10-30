package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MapsActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.GridNotificationsFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.AddedOffering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.RealmInteger;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.MyGsonBuilder;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.ServiceState;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class CustomGrid extends RecyclerView.Adapter<CustomGrid.ItemViewHolder> {

    private List<Offering> mListaOferecimentos;
    private Point windowSize;
    private int position;
    private final double INDEX_COLUMN = 0.20;
    private GridNotificationsFragment frag;


    public CustomGrid(List<Offering> listaOferecimentos, Point size, int position, GridNotificationsFragment grid) {
        this.mListaOferecimentos = listaOferecimentos;
        this.windowSize = size;
        this.position = position;
        this.frag = grid;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_grid, parent, false);
        return new ItemViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //Recuperar o objeto da lista
        Offering offer = mListaOferecimentos.get(position);

        //Setar os valores conforme a grid faz scroll
        holder.txtSigla.setText(offer.getSigla());
        String nome =  offer.getProfessor();
        int endIndex = nome.indexOf(' ');
        if (endIndex > 0)
            nome = nome.substring(0, endIndex);
        holder.txtProfessor.setText(nome);
        holder.mLayoutPrincipal.setTag(position);

        holder.mLayoutPrincipal.setBackgroundResource(R.drawable.shape_color);

//        Drawable shape;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            shape = holder.mLayoutPrincipal.getResources().getDrawable(R.drawable.shape_color, null);
//        }
//        else
//        {
//            shape = holder.mLayoutPrincipal.getResources().getDrawable(R.drawable.shape_color);
//        }
//
//        Log.d("TCC", "index" + position);
//        if ((position % GridNotificationsFragment.NUMBER_COLUMN == 0) ||
//                (position % GridNotificationsFragment.NUMBER_COLUMN == 2) ||
//                (position % GridNotificationsFragment.NUMBER_COLUMN == 4)){
//            //holder.mLayoutPrincipal.setBackgroundResource(R.color.colorCell);
//            holder.mLayoutPrincipal.setBackgroundResource(R.drawable.shape_color);
//            //shape.setColorFilter(holder.mLayoutPrincipal.getResources().getColor(R.color.colorCell), PorterDuff.Mode.SRC_IN);
//            //holder.mLayoutPrincipal.setBackgroundColor(0xAA32CD32);
//            Log.d("TCC", "Green" + position);
//        }
//        else{
//            //holder.mLayoutPrincipal.setBackgroundColor(Color.WHITE);
//            //holder.mLayoutPrincipal.setBackgroundResource(R.drawable.shape_border);
//            shape.setColorFilter(holder.mLayoutPrincipal.getResources().getColor(R.color.colorOptionMap), PorterDuff.Mode.SRC_IN);
//            Log.d("TCC", "White" + position);
//        }
//        holder.mLayoutPrincipal.setBackground(shape);
    }

    @Override
    public int getItemCount() {
        return mListaOferecimentos.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLayoutPrincipal;
        private TextView txtSigla;
        private TextView txtProfessor;
        //private CardView card;
        private LinearLayout cell;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
            txtProfessor = (TextView) itemView.findViewById(R.id.tv_professor);
            mLayoutPrincipal = (LinearLayout) itemView.findViewById(R.id.cell_layout2);

            /* //PEGAR ALTURA DA VIEW
            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;

            if (itemView.getContext().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,itemView.getResources().getDisplayMetrics());
            }
            */

            int height;
            int width;

            height = (int) ((windowSize.y - position) * INDEX_COLUMN);
            width = (int) (windowSize.x * INDEX_COLUMN);

            cell = (LinearLayout) itemView.findViewById(R.id.cell_layout2);
            cell.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Offering offer = mListaOferecimentos.get((Integer) v.getTag());

                    if ((offer != null) && (offer.getPk() > 0)){
                        Gson gson = MyGsonBuilder.getInstance().myGson();
                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = Calendar.getInstance().getTime();
                        Realm realm = Realm.getDefaultInstance();
                        //RealmResults<Notification> object = realm.where(Notification.class).equalTo("pk", offer.getPk()).findAllSorted("datahora", Sort.DESCENDING);
                        RealmResults<Notification> stepEntryResults = realm.where(Notification.class).
                                equalTo("id_user", MainActivity.getUserId()).findAll();
                        List<Notification> messageList = realm.copyFromRealm(stepEntryResults);
                        List<Notification> finalList = new ArrayList<Notification>();

                        for (Notification noti : messageList){
                            if (d.before(noti.getDatahora())){
                                RealmList<RealmInteger> list = noti.getRemetente();
                                for (RealmInteger integer : list){
                                    if (integer.getPk() == offer.getPk()) {
                                        finalList.add(noti);
                                        break;
                                    }
                                }
                            }
                        }

                        if (finalList.size() > 0) {
                            Collections.sort(finalList, new Comparator<Notification>(){
                                public int compare(Notification obj1, Notification obj2) {
                                    // ## Ascending order
                                    // return obj1.firstName.compareToIgnoreCase(obj2.firstName);  // To compare date values
                                    // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                                    // ## Descending order
                                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                                    // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values

                                    return obj1.getDatahora().compareTo(obj2.getDatahora()); // To compare date values
                                }
                            });

                            String json = gson.toJson(finalList.get(0));

                            Intent intent = new Intent(v.getContext(), MapsActivity.class);
                            intent.putExtra("notificacao", json);

                            //Não pode inserir essa opção aqui pois se cancelar o pedido de GPS vai fechar a app!!!
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            v.getContext().startActivity(intent);
                        }
                        else{
                            Toast.makeText(v.getContext(), "There is no notification related to this offering",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }


//                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
//                    intent.putExtra("oferecimento", offer);
//                    //Não pode inserir essa opção aqui pois se cancelar o pedido de GPS vai fechar a app!!!
//                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    v.getContext().startActivity(intent);



//                    Oferecimento offer = mListaOferecimentos.get((Integer) v.getTag());
//
//                    //Layout customizado na Dialog
//                    Context c = v.getContext();
//                    LayoutInflater l = LayoutInflater.from(c);
//                    final View view = l.inflate(R.layout.layout_dialog, null, false);
//                    //final View view = LayoutInflater.from(c).inflate(R.layout.layout_dialog, null, false);
//                    TextView texto = (TextView) view.findViewById(R.id.edtParametro);
//                    texto.setText(offer.getSigla());
//
//                    Button botao = (Button) view.findViewById(R.id.btnDialog);
//
//                    final Dialog d = new Dialog(v.getContext());
//                    d.setContentView(view);
//                    d.setCancelable(false); //Modal behavior
//                    d.show();
//                    botao.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            d.dismiss();
//                        }
//                    });
                }
            });

            mLayoutPrincipal.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    final Offering offer = mListaOferecimentos.get((Integer) v.getTag());
                    if (offer.getPk() > 0) {
                        new AlertDialog.Builder(v.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Removing Offering")
                                .setMessage("Are you sure you want to remove?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            //                                        final Realm realm = Realm.getDefaultInstance();
                                            //                                        realm.beginTransaction();
                                            //                                        AddedOffering deleteOffer = realm.where(AddedOffering.class).equalTo("pk", offer.getPk()).findFirst();
                                            //                                        if (deleteOffer != null) {
                                            //                                            deleteOffer.deleteFromRealm();
                                            //                                            frag.configurarAdapter();
                                            //                                        }
                                            //                                        realm.commitTransaction();

                                            Realm realm = Realm.getDefaultInstance();
                                            realm.executeTransactionAsync(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm bgRealm) {
                                                    Offering deleteOffer = bgRealm.where(Offering.class).equalTo("pk", offer.getPk()).findFirst();
                                                    if (deleteOffer != null) {
                                                        deleteOffer.removeAluno(MainActivity.getUserId());
                                                        AddedOffering deletedObj = new AddedOffering(deleteOffer);
                                                        bgRealm.copyToRealmOrUpdate(deletedObj);
                                                        //deleteOffer.deleteFromRealm();

//                                                        final Handler handler = new Handler();
//                                                        new Thread(){
//                                                            public void run() {
//                                                                boolean start = true;
//                                                                while (start) {
//                                                                    try {
//                                                                        Thread.sleep(1000);
//                                                                        if (FetchJSONService.getUpdateGrid() == true){
//                                                                            handler.post(new Runnable() {
//                                                                                public void run() {
//                                                                                    frag.configurarAdapter();
//                                                                                }
//                                                                            });
//                                                                            start = false;
//                                                                        }
//                                                                    } catch (InterruptedException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                }
//                                                            }
//                                                        }.start();

                                                        FetchJSONService.setOffering(deletedObj, ServiceState.EnumServiceState.ENUM_REMOVE_STUDENT_OFFERING);
                                                    }
                                                }
                                            }, new Realm.Transaction.OnSuccess() {
                                                @Override
                                                public void onSuccess() {

                                                    Log.d("TCC", "Offering removed");
                                                }
                                            }, new Realm.Transaction.OnError() {
                                                @Override
                                                public void onError(Throwable error) {
                                                    Log.d("TCC", "Error to insert removed: " + error.toString());
                                                }
                                            });
                                        } catch (Exception e) {
                                            Log.d("TCC", "Error to removed Offering: " + e.toString());
                                        }
                                        //Toast.makeText(v.getContext(), "Long click!", Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    return true;
                }

            });
        }
    }
}

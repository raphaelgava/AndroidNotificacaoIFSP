package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.GridNotificationsFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Oferecimento;

public class CustomGrid extends RecyclerView.Adapter<CustomGrid.ItemViewHolder> {

    private List<Oferecimento> mListaOferecimentos;


    public CustomGrid(List<Oferecimento> listaOferecimentos) {
        this.mListaOferecimentos = listaOferecimentos;
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
        Oferecimento offer = mListaOferecimentos.get(position);

        //Setar os valores conforme a grid faz scroll
        holder.txtSigla.setText(offer.getSigla());
        holder.txtDate.setText(offer.getDataString());
        holder.mLayoutPrincipal.setTag(position);

        Log.d("TCC", "index" + position);
        if ((position % GridNotificationsFragment.NUMBER_COLUMN == 0) ||
                (position % GridNotificationsFragment.NUMBER_COLUMN == 2) ||
                (position % GridNotificationsFragment.NUMBER_COLUMN == 4)){
            holder.mLayoutPrincipal.setBackgroundColor(0xAA32CD32);
            Log.d("TCC", "Green" + position);
        }
        else{
            holder.mLayoutPrincipal.setBackgroundColor(Color.WHITE);
            Log.d("TCC", "Blue" + position);
        }
    }

    @Override
    public int getItemCount() {
        return mListaOferecimentos.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLayoutPrincipal;
        private TextView txtSigla;
        private TextView txtDate;


        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
            txtDate = (TextView) itemView.findViewById(R.id.tv_data);
            mLayoutPrincipal = (LinearLayout) itemView.findViewById(R.id.cell_layout2);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Oferecimento offer = mListaOferecimentos.get((Integer) v.getTag());
                    //Layout customizado na Dialog
                    final View view = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_dialog, null, false);
                    TextView texto = (TextView) view.findViewById(R.id.edtParametro);
                    texto.setText(offer.getSigla());

                    Button botao = (Button) view.findViewById(R.id.btnDialog);

                    final Dialog d = new Dialog(v.getContext());
                    d.setContentView(view);
                    d.show();
                    botao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });

                }
            });
        }
    }

//public class CustomGrid extends List<Oferecimento> {
//    private Context mContext;
//    //    private final String[] web;
////    private final int[] Imageid;
//    private LayoutInflater inflador;
//
//    public CustomGrid(Activity tela, List<Oferecimento> listaOferecimentos) {
//        super(tela, R.layout.celula_grid, listaOferecimentos);
//        inflador = (LayoutInflater) tela.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if ( convertView == null ) {
//            // infla uma nova célula
//            convertView = inflador.inflate(R.layout.celula_grid, null);
//            holder = new ViewHolder();
//            holder.data = (TextView) convertView.findViewById(R.id.tv_data);
//            holder.sigla = (TextView) convertView.findViewById(R.id.tv_sigla);;
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        Oferecimento ofer = getItem(position);
//        holder.data.setText(ofer.getData().toString());
//        holder.sigla.setText(ofer.getSigla());
//        return convertView;
//    }
//
//    static class ViewHolder {
//        public TextView data;
//        public TextView sigla;
//    }





//        public CustomGrid(Context c,String[] web,int[] Imageid ) {
//            mContext = c;
//            this.Imageid = Imageid;
//            this.web = web;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            View grid;
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            if (convertView == null) {
//                grid = new View(mContext);
//                grid = inflater.inflate(R.layout.celula_grid, null);
//                TextView data = (TextView) grid.findViewById(R.id.tv_data);
//                TextView oferecimento = (TextView)grid.findViewById(R.id.tv_oferecimento);
//                data.setText(web[position]);
//                oferecimento.setText(Imageid[position]);
//            } else {
//                grid = (View) convertView;
//            }
//
//            return grid;
//        }
}

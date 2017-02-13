package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
            holder.mLayoutPrincipal.setBackgroundResource(R.color.colorCell);
            //holder.mLayoutPrincipal.setBackgroundColor(0xAA32CD32);
            Log.d("TCC", "Green" + position);
        }
        else{
            holder.mLayoutPrincipal.setBackgroundColor(Color.WHITE);
            Log.d("TCC", "White" + position);
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
                    d.setCancelable(false); //Modal behavior
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
}

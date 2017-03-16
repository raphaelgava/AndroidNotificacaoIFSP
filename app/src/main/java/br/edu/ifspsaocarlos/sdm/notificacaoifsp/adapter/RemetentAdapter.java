package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.RemetentListActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Remetente;

/**
 * Created by rapha on 3/15/2017.
 */

public class RemetentAdapter extends RecyclerView.Adapter<RemetentAdapter.ItemViewHolder> {

    private List<Remetente> mListaRemetentes;


    public RemetentAdapter(List<Remetente> listaOferecimentos) {
        this.mListaRemetentes = listaOferecimentos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_remetent, parent, false);
        return new ItemViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //Recuperar o objeto da lista
        Remetente remetente = mListaRemetentes.get(position);

        //Setar os valores conforme a grid faz scroll
        holder.txtCode.setText(Integer.toString((remetente.getCode())));
        holder.txtDescription.setText(remetente.getDescrption());
        holder.mLayoutPrincipal.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mListaRemetentes.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLayoutPrincipal;
        private TextView txtCode;
        private TextView txtDescription;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtRemetentCode);
            txtDescription = (TextView) itemView.findViewById(R.id.txtRemetentDesc);
            mLayoutPrincipal = (LinearLayout) itemView.findViewById(R.id.llRemetentList);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Remetente remetente = mListaRemetentes.get((Integer) v.getTag());

                    Intent intent = new Intent(v.getContext(), RemetentListActivity.class);
                    intent.putExtra("remetente", remetente);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

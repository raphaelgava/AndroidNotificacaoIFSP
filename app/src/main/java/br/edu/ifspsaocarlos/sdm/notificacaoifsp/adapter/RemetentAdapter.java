package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.RemetentListActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Remetente;

/**
 * Created by rapha on 3/15/2017.
 */

public class RemetentAdapter extends RecyclerView.Adapter<RemetentAdapter.ItemViewHolder> {

    private List<Remetente> mListaRemetentes;
    private List<Remetente> allItems;


    public RemetentAdapter(List<Remetente> listaRemententes) {
        this.mListaRemetentes = listaRemententes;
        this.allItems = listaRemententes;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = allItems.size();
                    results.values = allItems;
                } else {
                    mListaRemetentes = new ArrayList<Remetente>();
                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < allItems.size(); i++) {
                        Remetente data = allItems.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getDescription().toLowerCase();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            mListaRemetentes.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = mListaRemetentes.size();
                    results.values = mListaRemetentes;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                mListaRemetentes = (List<Remetente>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
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

        if (remetente != null) {
            //Setar os valores conforme a grid faz scroll
            holder.txtCode.setText(Integer.toString((remetente.getCode())));
            holder.txtDescription.setText(remetente.getDescription());
            holder.mLayoutPrincipal.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mListaRemetentes.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutPrincipal;
        private TextView txtCode;
        private TextView txtDescription;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtRemetentCode);
            txtDescription = (TextView) itemView.findViewById(R.id.txtRemetentDesc);
            mLayoutPrincipal = (RelativeLayout) itemView.findViewById(R.id.llRemetentList);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Remetente remetente = mListaRemetentes.get((Integer) v.getTag());
                    if (remetente != null) {
                        Intent intent = new Intent(v.getContext(), RemetentListActivity.class);
                        intent.putExtra("remetente", remetente);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}

package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
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
            holder.ckbSelected.setChecked(remetente.isChecked());
            //set position to identify which object was selected
            holder.ckbSelected.setTag(position);
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
        private CheckBox ckbSelected;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtRemetentCode);
            txtDescription = (TextView) itemView.findViewById(R.id.txtRemetentDesc);
            ckbSelected = (CheckBox) itemView.findViewById(R.id.ckbSelected);
            ckbSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //It was used onClick instead of onCheckedChanged because it is making problem when setChecked
                    selectItem(v);
                }
            });

            mLayoutPrincipal = (RelativeLayout) itemView.findViewById(R.id.llRemetentList);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(v);
                }
            });
        }

        private void selectItem(View v) {
            Remetente remetente = mListaRemetentes.get((Integer) v.getTag());
            if(remetente != null)
            {
                if (remetente.isChecked() == ckbSelected.isChecked()) {
                    //it means that layout was toched
                    ckbSelected.setChecked(!ckbSelected.isChecked());
                }
                remetente.setChecked(ckbSelected.isChecked());
            }
        }
    }
}

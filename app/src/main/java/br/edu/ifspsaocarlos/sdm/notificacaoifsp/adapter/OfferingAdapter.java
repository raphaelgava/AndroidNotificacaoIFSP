package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import io.realm.Realm;

/**
 * Created by rapha on 3/15/2017.
 */

public class OfferingAdapter extends RecyclerView.Adapter<OfferingAdapter.ItemViewHolder> {

    private List<Offering> mListaOffering;
    private List<Offering> allItems;
    private HashSet<Offering> mListChecked;


    public OfferingAdapter(List<Offering> listaOffering, HashSet<Offering> array) {
        this.mListaOffering = listaOffering;
        this.allItems = listaOffering;
        this.mListChecked = array;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = allItems.size();
                    results.values = allItems;
                } else {
                    try {
                        mListaOffering = new ArrayList<Offering>();
                        //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                        for (int i = 0; i < allItems.size(); i++) {
                            Offering data = allItems.get(i);

                            filtro = filtro.toString().toLowerCase();
                            String desc = data.getDescricao();
                            String condicao = desc.toLowerCase();

                            if (condicao.contains(filtro)) {
                                //se conter adiciona na lista de itens filtrados.
                                mListaOffering.add(data);
                            }
                        }
                        // Define o resultado do filtro na variavel FilterResults
                        results.count = mListaOffering.size();
                        results.values = mListaOffering;
                    }catch (Exception e){
                        Log.d("TCC", "Erro: " + e.toString());
                    }
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListaOffering = (List<Offering>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_offering, parent, false);
        return new ItemViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //Recuperar o objeto da lista
        Offering offering = mListaOffering.get(position);

        if (offering != null) {
            //Setar os valores conforme a grid faz scroll
            holder.txtProf.setText(offering.getProfessor());
            holder.txtDescription.setText(offering.getDescricao());
            holder.ckbSelected.setChecked(offering.isChecked());
            //set position to identify which object was selected
            holder.ckbSelected.setTag(position);
            holder.mLayoutPrincipal.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mListaOffering.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutPrincipal;
        private TextView txtProf;
        private TextView txtDescription;
        private CheckBox ckbSelected;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtProf = (TextView) itemView.findViewById(R.id.txtOfferingProf);
            txtDescription = (TextView) itemView.findViewById(R.id.txtOfferingDesc);
            ckbSelected = (CheckBox) itemView.findViewById(R.id.ckbSelectedOffering);
            ckbSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //It was used onClick instead of onCheckedChanged because it is making problem when setChecked
                    selectItem(v);
                }
            });

            mLayoutPrincipal = (RelativeLayout) itemView.findViewById(R.id.llOfferingList);
            mLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(v);
                }
            });
        }

        private void selectItem(View v) {
            Offering offering = mListaOffering.get((Integer) v.getTag());
            if(offering != null)
            {
                boolean flag = ckbSelected.isChecked();

                if (v.getId() != R.id.ckbSelectedOffering){
                    ckbSelected.setChecked(!flag);
                }

                Realm.getDefaultInstance().beginTransaction();
                offering.setChecked(ckbSelected.isChecked());
                if (ckbSelected.isChecked())
                    mListChecked.add(offering);
                Realm.getDefaultInstance().cancelTransaction();
            }
        }
    }
}

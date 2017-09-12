package br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MapsActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Oferecimento;

/*
public class CustomGrid extends ArrayAdapter<Oferecimento> {
    private LayoutInflater inflador;

    public CustomGrid(Activity tela, List<Oferecimento> listaOferecimentos) {
        super(tela, R.layout.celula_grid, listaOferecimentos);
        inflador = (LayoutInflater) tela.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
// infla uma nova célula
            convertView = inflador.inflate(R.layout.celula_grid, null);
            holder = new ViewHolder();

            holder.txtSigla = (TextView) convertView.findViewById(R.id.tv_sigla);
            holder.txtDate = (TextView) convertView.findViewById(R.id.tv_data);
            //mLayoutPrincipal = (LinearLayout) itemView.findViewById(R.id.cell_layout2);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Oferecimento offer = getItem(position);
        holder.txtSigla.setText(offer.getSigla());
        holder.txtDate.setText(offer.getDataString());
        return convertView;
    }
    static class ViewHolder {
        private TextView txtSigla;
        private TextView txtDate;
    }
}
*/

public class CustomGrid extends RecyclerView.Adapter<CustomGrid.ItemViewHolder> {

    private List<Oferecimento> mListaOferecimentos;
    private Point windowSize;
    private int position;
    private final double INDEX_COLUMN = 0.20;


    public CustomGrid(List<Oferecimento> listaOferecimentos, Point size, int position) {
        this.mListaOferecimentos = listaOferecimentos;
        this.windowSize = size;
        this.position = position;
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
        private TextView txtDate;
        //private CardView card;
        private LinearLayout cell;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            txtSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
            txtDate = (TextView) itemView.findViewById(R.id.tv_data);
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
                    Oferecimento offer = mListaOferecimentos.get((Integer) v.getTag());

                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    intent.putExtra("oferecimento", offer);
                    //Não pode inserir essa opção aqui pois se cancelar o pedido de GPS vai fechar a app!!!
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    v.getContext().startActivity(intent);

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
        }
    }
}

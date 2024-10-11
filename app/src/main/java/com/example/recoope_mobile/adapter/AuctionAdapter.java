package com.example.recoope_mobile.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.BidFragment;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.utils.PtBrUtils;

import java.util.List;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {
    private List<Auction> auctions;
    private Context context;
    private final String LOG_TAG = "CardFeed";


    public AuctionAdapter(List<Auction> auctions, Context context) {
        this.auctions = auctions;
        this.context = context;
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_auction, parent, false);
        return new AuctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionViewHolder holder, int position) {
        Auction auction = auctions.get(position);

        Log.e(LOG_TAG, auction.toString());

        // Verificar se a cooperativa não é nula antes de acessar seus atributos
        if (auction.getCooperative() != null) {
            holder.auctionCoopName.setText(auction.getCooperative().getName());
        } else {
            holder.auctionCoopName.setText("Cooperativa não disponível");
        }

        // Verificar se o produto não é nulo antes de acessar seus atributos
        if (auction.getProduct() != null) {
            holder.auctionMaterial.setText(auction.getProduct().getProductType());
            holder.auctionWeight.setText(PtBrUtils.formatDate(auction.getEndDate()));
            holder.auctionPrice.setText(PtBrUtils.formatReal(auction.getProduct().getInitialValue()));

            // Carregar a imagem do leilão (produto) usando Glide, se a URL não for nula
            if (auction.getProduct().getPhoto() != null) {
                Glide.with(context)
                        .load(auction.getProduct().getPhoto())
                        .into(holder.auctionImg);
            } else {
                holder.auctionImg.setImageResource(R.drawable.glass_image); // Exemplo de imagem padrão
            }
        } else {
            holder.auctionMaterial.setText("Material não disponível");
            holder.auctionWeight.setText("Peso não disponível");
            holder.auctionPrice.setText("Preço não disponível");
            holder.auctionImg.setImageResource(R.drawable.glass_image_2); // Imagem padrão
        }

        // Preencher outras informações que não dependem de nulos
        holder.auctionDate.setText(PtBrUtils.formatDate(auction.getEndDate()));
        holder.idAuction.setText(PtBrUtils.formatId(auction.getAuctionId()));

        // Clique para ver detalhes
        holder.auctionDetailBtn.setOnClickListener(v -> {
            Log.e(LOG_TAG, "Clicado no botão detalhes (Implementar fluxo) leilao:" + auction.getAuctionId());
        });

        // Clique para participar
        holder.auctionParticipateBtn.setOnClickListener(v -> {
            // Criar um bundle para passar o auctionId
            Bundle bundle = new Bundle();
            bundle.putInt("AUCTION_ID", auction.getAuctionId());

            // Instanciar o fragmento de detalhes e passar o bundle
            BidFragment auctionDetailFragment = new BidFragment();
            auctionDetailFragment.setArguments(bundle);

            // Navegar para o fragmento de detalhes usando FragmentManager
            ((FragmentActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContent, auctionDetailFragment) // substitui pelo ID do container do fragment
                    .addToBackStack(null) // Adiciona à pilha para voltar
                    .commit();

            Log.e(LOG_TAG, "Clicado no botão detalhes leilao:" + auction.getAuctionId());
        });
    }


    @Override
    public int getItemCount() {
        return auctions.size();
    }

    public static class AuctionViewHolder extends RecyclerView.ViewHolder {
        TextView auctionCoopName, auctionDate, auctionMaterial, auctionWeight, auctionPrice, idAuction;
        ImageView auctionImg;
        Button auctionDetailBtn, auctionParticipateBtn;

        public AuctionViewHolder(@NonNull View itemView) {
            super(itemView);
            auctionImg = itemView.findViewById(R.id.auctionImg);
            idAuction = itemView.findViewById(R.id.auctionId);
            auctionCoopName = itemView.findViewById(R.id.auctionCoopName);
            auctionDate = itemView.findViewById(R.id.auctionDate);
            auctionMaterial = itemView.findViewById(R.id.auctionMaterial);
            auctionWeight = itemView.findViewById(R.id.auctionWeight);
            auctionPrice = itemView.findViewById(R.id.auctionPrice);
            auctionDetailBtn = itemView.findViewById(R.id.auctionDetailBtn);
            auctionParticipateBtn = itemView.findViewById(R.id.auctionParticipateBtn);
        }
    }
}

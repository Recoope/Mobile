package com.example.recoope_mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.model.Auction;

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

        // Defina as informações do card
        holder.auctionCoopName.setText(auction.getCooperative().getName());
        holder.auctionDate.setText(auction.getEndDate());
        holder.auctionMaterial.setText(auction.getProduct().getProductType());
        holder.auctionWeight.setText(String.valueOf(auction.getProduct().getWeight()));
        holder.auctionPrice.setText(String.valueOf(auction.getProduct().getInitialValue()));
        holder.idAuction.setText(String.valueOf(auction.getAuctionId()));

        // Carregar a imagem do leilão (produto) usando Glide
        Glide.with(context)
                .load(auction.getProduct().getPhoto()) // URL da imagem do produto
                .into(holder.auctionImg);

        // Clique para ver detalhes
        holder.auctionDetailBtn.setOnClickListener(v -> {
            Log.e(LOG_TAG, "DETALHES!");
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

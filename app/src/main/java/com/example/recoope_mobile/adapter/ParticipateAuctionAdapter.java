package com.example.recoope_mobile.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.PtBrUtils;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipateAuctionAdapter extends RecyclerView.Adapter<ParticipateAuctionAdapter.AuctionViewHolder> {
    private List<Auction> auctions;
    private Context context;
    private ApiService apiService;
    private final String LOG_TAG = "CardFeed";


    public ParticipateAuctionAdapter(List<Auction> auctions, Context context) {
        this.auctions = auctions;
        this.context = context;
        apiService = RetrofitClient.getClient(context).create(ApiService.class);
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_auction, parent, false);
        return new AuctionViewHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
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
            holder.auctionWeight.setText(PtBrUtils.formatWeight(auction.getProduct().getWeight()));
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

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.item_cancel_popup);

                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
                        String cnpj = sp.getString("cnpj", "");

                        Call call = apiService.deleteBid(cnpj, auction.getAuctionId());
                        call.enqueue(new Callback<ApiDataResponse<Auction>>() {
                            @Override
                            public void onResponse(Call<ApiDataResponse<Auction>> call, Response<ApiDataResponse<Auction>> response) {
                                if (response.code() == 200) {
                                    auctions.remove(position);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                } else {
                                    Log.e(LOG_TAG, "Error deleting auction: " + response.code());
                                    Toast.makeText(context, "Erro ao deletar leilão", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiDataResponse<Auction>> call, Throwable t) {
                            }
                        });
                    }
                });

                dialog.findViewById(R.id.x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return auctions.size();
    }

    public static class AuctionViewHolder extends RecyclerView.ViewHolder {
        TextView auctionCoopName, auctionDate, auctionMaterial, auctionWeight, auctionPrice, idAuction;
        ImageView auctionImg, deleteBtn;
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
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}

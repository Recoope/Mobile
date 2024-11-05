package com.example.recoope_mobile.adapter;

import static com.example.recoope_mobile.utils.ValidationUtils.calculateCardWidthDp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.activity.fragments.BidFragment;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.PtBrUtils;
import com.example.recoope_mobile.utils.ValidationUtils;

import java.sql.Time;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {
    private List<Auction> auctions;
    private Context context;
    private ApiService apiService;
    private final String LOG_TAG = "CardFeed";
    private int screenWidthDp;

    public AuctionAdapter(List<Auction> auctions, Context context) {
        this.auctions = auctions;
        this.context = context;
        this.apiService = RetrofitClient.getClient(context).create(ApiService.class);
        this.screenWidthDp = context.getResources().getDisplayMetrics().widthPixels; // Pega a largura da tela em pixels
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
            holder.auctionCoopName.setText(ValidationUtils.truncateString(context, auction.getCooperative().getName(), calculateCardWidthDp(context, 0.53)));
        } else {
            holder.auctionCoopName.setText("Cooperativa não disponível");
        }

        // Verificar se o produto não é nulo antes de acessar seus atributos
        if (auction.getProduct() != null) {
            holder.auctionMaterial.setText(ValidationUtils.truncateString(context, auction.getProduct().getProductType(), calculateCardWidthDp(context, 0.30)));
            holder.auctionWeight.setText(ValidationUtils.truncateString(context, PtBrUtils.formatWeight(auction.getProduct().getWeight()), calculateCardWidthDp(context, 0.35)));
            holder.auctionPrice.setText(ValidationUtils.truncateString(context, PtBrUtils.formatReal(auction.getProduct().getInitialValue()), calculateCardWidthDp(context, 0.28)));

            // Carregar a imagem do leilão (produto) usando Glide, se a URL não for nula
            if (auction.getProduct().getPhoto() != null) {
                Glide.with(context)
                        .load(auction.getProduct().getPhoto())
                        .into(holder.auctionImg);
            } else {
                holder.auctionImg.setImageResource(R.drawable.ic_launcher_background); // Exemplo de imagem padrão
            }
        } else {
            holder.auctionMaterial.setText("Material não disponível");
            holder.auctionWeight.setText("Peso não disponível");
            holder.auctionPrice.setText("Preço não disponível");
            holder.auctionImg.setImageResource(R.drawable.ic_launcher_background); // Imagem padrão
        }
        // Preencher outras informações que não dependem de nulos
        holder.auctionDate.setText(PtBrUtils.formatDate(auction.getEndDate()));
        holder.idAuction.setText(ValidationUtils.truncateString(context, PtBrUtils.formatId(auction.getAuctionId()), calculateCardWidthDp(context, 0.45)));

        // Clique para ver detalhes
        holder.auctionDetailBtn.setOnClickListener(v -> {
            Call<ApiDataResponse<AuctionDetails>> call = apiService.getAuctionDetails(auction.getAuctionId());

            call.enqueue(new Callback<ApiDataResponse<AuctionDetails>>() {
                @Override
                public void onResponse(Call<ApiDataResponse<AuctionDetails>> call, Response<ApiDataResponse<AuctionDetails>> response) {
                    AuctionDetails auctionDetails = response.body().getData();

                    Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.detail_dialog);
                    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                    int width = (int) (displayMetrics.widthPixels * 0.85); // ajusta para 85% da largura da tela

                    dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                    ((TextView) dialog.findViewById(R.id.detailAuctionId)).setText("Leilão " + ValidationUtils.truncateString(context, PtBrUtils.formatId(auctionDetails.getAuctionId()), calculateCardWidthDp(context, 0.53)));
                    ((TextView) dialog.findViewById(R.id.coopName)).setText(ValidationUtils.truncateString(context, auctionDetails.getCooperative().getName(), calculateCardWidthDp(context, 0.53)));
                    ((TextView) dialog.findViewById(R.id.remainingTime)).setText("Inicia em " + PtBrUtils.getRemaingTimeMsgPTBR(auctionDetails.getEndDate(), Time.valueOf(auctionDetails.getTime())));
                    ((TextView) dialog.findViewById(R.id.startBidPrice)).setText(ValidationUtils.truncateString(context, PtBrUtils.formatReal(auctionDetails.getProduct().getInitialValue()), calculateCardWidthDp(context, 0.3)));
                    ((TextView) dialog.findViewById(R.id.detailAuctionMaterial)).setText(ValidationUtils.truncateString(context, auctionDetails.getProduct().getProductType(), calculateCardWidthDp(context, 0.4)));
                    ((TextView) dialog.findViewById(R.id.detailAuctionWeight)).setText(ValidationUtils.truncateString(context, PtBrUtils.formatWeight(auctionDetails.getProduct().getWeight()), calculateCardWidthDp(context, 0.45)));
                    ((TextView) dialog.findViewById(R.id.detailAuctionDate)).setText(PtBrUtils.formatDate(auctionDetails.getEndDate()));
                    ((TextView) dialog.findViewById(R.id.detailAuctionHour)).setText(auctionDetails.getTime().toString());
                    ((TextView) dialog.findViewById(R.id.detailAuctionEmail)).setText(ValidationUtils.truncateString(context, auctionDetails.getCooperative().getEmail(), calculateCardWidthDp(context, 0.55)));

                    dialog.findViewById(R.id.exitButton).setOnClickListener((view) -> dialog.cancel());
                    dialog.findViewById(R.id.detailAuctionParticipateBtn).setOnClickListener((view) -> {
                        dialog.cancel();
                        openBidScreen(auctionDetails.getAuctionId());
                    });

                    dialog.show();
                }

                @Override
                public void onFailure(Call<ApiDataResponse<AuctionDetails>> call, Throwable t) {
                    Log.e(LOG_TAG, "Erro ao mostrar detalhes: " + t.getMessage());
                }
            });
            Log.e(LOG_TAG, "Clicado no botão detalhes, leilao: " + auction.getAuctionId());
        });

        // Clique para participar
        holder.auctionParticipateBtn.setOnClickListener(v -> openBidScreen(auction.getAuctionId()));
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
            auctionImg = itemView.findViewById(R.id.auctionImgItem);
            idAuction = itemView.findViewById(R.id.auctionIdItem);
            auctionCoopName = itemView.findViewById(R.id.auctionCoopNameItem);
            auctionDate = itemView.findViewById(R.id.auctionDateItem);
            auctionMaterial = itemView.findViewById(R.id.auctionMaterialItem);
            auctionWeight = itemView.findViewById(R.id.auctionWeightItem);
            auctionPrice = itemView.findViewById(R.id.auctionPriceItem);
            auctionDetailBtn = itemView.findViewById(R.id.auctionDetailBtn);
            auctionParticipateBtn = itemView.findViewById(R.id.auctionParticipateBtn);
        }
    }

    public void openBidScreen(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("AUCTION_ID", id);
        BidFragment auctionDetailFragment = new BidFragment();
        auctionDetailFragment.setArguments(bundle);
        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContent, auctionDetailFragment)
                .addToBackStack(null)
                .commit();
        Log.e(LOG_TAG, "Clicado no botão participar, leilao: " + id);
    }
}


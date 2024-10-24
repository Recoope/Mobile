package com.example.recoope_mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;

import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.PtBrUtils;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<Payment> payments;
    private Context context;
    private final String LOG_TAG = "CardFeed";
    private ApiService apiService;


    public PaymentAdapter(List<Payment> payments, Context context) {
        this.payments = payments;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);

        Log.e(LOG_TAG, payment.toString());

        if (payment != null) {
            holder.txtNumPayment.setText("Recibo N°" + payment.getId());
            holder.txtNamePayment.setText(payment.getCooperativeName());
            holder.txtDatePayment.setText(PtBrUtils.formatDate(PtBrUtils.parseDate(payment.getEmissionDate())));
            holder.txtValuePayment.setText(PtBrUtils.formatReal(payment.getAmount()));

        } else {
            holder.txtNumPayment.setText("999+");
            holder.txtNamePayment.setText("999+");
            holder.txtDatePayment.setText("--/--/----");
            holder.txtValuePayment.setText("999+");
        }

        // Clique para ver detalhes
        holder.itemView.setOnClickListener(v -> {
            DialogUtils.showPaymentDialog(payment, v.getContext());
        });
    }



    @Override
    public int getItemCount() {
        return payments.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumPayment, txtDatePayment, txtNamePayment, txtValuePayment;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumPayment = itemView.findViewById(R.id.txtNumPayment);
            txtDatePayment = itemView.findViewById(R.id.txtDatePaymentD);
            txtNamePayment = itemView.findViewById(R.id.txtNamePayment);
            txtValuePayment = itemView.findViewById(R.id.txtValuePayment);
        }
    }
}

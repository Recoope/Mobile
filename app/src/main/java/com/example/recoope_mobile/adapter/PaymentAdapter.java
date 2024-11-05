package com.example.recoope_mobile.adapter;

import static com.example.recoope_mobile.utils.ValidationUtils.calculateCardWidthDp;

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
import com.example.recoope_mobile.utils.ValidationUtils;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<Payment> payments;
    private Context context;
    private final String LOG_TAG = "CardFeed";
    private ApiService apiService;
    private int screenWidthDp; // Variável para armazenar a largura da tela

    public PaymentAdapter(List<Payment> payments, Context context, int screenWidthDp) {
        this.payments = payments;
        this.context = context;
        this.screenWidthDp = screenWidthDp; // Inicializa a largura da tela
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
            holder.txtNumPayment.setText(ValidationUtils.truncateString(context, "Recibo N°" + payment.getId(), calculateCardWidthDp(context, 0.75)));
            holder.txtNamePayment.setText(ValidationUtils.truncateString(context, payment.getCooperativeName(), calculateCardWidthDp(context, 0.7)));
            holder.txtDatePayment.setText(PtBrUtils.formatDate(PtBrUtils.parseDate(payment.getEmissionDate())));
            holder.txtValuePayment.setText(ValidationUtils.truncateString(context, PtBrUtils.formatReal(payment.getAmount()), calculateCardWidthDp(context, 0.53)));

        } else {
            holder.txtNumPayment.setText("999+");
            holder.txtNamePayment.setText("999+");
            holder.txtDatePayment.setText("--/--/----");
            holder.txtValuePayment.setText("999+");
        }

        // Clique para ver detalhes
        holder.itemView.setOnClickListener(v -> {
            DialogUtils.showPaymentDialog(payment, v.getContext(), screenWidthDp);
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

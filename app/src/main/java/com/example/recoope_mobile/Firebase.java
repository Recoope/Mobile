package com.example.recoope_mobile;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.http.GET;

public class Firebase {
    String url = "https://recoopeapi.onrender.com/";
    String routeProduct = "";

    String routeAddress = "";
    String routeCompany = "";
    String routeMove = "";
    String routeAuction = "";
    String routeCooperative = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Firebase(){

    }

//    public void saveCompany(Company company){
//
//        db.collection("company").document(company.getCnpj())
//                .set(company)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + company.getCnpj());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }



}

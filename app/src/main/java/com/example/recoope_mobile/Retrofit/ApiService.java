package com.example.recoope_mobile.Retrofit;

import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.Company;
import com.example.recoope_mobile.model.LoginParams;
import com.example.recoope_mobile.response.ApiDataResponseAuction;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    /////Empresa
    // Criar um novo recurso (POST)
    @POST("empresa/cadastrar")
    Call<ResponseBody> createCompany(@Body Company company);

    // Ler um recurso específico (GET)
    @GET("empresa/{id}")
    Call<Company> getCompanyById(@Path("id") String id);

    @POST("empresa/login")
    Call<ResponseBody> authenticationCompany(@Body LoginParams loginParams);

    // Atualizar um recurso existente (PUT)
    @PATCH("empresa/alterar/{id}")
    Call<Company> updateCompany(@Path("id") String id, @Body Company company);

    // Excluir um recurso (DELETE)
    @DELETE("empresa/remover/{id}")
    Call<Void> deleteCompany(@Path("id") String id);

    /////Empresa


    /////Leilão

    // Ler todos recursos (GET)
    @GET("leilao")
    Call<ApiDataResponseAuction<List<Auction>>> getAllAuctions();

    // Ler um recurso específico (GET)
    @GET("leilao/{id}")
    Call<Auction> getByIdAuction(@Path("id") String id);

    // Ler um recurso filtrado por material (GET)
    @GET("leilao/material/{material}")
    Call<ApiDataResponseAuction<List<Auction>>> getFiltredAuction(@Path("id") String id);

    // Ler um recurso por data (GET)
    @GET("leilao/fim/material")
    Call<Auction> getFiltredByDateAuction(@Path("id") String id);


    /////Leilão

}

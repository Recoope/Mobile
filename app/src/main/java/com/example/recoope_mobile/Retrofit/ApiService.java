package com.example.recoope_mobile.Retrofit;

import com.example.recoope_mobile.models.Company;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Criar um novo recurso (POST)
    @POST("empresa/cadastrar")
    Call<Company> createCompany(@Body Company company);

    // Ler um recurso específico (GET)
    @GET("empresa/{id}")
    Call<Company> getCompanyById(@Path("id") String id);

    // Atualizar um recurso existente (PUT)
    @PATCH("empresa/alterar/{id}")
    Call<Company> updateCompany(@Path("id") String id, @Body Company company);

    // Excluir um recurso (DELETE)
    @DELETE("empresa/remover/{id}")
    Call<Void> deleteCompany(@Path("id") String id);
}

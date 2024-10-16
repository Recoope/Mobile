package com.example.recoope_mobile.Retrofit;

import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.model.Company;
import com.example.recoope_mobile.model.CompanyProfile;
import com.example.recoope_mobile.model.LoginParams;
import com.example.recoope_mobile.response.ApiDataResponseAuction;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /////Autenticação
    @POST("login")
    Call<ResponseBody> authenticationCompany(@Body LoginParams loginParams);

    /////Empresa
    // Criar um novo recurso (POST)
    @POST("empresa/cadastrar")
    Call<ResponseBody> createCompany(@Body Company company);

    // Ler um recurso específico (GET)
    @GET("empresa/{id}")
    Call<ApiDataResponseAuction<CompanyProfile>> getCompanyById(@Path("id") String id);

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
    @GET("/leilao")
    Call<ApiDataResponseAuction<List<Auction>>> getFilteredAuctions(
            @Query("materiais") List<String> materials,
            @Query("ate") String closeAt,
            @Query("pesoMin") String weightMin,
            @Query("pesoMax") String weightMax
    );

    // Ler um recurso por data (GET)
    @GET("leilao/participados/{cnpj}")
    Call<ApiDataResponseAuction<List<Auction>>> getParticipations(@Path("cnpj") String cnpj);
    @GET("leilao/participados/{cnpj}")
    Call<ApiDataResponseAuction<List<Auction>>> getParticipationsByExpiringDate(@Path("cnpj") String cnpj, @Query("fim") String date);
    @GET("leilao/{id}")
    Call<ApiDataResponseAuction<AuctionDetails>> getAuctionDetails(@Path("id") int id);
    @GET("leilao/vencimentos/{cnpj}")
    Call<ApiDataResponseAuction<List<Date>>> getExpiringDates(@Path("cnpj") String cnpj);
}

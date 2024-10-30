package com.example.recoope_mobile.Retrofit;

import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.model.Bid;
import com.example.recoope_mobile.model.BidInfo;
import com.example.recoope_mobile.model.Company;
import com.example.recoope_mobile.model.CompanyProfile;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.model.LoginParams;
import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.response.ApiDataResponse;

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
    @POST("empresa/cadastrar/")
    Call<ResponseBody> createCompany(@Body Company company);
    @GET("empresa/{id}")
    Call<ApiDataResponse<CompanyProfile>> getCompanyById(@Path("id") String id);
    @PATCH("empresa/alterar/{id}")
    Call<Company> updateCompany(@Path("id") String id, @Body Company company);
    @DELETE("empresa/remover/{id}")
    Call<Void> deleteCompany(@Path("id") String id);
    ///// Leilão
    @GET("leilao")
    Call<ApiDataResponse<List<Auction>>> getAllAuctions();
    @GET("leilao/{id}")
    Call<Auction> getByIdAuction(@Path("id") String id);
    @GET("/leilao")
    Call<ApiDataResponse<List<Auction>>> getFilteredAuctions(
            @Query("materiais") List<String> materials,
            @Query("ate") String closeAt,
            @Query("pesoMin") String weightMin,
            @Query("pesoMax") String weightMax
    );
    @GET("leilao/participados/{cnpj}")
    Call<ApiDataResponse<List<Auction>>> getParticipations(@Path("cnpj") String cnpj);
    @GET("leilao/participados/{cnpj}")
    Call<ApiDataResponse<List<Auction>>> getParticipationsByExpiringDate(@Path("cnpj") String cnpj, @Query("fim") String date);
    @GET("leilao/{id}")
    Call<ApiDataResponse<AuctionDetails>> getAuctionDetails(@Path("id") int id);
    @GET("leilao/vencimentos/{cnpj}")
    Call<ApiDataResponse<List<Date>>> getExpiringDates(@Path("cnpj") String cnpj);

    /////Cooperativa
    @GET("cooperativa/buscar/{nomeCooperativa}")
    Call<ApiDataResponse<List<Cooperative>>> getSearchCooperative(
            @Path("nomeCooperativa") String nameCooperative
    );
    /////Lance
    // Excluir um recurso filtrado por material (DELETE)
    @DELETE("lance/cancelar")
    Call<ApiDataResponse<List<Bid>>> deleteBid(
            @Query("cnpj") String cnpj,
            @Query("idLeilao") int idLeilao
    );

    @GET("recibo/{cnpj}")
    Call<ApiDataResponse<List<Payment>>> getPayment(@Path("cnpj") String cnpj);

    /////Lance
    @POST("lance/{idLeilao}")
    Call<Void> bid(
            @Path("idLeilao") int idLeilao,
            @Body BidInfo bidInfo
    );
    @GET("cooperativa/{cnpj}")
    Call<ApiDataResponse<Cooperative>> getIdCooperative(
            @Path("cnpj") String cnpj
    );




}

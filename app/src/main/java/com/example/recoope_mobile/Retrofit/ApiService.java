package com.example.recoope_mobile.Retrofit;

import com.example.recoope_mobile.dto.CompanyDto;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.model.Bid;
import com.example.recoope_mobile.model.BidInfo;
import com.example.recoope_mobile.model.Company;
import com.example.recoope_mobile.model.CompanyProfile;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.model.LoginParams;
import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.model.ParticipatedAuction;
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

    @POST("login/refreshToken/{cnpj}")
    Call<ResponseBody> refreshToken(@Path("cnpj") String cnpj, @Query("refreshTokenParam") String token);

    /////Empresa
    @POST("empresa/cadastrar/")
    Call<ResponseBody> createCompany(@Body Company company);
    @GET("empresa/{id}")
    Call<ApiDataResponse<CompanyProfile>> getCompanyById(@Path("id") String id);
    @PATCH("empresa/alterar/{cnpj}")
    Call<ResponseBody> updateCompany(@Path("cnpj") String cnpj, @Body CompanyDto request);

    @PATCH("empresa/alterarSenha/{cnpj}")
    Call<ResponseBody> updateCompanyPassword(@Path("cnpj") String cnpj, @Query("novaSenha") String newPassword);

    @DELETE("empresa/remover/{cnpj}")
    Call<Void> deleteCompany(@Path("cnpj") String cnpj);
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
    Call<ApiDataResponse<List<ParticipatedAuction>>> getParticipations(@Path("cnpj") String cnpj);
    @GET("leilao/participados/{cnpj}")
    Call<ApiDataResponse<List<ParticipatedAuction>>> getParticipationsByExpiringDate(@Path("cnpj") String cnpj, @Query("fim") String date);
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
    Call<ApiDataResponse<List<Payment>>> getPayment(@Path("cnpj") String cnpj, @Query("dataDesc") boolean dataDesc);

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

    @POST("login/recuperar/{cnpjOuEmail}")
    Call<ApiDataResponse> generateCodeForgotPassword(
            @Path("cnpjOuEmail") String cnpjOuEmail
    );

    @POST("login/validarRecuperacao/{cnpj}")
    Call<ApiDataResponse> validatedCodeForgotPassword(
            @Path("cnpj") String cnpj, @Query("code") String code
    );

}

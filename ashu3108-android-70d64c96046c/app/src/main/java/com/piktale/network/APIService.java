package com.piktale.network;

import com.piktale.BuildConfig;
import com.piktale.models.BasicResponse;
import com.piktale.models.GenericRequestBody;
import com.piktale.models.User;
import com.piktale.models.requestBody.Register;
import com.piktale.models.requestBody.VerifyOTP;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by sayagodshala on 31/10/17.
 */

public interface APIService {

    @POST(BuildConfig.PATH + "/usernameAvailablity")
    Observable<Response<BasicResponse<String>>> usernameAvailability(@Body User body);

    @POST(BuildConfig.PATH + "/updateUsername")
    Observable<Response<BasicResponse<String>>> updateUsername(@Body User body);

    @POST(BuildConfig.PATH + "/register")
    Observable<Response<BasicResponse<User>>> register(@Body Register body);

    @Multipart
    @POST(BuildConfig.PATH + "/register")
    Observable<Response<BasicResponse<String>>> register(@Part("name") RequestBody name,
                                                         @Part("password") RequestBody password,
                                                         @Part("phone") RequestBody phone,
                                                         @Part("email") RequestBody email,
                                                         @Part("isBusiness") RequestBody isBusiness,
                                                         @Part("deviceType") RequestBody regType,
                                                         @Part("isPicture") RequestBody isPicture,
                                                         @Part MultipartBody.Part file);

    @Multipart
    @POST(BuildConfig.PATH + "/register")
    Observable<Response<BasicResponse<String>>> register(@Part("name") RequestBody name,
                                                         @Part("password") RequestBody password,
                                                         @Part("phone") RequestBody phone,
                                                         @Part("email") RequestBody email,
                                                         @Part("isBusiness") RequestBody isBusiness,
                                                         @Part("deviceType") RequestBody regType,
                                                         @Part("isPicture") RequestBody isPicture);

    @POST(BuildConfig.PATH + "/login")
    Observable<Response<BasicResponse<User>>> login(@Body User body);

    @POST(BuildConfig.PATH + "/forgot")
    Observable<Response<BasicResponse<String>>> forgot(@Body User body);

    @POST(BuildConfig.PATH + "/verifyOTP")
    Observable<Response<BasicResponse<User>>> verifyOTP(@Body VerifyOTP body);

    @POST(BuildConfig.PATH + "/verifyOTP")
    Observable<Response<BasicResponse<String>>> verifyOTPForgotPassword(@Body VerifyOTP body);

    @POST(BuildConfig.PATH + "/updatePassword")
    Observable<Response<BasicResponse<String>>> updatePassword(@Body User body);

    @POST(BuildConfig.PATH + "/temporaryUser")
    Observable<Response<BasicResponse<String>>> temporaryUser(@Body User body);

    @POST(BuildConfig.PATH + "/updateProfile")
    Observable<Response<BasicResponse<User>>> updateProfile(@Body User body);
//
//    @POST(BuildConfig.PATH + "/user/login")
//    Observable<Response<BasicResponse>> login(@Body User user);
//
//    @GET(BuildConfig.PATH + "/journal/types")
//    Observable<Response<BasicResponse>> getJournals(@Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/case/list")
//    Observable<Response<BasicResponse>> getCases(@Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/journals/types/{id}")
//    Observable<Response<BasicResponse>> getJournalsArticles(@Path("id") String id, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/journal/request/{id}")
//    Observable<Response<BasicResponse>> requestJournal(@Path("id") String id, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/library/request/{id}")
//    Observable<Response<BasicResponse>> requestLibrary(@Path("id") String id, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/medical/news")
//    Observable<Response<BasicResponse>> getMedicalNews(@Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/medical/news/{id}")
//    Observable<Response<MedicalNewsResponse>> getMedicalNewsDetail(@Path("id") String id, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/library/types")
//    Observable<Response<BasicResponse>> getLibraryTypes(@Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/library/types/{libraryId}")
//    Observable<Response<BasicResponse>> getBooksByLibrary(@Path("libraryId") String libraryId, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/user")
//    Observable<Response<BasicResponse>> validateSession(@Query("access_token") String accessToken);
//
//    @POST(BuildConfig.PATH + "/user/forgotpass/email")
//    Observable<Response<BasicResponse>> forgotPassEmail(@Body UserRegistration userRegistration);
//
//    @POST(BuildConfig.PATH + "/user/randomcode/confirmation")
//    Observable<Response<BasicResponse>> forgotPassRandomCode(@Body UserRegistration userRegistration);
//
//    @POST(BuildConfig.PATH + "/user/forgotpass/change")
//    Observable<Response<BasicResponse>> forgotPassPasswordChange(@Query("access_token") String accessToken, @Body UserRegistration userRegistration);

}

package com.twilio.video.app;


import com.twilio.video.app.Apis.ChatApi;
import com.twilio.video.app.Apis.ClassesAPI;
import com.twilio.video.app.Apis.JobsApi;
import com.twilio.video.app.Apis.NotificationApi;
import com.twilio.video.app.Apis.OTpApi;
import com.twilio.video.app.Apis.PostApi;
import com.twilio.video.app.Apis.ProfilePicUploadApis;
import com.twilio.video.app.Apis.RegisterAPI;
import com.twilio.video.app.Apis.SearchApi;
import com.twilio.video.app.Apis.SettingsApI;
import com.twilio.video.app.Apis.SkillApis;
import com.twilio.video.app.Apis.TeamsApi;
import com.twilio.video.app.Apis.UserAPI;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrifitClient {

    private static final String BASE_URL = "https://www.nexgeno.com/api/";
    private static RetrifitClient myinstance;
    private Retrofit retrofit;

    private RetrifitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private RetrifitClient(String classId){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL+"/"+classId)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static synchronized  RetrifitClient getInstance(){
        if(myinstance == null){

            myinstance = new RetrifitClient();

        }
        return  myinstance;
    }

    public MyAPI getApi()
    {
        return  retrofit.create(MyAPI.class);
    }

    public PostApi getPostApi()
    {
        return  retrofit.create(PostApi.class);
    }

    public RegisterAPI getRegAPi()
    {
        return  retrofit.create(RegisterAPI.class);
    }

    public OTpApi getOTpApi()
    {
        return  retrofit.create(OTpApi.class);
    }

    public UserAPI getUserApi()
    {
        return  retrofit.create(UserAPI.class);
    }

    public ClassesAPI getClassesApi()
    {
        return  retrofit.create(ClassesAPI.class);
    }

    public ProfilePicUploadApis getUploadPicApi()
    {
        return  retrofit.create(ProfilePicUploadApis.class);
    }

    public NotificationApi getNotificationApi()
    {
        return  retrofit.create(NotificationApi.class);
    }


    public ChatApi getChatApi()
    {
        return  retrofit.create(ChatApi.class);
    }

    public SkillApis getSkillApi(){
        return  retrofit.create(SkillApis.class);
    }
    public TeamsApi getTeamsApi(){
        return  retrofit.create(TeamsApi.class);
    }
    public SettingsApI getSettingsApi(){
        return  retrofit.create(SettingsApI.class);
    }

    public SearchApi getSearchApi(){
        return  retrofit.create(SearchApi.class);
    }

    public JobsApi gteJobsApi()
    {
        return  retrofit.create(JobsApi.class);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient(){

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

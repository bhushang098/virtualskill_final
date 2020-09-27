package com.twilio.video.app.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.LoginPage;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConformationDialog {

   public static Context context;

    public ConformationDialog(Context context) {
        this.context = context;
    }

    public int  showConformDialog(Context context, String title, String message
    , String purpose){


        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        if(purpose.equalsIgnoreCase("log_out"))
                        {
                            SharedPreferences settings = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                            settings.edit().clear().commit();
                            context.startActivity(new Intent(context, LoginPage.class));
                            ((Activity) context).finish();
                        }
                        if(purpose.split(",>>>")[0].equalsIgnoreCase("apply_job"))
                        {
                            callJobApplyApi(purpose.split(",>>>")[1],purpose.split(",>>>")[2]);
                        }

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.plcicon)
                .show();
        return  0;

    }

    private static void callJobApplyApi(String jobId,String token) {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .gteJobsApi().applyJob("job/"+jobId+"/apply",token);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response>>",response.raw().toString());
                Toast.makeText(context, "Requested Job Apply", Toast.LENGTH_SHORT).show();
                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(context, "Applied For Job", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, HomePage.class));
                        ((Activity)context).finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d("Error>>",t.toString());
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

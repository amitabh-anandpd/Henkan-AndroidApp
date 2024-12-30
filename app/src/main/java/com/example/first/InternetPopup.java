package com.example.first;

import android.app.AlertDialog;
import android.content.Context;

public class InternetPopup {
    public static void showNoInternetDialog(Context context, Runnable retryAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage("No internet connection detected. Please check your connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (retryAction != null) {
                        retryAction.run(); // Execute the retry action
                    }
                })
                .setNegativeButton("Close", (dialog, which) -> {
                    dialog.dismiss(); // Close the dialog
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

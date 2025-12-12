package com.wyh.happyyousdk.ehr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityPdfReaderBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfReaderActivity extends AppCompatActivity {

    ActivityPdfReaderBinding binding;
    String comingFrom = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf_reader);

        context = this;

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());


        String pdfurl = getIntent().getStringExtra("image");
        pdfurl = pdfurl.trim();
        new RetrievePDFfromUrl().execute(pdfurl);

    }


    class RetrievePDFfromUrl extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            String fileName = strings[0].substring(strings[0].lastIndexOf('/') + 1);
            File file = new File(context.getExternalCacheDir(), fileName);
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    copyInputStreamToFile(inputStream, file);
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.

            if (file != null)
                binding.pdfView.fromFile(file.getAbsolutePath()).show();
            else {
                Toast.makeText(context, "File not loaded properly, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void copyInputStreamToFile(InputStream inputStream, File outputFile) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            // Use a buffer size of 4KB (4096 bytes) for efficient transfer
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush(); // Ensure all data is written to the file
        } finally {
            // Ensure the input stream is closed, regardless of success or failure
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
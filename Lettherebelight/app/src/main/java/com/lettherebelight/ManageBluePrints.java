package com.lettherebelight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lettherebelight.Entity.PDF;

import java.util.ArrayList;

public class ManageBluePrints extends AppCompatActivity {
    Button button;
    ArrayList<PDF> pdfList = new ArrayList<PDF>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_blue_prints);
        //createPdfList();
       // Log.d("mytag", pdfList.get(0).getFileName());

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageBluePrints.this, PdfViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

//    public ArrayList<PDF> createPdfList() {
//        PDF pdf = new PDF();
//
//        pdfList.add(pdf);
//        return pdfList;
//    }

    public ArrayList<String> createSpinnerList(){
        ArrayList<String> spinnerList = new ArrayList<>();
        for(int i = 0; i < pdfList.size(); i++){
          spinnerList.add(pdfList.get(i).getFileName());
        }
        return spinnerList;
    }
}
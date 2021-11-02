package com.lettherebelight;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import com.lettherebelight.Entity.PDF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PdfViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;
    private ImageView pdfImageView;
    private static String FILE_NAME;
    private int pdfId;
    private ArrayList<PDF> pdfList = new ArrayList<PDF>();

    private Spinner pdfSpinner;
    private ManageBluePrints manageBluePrints = new ManageBluePrints();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfImageView = findViewById(R.id.pdfImageView);
        pdfSpinner = findViewById(R.id.pdf_spinner);
        createPdfList();
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("select a blueprint");
        for(int i = 0; i < pdfList.size(); i++){
            spinnerList.add(pdfList.get(i).getFileName());
        }Log.d("myTag", spinnerList.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pdfSpinner.setAdapter(adapter);
        pdfSpinner.setSelection(0, false);
        pdfSpinner.setOnItemSelectedListener(this);


    }

    private void tryToOpenPdf() {
        Log.d("in tryToOpenPdf", FILE_NAME);
        try {
            openPdfWithAndroidSDK(pdfImageView, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PDF> createPdfList() {
        PDF pdf = new PDF();
        pdf.setFileId(R.raw.blueprints1);
        pdf.setFileName("blueprints1.pdf");
        pdfList.add(pdf);
        PDF pdf2 = new PDF();
        pdf2.setFileId(R.raw.blueprints2);
        pdf2.setFileName("blueprints2.pdf");
        pdfList.add(pdf2);
        return pdfList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPdfPage != null) {
            mPdfPage.close();
        }
        if (mPdfRenderer != null) {
            mPdfRenderer.close();
        }
    }



    /**
     * Render a given page in the PDF document into an ImageView.
     *
     * @param imageView  used to display the PDF
     * @param pageNumber page of the PDF to view (index starting at 0)
     */
    void openPdfWithAndroidSDK(ImageView imageView, int pageNumber) throws IOException {
        // Copy sample.pdf from raw resource folder into local cache, so PdfRenderer can handle it
        File fileCopy = new File(getCacheDir(), FILE_NAME);
        copyToLocalCache(fileCopy, pdfId);

        // We will get a page from the PDF file by calling PdfRenderer.openPage
        ParcelFileDescriptor fileDescriptor =
                ParcelFileDescriptor.open(fileCopy,
                        ParcelFileDescriptor.MODE_READ_ONLY);
        mPdfRenderer = new PdfRenderer(fileDescriptor);
        mPdfPage = mPdfRenderer.openPage(pageNumber);

        // Create a new bitmap and render the page contents on to it
        Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                mPdfPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Set the bitmap in the ImageView so we can view it
        imageView.setImageBitmap(bitmap);//check
    }

    /**
     * Copies the resource PDF file locally so that {@link PdfRenderer} can handle the file
     *
     * @param outputFile  location of copied file
     * @param pdfResource pdf resource file
     */
    void copyToLocalCache(File outputFile, @RawRes int pdfResource) throws IOException {
        if (!outputFile.exists()) {
            InputStream input = getResources().openRawResource(pdfResource);
            FileOutputStream output;
            output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int size;
            // Just copy the entire contents of the file
            while ((size = input.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            input.close();
            output.close();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        FILE_NAME = adapterView.getItemAtPosition(i).toString();
        if(FILE_NAME.contains(".pdf")){
            pdfId = pdfList.get(i-1).getFileId();
            Log.d("in onItemSelected", FILE_NAME);
            tryToOpenPdf();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
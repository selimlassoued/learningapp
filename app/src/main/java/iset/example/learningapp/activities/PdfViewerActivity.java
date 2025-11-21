package iset.example.learningapp.activities;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import iset.example.learningapp.R;

public class PdfViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PDF_URL = "pdf_url";
    public static final String EXTRA_PDF_TITLE = "pdf_title";

    private WebView webView;
    private ProgressBar progressBar;
    private TextView tvPdfTitle;
    private ImageButton btnBack;
    private MaterialButton btnDownload;

    private String pdfUrl;
    private String pdfTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        initViews();
        getIntentData();
        setupWebView();
        loadPdf();
    }

    private void initViews() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        tvPdfTitle = findViewById(R.id.tvPdfTitle);
        btnBack = findViewById(R.id.btnBack);
        btnDownload = findViewById(R.id.btnDownload);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnDownload.setOnClickListener(v -> downloadPdf());
    }

    private void getIntentData() {
        pdfUrl = getIntent().getStringExtra(EXTRA_PDF_URL);
        pdfTitle = getIntent().getStringExtra(EXTRA_PDF_TITLE);

        if (pdfTitle != null) {
            tvPdfTitle.setText(pdfTitle);
        }

        // URL de test si aucune URL fournie
        if (pdfUrl == null || pdfUrl.isEmpty()) {
            pdfUrl = "https://www.africau.edu/images/default/sample.pdf";
        }
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PdfViewerActivity.this,
                        "Erreur de chargement du PDF", Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadPdf() {
        // Utiliser Google Docs Viewer pour afficher le PDF
        String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;
        webView.loadUrl(googleDocsUrl);
    }

    private void downloadPdf() {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));

            // Titre et description de la notification
            request.setTitle(pdfTitle != null ? pdfTitle : "Document PDF");
            request.setDescription("Téléchargement en cours...");

            // Notification quand terminé
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Destination du fichier
            String fileName = pdfTitle != null ?
                    pdfTitle.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf" : "document.pdf";
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, fileName);

            // Lancer le téléchargement
            DownloadManager downloadManager =
                    (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                Toast.makeText(this, "Téléchargement démarré", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
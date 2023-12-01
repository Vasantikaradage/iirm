package com.indiainsure.android.MB360.insurance;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.indiainsure.android.MB360.databinding.FragmentEnrollmentWebViewBinding;
import com.indiainsure.android.MB360.utilities.LoadingInsuranceDialogue;

public class EnrollmentWebView extends Fragment {

    FragmentEnrollmentWebViewBinding binding;
    View view;

    String URL = "";

    LoadingInsuranceDialogue loadingInsuranceDialogue;

    public EnrollmentWebView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingInsuranceDialogue = new LoadingInsuranceDialogue(requireContext(), requireActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEnrollmentWebViewBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        EnrollmentWebClient webViewClient = new EnrollmentWebClient(loadingInsuranceDialogue);


        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setBuiltInZoomControls(false);
        binding.webview.getSettings().setSupportZoom(false);
        binding.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webview.getSettings().setAllowFileAccess(true);
        binding.webview.getSettings().setDomStorageEnabled(true);


        binding.webview.setWebViewClient(webViewClient);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        URL = "" + EnrollmentWebViewArgs.fromBundle(getArguments()).getGetEnrollmentUrl();
        if (URL.isEmpty()) {
            //something went wrong
        } else {
            //show the URL
            binding.webview.loadUrl(URL);
        }
    }

    class EnrollmentWebClient extends WebViewClient {
        LoadingInsuranceDialogue loadingInsuranceDialogue;

        public EnrollmentWebClient(LoadingInsuranceDialogue loadingInsuranceDialogue) {
            //loading
            this.loadingInsuranceDialogue = loadingInsuranceDialogue;
            this.loadingInsuranceDialogue.showLoading("");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //loading
            loadingInsuranceDialogue.hideLoading();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            view.loadUrl(request.getUrl().toString());
            return true;
        }


    }
}
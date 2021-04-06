package com.example.fitnessplayground.ui.developer

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitnessplayground.R
import com.example.fitnessplayground.ui.home.HomeFragment
import com.example.fitnessplayground.ui.slideshow.SlideshowFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.coroutineContext

//developer
class DeveloperFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_developer, container, false)

        val webView: WebView = root.findViewById(R.id.webView_about)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String?
            ): Boolean {
                if(url!!.contains("about")) {
                    view!!.loadUrl(url);
                }
                else{
                    val fragment = HomeFragment()
                    var transa = getFragmentManager()?.beginTransaction()
                    transa?.replace(R.id.nav_host_fragment, fragment)?.commit()
                }
                return true
            }
        }

        webView.loadUrl("https://rushikeshpotdar.blogspot.com/p/about.html")

        return root
    }


}

package com.android.vidrebany.utils

import android.os.AsyncTask
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// create an async task class for loading pdf file from URL.

internal class RetrievePDFfromUrl(// create a pdf view object
    private val pdfView: PDFView
) : AsyncTask<String?, Void?, InputStream?>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg strings: String?): InputStream? {
        // we are using inputstream
        // for getting out PDF.
        var inputStream: InputStream? = null
        try {
            val url = URL(strings[0])
            // below is the step where we are
            // creating our connection.
            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
            if (urlConnection.getResponseCode() === 200) {
                // response is success.
                // we are getting input stream from url
                // and storing it in our variable.
                inputStream = BufferedInputStream(urlConnection.getInputStream())

            }
        } catch (e: IOException) {
            // this is the method
            // to handle errors.
            e.printStackTrace()
            return null
        }
        return inputStream
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(inputStream: InputStream?) {
        // after the execution of our async
        // task we are loading our pdf in our pdf view.
        pdfView.fromStream(inputStream).autoSpacing(true).fitEachPage(true).load()
    }
}
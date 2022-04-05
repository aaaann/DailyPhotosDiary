package com.annevonwolffen.about_impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.annevonwolffen.mainscreen_api.ToolbarFragment

internal class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (parentFragment?.parentFragment as? ToolbarFragment)?.clearToolbarMenu()

        val contentEditText = view.findViewById<EditText>(R.id.et_content)
        view.findViewById<Button>(R.id.btn_send).setOnClickListener {
            val message = contentEditText.text.toString()
            sendTelegramMessage(message)
        }
    }

    private fun sendTelegramMessage(message: String) {
        val appName = "org.telegram.messenger"
        val telegramIntent = createTelegramIntent(message)
        val isTelegramInstalled = isAppAvailable(requireContext(), appName)
        if (/*isTelegramInstalled*/true) {
            startActivity(telegramIntent)
        } else {
            Toast.makeText(requireActivity(), "Telegram not Installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTelegramIntent(message: String): Intent {
        val telegramIntent = Intent(Intent.ACTION_VIEW)
        telegramIntent.type = "text/plain"
        telegramIntent.putExtra(Intent.EXTRA_TEXT, message)
        telegramIntent.data = Uri.parse("http://telegram.me/AnneVonWolffen")
        return telegramIntent
    }

    fun isAppAvailable(context: Context, appName: String): Boolean {
        val pm: PackageManager = context.packageManager
        return try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
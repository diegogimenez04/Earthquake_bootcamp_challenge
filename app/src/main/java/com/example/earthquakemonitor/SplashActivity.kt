package com.example.earthquakemonitor

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.earthquakemonitor.databinding.ActivitySplashBinding
import com.example.earthquakemonitor.main.MainActivity

class SplashActivity : AppCompatActivity() {
    companion object {
        const val SPLASH_DURATION: Long = 2000
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayAppVersion()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Agregar finish para que al volver atras se cierre la app
        }, SPLASH_DURATION)

    }

    private fun displayAppVersion() {
        try {
            // Obtiene el numero de version y lo carga el textview de abajo a la derecha
            val version = this.packageManager.getPackageInfo(this.packageName, 0).versionName
            binding.tvVersioname.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            // Log por si falla
            e.printStackTrace()
        }
    }
}
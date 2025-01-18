package me.safarov399.sigmafilemanagerviews

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.sigmafilemanagerviews.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val statusBarBackground = binding!!.statusBarBackground
        ViewCompat.setOnApplyWindowInsetsListener(statusBarBackground) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            binding!!.statusBarBackground.apply {
                layoutParams.height = statusBarHeight
                setBackgroundColor(ContextCompat.getColor(context, me.safarov399.uikit.R.color.toolbar))
            }
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
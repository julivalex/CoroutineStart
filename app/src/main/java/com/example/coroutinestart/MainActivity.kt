package com.example.coroutinestart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.coroutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            println("HANDLE_MSG $msg")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            loadData()
        }
        thread {
            handler.sendMessage(Message.obtain(handler, 0, 17))
        }
    }

    private fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        loadCity { city ->
            binding.tvLocation.text = city
            getTemperature(city) { temp ->
                binding.tvTemperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                callback.invoke("Moscow")
            }
        }
    }

    private fun getTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            runOnUiThread {
                Toast.makeText(
                    this,
                    getString(R.string.loading_temperature_toast, city),
                    Toast.LENGTH_LONG
                ).show()
            }
            Thread.sleep(2000)
            runOnUiThread {
                callback.invoke(17)
            }
        }
    }
}
package com.example.coroutinestart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.coroutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
//            lifecycleScope.launch {
//                loadData()
//            }
            loadDataWithoutCoroutine()
        }
    }

    private suspend fun loadData() {
        Log.d("MainActivity2", "Load started: $this")
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()

        binding.tvLocation.text = city
        val temp = getTemperature(city)

        binding.tvTemperature.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonLoad.isEnabled = true
        Log.d("MainActivity2", "Load finished: $this")
    }

    private suspend fun loadCity(): String {
        delay(2000)
        return "Moscow"
    }

    private suspend fun getTemperature(city: String): Int {
        Toast.makeText(
            this,
            getString(R.string.loading_temperature_toast, city),
            Toast.LENGTH_LONG
        ).show()
        delay(2000)
        return 17
    }

    private fun loadDataWithoutCoroutine(step: Int = 0, obj: Any? = null) {
        when (step) {
            0 -> {
                Log.d("MainActivity2", "Load started: $this")
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithoutCoroutine { city ->
                    loadDataWithoutCoroutine(1, city)
                }
            }
            1 -> {
                val city = obj as String
                binding.tvLocation.text = city
                getTemperatureWithoutCoroutine(city) { temp ->
                    loadDataWithoutCoroutine(2, temp)
                }
            }
            2 -> {
                val temp = obj as Int
                binding.tvTemperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
                Log.d("MainActivity2", "Load finished: $this")
            }
        }
    }

    private fun loadCityWithoutCoroutine(callback: (String) -> Unit) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                callback.invoke("Moscow")
            }
        }
    }

    private fun getTemperatureWithoutCoroutine(city: String, callback: (Int) -> Unit) {
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
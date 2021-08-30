package com.example.asynctask_by_coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.asynctask_by_coroutine.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var counterTask: CounterTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etInput.text.clear()

        binding.btnStart.setOnClickListener {
            binding.tvCancel.text = ""
            try {
                val n = binding.etInput.text.toString().toInt()
                counterTask = CounterTask()
                counterTask.execute(n)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Please Enter Number First", Toast.LENGTH_SHORT)
                    .show()
                binding.etInput.requestFocus()
            }
        }

        binding.btnStop.setOnClickListener {
            try {
                if (counterTask.checkIsCancelled()) {
                    Toast.makeText(this@MainActivity, "Please Start the Counter First", Toast.LENGTH_SHORT).show()
                } else {
                    counterTask.cancel(false)
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Please Start the Counter First", Toast.LENGTH_SHORT)
                    .show()
                binding.etInput.requestFocus()
            }

        }
    }

    inner class CounterTask : CoroutineAsyncTask<Int, Int, String>() {

        override fun doInBackground(vararg params: Int?): String {
            var count = params[0] ?: 0
            while (count >= 0) {
                if (isCancelled) return "Shit"
                Thread.sleep(1000)
                publishProgress(count--)
            }
            return "Done"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            val progress = values[0] ?: 0
            binding.tvCounter.text = progress.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.tvCounter.text = result
        }

        override fun onCancelled(result: String?) {
            super.onCancelled(result)
            binding.tvCounter.text = ""
            binding.etInput.text.clear()
            binding.tvCancel.text = result
        }

        //Not present in AsyncTask added by me
        fun checkIsCancelled():Boolean {
            if (isCancelled) return true
            return false
        }
    }
}
package com.example.asynctask_by_coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class CoroutineAsyncTask<Params, Progress, Result> {

    protected var isCancelled = false

    protected abstract fun doInBackground(vararg params: Params?): Result

    open fun onPreExecute() {}

    open fun onProgressUpdate(vararg values: Progress?) {}

    open fun onPostExecute(result: Result?) {}

    open fun onCancelled(result: Result?) {}

    protected fun publishProgress(vararg progress: Progress?) {
        GlobalScope.launch(Dispatchers.Main) {
            onProgressUpdate(*progress)
        }
    }

    fun execute(vararg params: Params?) {
        GlobalScope.launch(Dispatchers.Default) {
            val result = doInBackground(*params)
            launch(Dispatchers.Main) {
                if (isCancelled) {
                    onCancelled(result)
                } else {
                    onPostExecute(result)
                }
            }
        }
    }

    fun cancel(mayInterruptIfRunning: Boolean) {
        isCancelled = true
    }

}
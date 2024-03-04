package club.mobile.d21.personalassistant_ver2_kotlin.service

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import androidx.work.WorkerParameters
import club.mobile.d21.personalassistant_ver2_kotlin.ui.home.HomeViewModel

class UpdateDailyTaskWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val application = applicationContext as Application
        val homeViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(HomeViewModel::class.java)

        // Thực hiện cập nhật trạng thái của DailyTask
        homeViewModel.updateDailyTaskStatus()

        return Result.success()
    }
}
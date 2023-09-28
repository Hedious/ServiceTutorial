package com.example.servicetutorial

import android.app.DownloadManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.work.BackoffPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.servicetutorial.ui.theme.ServiceTutorialTheme
import com.example.servicetutorial.worker.CustomWorker
import com.example.servicetutorial.worker.DownloadWorker
import com.example.servicetutorial.worker.PWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
            .setInitialDelay(Duration.ofSeconds(10))
            .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(15))
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        setContent {

            ServiceTutorialTheme {

                LaunchedEffect(key1 = Unit) {
                    val workRequest = PeriodicWorkRequestBuilder<PWorker>(
                        repeatInterval = 1,
                        repeatIntervalTimeUnit = TimeUnit.HOURS
                    ).setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        Duration.ofSeconds(15)
                    ).build()

                    val workManager = WorkManager.getInstance(applicationContext)
                    workManager.enqueue(workRequest)
                }

                // A surface container using the 'background' color from the theme

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Button(
                            shape = RectangleShape,
                            onClick = {
                                ForegroundService.startService(
                                    applicationContext,
                                    "Testing Message"
                                )
                            }) {
                            Text("Start Service")
                        }
                        Button(
                            shape = RectangleShape,
                            onClick = {
                                ForegroundService.stopService(applicationContext)
                            }) {
                            Text("Stop Service")
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {

                        Button(
                            onClick = {
                                val constraints = androidx.work.Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                                    .setRequiresBatteryNotLow(true)
                                    .setRequiresStorageNotLow(true)
                                    .build()

                                val downloadWorkRequest =
                                    OneTimeWorkRequestBuilder<DownloadWorker>().setConstraints(
                                        constraints
                                    ).build()

                                WorkManager.getInstance(applicationContext)
                                    .enqueue(downloadWorkRequest)

                            }) {
                            Text(
                                text = "Download Image",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Button(
                            onClick = {


                            }) {
                            Text(
                                text = "Periodic Work",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

        }
    }
}

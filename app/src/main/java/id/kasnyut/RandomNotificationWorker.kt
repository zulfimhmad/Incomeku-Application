package id.kasnyut
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class RandomNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Generate a random delay between 1 and 10 minutes
            val random = Random()
            val delay = random.nextInt(2) + 1
            val delayMinutes = TimeUnit.MINUTES.toMillis(delay.toLong())

            // Simulate some task being performed
            Thread.sleep(delayMinutes)

            // Build and show the notification
            val notification = buildNotification()
            showNotification(notification)

            // Return success
            Result.success()
        } catch (e: Exception) {
            // Return failure
            Result.failure()
        }
    }

    private fun buildNotification(): NotificationCompat.Builder {
        // Build your notification using NotificationCompat.Builder
        // Customize the notification as per your requirements
        // ...

        return NotificationCompat.Builder(context, "channel_id")
            .setContentTitle("Jangan lupa catat pengeluaranmu!")
            .setContentText("Incomeku selalu tersedia didalam HP kamu untuk mencatat pengeluaranmu. Catat sekarang juga jangan sampaui lupa")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
    }

    private fun showNotification(notification: NotificationCompat.Builder) {
        val notificationManager = NotificationManagerCompat.from(context)
        val notificationId = 1 // Unique notification ID

        notificationManager.notify(notificationId, notification.build())
    }
}

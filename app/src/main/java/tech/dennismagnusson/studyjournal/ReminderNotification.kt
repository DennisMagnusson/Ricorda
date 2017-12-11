package tech.dennismagnusson.studyjournal

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.example.d.studyjournal.R
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for showing and canceling reminder
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object ReminderNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private val NOTIFICATION_TAG = "Reminder"

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     *
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     *
     *
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of reminder notifications. Make
     * sure to follow the
     * [
 * Notification design guidelines](https://developer.android.com/design/patterns/notifications.html) when doing so.
     *
     * @see .cancel
     */
    fun notify(context: Context?) {
        val res = context?.resources

        if(isDoneFile(context)) return

        val title = "Don't forget to practice your skills!"
        val text = "You have skills to practice"

        val builder = NotificationCompat.Builder(context)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_stat_reminder)
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.
                .setPriority(NotificationManager.IMPORTANCE_LOW)

                // Set the pending intent to be initiated when the user touches the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent(context, MainActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true)

        notify(context, builder.build())
    }

    private fun isDoneFile(context: Context?): Boolean {
        val res = context?.resources

        val filename = context?.filesDir?.path + "/.done"

        val file = File(filename)
        if(!file.exists())
            FileOutputStream(filename).close()

        val reader = FileReader(filename)

        val format = SimpleDateFormat(res?.getString(R.string.date_format))
        val dateStr = format.format(Date())
        for(line in reader.readLines()) {
            if(line == dateStr)
                return true
        }

        return false
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context?, notification: Notification) {
        val nm = context
                ?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * [.notify].
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }
}

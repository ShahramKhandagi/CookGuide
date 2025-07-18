package ir.shahramkhandagi.cookguide.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils(private val activity: Activity) {

    fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // بررسی اینکه آیا باید دلیل درخواست دسترسی نمایش داده شود
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                    // نمایش دیالوگ برای توضیح به کاربر
                    showRationaleDialog(
                        "نیاز به دسترسی نوتیفیکیشن",
                        "این دسترسی برای نمایش اعلان‌ها ضروری است."
                    ) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            NotificationUtils.REQUEST_NOTIFICATION_PERMISSION
                        )
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NotificationUtils.REQUEST_NOTIFICATION_PERMISSION
                    )
                }
            }
        }
    }

    private fun showRationaleDialog(title: String, message: String, onPositiveButtonClick: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("باشه") { _, _ ->
                onPositiveButtonClick()
            }
            .setNegativeButton("لغو") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}


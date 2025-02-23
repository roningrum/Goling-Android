package dev.antasource.goling.util.permission

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import androidx.fragment.app.FragmentManager
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.appcompat.app.AppCompatActivity


class PermissionUtil(
    caller: ActivityResultCaller,
    context: Context,
    fragmentManager: FragmentManager? = null,
    activity: Activity? = null,
    shouldShowPermissionRationale: (permission: String) -> Boolean
) {

    init {
        if (activity == null && fragmentManager == null) {
            throw IllegalArgumentException("Harus menyediakan minimal activity atau fragmentManager.")
        }
    }
    private val permissionManager by lazy {
        if(activity != null){
            PermissionManager(
                caller,
                context,
                (activity as? AppCompatActivity)?.supportFragmentManager
                    ?: throw IllegalArgumentException("Activity harus merupakan AppCompatActivity untuk mendapatkan supportFragmentManager."),
                shouldShowPermissionRationale
            )
        } else{
            PermissionManager(
                caller,
                context,
                fragmentManager!!,
                shouldShowPermissionRationale
            )
        }
    }

    fun checkLocationPermissions(onPermissionResult: (Boolean) -> Unit){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionManager.checkPermissions(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                ACCESS_BACKGROUND_LOCATION,
                onPermissionsGranted = onPermissionResult
            )
        } else{
            permissionManager.checkPermissions(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                onPermissionsGranted = onPermissionResult
            )
        }
    }

    fun checkCameraPermission(onPermissionResult: (Boolean) -> Unit){
        permissionManager.checkPermissions(
            CAMERA,
            onPermissionsGranted = onPermissionResult
        )
    }

    fun checkNotificationPermission(onPermissionResult: (Boolean) -> Unit){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.checkPermissions(
                POST_NOTIFICATIONS,
                onPermissionsGranted = onPermissionResult
            )
        } else {
            onPermissionResult(true)
        }
    }

    fun checkAllPermissions(
        onAllPermissionsResult: (locationGranted: Boolean, notificationGranted: Boolean) -> Unit
    ) {
        checkLocationPermissions { locationGranted ->
            checkNotificationPermission { notificationGranted ->
                onAllPermissionsResult(locationGranted, notificationGranted)
            }
        }
    }
}
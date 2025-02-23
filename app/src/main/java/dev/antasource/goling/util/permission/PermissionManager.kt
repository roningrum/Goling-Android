package dev.antasource.goling.util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class PermissionManager(
    caller: ActivityResultCaller,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val shouldShowPermissionRationale: (permission: String) -> Boolean
) {
    constructor(activity: ComponentActivity): this(
        caller = activity,
        context = activity,
        fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
        shouldShowPermissionRationale = {activity.shouldShowRequestPermissionRationale(it)}
    )
    constructor(fragment: Fragment):this(
        caller = fragment,
        context = fragment.requireContext(),
        fragmentManager = fragment.parentFragmentManager,
        shouldShowPermissionRationale = {fragment.shouldShowRequestPermissionRationale(it)}
    )

    private val requestPermissionLauncher =
        caller.registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->

            if(!isGranted){
            }
            onPermissionsGranted?.invoke(isGranted)

        }
    private val requestMultiplePermissionsLauncher =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val isGranted = result.values.all { it == true }
            if (!isGranted) {
                // As above: Handle the case where permissions are denied.
            }
            onPermissionsGranted?.invoke(isGranted)
        }
    private var onPermissionsGranted: ((isGranted: Boolean) -> Unit)? = null

    private fun requestPermissions(permissionsToBeRequested : List<String>){
        if(permissionsToBeRequested.size > 1){
            requestMultiplePermissionsLauncher.launch(permissionsToBeRequested.toTypedArray())
        }else{
            requestPermissionLauncher.launch(permissionsToBeRequested.firstOrNull().toString())
        }
    }

    fun checkPermissions(
        vararg permission: String,
        onPermissionsGranted: ((isGranted: Boolean) -> Unit)? = null
    ){
        this.onPermissionsGranted = onPermissionsGranted
        val permissionsToBeRequested = permission.filter { permissions ->
            ContextCompat.checkSelfPermission(
                context,
                permissions
            ) != PackageManager.PERMISSION_GRANTED
        }
        val shouldShowRequestPermissionRationale = permissionsToBeRequested.any {
            shouldShowPermissionRationale.invoke(it)
        }

        when{
            permissionsToBeRequested.isEmpty() -> onPermissionsGranted?.invoke(true)
            shouldShowRequestPermissionRationale -> {
                onPermissionsGranted?.invoke(false)
            }
            else -> requestPermissions(permissionsToBeRequested)
        }
    }
}
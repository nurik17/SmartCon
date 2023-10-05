package kz.cifron.smartcon.feature_counter

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import kz.cifron.smartcon.feature_home.data.Tasks

class CounterViewModel(private val context: Context) : ViewModel() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    private val _isFlashLightOn = MutableLiveData<Boolean>()
    val isFlashLightOn: LiveData<Boolean> = _isFlashLightOn

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> = _permissionGranted

    private val _task = MutableLiveData<Tasks>()
    val task: LiveData<Tasks> = _task

    init {
        initializeCamera()
        checkCameraPermission()
    }

    private fun initializeCamera() {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun checkCameraPermission() {
        val hasCameraPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        _permissionGranted.value = hasCameraPermission
    }

    fun toggleFlashLight() {
        viewModelScope.launch {
            if (cameraId.isNotEmpty() && _permissionGranted.value == true) {
                try {
                    cameraManager.setTorchMode(cameraId, !_isFlashLightOn.value!!)
                    _isFlashLightOn.postValue(!_isFlashLightOn.value!!)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }

}

package kz.cifron.smartcon.otherFragments.counter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCaptureException
import androidx.navigation.fragment.findNavController
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentCameraBinding
import kz.cifron.smartcon.feature_home.data.Tasks
import kz.cifron.smartcon.feature_result.presentation.ResultFragment

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
private const val TAG = "CameraFragment"

class CameraFragment : Fragment() {

    private var _binding : FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: Executor
    var imageCapture: ImageCapture? = null

    private var receivedTask: Tasks? = null
    private var filledValues : String? = null


    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "permission is not Granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val galleryLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                galleryNavigation(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        cameraExecutor = ContextCompat.getMainExecutor(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }
        startCamera()
        checkPermission()

        binding.btnGallery.setOnClickListener {
            openGallery()
        }
        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        arguments?.let {
            receivedTask = it.getParcelable("task")
            Log.d("CameraFragment", receivedTask!!.ADDR)
        }

        filledValues = arguments?.getString("filledValues")
        Log.d(TAG, "getData $filledValues ")

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val contentResolver = requireContext().contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageUri = outputFileResults.savedUri

                    val bundle = Bundle()
                    bundle.putString("imageUri", imageUri.toString())

                    val imageFragment = ImageFragment()
                    bundle.putParcelable("task",receivedTask)
                    bundle.putString("filledValues",filledValues)
                    Log.d(TAG, "sendData $filledValues")
                    imageFragment.arguments = bundle

                    findNavController().navigate(R.id.action_cameraFragment_to_imageFragment,bundle)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Photo failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    exception.printStackTrace()
                }
            }
        )
    }

    private fun galleryNavigation(outputFileResults : Uri){
        val bundle = Bundle()
        bundle.putString("imageUri", outputFileResults.toString())

        val resultFragment = ResultFragment()
        bundle.putParcelable("task",receivedTask)
        resultFragment.arguments = bundle

        findNavController().navigate(R.id.action_cameraFragment_to_resultFragment,bundle)

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                requireActivity(),
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, cameraExecutor)
    }


    private fun openGallery() {
        galleryLauncher.launch("image/*") // Запуск активности выбора изображения из галереи
    }
    private fun checkPermission() {
        val isAllGranted = REQUEST_PERMISSION.all{permission->
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted){
            startCamera()
            Toast.makeText(requireContext(), "permission is granted", Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(REQUEST_PERMISSION)
        }
    }


    companion object {
        private val REQUEST_PERMISSION: Array<String> = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

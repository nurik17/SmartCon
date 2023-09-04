package kz.cifron.smartcon.presentation.counter

import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.widget.Toast
import kz.cifron.smartcon.databinding.FragmentCounterBinding
import kz.cifron.smartcon.presentation.dialog.BottomSheetFragment
import kz.cifron.smartcon.presentation.dialog.FirstDialogFragment
import kz.cifron.smartcon.presentation.home.Tasks

class CounterFragment : Fragment() {
    private var _binding : FragmentCounterBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId : String
    private var isFlashlightOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCounterBinding.inflate(inflater,container,false)

        cameraManager = activity?.getSystemService(CameraManager::class.java) as CameraManager
        try{
            cameraId = cameraManager.cameraIdList[0]
        }catch (e : CameraAccessException){
            e.printStackTrace()
        }

        val toggleFlashLightOn = binding.flashLightBtn
        toggleFlashLightOn.setOnClickListener {
            toggleFlashlight()
            Toast.makeText(requireContext(), "flash on", Toast.LENGTH_SHORT).show()
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }

        return binding.root
    }

    private fun toggleFlashlight() {
        isFlashlightOn = !isFlashlightOn
        try{
            cameraManager.setTorchMode(cameraId,isFlashlightOn)
        }catch (e : CameraAccessException){
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено, вы можете использовать фонарик.
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveDataFromHome()
        navigationBackArrow()

        binding.textDetail.setOnClickListener {
            val firstDialogFragment = FirstDialogFragment()
            firstDialogFragment.show(parentFragmentManager,"custom_dialog")
        }
    }


    private fun receiveDataFromHome(){
        val task = arguments?.getParcelable<Tasks>("task")
        if (task != null) {
            binding.textAddress.text = task.ADDR
            binding.textLsNumber.text = task.LS.toString()
            binding.textCvNumber.text = task.NAME_SCH
            binding.textDate.text = task.POVERKA_DATE
        }else{
            Log.e("CounterFragment", "Task argument is null")
        }
    }

    private fun navigationBackArrow(){
        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun showBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
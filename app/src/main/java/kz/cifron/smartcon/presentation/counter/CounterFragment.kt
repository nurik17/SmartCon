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
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentCounterBinding
import kz.cifron.smartcon.presentation.dialog.BottomSheetFragment
import kz.cifron.smartcon.presentation.dialog.FirstDialogFragment
import kz.cifron.smartcon.presentation.home.Tasks

class CounterFragment : Fragment() {
    private var _binding : FragmentCounterBinding? = null
    private val binding get() = _binding!!

    private var task : Tasks? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId : String

    private var isFlashLightOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCounterBinding.inflate(inflater,container,false)

        cameraManager = activity?.getSystemService(CameraManager::class.java) as CameraManager

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

        try{
            cameraId = cameraManager.cameraIdList[0]
        }catch (e : CameraAccessException){
            e.printStackTrace()
        }

        val toggleFlashLightOn = binding.flashLightBtn
        toggleFlashLightOn.setOnClickListener {
            if(isFlashLightOn){
                turnOffFlashLight()
            }else{
                turnOnFlashLight()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveDataFromHome()

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textDetail.setOnClickListener {
            val customDialogFragment = FirstDialogFragment()
            customDialogFragment.show(childFragmentManager, "custom_dialog")

        }
        binding.counterBtn.setOnClickListener {
            sendData()
        }
    }

    private fun turnOnFlashLight() {
        try{
            if(cameraId.isNotEmpty()){
                cameraManager.setTorchMode(cameraId,true)
                isFlashLightOn = true
                Log.d("CounterFragment", "turnOnFlashLight: true")
            }
        }catch (e : CameraAccessException){
            e.printStackTrace()
        }
    }

    private fun turnOffFlashLight() {
        try {
            if(cameraId.isNotEmpty()){
                cameraManager.setTorchMode(cameraId,false)
                isFlashLightOn = false
                Log.d("CounterFragment", "turnOnFlashLight: false")

            }
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
            }
        }
    }



    private fun receiveDataFromHome(){
        task = arguments?.getParcelable<Tasks>("task")
        if (task != null) {
            binding.textAddress.text = task!!.ADDR
            binding.textLsNumber.text = task!!.LS.toString()
            binding.textCvNumber.text = task!!.NAME_SCH
            binding.textDate.text = task!!.POVERKA_DATE
        }else{
            Log.e("CounterFragment", "Task argument is null")
        }
    }


    private fun sendData() {
        val cameraFragment = CameraFragment()
        val bundle = Bundle()
        bundle.putParcelable("task", task)
        cameraFragment.arguments = bundle
        findNavController().navigate(R.id.action_id_counterFragment_to_cameraFragment, bundle)

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
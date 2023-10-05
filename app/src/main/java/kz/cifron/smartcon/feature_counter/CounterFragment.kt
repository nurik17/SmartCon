package kz.cifron.smartcon.feature_counter

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
import android.content.res.ColorStateList
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentCounterBinding
import kz.cifron.smartcon.otherFragments.dialog.FirstDialogFragment
import kz.cifron.smartcon.feature_home.data.Tasks
import kz.cifron.smartcon.otherFragments.counter.CameraFragment


private const val TAG = "CounterFragment"

class CounterFragment : Fragment() {
    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!

    private lateinit var counterAdapter: CounterAdapter

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    private var isFlashLightOn = false
    private var task: Tasks? = null
    private var isRvItemsEmpty = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCounterBinding.inflate(inflater, container, false)

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

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        val toggleFlashLightOn = binding.flashLightBtn
        toggleFlashLightOn.setOnClickListener {
            if (isFlashLightOn) {
                turnOffFlashLight()
            } else {
                turnOnFlashLight()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveDataFromHome()
        setUpRecyclerView()
        itemClick()

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textDetail.setOnClickListener {
            customDialog()
        }
        binding.counterBtn.setOnClickListener {
            sendData()
        }
        updateButtonState()
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

    private fun updateButtonState(){
        if(isRvItemsEmpty){
            binding.counterBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.isNotActiveButton))
            binding.counterBtn.isEnabled = false
        }else{
            binding.counterBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.isActiveButton))
            binding.counterBtn.isEnabled = true
        }
    }
    private fun customDialog() {
        val customDialogFragment = FirstDialogFragment()
        customDialogFragment.show(childFragmentManager, "custom_dialog")
    }

    private fun itemClick() {
        val numericButtons = listOf(
            binding.one, binding.two, binding.three, binding.four, binding.five,
            binding.six, binding.seven, binding.eight, binding.nine, binding.zero
        )

        numericButtons.forEach { button ->
            button.setOnClickListener {
                val value = button.text.toString()
                counterAdapter.updateValue(value)

                isRvItemsEmpty = counterAdapter.itemCount == 0
                updateButtonState()
            }
        }
        binding.icDelete.setOnClickListener {
            counterAdapter.resetValues()
            isRvItemsEmpty = counterAdapter.itemCount == 0
            updateButtonState()
        }

    }

    private fun setUpRecyclerView() {
        val razTipSchCount = task?.RAZ_TIPSCH ?: 0
        counterAdapter = CounterAdapter(razTipSchCount)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), razTipSchCount)
        binding.recyclerView.adapter = counterAdapter

        isRvItemsEmpty = counterAdapter.itemCount == 0
        updateButtonState()
    }

    private fun turnOnFlashLight() {
        try {
            if (cameraId.isNotEmpty()) {
                cameraManager.setTorchMode(cameraId, true)
                isFlashLightOn = true
                Log.d("CounterFragment", "turnOnFlashLight: true")
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun turnOffFlashLight() {
        try {
            if (cameraId.isNotEmpty()) {
                cameraManager.setTorchMode(cameraId, false)
                isFlashLightOn = false
                Log.d("CounterFragment", "turnOnFlashLight: false")
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun receiveDataFromHome() {
        task = arguments?.getParcelable<Tasks>("task")
        if (task != null) {
            binding.textAddress.text = task!!.ADDR
            binding.textLsNumber.text = task!!.LS.toString()
            binding.textCvNumber.text = task!!.NAME_SCH
            binding.textDate.text = task!!.POVERKA_DATE
        } else {
            Log.e("CounterFragment", "Task argument is null")
        }
    }

    private fun sendData() {
        val cameraFragment = CameraFragment()
        val bundle = Bundle()
        bundle.putParcelable("task", task)
        val filledValues = counterAdapter.getFilledValuesAsString()
        bundle.putString("filledValues", filledValues)
        Log.d(TAG, "sendData: $filledValues")
        cameraFragment.arguments = bundle

        val requiredLength = counterAdapter.razTipSchCount
        if (filledValues.length != requiredLength) {
            val snackBar = Snackbar.make(
                requireView(),
                "Заполните все ячейки полностью должно быть $requiredLength символов",
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }else{
            findNavController().navigate(R.id.action_id_counterFragment_to_cameraFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

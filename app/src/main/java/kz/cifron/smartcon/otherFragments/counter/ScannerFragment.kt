package kz.cifron.smartcon.otherFragments.counter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentScannerBinding
import java.io.IOException


class ScannerFragment : Fragment() {
    private var _binding : FragmentScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource : CameraSource
    var intentData = ""

    private var requestCamera : ActivityResultLauncher<String>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)

        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(),) {
            if (it) {
                findNavController().navigate(R.id.action_id_homeFragment_to_scannerFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "camera(scan) permission not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            initBarCode()

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun initBarCode(){
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(requireContext(),barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .build()
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try{
                    cameraSource.start(binding.surfaceView.holder)
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(requireContext(), "barcode scanner has been stopped", Toast.LENGTH_SHORT).show()
            }
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if(barcodes.size() != 0){
                    binding.cardViewText.post{
                        intentData = barcodes.valueAt(0).displayValue
                        binding.cardViewText.text = Editable.Factory.getInstance().newEditable(intentData)
                        findNavController().popBackStack()
                    }
                }
            }
        })
    }

}
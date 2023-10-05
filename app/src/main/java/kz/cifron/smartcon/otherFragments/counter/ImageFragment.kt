package kz.cifron.smartcon.otherFragments.counter

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentImageBinding
import kz.cifron.smartcon.feature_home.data.Tasks
import kz.cifron.smartcon.feature_result.presentation.ResultFragment

private const val TAG = "ImageFragment"

class ImageFragment : Fragment() {

    private var _binding : FragmentImageBinding? = null
    private val binding get() = _binding!!
    private var receivedTask: Tasks? = null
    private var filledValues : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        receivedTask = arguments?.getParcelable("task")
        filledValues = arguments?.getString("filledValues")
        Log.d(TAG, "getData $filledValues ")

        val imageUriString = arguments?.getString("imageUri")
        if (!imageUriString.isNullOrEmpty()) {
           val imageUri = Uri.parse(imageUriString)

            val imageView = binding.imageFromCamera
            Glide.with(requireContext())
                .load(imageUri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
        }

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnImageFragment.setOnClickListener {
            sendData()
        }
    }

    private fun sendData(){
        val resultFragment = ResultFragment()
        val bundle = Bundle()
        bundle.putParcelable("task",receivedTask)
        val imageUriString = arguments?.getString("imageUri")
        bundle.putString("imageUri", imageUriString)
        bundle.putString("filledValues",filledValues)
        resultFragment.arguments = bundle
        findNavController().navigate(R.id.action_imageFragment_to_resultFragment,bundle)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
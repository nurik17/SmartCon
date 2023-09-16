package kz.cifron.smartcon.presentation.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kz.cifron.smartcon.databinding.FragmentResultBinding
import kz.cifron.smartcon.presentation.home.Tasks
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var receivedTask: Tasks? = null
    private lateinit var viewModel: ResultViewModel
    private lateinit var apiService: ResultApi



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater,container,false)

        apiService = ResultRetrofitClient.resultInstanceApi

        viewModel = ViewModelProvider(this, ResultViewModelFactory(ResultRepository(apiService))
        )[ResultViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val receivedTask = arguments?.getParcelable<Tasks>("task")
        binding.textAddress.text = receivedTask!!.ADDR

        val imageUriString = arguments?.getString("imageUri")
        Glide.with(requireContext())
            .load(imageUriString)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.resultImage)


        binding.btnSend.setOnClickListener {
            //create image form-data
            val imageFile = File(imageUriString!!)
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val newInSchText = "126234".toRequestBody()
            val idRequestBody = receivedTask.id.toString().toRequestBody()

            viewModel.uploadDataToServer(idRequestBody, newInSchText, imagePart)
        }


        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            if (result == "Data has been updated") {
                Log.d("ResultFragment", "Success server")
            } else {
                Log.d("ResultFragment", "Error server")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
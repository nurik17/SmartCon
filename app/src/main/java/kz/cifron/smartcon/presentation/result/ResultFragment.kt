package kz.cifron.smartcon.presentation.result

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.io.File
class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
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

        val imageFile = Uri.parse(imageUriString).path?.let { File(it) }
        val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
        val imagePart = MultipartBody.Part.createFormData("image",imageFile.name,imageRequestBody)

        val id = receivedTask.id.toString()
        val textAsNumber = "65222"
        binding.btnSend.setOnClickListener {
            viewModel.uploadData(id,textAsNumber,imagePart)

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
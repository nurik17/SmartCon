package kz.cifron.smartcon.presentation.result

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentResultBinding
import kz.cifron.smartcon.feature_home.data.Tasks
import kz.cifron.smartcon.utils.Constant
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
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

        val filledValues = arguments?.getString("filled_values","")
        Log.d("ResultFragment", "filledValues received: $filledValues") // Вывод в лог

        binding.textNumber.text = filledValues



        val imageUriString = arguments?.getString("imageUri")

        Glide.with(requireContext())
            .load(imageUriString)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.resultImage)


        val id = receivedTask.id.toString()


        binding.btnSend.setOnClickListener {
            val imageFile = File(imageUriString)
            val text = "265469"
            viewModel.uploadData(imageFile,text, id)
            viewModel.uploadResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Log.d("ResultFragment", "Success 200")
                } else {
                    Log.d("ResultFragment", "Error 200")
                }
            }
        }

        binding.btnAgain.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_id_counterFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


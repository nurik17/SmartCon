package kz.cifron.smartcon.feature_result.presentation

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentResultBinding
import kz.cifron.smartcon.feature_home.data.Tasks
import kz.cifron.smartcon.feature_result.data.ResultDatabase
import kz.cifron.smartcon.feature_result.data.ResultEntity


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        val receivedTask = arguments?.getParcelable<Tasks>("task")
        binding.textAddress.text = receivedTask!!.ADDR

        val filledValues = arguments?.getString("filledValues")
        binding.textNumber.text = filledValues

        val imageUriString = arguments?.getString("imageUri")
        Glide.with(requireContext()).load(imageUriString).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.resultImage)


        binding.btnSend.setOnClickListener {
        }
        binding.btnAgain.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_id_counterFragment)
        }
    }


    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            filePath = it.getString(columnIndex)
        }
        return filePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



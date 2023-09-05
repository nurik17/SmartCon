package kz.cifron.smartcon.presentation.counter

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentCounterBinding
import kz.cifron.smartcon.databinding.FragmentImageBinding


class ImageFragment : Fragment() {

    private var _binding : FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUriString = arguments?.getString("imageUri")
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            val imageView = binding.imageFromCamera
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }

        binding.arrowBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnImageFragment.setOnClickListener {
            findNavController().navigate(R.id.action_imageFragment_to_resultFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
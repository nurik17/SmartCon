package kz.cifron.smartcon.presentation.counter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kz.cifron.smartcon.databinding.FragmentResultBinding
import kz.cifron.smartcon.presentation.home.Tasks


class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var receivedTask: Tasks? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater,container,false)
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
            .into(binding.resultImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package kz.cifron.smartcon.presentation.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentFirstDialogBinding

class FirstDialogFragment : DialogFragment() {
    private var _binding : FragmentFirstDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstDialogBinding.inflate(inflater, container, false)

        binding.textView1.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.action_firstDialogFragment_to_bottomSheetFragment)
        }
        return binding.root
    }

}

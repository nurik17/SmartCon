package kz.cifron.smartcon.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kz.cifron.smartcon.R
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kz.cifron.smartcon.presentation.home.HomeFragment

class SecondDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_second_dialog, container, false)

        val btnOtmena = rootView.findViewById<Button>(R.id.btn_otmena)
        val btnOk = rootView.findViewById<Button>(R.id.btn_ok)

        btnOtmena.setOnClickListener {
            dismiss()
        }

        btnOk.setOnClickListener {
            dismiss()
            val dialog1 = HomeFragment()
            findNavController().navigate(R.id.action_id_counterFragment_to_id_homeFragment)
        }

        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
        }
    }
}

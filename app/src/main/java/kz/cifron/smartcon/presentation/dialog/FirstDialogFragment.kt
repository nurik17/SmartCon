package kz.cifron.smartcon.presentation.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import kz.cifron.smartcon.R

class FirstDialogFragment : DialogFragment() {


        @SuppressLint("UseGetLayoutInflater")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val inflater = LayoutInflater.from(requireContext())
            val view = inflater.inflate(R.layout.fragment_first_dialog, null)

            val button1 = view.findViewById<TextView>(R.id.textView1)
            val button2 = view.findViewById<TextView>(R.id.textView2)
            val mainText = view.findViewById<TextView>(R.id.main_text)


            button1.setOnClickListener {
                val otherDialogFragment = BottomSheetFragment()
                otherDialogFragment.show(childFragmentManager, "other_dialog")
                //findNavController().navigate(R.id.action_firstDialogFragment_to_bottomSheetFragment)
                dismiss()
            }


            button2.setOnClickListener {
                val otherDialogFragment2 = SecondDialogFragment()
                otherDialogFragment2.show(childFragmentManager, "other_dialog2")
            }
            val builder = AlertDialog.Builder(requireContext())

            builder.setView(view)
            return builder.create()
        }
    }

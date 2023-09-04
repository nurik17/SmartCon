package kz.cifron.smartcon.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kz.cifron.smartcon.R

class SecondDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.fragment_second_dialog, null)
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(view)
        return builder.create()
    }
}
package kz.cifron.smartcon.otherFragments.dialog


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kz.cifron.smartcon.R

class FirstDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_first_dialog, container, false)

        val buttonOpenDialog1 = rootView.findViewById<TextView>(R.id.textView1)
        val buttonOpenDialog2 = rootView.findViewById<TextView>(R.id.textView2)

        buttonOpenDialog1.setOnClickListener {
            dismiss()
            val dialog1 = BottomSheetFragment()
            dialog1.show(parentFragmentManager, "dialog1")


        }

        buttonOpenDialog2.setOnClickListener {
            dismiss()
            val dialog2 = SecondDialogFragment()
            dialog2.show(parentFragmentManager, "dialog2")
        }

        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
        }
    }
}

package com.apkakisan.myapplication.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.apkakisan.myapplication.R

class AppDialogFragment : DialogFragment() {

    private lateinit var onDonePressed: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tvTitle).text = arguments?.getString(ARG_TITLE)
        view.findViewById<TextView>(R.id.tvMessage).text = arguments?.getString(ARG_MESSAGE)
        view.findViewById<AppCompatButton>(R.id.tvDone).setOnClickListener {
            onDonePressed()
            dismiss()
        }
    }

    fun onDonePressed(onDonePressed: () -> Unit) {
        this.onDonePressed = onDonePressed
    }

    companion object {
        val TAG: String = AppDialogFragment::class.java.simpleName

        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"

        fun newInstance(
            title: String,
            message: String
        ) = AppDialogFragment().apply {
            arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message
            )
        }
    }
}
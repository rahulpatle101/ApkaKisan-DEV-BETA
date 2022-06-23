package com.apkakisan.myapplication.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.apkakisan.myapplication.R

class OrderCreatedDialogFragment : DialogFragment() {

    private lateinit var onCreateAnotherOrderPressed: () -> Unit
    private lateinit var onDonePressed: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_order_created, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.tvCreateAnotherSellOrder).setOnClickListener {
            onCreateAnotherOrderPressed()
            dismiss()
        }

        view.findViewById<AppCompatButton>(R.id.tvDone).setOnClickListener {
            onDonePressed()
            dismiss()
        }
    }

    fun onCreateAnotherOrderPressed(onCreateAnotherOrderPressed: () -> Unit) {
        this.onCreateAnotherOrderPressed = onCreateAnotherOrderPressed
    }

    fun onCancelPressed(onDonePressed: () -> Unit) {
        this.onDonePressed = onDonePressed
    }

    companion object {
        val TAG: String = OrderCreatedDialogFragment::class.java.simpleName
        fun newInstance() = OrderCreatedDialogFragment()
    }
}
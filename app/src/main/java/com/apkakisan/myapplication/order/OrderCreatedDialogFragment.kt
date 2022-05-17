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
    private lateinit var onCancelPressed: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_order_created, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvCreateAnotherSellOrder =
            view.findViewById<AppCompatButton>(R.id.tvCreateAnotherSellOrder)
        tvCreateAnotherSellOrder.setOnClickListener {
            onCreateAnotherOrderPressed()
            dismiss()
        }

        val tvCancel = view.findViewById<AppCompatButton>(R.id.tvCancel)
        tvCancel.setOnClickListener {
            onCancelPressed()
            dismiss()
        }
    }

    fun onCreateAnotherOrderPressed(onCreateAnotherOrderPressed: () -> Unit) {
        this.onCreateAnotherOrderPressed = onCreateAnotherOrderPressed
    }

    fun onCancelPressed(onCancelPressed: () -> Unit) {
        this.onCancelPressed = onCancelPressed
    }

    companion object {
        val TAG: String = OrderCreatedDialogFragment::class.java.simpleName
        fun newInstance() = OrderCreatedDialogFragment()
    }
}
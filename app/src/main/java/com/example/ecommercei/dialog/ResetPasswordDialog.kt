package com.example.ecommercei.dialog

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ecommercei.R
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

@SuppressLint("MissingInflatedId")
fun Fragment.setupButtonSheetDialog(
    onSendClick:(String)->Unit,
){
    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStylle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.edResetPassword)
    val btnSend = view.findViewById<CircularProgressButton>(R.id.buttonSendResetPassword)
    val btnCancel = view.findViewById<CircularProgressButton>(R.id.buttonCancelResetPassword)

    btnSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
            onSendClick(email)
            dialog.dismiss()

    }
    btnCancel.setOnClickListener{
        dialog.dismiss()
    }
}
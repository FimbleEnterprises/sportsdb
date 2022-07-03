package com.fimbleenterprises.sportsdb

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment

/**
 * Basic dialog fragment to prompt the user to give us money and happiness for all parties!  I
 * mean, it would be dumb because this app is kinda dumb but a fool and his money are easily
 * parted!
 */
class PurchaseProDialogFragment
    constructor(
        private val decisionListener: OnPurchaseDecisionListener
    ) : DialogFragment() {

    interface OnPurchaseDecisionListener {
        fun purchased()
        fun declined()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.purchase_pro_version))
            .setPositiveButton(getString(R.string.yes)) { _,_ ->
                Toast.makeText(context, getString(R.string.thankyou), Toast.LENGTH_SHORT).show()
                decisionListener.purchased()
                Log.i(TAG,"-= PURCHASE CONFIRMED! =-")
                this.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) {_,_ ->
                Toast.makeText(context, getString(R.string.no_thankyou), Toast.LENGTH_SHORT).show()
                decisionListener.declined()
                Log.i(TAG, "-= PURCHASE DECLINED =-")
                this.dismiss()
            }
            .create()

    companion object {
        private const val TAG = "FIMTOWN|PurchaseConfirmationDialogFragment"
    }

    init {
        Log.i(TAG, "Initialized:PurchaseConfirmationDialogFragment")
    }

}
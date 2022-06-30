package com.fimbleenterprises.sportsdb

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

/**
 * NOT IMPLEMENTED YET!  PLACEHOLDER!  NEEDS WORK!
 * Was spending too much time to justify implementation.  Maybe in future i'll circle back.
 * FROM:
 * https://adapty.io/blog/android-in-app-purchases-google-play-billing-library-part-1
 * https://adapty.io/blog/android-in-app-purchases-google-play-billing-library-part-2
 */
class MyBillingWrapper(context: Context) : PurchasesUpdatedListener {

    interface OnQueryProductsListener {
        fun onSuccess(products: List<SkuDetails>)
        fun onFailure(error: Error)
    }

    class Error(val responseCode: Int, val debugMessage: String)

    private val billingClient = BillingClient
        .newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    /**
     * Returns our available in-app products (or subscriptions)
     */
    fun queryProducts(listener: OnQueryProductsListener) : Unit {
        val skusList = listOf(PRO_VER)
        queryProductsForType(
            skusList,
            BillingClient.SkuType.SUBS
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val products = skuDetailsList ?: mutableListOf()
                queryProductsForType(
                    skusList,
                    BillingClient.SkuType.INAPP
                ) { billingRslt: BillingResult, skuDetails: MutableList<SkuDetails>? ->
                    if (billingRslt.responseCode == BillingClient.BillingResponseCode.OK) {
                        products.addAll(skuDetails ?: listOf())
                        listener.onSuccess(products)
                    } else {
                        listener.onFailure(
                            Error(billingRslt.responseCode, billingRslt.debugMessage)
                        )
                    }
                }
            } else {
                listener.onFailure(
                    Error(billingResult.responseCode, billingResult.debugMessage)
                )
            }
        }
    }

    private fun queryProductsForType(
        skusList: List<String>,
        @BillingClient.SkuType type: String,
        listener: SkuDetailsResponseListener
    ) {
        onConnected {
            billingClient.querySkuDetailsAsync(
                SkuDetailsParams.newBuilder().setSkusList(skusList).setType(type).build(),
                listener
            )
        }

    }

    private fun onConnected(block: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                block()
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun purchase(activity: Activity, product: SkuDetails) {
        onConnected {
            activity.runOnUiThread {
                billingClient.launchBillingFlow(
                    activity,
                    BillingFlowParams.newBuilder().setSkuDetails(product).build()
                )
            }
        }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {

        Log.i(TAG, "-=BillingClientWrapper:onPurchasesUpdated  =-")
    }

    companion object {
        private const val TAG = "FIMTOWN|BillingClientWrapper"
        private const val PRO_VER = "sportsdb_pro_1"
    }

    init {
        Log.i(TAG, "Initialized:BillingClientWrapper")
    }
}
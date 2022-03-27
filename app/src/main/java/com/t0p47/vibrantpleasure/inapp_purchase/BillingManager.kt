package com.t0p47.vibrantpleasure.inapp_purchase

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.android.billingclient.api.*
import com.t0p47.vibrantpleasure.VibroApp
import com.t0p47.vibrantpleasure.utils.SingleLiveEvent
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingManager @Inject constructor (private var app: VibroApp): LifecycleObserver{

    private lateinit var billingClient: BillingClient

    val skusWithSkuDetails = MutableLiveData<Map<String, SkuDetails>>()

    val purchaseUpdateEvent = SingleLiveEvent<Purchase>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create(){
        billingClient = BillingClient.newBuilder(app.applicationContext)
                .setListener(purchaseUpdatedListener)
                .enablePendingPurchases()
                .build()
        if(!billingClient.isReady){
            startConnection()
        }
    }

    private val purchaseUpdatedListener = PurchasesUpdatedListener{ billingResult, purchases ->
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.d(LOG_TAG,"BillingManagerNew: onPurchasesUpdated: responseCode: $responseCode, debugMessage: $debugMessage")
        when(responseCode){
            BillingClient.BillingResponseCode.OK ->{
                if(purchases != null){
                    for(purchase in purchases){
                        handleNonConsumablePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(LOG_TAG, "BillingManagerNew: onPurchasesUpdated: User canceled the purchase")
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(LOG_TAG, "BillingManagerNew: onPurchasesUpdated: The user already owns this item")
            }
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                Log.d(LOG_TAG, "BillingManagerNew: onPurchasesUpdated: Developer error means that Google Play " +
                        "does not recognize the configuration. If you are just getting started, " +
                        "make sure you have configured the application correctly in the " +
                        "Google Play Console. The SKU product ID must match and the APK you " +
                        "are using must be signed with release keys."
                )
            }
        }
    }

    private fun handleNonConsumablePurchase(purchase: Purchase){
        Log.d(LOG_TAG, "BillingManager: handleNonConsumablePurchase: $purchase")
        if(purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
            if(!purchase.isAcknowledged){
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams){ billingResult ->
                    val responseCode = billingResult.responseCode
                    val debugMessage = billingResult.debugMessage

                    purchaseUpdateEvent.postValue(purchase)

                    Log.d(LOG_TAG, "BillingManager: handleNonConsumablePurchase: responseCode: $responseCode, debugMessage: $debugMessage")
                }
            }
        }
    }

    private fun startConnection(){
        billingClient.startConnection(object: BillingClientStateListener{
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    Log.d(LOG_TAG,"BillingManager: startConnection: setupBillingDone")
                    queryAvailableProducts()
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(LOG_TAG, "BillingManagerNew: onBillingServiceDisconnected")
                // TODO: Try connecting again with exponential backoff.
            }
        })
    }

    private fun queryPurchases(){
        if(!billingClient.isReady){
            Log.d(LOG_TAG, "BillingManagerNew: queryPurchases: BillingClient is not ready")
        }
        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        val responseCode = result.responseCode
        val billingResultDebugMessage = result.billingResult.debugMessage
        val billingResultResponseCode = result.billingResult.responseCode
        val purchasesList = result.purchasesList

        Log.d(LOG_TAG, "BillingManagerNew: queryPurchases: responseCode: $responseCode, " +
                "billingResultDebugMessage: $billingResultDebugMessage, " +
                "billingResultResponseCode: $billingResultResponseCode, " +
                "purchasesList: $purchasesList")

        if(result.purchasesList != null && result.purchasesList?.isNotEmpty() == true){
            purchaseUpdateEvent.postValue(result.purchasesList!![0])
        }
    }

    private fun queryAvailableProducts(){
        val skuList = ArrayList<String>()
        skuList.add("update_to_professional")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()){ billingResult, skuDetailsList ->

            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage

            when(billingResult.responseCode){
                BillingClient.BillingResponseCode.OK -> {
                    Log.d(LOG_TAG, "BillingManager: queryAvailableProducts: responseCode: $responseCode, debug: $debugMessage")
                    skusWithSkuDetails.postValue(HashMap<String, SkuDetails>().apply {
                        if(skuDetailsList != null){
                            Log.d(LOG_TAG, "BillingManager: queryAvailableProducts: skuDetailsList: $skuDetailsList")
                            for(detail in skuDetailsList){
                                put(detail.sku, detail)
                            }
                        }
                    })
                }
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
                BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
                BillingClient.BillingResponseCode.DEVELOPER_ERROR,
                BillingClient.BillingResponseCode.ERROR -> {
                    Log.d(LOG_TAG, "BillingManager: queryAvailableProducts(ServiceError): responseCode: $responseCode, debug: $debugMessage")
                }
                BillingClient.BillingResponseCode.USER_CANCELED,
                BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    // These response codes are not expected.
                    Log.d(LOG_TAG, "BillingManager: queryAvailableProducts(Canceled): responseCode: $responseCode, debug: $debugMessage")
                }
            }
        }
    }

    fun buyProVersion(activity: Activity, sku: String){
        Log.d(LOG_TAG, "BillingManager: buyProVersion: sku: $sku")
        val skuDetail = skusWithSkuDetails.value?.get(sku)?: run{
            Log.d(LOG_TAG, "BillingManager: buyProVersion: no skuDetail found")
            return
        }

        Log.d(LOG_TAG, "BillingManager: buyProVersion: skuDetail: $skuDetail")

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetail)
            .build()
        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage

        Log.d(LOG_TAG,"BillingManagerNew: launchBillingFlow: billingResponse: responseCode: $responseCode, debugMessage: $debugMessage")
    }

}
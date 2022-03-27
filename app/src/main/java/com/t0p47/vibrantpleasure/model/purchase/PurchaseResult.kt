package com.t0p47.vibrantpleasure.model.purchase

import com.android.billingclient.api.Purchase

data class PurchaseResult(
    var responseCode: Int,
    var purchases: List<Purchase>
)
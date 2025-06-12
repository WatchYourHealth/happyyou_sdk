package com.wyh.happyyousdk.model.response

data class HappyMartCategoryResponse(val msg: String, val success: Boolean,val data: ArrayList<CategoryData>)

data class CategoryData(val happyMartSubCategoryResponse: HappyMartSubCategoryResponse)

data class HappyMartSubCategoryResponse (

   var id: Int?= null,
   var vendorName: String?= null,
   var vendorLogo: String? = null,
   var redirectionUrl: String?= null,
   var happyMartSubCategoryChildResponse : ArrayList<HappyMartSubCategoryChildResponse> = arrayListOf(),
   var isParent: Boolean?= null,
   var parentId: Int?= null

)


data class HappyMartSubCategoryChildResponse (

    var id             : Int?     = null,
    var vendorName     : String?  = null,
    var vendorLogo     : String?  = null,
    var redirectionUrl : String?  = null,
    var isParent       : Boolean? = null

)

package com.wyh.happyyousdk.model.response

data class GetCategoryResponse(var msg: String, var success: Boolean, val data: CategoryData)

data class CategoryData2(
    var categories: ArrayList<Category>,
    var isbookingAllowed: Boolean,
    var userConsultationDetails: ArrayList<ConsultationDetails>
)

data class Category(
    val categoryName: String,
    val categoryId: Int,
    val subCategories: ArrayList<SubCategory>
)

data class SubCategory(val subCategoryName: String, val categoryId: Int, val subCategoryId: Int)

data class ConsultationDetails(
    var description: String,
    var category: String,
    var documentPath: String,
    var expertAdvice: String,
    var uploadDate: String,
    var subCategory: String?,
    var status: String
)

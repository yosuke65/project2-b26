package com.example.project2.models

data class UploadImageResponse(
    val `data`: Upload,
    val error: Boolean,
    val message: String
)

data class Upload(
    val acl: String,
    val bucket: String,
    val contentDisposition: Any,
    val contentType: String,
    val encoding: String,
    val etag: String,
    val fieldname: String,
    val key: String,
    val location: String,
    val metadata: Metadata,
    val mimetype: String,
    val originalname: String,
    val serverSideEncryption: Any,
    val size: Int,
    val storageClass: String
)

data class Metadata(
    val fieldName: String
)
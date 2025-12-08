package com.documentconverter.service

interface LibreOfficeConverter {
    suspend fun convertDocxToPdf(docx: ByteArray): ByteArray
}

package com.documentconverter.web

import com.documentconverter.service.LibreOfficeConverter
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConvertController(
    private val libreOfficeConverter: LibreOfficeConverter
) {

    private val log = KotlinLogging.logger {}

    @PostMapping(
        "/convert/docx-to-pdf",
        consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE],
//        produces = [MediaType.APPLICATION_PDF_VALUE]
    )
    suspend fun convertDocxToPdf(@RequestBody docx: ByteArray): ResponseEntity<ByteArray> {
        val pdfBytes = libreOfficeConverter.convertDocxToPdf(docx)
        log.info { "pdfBytes $pdfBytes \n "}
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes)
    }
}

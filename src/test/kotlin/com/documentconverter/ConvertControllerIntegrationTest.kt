//package com.documentconverter
//
//import org.junit.jupiter.api.Assertions.assertArrayEquals
//import org.junit.jupiter.api.Assertions.assertFalse
//import org.junit.jupiter.api.Assertions.fail
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.core.io.ClassPathResource
//import org.springframework.http.MediaType
//import org.springframework.test.web.reactive.server.WebTestClient
//import org.springframework.beans.factory.annotation.Autowired
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//class ConvertControllerIntegrationTest @Autowired constructor(
//    private val webTestClient: WebTestClient
//) {
//
//    @Test
//    fun `should convert docx to pdf`() {
//        val resource = ClassPathResource("sample.docx")
//        val docxBytes = resource.inputStream.use { it.readAllBytes() }
//
//        val responseBody = webTestClient.post()
//            .uri("/convert/docx-to-pdf")
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .bodyValue(docxBytes)
//            .exchange()
//            .expectStatus().isOk
//            .expectHeader().contentType(MediaType.APPLICATION_PDF)
//            .expectBody()
//            .returnResult()
//            .responseBody
//
//        val body = responseBody ?: fail("Response body should not be null")
//        assertFalse(body.isEmpty(), "PDF content should not be empty")
//
//        val expectedHeader = byteArrayOf(0x25, 0x50, 0x44, 0x46)
//        val actualHeader = body.take(expectedHeader.size).toByteArray()
//        assertArrayEquals(expectedHeader, actualHeader, "PDF should start with %PDF header")
//    }
//}

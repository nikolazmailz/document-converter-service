package com.documentconverter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.core.io.ClassPathResource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.HttpURLConnection
import java.net.URL

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SystemDocumentConverterTest {

    companion object {
        // Используем уже собранный образ
        @Container
        @JvmStatic
        val converterContainer: GenericContainer<*> =
            GenericContainer("document-converter-service:latest")
                .withExposedPorts(8080)
    }

    @Test
    fun `should convert real docx to pdf via docker container`() {
        // 1. Берём тестовый DOCX из ресурсов
        val resource = ClassPathResource("sample.docx")
        val docxBytes = resource.inputStream.use { it.readBytes() }

        // 2. Собираем URL до контейнера
        val host = converterContainer.host
        val port = converterContainer.getMappedPort(8080)
        val url = URL("http://$host:$port/convert/docx-to-pdf")

        // 3. Открываем HTTP-соединение и шлём байты DOCX
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/octet-stream")
        }

        connection.outputStream.use { os ->
            os.write(docxBytes)
        }

        // 4. Читаем ответ
        val status = connection.responseCode
        assertEquals(200, status, "Expected 200 OK from document-converter-service")

        val contentType = connection.getHeaderField("Content-Type")
        // Важно: может быть "application/pdf;charset=UTF-8" — поэтому contains
        assertTrue(contentType?.contains("application/pdf") == true) {
            "Expected Content-Type to contain application/pdf but was: $contentType"
        }

        val pdfBytes = connection.inputStream.use { it.readBytes() }

        // 5. Проверяем, что это похоже на PDF
        assertTrue(pdfBytes.isNotEmpty(), "PDF response is empty")

        // PDF начинается с "%PDF"
        val prefix = pdfBytes.take(4).toByteArray()
        assertTrue(prefix.contentEquals("%PDF".toByteArray())) {
            "Response does not look like a PDF, prefix: ${prefix.decodeToString()}"
        }
    }

}
package com.documentconverter.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import java.util.Comparator
import java.nio.file.Files
import java.util.concurrent.TimeUnit

@Component
class LibreOfficeCliConverter : LibreOfficeConverter {
    override suspend fun convertDocxToPdf(docx: ByteArray): ByteArray = withContext(Dispatchers.IO) {
        val tempDir = Files.createTempDirectory("libreoffice-converter")
        val outputDir = Files.createDirectories(tempDir.resolve("output"))
        val inputFile = tempDir.resolve("input.docx")

        try {
            Files.write(inputFile, docx)

            val process = ProcessBuilder(
                "soffice",
                "--headless",
                "--nologo",
                "--convert-to",
                "pdf",
                "--outdir",
                outputDir.toAbsolutePath().toString(),
                inputFile.toAbsolutePath().toString()
            ).redirectErrorStream(true).start()

            val finished = process.waitFor(60, TimeUnit.SECONDS)
            if (!finished) {
                process.destroyForcibly()
                throw IllegalStateException("LibreOffice conversion timeout")
            }

            if (process.exitValue() != 0) {
                val output = process.inputStream.bufferedReader().use { it.readText() }
                throw IllegalStateException("LibreOffice conversion failed: $output")
            }

            val pdfFile = Files.list(outputDir)
                .filter { path -> path.fileName.toString().endsWith(".pdf", ignoreCase = true) }
                .findFirst()
                .orElseThrow { IllegalStateException("No PDF file generated") }
                .toFile()

            Files.readAllBytes(pdfFile.toPath())
        } finally {
            Files.walk(tempDir).use { paths ->
                paths.sorted(Comparator.reverseOrder())
                    .forEach { path ->
                        Files.deleteIfExists(path)
                    }
            }
        }
    }
}

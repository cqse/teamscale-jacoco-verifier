import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

enum class VerificationResult(val userReadableDescription: String) {

    SUCCESS("The report looks valid"),
    NO_COVERAGE_AT_ALL(
        """
            None of the classes in your coverage report have any coverage.
            You probably set "includes"/"excludes" patterns that are too restrictive.
        """.trimIndent()
    ),
    NO_COVERAGE_IN_PACKAGE(
        """
            The report contains coverage, but not in your package.
            You probably set "includes"/"excludes" patterns that are too restrictive.
        """.trimIndent()
    ),
    PACKAGE_NOT_IN_REPORT(
        """
            Your package is not contained in the report.
            You probably provided an incorrect "class dir".
        """.trimIndent()),
    NOT_A_JACOCO_REPORT("This file does not look like a JaCoCo report at all")

}

object CoverageFileVerifier {

    private val PACKAGE_NAME_REGEX = "^([a-z_][a-z0-9_]*)([.][a-z_][a-z0-9_]*)*$".toRegex(RegexOption.IGNORE_CASE)

    fun isValidPackageName(packageName: String) = PACKAGE_NAME_REGEX.matches(packageName)

    private class Handler(val packageName: String) : DefaultHandler() {

        var hasCoverageAtAll = false
        var hasCoverageInPackage = false
        var foundPackage = false
        var isProbablyValidReport = false

        private var isInPackage = false

        override fun resolveEntity(publicId: String?, systemId: String?): InputSource {
            return InputSource()
        }

        override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes) {
            val covered = attributes.getValue("covered")
            if (covered != "0" && covered != null) {
                hasCoverageAtAll = true
                if (isInPackage) {
                    hasCoverageInPackage = true
                }
            }

            when (qName) {
                "counter" -> isProbablyValidReport = true
                "package" -> {
                    val currentPackageName = attributes.getValue("name")?.replace("/".toRegex(), ".") ?: ""
                    isInPackage = currentPackageName.startsWith("$packageName.") || currentPackageName == packageName
                    if (isInPackage) {
                        foundPackage = true
                    }
                }
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            when (qName) {
                "package" -> isInPackage = false
            }
        }

    }

    fun verify(xmlContent: String, packageName: String): VerificationResult {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        val handler = Handler(packageName)

        try {
            parser.parse(xmlContent.byteInputStream(), handler)
        } catch (e: Exception) {
            return VerificationResult.NOT_A_JACOCO_REPORT
        }

        return when {
            !handler.isProbablyValidReport -> VerificationResult.NOT_A_JACOCO_REPORT
            !handler.foundPackage -> VerificationResult.PACKAGE_NOT_IN_REPORT
            !handler.hasCoverageAtAll -> VerificationResult.NO_COVERAGE_AT_ALL
            !handler.hasCoverageInPackage -> VerificationResult.NO_COVERAGE_IN_PACKAGE
            else -> VerificationResult.SUCCESS
        }
    }

}
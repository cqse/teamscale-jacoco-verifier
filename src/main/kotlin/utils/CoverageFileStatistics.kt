package utils
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.xml.parsers.SAXParserFactory
import kotlin.math.min

object CoverageFileStatistics {

    private class Handler() : DefaultHandler() {

        var currentPrefix = ""
        var coveredMethods = 0
        var missedMethods = 0
        var coveredByPrefix = HashMap<String, Int>()
        var missedByPrefix = HashMap<String, Int>()

        override fun resolveEntity(publicId: String?, systemId: String?): InputSource {
            return InputSource(ByteArrayInputStream(byteArrayOf()))
        }

        override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes) {
            when (qName) {
                "counter" -> {
                    if (attributes.getValue("type") == "METHOD") {
                        coveredMethods = Integer.valueOf(attributes.getValue("covered"))
                        missedMethods = Integer.valueOf(attributes.getValue("missed"))
                    }
                }
                "package" -> {
                    val currentPackageName = attributes.getValue("name")?.replace("/".toRegex(), ".") ?: ""
                    val parts = currentPackageName.split(".")
                    currentPrefix = parts.subList(0, min(parts.size, 3)).joinToString(".")
                }
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            when (qName) {
                "package" -> {
                    // last method counter before package closes is package summary
                    coveredByPrefix.merge(currentPrefix, coveredMethods, Integer::sum)
                    missedByPrefix.merge(currentPrefix, missedMethods, Integer::sum)
                }
            }
        }

        fun printStatistics() {
            coveredByPrefix.keys.sorted().forEach { prefix ->
                val covered = coveredByPrefix[prefix]
                val total = covered?.let { missedByPrefix[prefix]?.plus(it) }
                println("$prefix : $covered / $total")
            }
        }
    }

    fun print(xmlStream: InputStream) {
        val handler = Handler()
        SAXParserFactory.newInstance().newSAXParser().parse(xmlStream, handler)
        handler.printStatistics()
    }
}
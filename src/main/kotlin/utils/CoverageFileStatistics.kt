package utils
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.PrintStream
import javax.xml.parsers.SAXParserFactory
import kotlin.math.min

/**
 * Builds some basic statistics for a JaCoCo XML report. In particular, prints out all package
 * prefixes of length 3, and the number of executed methods as well as total method count.
 */
object CoverageFileStatistics {

    private class Handler() : DefaultHandler() {

        /** Prefix of the package name last recently read from the XML. */
        private var currentPackagePrefix = ""

        /** The number of covered methods last recently read from the XML. */
        private var coveredMethods = 0

        /** The number of missed methods last recently read from the XML. */
        private var missedMethods = 0

        /** Aggregated number of covered methods by package prefix */
        private val coveredByPrefix = HashMap<String, Int>()

        /** Aggregated number of missed methods by package prefix */
        private val missedByPrefix = HashMap<String, Int>()

        /** Returns an empty InputSource, because we do not need external resolving. */
        override fun resolveEntity(publicId: String?, systemId: String?): InputSource {
            return InputSource(ByteArrayInputStream(byteArrayOf()))
        }

        override fun startElement(uri: String?, localName: String?, qualifiedName: String, attributes: Attributes) {
            when (qualifiedName) {
                "counter" -> {
                    if (attributes.getValue("type") == "METHOD") {
                        coveredMethods = attributes.getValue("covered").toInt();
                        missedMethods = attributes.getValue("missed").toInt();
                    }
                }
                "package" -> {
                    currentPackagePrefix = getPackagePrefix(attributes.getValue("name"), 3)
                }
            }
        }

        /**
         * Converts a JaCoCo package descriptor (e.g. "eu/cqse/teamscale/service") to a Java package prefix of
         * the given length (e.g. "eu.cqse.teamscale" for length 3).
         */
        private fun getPackagePrefix(name: String?, length: Int): String {
            val currentPackageName = name?.replace("/".toRegex(), ".") ?: return ""
            val parts = currentPackageName.split(".")
            return parts.subList(0, min(parts.size, length)).joinToString(".")
        }

        override fun endElement(uri: String?, localName: String?, qualifiedName: String) {
            when (qualifiedName) {
                "package" -> {
                    // last method counter before package closes is package summary
                    coveredByPrefix.merge(currentPackagePrefix, coveredMethods, Integer::sum)
                    missedByPrefix.merge(currentPackagePrefix, missedMethods, Integer::sum)
                }
            }
        }

        fun printStatistics(out: PrintStream) {
            for (prefix in coveredByPrefix.keys.sorted()) {
                val covered = coveredByPrefix[prefix]!!
                val total = covered + missedByPrefix[prefix]!!
                out.println("$prefix : $covered / $total")
            }
            val covered = coveredByPrefix.values.sum();
            val total = covered + missedByPrefix.values.sum();
            out.println("-------------------\nTOTAL : $covered / $total")
        }
    }

     fun print(xmlStream: InputStream, out: PrintStream) {
        val handler = Handler()
        SAXParserFactory.newInstance().newSAXParser().parse(xmlStream, handler)
        handler.printStatistics(out)
    }
}
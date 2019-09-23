import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import utils.CoverageFileStatistics
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CoverageFileStatisticsTest {

    @Test
    fun `single short package`() {
        val out = ByteArrayOutputStream()

        CoverageFileStatistics.print("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"></method></class><counter type="METHOD" missed="3" covered="3"/></package></group></report>
        """.trimIndent().byteInputStream(), PrintStream(out))
        assertk.assert(out.toString().lines()).contains("com.mypackage : 3 / 6")
    }

    @Test
    fun `single long package`() {
        val out = ByteArrayOutputStream()

        CoverageFileStatistics.print("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/some/long/package"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"></method></class><counter type="METHOD" missed="2" covered="4"/></package></group></report>
        """.trimIndent().byteInputStream(), PrintStream(out))
        assertk.assert(out.toString().lines()).contains("com.some.long : 4 / 6")
    }

    @Test
    fun `multiple packages with total`() {
        val out = ByteArrayOutputStream()

        CoverageFileStatistics.print("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"></method></class><counter type="METHOD" missed="3" covered="3"/></package><package name="com/some/long/package"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"></method></class><counter type="METHOD" missed="2" covered="4"/></package></group></report>
        """.trimIndent().byteInputStream(), PrintStream(out))
        assertk.assert(out.toString().lines()).containsAll("com.mypackage : 3 / 6", "com.some.long : 4 / 6", "TOTAL : 7 / 12")
    }

}
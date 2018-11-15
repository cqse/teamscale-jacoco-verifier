import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CoverageFileVerifierTest {

    @Test
    fun `valid file with coverage in package`() {
        val result = CoverageFileVerifier.verify("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"><counter type="INSTRUCTION" missed="3" covered="3"/></method></class></package></group></report>
        """.trimIndent(), "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.SUCCESS)
    }

    @Test
    fun `valid file with doctype`() {
        val result = CoverageFileVerifier.verify("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><!DOCTYPE report PUBLIC "-//JACOCO//DTD Report 1.0//EN" "report.dtd"><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"><counter type="INSTRUCTION" missed="3" covered="3"/></method></class></package></group></report>
        """.trimIndent(), "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.SUCCESS)
    }

    @Test
    fun `package not in report`() {
        val result = CoverageFileVerifier.verify("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"></group><counter type="INSTRUCTION" missed="3" covered="0"/></report>
        """.trimIndent(), "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.PACKAGE_NOT_IN_REPORT)
    }

    @Test
    fun `no coverage at all`() {
        val result = CoverageFileVerifier.verify("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"><counter type="INSTRUCTION" missed="3" covered="0"/></method></class></package></group></report>
        """.trimIndent(), "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.NO_COVERAGE_AT_ALL)
    }

    @Test
    fun `no coverage in package`() {
        val result = CoverageFileVerifier.verify("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?><report name="test"><group name="coverageTest"><package name="com/mypackage"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"><counter type="INSTRUCTION" missed="3" covered="0"/></method></class></package><package name="com/other"><class name="com/acme/Customer"><method name="&lt;init&gt;" desc="()V" line="3"><counter type="INSTRUCTION" missed="3" covered="1"/></method></class></package></group></report>
        """.trimIndent(), "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.NO_COVERAGE_IN_PACKAGE)
    }

    @Test
    fun `totally invalid file`() {
        val result = CoverageFileVerifier.verify("invalid file content", "com.mypackage")
        assertk.assert(result).isEqualTo(VerificationResult.NOT_A_JACOCO_REPORT)
    }

}
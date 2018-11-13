enum class VerificationResult {

    SUCCESS, NO_COVERAGE_AT_ALL, NO_COVERAGE_IN_PACKAGE, PACKAGE_NOT_IN_REPORT

}

object CoverageFileVerifier {

    private val PACKAGE_NAME_REGEX = "^([a-z_][a-z0-9_]*)([.][a-z_][a-z0-9_]*)*$".toRegex(RegexOption.IGNORE_CASE)

    fun isValidPackageName(packageName:String) = PACKAGE_NAME_REGEX.matches(packageName)

    fun verify() : VerificationResult {
        return VerificationResult.SUCCESS
    }

}
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import utils.addOnChangeListener
import java.awt.Dimension
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame

private enum class EPackageNameValidity {
    NOT_SET, VALID, INVALID
}

private data class ViewModel(
    val verificationResult: VerificationResult?,
    val selectedFile: String?,
    val packageName: String?
) {

    val packageNameValidity
        get() = when {
            packageName == null -> EPackageNameValidity.NOT_SET
            CoverageFileVerifier.isValidPackageName(packageName) -> EPackageNameValidity.VALID
            else -> EPackageNameValidity.INVALID
        }

}

class MainController(private val main: Main) : CoroutineScope {

    override val coroutineContext = Dispatchers.Swing

    private var viewModel = ViewModel(
        verificationResult = null,
        selectedFile = null,
        packageName = null
    )
        set(value) {
            if (value == field) {
                return
            }

            field = value
            update()
        }

    fun run() {
        JFrame("JaCoCo Coverage File Verifier").apply {
            contentPane = main.root
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            minimumSize = Dimension(600, 400)

            pack()
            isVisible = true
        }

        main.verifyCoverageFileButton.addActionListener {
            launch(Dispatchers.Default) {
                val packageName = viewModel.packageName ?: return@launch
                val xmlContent = File(viewModel.selectedFile).readText()
                val result = CoverageFileVerifier.verify(xmlContent, packageName)
                viewModel = viewModel.copy(verificationResult = result)
            }
        }

        main.packageText.addOnChangeListener {
            viewModel = viewModel.copy(packageName = it)
        }

        main.selectButton.addActionListener {
            val chooser = JFileChooser()
            if (chooser.showOpenDialog(main.root) == JFileChooser.APPROVE_OPTION) {
                viewModel = viewModel.copy(selectedFile = chooser.selectedFile.absolutePath)
            }
        }
    }

    private fun update() {
        launch(Dispatchers.Swing) {
            main.resultLabel.text = when (viewModel.verificationResult) {
                null -> ""
                VerificationResult.SUCCESS -> "success"
                else -> "failed"
            }

            main.coverageFileLabel.text = when (viewModel.selectedFile) {
                null -> "(please select)"
                else -> viewModel.selectedFile
            }

            main.verifyCoverageFileButton.isEnabled = when (viewModel.packageNameValidity) {
                EPackageNameValidity.VALID -> true
                else -> false
            }

            if (viewModel.packageNameValidity == EPackageNameValidity.INVALID) {
                main.resultLabel.text = "Invalid package name: ${viewModel.packageName}"
            }
        }
    }

}
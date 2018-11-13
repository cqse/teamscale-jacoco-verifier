import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import utils.addOnChangeListener
import java.awt.Color
import java.awt.Dimension
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.filechooser.FileNameExtensionFilter



private data class ViewModel(
    val verificationResult: VerificationResult?,
    val selectedFile: String?,
    val packageName: String
) {

    val packageNameIsValid
        get() = CoverageFileVerifier.isValidPackageName(packageName)

}

class MainController(private val main: MainForm) : CoroutineScope {

    override val coroutineContext = Dispatchers.Swing

    private var viewModel = ViewModel(
        verificationResult = null,
        selectedFile = null,
        packageName = "com.yourpackage"
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
            chooser.fileFilter = FileNameExtensionFilter("JaCoCo XML reports", "xml")
            if (chooser.showOpenDialog(main.root) == JFileChooser.APPROVE_OPTION) {
                viewModel = viewModel.copy(selectedFile = chooser.selectedFile.absolutePath)
            }
        }

        update()
    }

    private fun update() {
        launch(Dispatchers.Swing) {
            val userReadableDescription = viewModel.verificationResult?.userReadableDescription ?: ""
            val htmlDescription = userReadableDescription.replace("\n".toRegex(), "<br/>")
            main.resultLabel.text = "<html>$htmlDescription</html>"

            main.resultLabel.foreground = when (viewModel.verificationResult) {
                VerificationResult.SUCCESS -> Color.GREEN
                else -> Color.RED
            }

            main.coverageFileLabel.text = viewModel.selectedFile ?: "(please select a file)"

            main.verifyCoverageFileButton.isEnabled = viewModel.packageNameIsValid && viewModel.selectedFile != null

            if (!viewModel.packageNameIsValid) {
                main.resultLabel.text = "Invalid package name: ${viewModel.packageName}"
            }
        }
    }


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size == 2) {
                val coverageFile = args[0]
                val packageName = args[1]
                println("Verifying coverage report $coverageFile and looking for package $packageName...")
                val result = CoverageFileVerifier.verify(File(coverageFile).readText(), packageName)
                println(result.userReadableDescription)
                return
            }

            println("Starting GUI...")
            MainController(MainForm()).run()
        }
    }
}

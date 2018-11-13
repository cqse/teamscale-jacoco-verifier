package utils

import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun JTextField.addOnChangeListener(listener: (String) -> Unit) {
    document.addDocumentListener(object : DocumentListener {
        override fun changedUpdate(event: DocumentEvent) {
            listener(text)
        }

        override fun insertUpdate(event: DocumentEvent) {
            listener(text)
        }

        override fun removeUpdate(event: DocumentEvent) {
            listener(text)
        }
    })
}
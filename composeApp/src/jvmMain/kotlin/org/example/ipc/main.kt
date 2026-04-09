package org.example.ipc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Inter Process Communication Debugger",
    ) {
        App()
    }
}
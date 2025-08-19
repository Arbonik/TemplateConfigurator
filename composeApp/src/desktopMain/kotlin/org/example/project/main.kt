package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import project.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(placement = WindowPlacement.Maximized),
        title = "H5 Lobby - Template Editor",
    ) {
        App()
    }
}
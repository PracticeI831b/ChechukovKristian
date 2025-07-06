import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.MainScreen

/**
 * Точка входа в приложение
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Решение кубических уравнений"
    ) {
        MainScreen()
    }
}
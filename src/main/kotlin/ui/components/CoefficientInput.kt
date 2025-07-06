package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Компонент для ввода коэффициентов уравнения
 *
 * @param label Подпись поля ввода
 * @param value Текущее значение коэффициента
 * @param onValueChange Обработчик изменения значения
 */
@Composable
fun CoefficientInput(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Подпись поля
        Text(
            label,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Поле ввода
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Фильтрация ввода: разрешаем только цифры, минус, точки и запятые
                val filtered = newValue.filter {
                    it.isDigit() || it == '-' || it == '.' || it == ','
                }
                onValueChange(filtered)
            },
            singleLine = true,
            placeholder = {
                Text(
                    "Например, 1.0",
                    color = Color(0xFF666666)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colors.primary,
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = Color(0xFF444444)
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )
    }
}
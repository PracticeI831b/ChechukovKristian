package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import math.findAllRoots
import math.findNegativeRoots
import ui.components.CoefficientInput
import ui.theme.DarkColorPalette

/**
 * Главный экран приложения для решения кубических уравнений
 */
@Composable
fun MainScreen() {
    // Состояния для коэффициентов уравнения
    var a by remember { mutableStateOf("1.0") }
    var b by remember { mutableStateOf("1.0") }
    var c by remember { mutableStateOf("1.0") }
    var d by remember { mutableStateOf("1.0") }

    // Состояния для результатов вычислений
    var negativeRoots by remember { mutableStateOf(emptyList<Double>()) }
    var allRoots by remember { mutableStateOf(emptyList<Double>()) }
    var error by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Для выпадающего меню

    // Применяем цветовую тему
    MaterialTheme(colors = DarkColorPalette) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                HeaderSection()

                // Формула уравнения
                EquationFormula()

                // Ввод коэффициентов
                CoefficientInputSection(a, b, c, d,
                    onAChange = { a = it },
                    onBChange = { b = it },
                    onCChange = { c = it },
                    onDChange = { d = it }
                )

                // Кнопка расчета
                CalculateButton(
                    onClick = {
                        // Обработка нажатия кнопки расчета
                        error = ""
                        negativeRoots = emptyList()
                        allRoots = emptyList()
                        expanded = false

                        // Нормализация разделителей десятичных дробей
                        val cleanA = a.replace(',', '.')
                        val cleanB = b.replace(',', '.')
                        val cleanC = c.replace(',', '.')
                        val cleanD = d.replace(',', '.')

                        // Парсинг значений
                        val aVal = cleanA.toDoubleOrNull()
                        val bVal = cleanB.toDoubleOrNull()
                        val cVal = cleanC.toDoubleOrNull()
                        val dVal = cleanD.toDoubleOrNull()

                        // Валидация ввода
                        when {
                            aVal == null || bVal == null || cVal == null || dVal == null -> {
                                error = "Ошибка: все коэффициенты должны быть числами"
                            }
                            aVal == 0.0 -> {
                                error = "Ошибка: коэффициент 'a' не может быть равен 0"
                            }
                            else -> {
                                // Вычисление корней
                                negativeRoots = findNegativeRoots(aVal, bVal, cVal, dVal)
                                allRoots = findAllRoots(aVal, bVal, cVal, dVal)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Отображение ошибок
                ErrorDisplay(error)

                // Отображение результатов
                ResultsSection(negativeRoots, allRoots, error, expanded,
                    onExpandChange = { expanded = it }
                )

                // Гибкий спейсер для центрирования
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

/**
 * Секция заголовка приложения
 */
@Composable
private fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            "Решение кубического уравнения",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
        )
    }
}

/**
 * Отображение формулы уравнения
 */
@Composable
private fun EquationFormula() {
    Text(
        "ax³ - bx² + cx + d = 0",
        style = TextStyle(
            color = Color(0xFFBB86FC),
            fontSize = 20.sp,
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

/**
 * Секция ввода коэффициентов
 */
@Composable
private fun CoefficientInputSection(
    a: String,
    b: String,
    c: String,
    d: String,
    onAChange: (String) -> Unit,
    onBChange: (String) -> Unit,
    onCChange: (String) -> Unit,
    onDChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoefficientInput("Коэффициент a", a, onAChange)
            CoefficientInput("Коэффициент c", c, onCChange)
        }

        Spacer(Modifier.width(32.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoefficientInput("Коэффициент b", b, onBChange)
            CoefficientInput("Коэффициент d", d, onDChange)
        }
    }
}

/**
 * Кнопка расчета корней
 */
@Composable
private fun CalculateButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            "Найти отрицательные корни",
            style = MaterialTheme.typography.button
        )
    }
}

/**
 * Отображение ошибок
 */
@Composable
private fun ErrorDisplay(error: String) {
    if (error.isNotEmpty()) {
        Text(
            text = error,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

/**
 * Секция отображения результатов
 */
@Composable
private fun ResultsSection(
    negativeRoots: List<Double>,
    allRoots: List<Double>,
    error: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    // Отображение отрицательных корней
    if (negativeRoots.isNotEmpty()) {
        Card(
            backgroundColor = Color(0xFF1E1E1E),
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Отрицательные корни:",
                    style = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                negativeRoots.forEachIndexed { index, root ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(
                            "x${index + 1} = ",
                            style = TextStyle(
                                color = Color(0xFFCCCCCC),
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            "%.6f".format(root),
                            style = TextStyle(
                                color = MaterialTheme.colors.secondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    } else if (error.isEmpty() && allRoots.isNotEmpty()) {
        Text(
            "Отрицательные корни не найдены",
            style = TextStyle(
                color = Color(0xFFAAAAAA),
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(8.dp)
        )
    }

    // Кнопка для просмотра всех корней
    if (allRoots.isNotEmpty()) {
        Box(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            OutlinedButton(
                onClick = { onExpandChange(true) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ),
                border = ButtonDefaults.outlinedBorder,
                modifier = Modifier.width(240.dp)
            ) {
                Text(
                    "Показать все корни",
                    style = MaterialTheme.typography.button.copy(
                        color = MaterialTheme.colors.primary
                    )
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            // Выпадающее меню со всеми корнями
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) },
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    "Все корни уравнения:",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                allRoots.forEachIndexed { index, root ->
                    DropdownMenuItem(
                        onClick = { onExpandChange(false) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "x${index + 1} = ${"%.6f".format(root)}",
                            style = TextStyle(
                                color = if (root < 0) MaterialTheme.colors.secondary else Color(0xFFCCCCCC),
                                fontSize = 16.sp,
                                fontWeight = if (root < 0) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}
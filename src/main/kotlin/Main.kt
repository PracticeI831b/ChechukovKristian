import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.math.abs
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Обновленная цветовая палитра для тёмной темы
private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFF000000),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFCCCCCC),
    error = Color(0xFFCF6679)
)

@Composable
@Preview
fun App() {
    var a by remember { mutableStateOf("1.0") }
    var b by remember { mutableStateOf("1.0") }
    var c by remember { mutableStateOf("1.0") }
    var d by remember { mutableStateOf("1.0") }
    var negativeRoots by remember { mutableStateOf(emptyList<Double>()) }
    var allRoots by remember { mutableStateOf(emptyList<Double>()) }
    var error by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    MaterialTheme(
        colors = DarkColorPalette,
        content = {
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

                    // Формула уравнения - увеличенный текст
                    Text(
                        "ax³ - bx² + cx + d = 0",
                        style = TextStyle(
                            color = Color(0xFFBB86FC),
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Ввод коэффициентов в две колонки
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
                            CoefficientInput("Коэффициент a", a) { a = it }
                            CoefficientInput("Коэффициент c", c) { c = it }
                        }

                        Spacer(Modifier.width(32.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CoefficientInput("Коэффициент b", b) { b = it }
                            CoefficientInput("Коэффициент d", d) { d = it }
                        }
                    }

                    // Кнопка расчета
                    Button(
                        onClick = {
                            error = ""
                            negativeRoots = emptyList()
                            allRoots = emptyList()
                            expanded = false

                            // Заменяем запятые на точки для корректного преобразования
                            val cleanA = a.replace(',', '.')
                            val cleanB = b.replace(',', '.')
                            val cleanC = c.replace(',', '.')
                            val cleanD = d.replace(',', '.')

                            val aVal = cleanA.toDoubleOrNull()
                            val bVal = cleanB.toDoubleOrNull()
                            val cVal = cleanC.toDoubleOrNull()
                            val dVal = cleanD.toDoubleOrNull()

                            if (aVal == null || bVal == null || cVal == null || dVal == null) {
                                error = "Ошибка: все коэффициенты должны быть числами"
                                return@Button
                            }

                            if (aVal == 0.0) {
                                error = "Ошибка: коэффициент 'a' не может быть равен 0"
                                return@Button
                            }

                            negativeRoots = findNegativeRoots(aVal, bVal, cVal, dVal)
                            allRoots = findAllRoots(aVal, bVal, cVal, dVal)
                        },
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Блок результатов
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

                    // Вывод отрицательных корней
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
                                onClick = { expanded = true },
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
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
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
                                        onClick = { expanded = false },
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

                    // Гибкий спейсер для центрирования контента
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    )
}

@Composable
fun CoefficientInput(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            label,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Разрешаем ввод чисел с точкой и запятой
                val filtered = newValue.filter {
                    it.isDigit() || it == '-' || it == '.' || it == ','
                }
                onValueChange(filtered)
            },
            singleLine = true,
            placeholder = { // Добавляем плейсхолдер
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

private fun f(a: Double, b: Double, c: Double, d: Double, x: Double): Double {
    return a * x * x * x - b * x * x + c * x + d
}

private fun findRootByChord(
    a: Double,
    b: Double,
    c: Double,
    d: Double,
    x0: Double,
    x1: Double
): Double {
    val epsilon = 0.0001
    var xStart = x0
    var xEnd = x1
    var xMid = x1
    var iterations = 0
    val maxIterations = 10000

    do {
        val fStart = f(a, b, c, d, xStart)
        val fEnd = f(a, b, c, d, xEnd)

        // Защита от деления на ноль
        if (abs(fEnd - fStart) < 1e-12) {
            xMid = (xStart + xEnd) / 2
        } else {
            xMid = xEnd - fEnd * (xEnd - xStart) / (fEnd - fStart)
        }

        val fMid = f(a, b, c, d, xMid)

        when {
            fMid * fStart < 0 -> xEnd = xMid
            else -> xStart = xMid
        }

        iterations++
    } while (abs(fMid) > epsilon && iterations < maxIterations)

    return xMid
}

private fun findNegativeRoots(
    a: Double,
    b: Double,
    c: Double,
    d: Double
): List<Double> {
    val roots = mutableSetOf<Double>()
    val step = 0.1
    var xLeft = -100000.0
    val xRight = 0.0

    while (xLeft < xRight) {
        val xNext = minOf(xLeft + step, xRight)
        val fLeft = f(a, b, c, d, xLeft)
        val fRight = f(a, b, c, d, xNext)

        // Проверка смены знака
        if (fLeft * fRight <= 0) {
            val root = findRootByChord(a, b, c, d, xLeft, xNext)
            // Проверка что корень действительно в интервале и отрицательный
            if (root in xLeft..xNext && root < 0) {
                // Проверка на уникальность (избегаем дубликатов)
                if (roots.none { abs(it - root) < 0.001 }) {
                    roots.add(root)
                }
            }
        }
        xLeft = xNext
    }

    return roots.toList().sorted()
}

private fun findAllRoots(
    a: Double,
    b: Double,
    c: Double,
    d: Double
): List<Double> {
    val roots = mutableSetOf<Double>()
    val step = 0.1
    val range = -10000.0..10000.0
    var xLeft = range.start

    while (xLeft < range.endInclusive) {
        val xNext = minOf(xLeft + step, range.endInclusive)
        val fLeft = f(a, b, c, d, xLeft)
        val fRight = f(a, b, c, d, xNext)

        // Проверка смены знака
        if (fLeft * fRight <= 0) {
            val root = findRootByChord(a, b, c, d, xLeft, xNext)
            // Проверка что корень действительно в интервале
            if (root in xLeft..xNext) {
                // Проверка на уникальность (избегаем дубликатов)
                if (roots.none { abs(it - root) < 0.001 }) {
                    roots.add(root)
                }
            }
        }
        xLeft = xNext
    }

    return roots.toList().sorted()
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Решение кубических уравнений"
    ) {
        App()
    }
}
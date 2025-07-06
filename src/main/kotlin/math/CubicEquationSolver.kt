package math

import kotlin.math.abs

/**
 * Вычисление значения кубического уравнения в точке x
 *
 * @param a Коэффициент при x³
 * @param b Коэффициент при x²
 * @param c Коэффициент при x
 * @param d Свободный член
 * @param x Точка, в которой вычисляется значение
 * @return Значение уравнения в точке x
 */
fun f(a: Double, b: Double, c: Double, d: Double, x: Double): Double {
    return a * x * x * x - b * x * x + c * x + d
}

/**
 * Нахождение корня уравнения методом хорд
 *
 * @param a Коэффициент при x³
 * @param b Коэффициент при x²
 * @param c Коэффициент при x
 * @param d Свободный член
 * @param x0 Левая граница интервала
 * @param x1 Правая граница интервала
 * @return Найденный корень уравнения
 */
fun findRootByChord(
    a: Double,
    b: Double,
    c: Double,
    d: Double,
    x0: Double,
    x1: Double
): Double {
    val epsilon = 0.0001  // Точность вычислений
    var xStart = x0
    var xEnd = x1
    var xMid = x1
    var iterations = 0
    val maxIterations = 10000  // Максимальное количество итераций

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

        // Определение нового интервала
        when {
            fMid * fStart < 0 -> xEnd = xMid
            else -> xStart = xMid
        }

        iterations++
    } while (abs(fMid) > epsilon && iterations < maxIterations)

    return xMid
}

/**
 * Поиск отрицательных корней уравнения
 *
 * @param a Коэффициент при x³
 * @param b Коэффициент при x²
 * @param c Коэффициент при x
 * @param d Свободный член
 * @return Список отрицательных корней
 */
fun findNegativeRoots(
    a: Double,
    b: Double,
    c: Double,
    d: Double
): List<Double> {
    val roots = mutableSetOf<Double>()
    val step = 0.1  // Шаг поиска
    var xLeft = -100000.0  // Начало интервала поиска
    val xRight = 0.0  // Конец интервала поиска (только отрицательные значения)

    while (xLeft < xRight) {
        val xNext = minOf(xLeft + step, xRight)
        val fLeft = f(a, b, c, d, xLeft)
        val fRight = f(a, b, c, d, xNext)

        // Проверка смены знака (признак корня)
        if (fLeft * fRight <= 0) {
            val root = findRootByChord(a, b, c, d, xLeft, xNext)
            // Проверка что корень в интервале и отрицательный
            if (root in xLeft..xNext && root < 0) {
                // Проверка на уникальность
                if (roots.none { abs(it - root) < 0.001 }) {
                    roots.add(root)
                }
            }
        }
        xLeft = xNext
    }

    return roots.toList().sorted()
}

/**
 * Поиск всех корней уравнения
 *
 * @param a Коэффициент при x³
 * @param b Коэффициент при x²
 * @param c Коэффициент при x
 * @param d Свободный член
 * @return Список всех корней уравнения
 */
fun findAllRoots(
    a: Double,
    b: Double,
    c: Double,
    d: Double
): List<Double> {
    val roots = mutableSetOf<Double>()
    val step = 0.1
    val range = -10000.0..10000.0  // Диапазон поиска
    var xLeft = range.start

    while (xLeft < range.endInclusive) {
        val xNext = minOf(xLeft + step, range.endInclusive)
        val fLeft = f(a, b, c, d, xLeft)
        val fRight = f(a, b, c, d, xNext)

        // Проверка смены знака
        if (fLeft * fRight <= 0) {
            val root = findRootByChord(a, b, c, d, xLeft, xNext)
            // Проверка что корень в интервале
            if (root in xLeft..xNext) {
                // Проверка на уникальность
                if (roots.none { abs(it - root) < 0.001 }) {
                    roots.add(root)
                }
            }
        }
        xLeft = xNext
    }

    return roots.toList().sorted()
}
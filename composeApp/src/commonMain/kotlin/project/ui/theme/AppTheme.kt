package project.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Единая конфигурация шрифтов приложения.
 * В одном месте настраиваем 2 семейства шрифтов: заголовочный и обычный,
 * а также базовые размеры.
 *
 * Пример смены шрифта:
 * AppFontsConfig.headingFamily = FontFamily.Serif
 * AppFontsConfig.bodyFamily = FontFamily.SansSerif
 */
object AppFontsConfig {
    var headingFamily: FontFamily = FontFamily.Serif
    var bodyFamily: FontFamily = FontFamily.SansSerif

    var headingSize = 18.sp
    var bodySize = 14.sp
}

/**
 * Локали для доступа к унифицированным стилям текста.
 */
val LocalHeadingTextStyle = staticCompositionLocalOf {
    TextStyle(
        fontFamily = _root_ide_package_.project.ui.theme.AppFontsConfig.headingFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = _root_ide_package_.project.ui.theme.AppFontsConfig.headingSize
    )
}

val LocalBodyTextStyle = staticCompositionLocalOf {
    TextStyle(
        fontFamily = _root_ide_package_.project.ui.theme.AppFontsConfig.bodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = _root_ide_package_.project.ui.theme.AppFontsConfig.bodySize
    )
}

/**
 * Глобальная тема приложения, мапит оба семейства шрифтов в Typography Material3.
 * Обязательно оборачивайте корневой контент приложения в AppTheme.
 */
@Composable
fun AppTheme(
    headingFamily: FontFamily = AppFontsConfig.headingFamily,
    bodyFamily: FontFamily = AppFontsConfig.bodyFamily,
    content: @Composable () -> Unit
) {
    val typography = Typography(
        // Все заголовочные стили используют headingFamily
        displayLarge = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Bold),
        displayMedium = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Bold),
        displaySmall = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.SemiBold),
        headlineLarge = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.SemiBold),
        headlineMedium = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.SemiBold),
        headlineSmall = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Medium),
        titleLarge = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Medium),
        titleMedium = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Medium),
        titleSmall = TextStyle(fontFamily = headingFamily, fontWeight = FontWeight.Medium),

        // Все обычные стили используют bodyFamily
        bodyLarge = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Normal),
        bodyMedium = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Normal),
        bodySmall = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Light),

        labelLarge = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Medium),
        labelMedium = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Normal),
        labelSmall = TextStyle(fontFamily = bodyFamily, fontWeight = FontWeight.Light),
    )

    CompositionLocalProvider(
        LocalHeadingTextStyle provides TextStyle(
            fontFamily = headingFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = AppFontsConfig.headingSize
        ),
        LocalBodyTextStyle provides TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = AppFontsConfig.bodySize
        )
    ) {
        MaterialTheme(
            typography = typography,
            content = content,
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = Color.Black.copy(alpha = 0.9f),
            )
        )
    }
}

/**
 * Удобный компонент для заголовков, использует заголовочный стиль темы.
 */
@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(text = text, style = LocalHeadingTextStyle.current, modifier = modifier, color = color)
}

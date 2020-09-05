package org.github.mejiomah17.api.mos.ru.model.epd

/**
 * https://www.mos.ru/otvet-dom-i-dvor/kak-polzovatsya-epd/#:~:text=%C2%AB%D0%A2%D0%B5%D0%BA%D1%83%D1%89%D0%B8%D0%B9%20%D0%95%D0%9F%D0%94%C2%BB.,%C2%AB%D0%94%D0%BE%D0%BB%D0%B3%D0%BE%D0%B2%D0%BE%D0%B9%20%D0%95%D0%9F%D0%94%C2%BB.
 */
enum class EpdType(val value:String) {
    CURRENT("current"),
    DEBT("debt")
}
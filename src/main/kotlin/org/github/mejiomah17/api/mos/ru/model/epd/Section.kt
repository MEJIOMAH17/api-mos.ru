package org.github.mejiomah17.api.mos.ru.model.epd

data class Section(
    val title:String,
    //I'm to lazy to create dto for this multiobject. They use generic type. This can be list, or different object.
    val elements:Any
)
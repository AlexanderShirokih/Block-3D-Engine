package ru.aleshi.block3d.shader

/**
 * Used to mark some class property linkable to shader uniform
 * @param name uniform name
 */
@Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY)
annotation class LinkableProperty(val name: String = "")
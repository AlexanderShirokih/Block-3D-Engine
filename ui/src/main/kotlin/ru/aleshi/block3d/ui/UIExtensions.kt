package ru.aleshi.block3d.ui

import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.ui.widget.UIRootObject

/**
 * Returns UI root object of the scene
 */
val Scene.ui: UIRootObject
    get() = this.objects.first { it is UIRootObject } as UIRootObject


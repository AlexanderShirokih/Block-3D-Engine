package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.ui.Insets
import ru.aleshi.block3d.ui.widget.*


fun createBackButton(onPressed: () -> Unit): UIObject {
    return UIPadding(
        padding = Insets(10f),
        child = UIButton(
            child = UITightBox(
                child = UIPadding(
                    padding = Insets(10f),
                    child = UIImage(
                        imagePath = "preview/ic_back.png"
                    )
                )
            ), onPressed
        )
    )
}
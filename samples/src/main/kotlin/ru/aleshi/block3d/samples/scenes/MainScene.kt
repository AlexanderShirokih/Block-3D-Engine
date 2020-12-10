package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.World
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.*
import ru.aleshi.block3d.ui.widget.*

class MainScene : Scene() {

    class SceneDescriptor(
        val title: String,
        val description: String,
        val image: String,
        val sceneBuilder: () -> Scene,
    )

    override fun create() {
        super.create()
        background = SolidColorBackground(Color4f.white)

        ui.setContent(
            UIAlign(
                alignment = Alignment.center,
                child = buildGrid(
                    listOf(
                        SceneDescriptor(
                            title = "Трансформация объектов",
                            description = "Демонстрация относительности операций\n преобразований объектов",
                            image = "preview/transform_relations.png",
                            sceneBuilder = { TransformRelations() }
                        ),
                        SceneDescriptor(
                            title = "Оптимизация отрисовки",
                            description = "Демонстрация производительности при\nстатической группировки объектов (batching)",
                            image = "preview/batching.png",
                            sceneBuilder = { ObjectBatching() }
                        ),
                        SceneDescriptor(
                            title = "Освещение",
                            description = "Демонстрация работы точечного освещения \nв шейдерах",
                            image = "preview/lightning.png",
                            sceneBuilder = { LightningTest() }
                        ),
                        SceneDescriptor(
                            title = "Прозрачность",
                            description = "Демонстрация работы прозрачности объектов \n(blending)",
                            image = "preview/blending.png",
                            sceneBuilder = { TransparencyTest() }
                        ),
                        SceneDescriptor(
                            title = "Текстуры окружения",
                            description = "Демонстрация применения SkyBox - объекта \nокружающего мир",
                            image = "preview/skybox.png",
                            sceneBuilder = { SkyBoxTest() }
                        ),
                        SceneDescriptor(
                            title = "Загрузка моделей",
                            description = "Демонстрация загрузки моделей из внешних \nфайлов",
                            image = "preview/models.png",
                            sceneBuilder = { ComplexModelLoading() }
                        ),
                        SceneDescriptor(
                            title = "Frustum culling",
                            description = "Демонстрация отсечения невидимых \nобъектов",
                            image = "preview/frustum.png",
                            sceneBuilder = { BoundsTest() }
                        )
                    )
                )
            )
        )
    }

    private fun buildGrid(data: List<SceneDescriptor>): UIObject {
        return UILinearGroup(
            orientation = Orientation.Vertical,
            children =
            data.chunked(4) {
                UILinearGroup(
                    orientation = Orientation.Horizontal,
                    children = it.map { descriptor -> buildButton(descriptor) }
                )
            }
        )
    }

    private fun buildButton(info: SceneDescriptor): UIObject = UIButton(
        child = UIPadding(
            padding = Insets(30f),
            child = UITightBox(
                color = Color4f(0.9f, 0.9f, 0.9f, 0.9f),
                child = UIPadding(
                    padding = Insets(10f),
                    child = UILinearGroup(
                        orientation = Orientation.Vertical,
                        children = listOf(
                            UIBox(
                                preferredSize = Vector2f(256f, 160f),
                                child = UIImage(
                                    fit = ImageFit.Expand,
                                    imagePath = info.image
                                )
                            ),
                            UISpacer(height = 20f),
                            UIText(
                                fontSize = 24f,
                                text = info.title
                            ), UISpacer(height = 20f),
                            UIText(
                                color = Color4f.gray,
                                fontSize = 16f,
                                text = info.description
                            )
                        )
                    )
                )
            )
        )
    ) {
        World.current.launchSceneAsync(info.sceneBuilder())
    }

}
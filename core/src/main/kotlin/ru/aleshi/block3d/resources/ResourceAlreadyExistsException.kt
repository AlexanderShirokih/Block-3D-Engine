package ru.aleshi.block3d.resources

import ru.aleshi.block3d.Block3DException

/**
 * Used when resource with name already exists in corresponding category
 */
class ResourceAlreadyExistsException(name: String, categoryName: String) :
    Block3DException("Resource with name : $name is already exists in $categoryName list")
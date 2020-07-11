package ru.aleshi.block3d.resources

/**
 * Used when resource with name already exists in corresponding category
 */
class ResourceAlreadyExistsException(name: String, categoryName: String) :
    Exception("Resource with name : $name is already exists in $categoryName list")
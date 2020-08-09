package ru.aleshi.block3d.resources

import ru.aleshi.block3d.Block3DException

/**
 * Used when resource with the specific name was not found or resource cannot be loaded by [Loader].
 */
class ResourceNotFoundException(name: String) : Block3DException("Resource with name \"$name\" was not found")
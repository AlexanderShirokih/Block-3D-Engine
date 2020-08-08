package ru.aleshi.block3d.resources

/**
 * Used when resource with the specific name was not found or resource cannot be loaded by [Loader].
 */
class ResourceNotFoundException(name: String) : Exception("Resource with name \"$name\" was not found")
package ru.aleshi.block3d.resources

/**
 * Used when resource with the specific name was not found
 */
class ResourceNotFoundException(name: String) : Exception("Resource with name $name was not found")
package ru.aleshi.block3d.debug

/**
 * Throws by debugging functions when current renderer is not [DebugRenderer]
 */
class DebuggerRendererIsNotSetException :
    Exception("The current scene renderer is not ${DebugRenderer::javaClass.name}")
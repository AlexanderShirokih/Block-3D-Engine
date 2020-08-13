# B3D Model Specification
B3D is a file format using for string 3d models data, such as vertices, normals, texture coordinates, references to materials.
Currently, format still in a development stage.
Advantage of using this format is fast loading and saving models.

## Supported object types
B3D can store 3d models, cameras and light sources(not supported yet).

## File structure
The file consists of blocks. Each block has a tag (ub16), length(ub4), and content (*length* bytes).
Block has properties. Property names are UTF-8 strings (ub16 - length, then *length* bytes of string data).
After the property name follows the property content. The property name defines the content reading rules.

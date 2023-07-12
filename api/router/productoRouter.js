const express = require('express');
const router = express.Router();

const Producto = require('../models/producto');
const producto = new Producto();

/**
 * @swagger
 * tags:
 *   name: Productos
 *   description: API para manejar productos
 */

/**
 * @swagger
 * /productos:
 *   get:
 *     summary: Obtiene todos los productos
 *     tags: [Productos]
 *     responses:
 *       200:
 *         description: Lista de productos obtenida con éxito
 *       500:
 *         description: Error en el servidor
 */
router.get('/productos', (req, res) => {
  producto.getAllProductos((err, results) => {
    if (err) {
      console.error('Error al obtener los productos:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(results);
  });
});

/**
 * @swagger
 * /productos/id/{id}:
 *   get:
 *     summary: Obtiene un producto por su ID
 *     tags: [Productos]
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         description: ID del producto a buscar
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: Producto obtenido con éxito
 *       404:
 *         description: Producto no encontrado
 *       500:
 *         description: Error en el servidor
 */
router.get('/productos/id/:id', (req, res) => {
  const idProducto = req.params.id;
  producto.getProductoById(idProducto, (err, result) => {
    if (err) {
      console.error('Error al obtener el producto:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }

    res.json(result);
  });
});

/**
 * @swagger
 * /productos/categoria/{idCategoria}:
 *   get:
 *     summary: Obtiene los productos por ID de categoría
 *     tags: [Productos]
 *     parameters:
 *       - in: path
 *         name: idCategoria
 *         required: true
 *         description: ID de la categoría para filtrar los productos
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: OK
 *       500:
 *         description: Error en el servidor
 */
router.get('/productos/categoria/:idCategoria', (req, res) => {
  const idCategoria = req.params.idCategoria;

  producto.getProductosByCategoryId(idCategoria, (err, results) => {
    if (err) {
      console.error('Error al obtener los productos por ID de categoría:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }

    res.json(results);
  });
});

/**
 * @swagger
 * /productos:
 *   post:
 *     summary: Inserta un nuevo producto
 *     tags: [Productos]
 *     description: Inserta un nuevo producto en la base de datos.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               id_categoria:
 *                 type: integer
 *                 description: ID de la categoría del producto
 *               nombre:
 *                 type: string
 *                 description: Nombre del producto
 *               descripcion:
 *                 type: string
 *                 description: Descripción del producto
 *               precio:
 *                 type: number
 *                 description: Precio del producto
 *     responses:
 *       200:
 *         description: Producto insertado correctamente
 *       500:
 *         description: Error en el servidor
 */
router.post('/productos', (req, res) => {
  const { id_categoria, nombre, descripcion, precio } = req.body;

  producto.insertProducto(id_categoria, nombre, descripcion, precio, (err, result) => {
    if (err) {
      console.error('Error al insertar el producto:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

/**
 * @swagger
 * /productos/{idProducto}:
 *   put:
 *     summary: Actualiza un producto existente
 *     tags: [Productos]
 *     description: Actualiza un producto existente en la base de datos.
 *     parameters:
 *       - in: path
 *         name: idProducto
 *         required: true
 *         description: ID del producto a modificar
 *         schema:
 *           type: integer
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               id_categoria:
 *                 type: integer
 *                 description: Nuevo ID de la categoría del producto
 *               nombre:
 *                 type: string
 *                 description: Nuevo nombre del producto
 *               descripcion:
 *                 type: string
 *                 description: Nueva descripción del producto
 *               precio:
 *                 type: number
 *                 description: Nuevo precio del producto
 *     responses:
 *       200:
 *         description: Producto actualizado correctamente
 *       500:
 *         description: Error en el servidor
 */
router.put('/productos/:idProducto', (req, res) => {
  const idProducto = req.params.idProducto;
  const { id_categoria, nombre, descripcion, precio } = req.body;

  producto.updateProducto(idProducto, id_categoria, nombre, descripcion, precio, (err, result) => {
    if (err) {
      console.error('Error al modificar el producto:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

/**
 * @swagger
 * /productos/{idProducto}:
 *   delete:
 *     summary: Elimina un producto por su ID
 *     tags: [Productos]
 *     description: Elimina un producto por su ID de la base de datos.
 *     parameters:
 *       - in: path
 *         name: idProducto
 *         required: true
 *         description: ID del producto a eliminar
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: Producto eliminado correctamente
 *       500:
 *         description: Error en el servidor
 */
router.delete('/productos/:idProducto', (req, res) => {
  const idProducto = req.params.idProducto;

  producto.deleteProducto(idProducto, (err, result) => {
    if (err) {
      console.error('Error al eliminar el producto:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

module.exports = router;

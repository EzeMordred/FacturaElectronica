const express = require('express');
const router = express.Router();

const Producto = require('../models/producto');
const producto = new Producto();


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

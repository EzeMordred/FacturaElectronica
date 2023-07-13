const express = require('express');
const router = express.Router();

const Categoria = require('../models/categoria');
const categoria = new Categoria();

router.get('/categorias', (req, res) => {
  categoria.getAllCategorias((err, results) => {
    if (err) {
      console.error('Error al obtener las categorías:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(results);
  });
});

router.get('/categorias/id/:id', (req, res) => {
  const idCategoria = req.params.id;
  categoria.getCategoriaById(idCategoria, (err, result) => {
    if (err) {
      console.error('Error al obtener la categoría:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }

    res.json(result);
  });
});

router.post('/categorias', (req, res) => {
  const { categoriaPrincipal, categoriaSecundaria } = req.body;

  categoria.insertCategoria(categoriaPrincipal, categoriaSecundaria, (err, result) => {
    if (err) {
      console.error('Error al insertar la categoría:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

router.put('/categorias/:idCategoria', (req, res) => {
  const idCategoria = req.params.idCategoria;
  const { categoriaPrincipal, categoriaSecundaria } = req.body;

  categoria.updateCategoria(idCategoria, categoriaPrincipal, categoriaSecundaria, (err, result) => {
    if (err) {
      console.error('Error al modificar la categoría:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

router.delete('/categorias/:idCategoria', (req, res) => {
  const idCategoria = req.params.idCategoria;

  categoria.deleteCategoria(idCategoria, (err, result) => {
    if (err) {
      console.error('Error al eliminar la categoría:', err);
      res.status(500).json({ error: 'Error en el servidor' });
      return;
    }
    res.json(true);
  });
});

module.exports = router;

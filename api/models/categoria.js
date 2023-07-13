const mysql = require('mysql2');
const DB = require('../DB.JS');
const db = new DB();

class Categoria {
  constructor() {
    this.connection = db.connection; // Conexión a la base de datos
  }

  getAllCategorias(callback) {
    const query = 'SELECT * FROM Categoria';

    this.connection.query(query, (err, results) => {
      if (err) {
        console.error('Error al obtener las categorías:', err);
        callback(err, null);
        return;
      }
      callback(null, results);
    });
  }

  getCategoriaById(idCategoria, callback) {
    const query = 'SELECT * FROM Categoria WHERE id_categoria = ?';

    this.connection.query(query, [idCategoria], (err, results) => {
      if (err) {
        console.error('Error al obtener la categoría:', err);
        callback(err, null);
        return;
      }

      if (results.length === 0) {
        callback({ error: 'Categoría no encontrada' }, null);
        return;
      }

      callback(null, results[0]);
    });
  }

  insertCategoria(categoriaPrincipal, categoriaSecundaria, callback) {
    const query = 'INSERT INTO Categoria (categoriaPrincipal, categoriaSecundaria) VALUES (?, ?)';
    const values = [categoriaPrincipal, categoriaSecundaria];

    this.connection.query(query, values, (err, result) => {
      if (err) {
        console.error('Error al insertar la categoría:', err);
        callback(err, null);
        return;
      }
      callback(null, result);
    });
  }

  updateCategoria(idCategoria, categoriaPrincipal, categoriaSecundaria, callback) {
    const query = 'UPDATE Categoria SET categoriaPrincipal = ?, categoriaSecundaria = ? WHERE id_categoria = ?';
    const values = [categoriaPrincipal, categoriaSecundaria, idCategoria];

    this.connection.query(query, values, (err, result) => {
      if (err) {
        console.error('Error al modificar la categoría:', err);
        callback(err, null);
        return;
      }
      callback(null, result);
    });
  }

  deleteCategoria(idCategoria, callback) {
    const query = 'DELETE FROM Categoria WHERE id_categoria = ?';

    this.connection.query(query, [idCategoria], (err, result) => {
      if (err) {
        console.error('Error al eliminar la categoría:', err);
        callback(err, null);
        return;
      }
      callback(null, result);
    });
  }
}

module.exports = Categoria;